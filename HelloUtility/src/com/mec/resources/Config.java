package com.mec.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import com.mec.resources.Plugins.Plugin;

/**
 * To replace the Original {@link Config} class;
 * <pre>
 * Usage case: 
 * <dl>
 * <dt>Config.to("data/").of(this.getClass()).save("derp", Derp.class)</dt>
 * <dd>Save config <code>derp.xml</code> in ./data/{this.class.name}/derp.xml</dd>
 * 
 * <dt>Config.defaultData().of(this).save("foo", Foo.class);</dt>
 * <dd>Same as Config.to("data/").of(this).save("foo", Foo.class)</dd>
 * 
 * <dt>Config.pluginData().of(this).save("bar", Bar.class)</dt>
 * <dd>Same as Config.to("plugin/").of(this).save("bar", Bar.class)</dd>
 * 
 * <dt>Config.of(this).save("derpia", Derpia.class)</dt>
 * <dd>Same as Config.defaultData().of(this).save("derpia", Derpia.class)</dd>
 * 
 * <dt></dt>
 * <dd></dd>
 * </dl>
 * </pre>
 * @author MEC
 *
 */
public class Config {

	private Config(String dataRootDir){
		this(Paths.get(dataRootDir));
	}
	
	private Config(Path dataRoot){
		this(dataRoot, null);
	}
	
	/**
	 * Note used for now
	 * @param dataRootDir
	 * @param parentConfig
	 */
	private Config(String dataRootDir, Config parentConfig){
		this(Paths.get(dataRootDir), parentConfig);
	}
	
	/**
	 * Not used for now
	 * @param dataRoot
	 * @param parentConfig
	 */
	private Config(Path dataRoot, Config parentConfig){
		Objects.requireNonNull(dataRoot);
		this.dataRoot = dataRoot;
		this.parent = parentConfig;
	}
	
	//---------------------------------------------------
	/**
	 * Create the specific <code>path</code> if it does not exist. Possible <code>patchCreateMethod</code> would
	 * look like <code>Files::createDirectories</code>
	 * or <code>Files::createFile</code>
	 * @param path
	 * @param pathCreateMethod
	 */
	private void createIfNotExists(Path path, CreatePathMethod pathCreateMethod){
		if(!Files.exists(path)){
			try {
				pathCreateMethod.create(path);
				if(Files.isDirectory(path)){
					logger.log(Msg.get(Config.class, "log.create.directory"), path.toRealPath());
				}else{
					logger.log(Msg.get(Config.class, "log.create.file"), path.toRealPath());
				}
			} catch (IOException e) {
				logger.log(e);
			}
		}
	}

	//---------------------------------------------------
	/**
	 * Return a new {@link ConfigEndpoint} instance with this {@link Config}'s {@link #dataRoot} set to <code>dataRootDir</code>
	 * @param dataRootDir
	 * @return
	 */
	private static ConfigEndpoint to(String dataRootDir){
		Objects.requireNonNull(dataRootDir);
		String key = null;
//		try {
//			key = Paths.get(dataRootDir).toRealPath().toString();
			key = Paths.get(dataRootDir).normalize().toAbsolutePath().toString();
//		} catch (IOException e) {
//			MsgLogger.defaultLogger().log(e);
//			key = dataRootDir;
//		}
		return instances.computeIfAbsent(key, k -> new ConfigEndpoint(new Config(dataRootDir)));
//		ConfigEndpoint retval = instances.computeIfAbsent(key, k -> new ConfigEndpoint(new Config(dataRootDir)));
//		ConfigEndpoint retval = instances.get(key);
//		if(null == retval){
//			retval = new ConfigEndpoint(new Config(dataRootDir));
//			instances.put(key, retval);
//		}
//		return retval;
	}
	public static ConfigEndpoint defaultData(){
//		return to(Msg.get(Config.class, "data.path"));
		return DEFAULT_CONFIG_DATA;
	}
	public static ConfigEndpoint pluginData(){
		return to(Msg.get(Plugin.class, "plugin.root.dir"));
	}
	
	//---------------------------------------------------
	/**
	 * Use {@link #defaultData()} for this configuration, shorthand for {@link #defaultData()}.of(Object)}. 
	 * 
	 * <h4>Note: For compatible usage.</h4> 
	 * @param componentObj
	 * @return
	 */
	public static ConfigEndpoint of(Object componentObj){
		return defaultData().of(componentObj.getClass().getName());
	}
	/**
	 * Use {@link #defaultData()} for this configuration, shorthand for {@link #defaultData()}.of(Class)}. 
	 */
	public static ConfigEndpoint of(Class<?> componentClass){
		return defaultData().of(componentClass.getName());
	}
	/**
	 * Use {@link #defaultData()} for this configuration, shorthand for {@link #defaultData()}.of(componentConfigDirStr)}. 
	 */
	public static ConfigEndpoint of(String componentConfigDirStr){
		return defaultData().of(componentConfigDirStr);
	}
	
	@Override
	public String toString() {
		return "Config [dataRoot=" + dataRoot + ", parent=" + parent + "]";
	}

	//---------------------------------------------------
	/**
	 * The whole data path for this Config, may be null.
	 * @return
	 */
	protected Path getDataPath(){
		Optional<Path> retval = null;
		retval = Optional.ofNullable(getParent()).map(Config::getDataPath);
		return retval.map(parent -> parent.resolve(dataRoot)).orElse(dataRoot);
	}
	private Config getParent(){
		return this.parent;
	}
	public void setLogger(MsgLogger logger) {
		this.logger = logger;
	}
	private MsgLogger getLogger(){
		return this.logger;
	}
	//---------------------------------------------------
	
	/**
	 * Root directory for configurations managed by this Config instance.
	 * Note that it may reside in another Config's (a.k.a parentConfig's)
	 * dataRoot;
	 */
	private Path dataRoot;
	private Config parent;
	private MsgLogger logger = MsgLogger.defaultLogger();
	private static Map<String, ConfigEndpoint> instances = new HashMap<>();
	private static final String CONFIG_FILENAME_PATTERN = Msg.get(Config.class, "config.fileName.pattern");
	private static final ConfigEndpoint DEFAULT_CONFIG_DATA;
	static{
		String defaultDataPath = Msg.get(Config.class, "data.path");
		DEFAULT_CONFIG_DATA = new ConfigEndpoint(new Config(defaultDataPath));
		instances.put(defaultDataPath, DEFAULT_CONFIG_DATA);
	}
	
	/**
	 * The endpoint of a whole Config chain
	 * <p>
	 * Actual work is done here.
	 * </p>
	 * @author MEC
	 *
	 */
	public static class ConfigEndpoint{
		private ConfigEndpoint(Config configChain){
//			if(null != configChain.getDataPath()){
////				instances.put(configChain., value)
//			}
			this.configChain = configChain;
//			this.componentConfigDir = Paths.get(Msg.get(Config.class, "path.default.componentConfigDir"));
			setComponentConfigDir(this.componentConfigDir);
//			setComponentConfigDir(DEFAULT_COMPONENT_CONFIG_DIR);
//			setComponentConfigDir(this.configChain.getDataPath());
		}
		
		private ConfigEndpoint newSibling(Path siblingComponentConfigDir){
			ConfigEndpoint retval = new ConfigEndpoint(this.configChain);
			retval.setComponentConfigDir(siblingComponentConfigDir);
			return retval;
		}
		
		//-------------------------------------------------------------------
		/**
		 * For compatibility
		 * @param path
		 * @param pathCreateMethod
		 */
		public void createIfNotExists(Path path, CreatePathMethod pathCreateMethod){
			configChain.createIfNotExists(path, pathCreateMethod);
		}
		public void setLogger(MsgLogger logger){
			configChain.setLogger(logger);
		}
		//-------------------------------------------------------------------
		
		
		private Path getConfigWithFullPath(Path fullConfigPath, String tag){
			configChain.createIfNotExists(fullConfigPath, Files::createDirectories);
			
			Path retval = fullConfigPath.resolve(String.format(CONFIG_FILENAME_PATTERN, tag));
			configChain.createIfNotExists(retval, Files::createFile);
			return retval;
		}
		
		private Path getDataPath(String tag){
//			return getConfigWithFullPath(configChain.getDataPath(), tag);
			return getConfigWithFullPath(componentConfigDir, tag);
		}
		
		/**
		 * @param tag
		 * @param configObjClass
		 * @return
		 */
		public <T> Optional<T> load(String tag, Class<? extends T> configObjClass){
			Optional<T> retval;
			try {
				Path xmlFile = componentConfigDir.resolve(String.format(CONFIG_FILENAME_PATTERN, tag));
				if(!(Files.exists(componentConfigDir) && Files.exists(xmlFile))){
					throw new FileNotFoundException(xmlFile.normalize().toString());
				}
				retval = Optional.of(BeanMarshal.loadFromXML(configObjClass, xmlFile));
			} catch (JAXBException | FileNotFoundException e) {
				configChain.getLogger().log(e);
				retval = Optional.empty();
			}
			return retval;
		}
		
		public <T> void save(String tag, T configObj){
			Path xmlFile = getDataPath(tag);
			try {
				BeanMarshal.saveToXML(configObj, xmlFile);
			} catch (JAXBException e) {
				configChain.getLogger().log(e);
			}
		}
		
		//------------------------------------------
		public ConfigEndpoint of(Object componentObj){
			return of(componentObj.getClass().getName());
		}
		public ConfigEndpoint of(Class<?> componentClass){
			return of(componentClass.getName());
		}
		public ConfigEndpoint of(String componentConfigDirStr){
			return of(Paths.get(componentConfigDirStr));
		}
		public ConfigEndpoint of(Path componentConfigDir){
			Objects.requireNonNull(componentConfigDir);
//			setComponentConfigDir(componentConfigDir);
//			ConfigEndpoint retval = Optional.ofNullable(componentConfigs.get(getComponentConfigKey(componentConfigDir)))
//					.orElseGet(() -> {
//						
//					})
//					;
			String key = getComponentConfigKey(componentConfigDir);
			ConfigEndpoint retval = componentConfigs.get(key);
			if(null == retval){
				retval = this.newSibling(componentConfigDir);
				componentConfigs.put(key, retval);
			}
			return retval;
		}
		
		
		@Override
		public String toString() {
			return "ConfigEndpoint [configRootPath=" + configChain.getDataPath() + ", componentConfigDir=" + componentConfigDir + "]";
		}

		//----------------------------------------------
		private void setComponentConfigDir(Path componentConfigDir){
//			this.componentConfigDir = Optional.ofNullable(componentConfigDir).orElse(Config.defaultData().configChain.getDataPath());
			this.componentConfigDir = Optional.ofNullable(componentConfigDir).orElseGet(() -> Config.defaultData().configChain.getDataPath());
			Optional.ofNullable(configChain.getDataPath())
				.ifPresent(p -> this.componentConfigDir = p.resolve(this.componentConfigDir));
//			return this;
		}
//		private Path getComponentConfigDir(){
//			
//		}
		private String getComponentConfigKey(Path componentConfigDir){
			String retval = componentConfigDir.toString();
			
			Path configPath = configChain.getDataPath();
//			try {
				if (null != configPath) {
//					retval = configPath.resolve(componentConfigDir).toRealPath().toString();	//the file would exist so that toRealPath() can work properly
					retval = configPath.resolve(componentConfigDir).normalize().toAbsolutePath().toString();
				} else{
//					retval = componentConfigDir.toRealPath().toString();
					retval = componentConfigDir.normalize().toAbsolutePath().toString();
				}
//			} catch (IOException e) {
//				configChain.getLogger().log(e);
//			}
			return retval;
		}
		
		
		private Config configChain;
//		private Path componentConfigDir = Config.defaultData().configChain.getDataPath();
		private Path componentConfigDir = DEFAULT_COMPONENT_CONFIG_DIR;
		private Map<String, ConfigEndpoint> componentConfigs = new HashMap<>();
		private static final Path DEFAULT_COMPONENT_CONFIG_DIR = Paths.get(Msg.get(Config.class, "path.default.componentConfigDir"));
	}
	
	
	public static interface CreatePathMethod{
		Path create(Path p) throws IOException;
	}
	/**
	 * policy that specifies the behavior when config file already exists;
	 * @author MEC
	 *
	 */
	public enum ConfigExistedPolicy{
		OVERRIDE	//default policy: overriding existing configuration file
		,BACKUP_OLD
		;
	}
	
	
	/**
	 * Configuration directories generate policy
	 * @author MEC
	 *
	 */
	public enum DirectoryOrganizePolicy{
		FLAT
		,HIERARCHY
		;
		
	}
	
	
	
	static class BeanMarshal{
		
		public static <T> void saveToXML(T obj, Path xmlFile) throws JAXBException, PropertyException{
			Stream.of(obj, xmlFile).forEach(Objects::requireNonNull);
			
			JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//
			m.marshal(obj, xmlFile.toFile());
		}
		
		@SuppressWarnings("unchecked")
		public static <T> T loadFromXML(Class<? extends T> clazz, Path xmlFile) throws JAXBException{
			Stream.of(clazz, xmlFile).forEach(Objects::requireNonNull);
			
			JAXBContext ctx = JAXBContext.newInstance(clazz);
			Unmarshaller um = ctx.createUnmarshaller();
			
			//
			return (T) um.unmarshal(xmlFile.toFile());
		}
	}
	
}

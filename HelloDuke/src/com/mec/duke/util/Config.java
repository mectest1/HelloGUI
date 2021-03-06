package com.mec.duke.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * Configuration Factory
 * <p>
 * Structure of the configurations will look like this (with default policies):
 * <code>${configDir}/${componentConfigDir}/tag.xml</code>
 * <br/>
 * </p>
 * <p>
 * e.g.
 * <code>Config.config(Config.class).save("foo", barObject)</code> will generate the following file: 
 * <strong>./data/com.mec.resources.Config/foo.xml</strong>
 * </p>
 * <p>
 * The generated configuration XML file can later be loaded with
 * <code>Config.config(Config.class).load("foo", BarObject.class)</code>, which may be empty.
 * </p>
 * <h4>Note for reusage of Config:</h4>
 * <p>
 * To reuse the Config component, it's recommended to sub-class this class and:
 * <ol>
 * <li>Override {@link #getDataPath(String)} to provide your own data root directory </li>
 * <li>Supplier a constructor that receives one single String argument (in which you can invoke {@link #Config(String)}) of course)</li>
 * <li>And provide your own version of {@link #of(String)} (or something like that), in which you can invoke {@link #of(String, Function)}</li>
 * </ol>
 * 
 * Typical usage case:
 * <pre>
 * class AbcConfig
 * 
 * private AbcConfig(String abcConfigName){
 * 	super(abcConfigName);
 * }
 * public static Config of(String abcConfigName){
 * 	 return Config.of(abcConfigName, AbcConfig::new);
 * }
 * protected Path getDataPath(){
 * 	return "abc";
 * } 
 * </pre>
 * </p>
 * <p>
 * See {@link PluginConfig} for a concrete example.
 * </p>
 *
 * @author MEC
 *
 */
public class Config {

	protected Config(){
//		configDir = Paths.get(Msg.get(this, "data.path"));
		configDir = getDataRoot();
		createIfNotExists(configDir, Files::createDirectory);
	}
	
	private Config(Path componentConfigDir){
		this();
		this.componentConfigDir = configDir.resolve(componentConfigDir);
	}
	protected Config(String componentConfigDirStr){
		this(Paths.get(componentConfigDirStr));
	}
	
	//---------------------------------------------------------
	@Deprecated
	@SuppressWarnings("unchecked")
	private <T> Optional<T> loadConfig(Object obj, String tag, Class<? extends T> configObjClass){
		
		return loadConfig(obj.getClass(), tag, (Class<? extends T>) configObjClass);
	}
	/**
	 * @param objClassAsPath obj.class.name will be used as parent name for this config
	 * @param tag tag.xml to be saved
	 * @param configObj configuration object to be saved
	 */
	@Deprecated
	private <T> void saveConfig(Object objClassAsPath, String tag, T configObj){
		saveConfig(objClassAsPath.getClass(), tag, configObj);
	}
	
	//----------------------------------------------------------
	/**
	 * Create the specific <code>path</code> if it does not exist. Possible <code>patchCreateMethod</code> would
	 * look like <code>Files::createDirectories</code>
	 * or <code>Files::createFile</code>
	 * @param path
	 * @param pathCreateMethod
	 */
	public void createIfNotExists(Path path, CreatePathMethod pathCreateMethod){
		if(!Files.exists(path)){
			try {
//				Files.createDirectories(path);
				pathCreateMethod.create(path);
				if(Files.isDirectory(path)){
//					logger.log(Msg.get(this, "log.create.directory"), path.toRealPath());
					logger.log(Msg.get(Config.class, "log.create.directory"), path.toRealPath());
				}else{
//					logger.log(Msg.get(this, "log.create.file"), path.toRealPath());
					logger.log(Msg.get(Config.class, "log.create.file"), path.toRealPath());
				}
			} catch (IOException e) {
				logger.log(e);
			}
		}
	}
	

	@Deprecated
	private Path getDataPath(Object obj, String tag){
		return getDataPath(obj.getClass(), tag);
	}
	
	@Deprecated
	private Path getDataPath(Class<?> clazz, String tag){
		return getDataPath(clazz.getName(), tag);
	}
	
	@Deprecated
	private Path getDataPath(String componentConfigDirStr, String tag){
		Path fullConfigPath = configDir.resolve(componentConfigDirStr);
		return getConfigWithFullPath(fullConfigPath, tag);
	}
	private Path getConfigWithFullPath(Path fullConfigPath, String tag){
		createIfNotExists(fullConfigPath, Files::createDirectories);
		
		Path retval = fullConfigPath.resolve(String.format(CONFIG_FILENAME_PATTERN, tag));
		createIfNotExists(retval, Files::createFile);
		return retval;
	}
	/**
	 * Get data file for the config with specified tag. The config file will bear this form:
	 * <code>componentConfigDir/tag.xml</code>. If this file (and all its parent directories) doesn't exist 
	 * yet, it will be created.
	 * @param tag tag.xml
	 * @return
	 */
	private Path getDataPath(String tag){
//		Path fullConfigPath = configDir.resolve(componentConfigDir);
		return getConfigWithFullPath(componentConfigDir, tag);
	}
	
	
	//------------------------------------------------------------
	@Deprecated
	private <T> Optional<T> loadConfig(Class<?> clazz, String tag, Class<? extends T> configObjClass){
//		Path xmlFile = getDataPath(clazz, tag);
		Optional<T> retval;
		try {
			Path componentConfigDir = configDir.resolve(clazz.getName());
			Path xmlFile = componentConfigDir.resolve(String.format(CONFIG_FILENAME_PATTERN, tag));
			if(!(Files.exists(componentConfigDir) && Files.exists(xmlFile))){
				throw new FileNotFoundException(xmlFile.normalize().toString());
//				throw new FileNotFoundException(xmlFile.toRealPath().toString());
			}
			
			retval = Optional.of(BeanMarshal.loadFromXML(configObjClass, xmlFile));
		} catch (JAXBException | FileNotFoundException e) {
			logger.log(e);
			retval = Optional.empty();
		}
		return retval;
	}
	/**
	 * @param clazz class.name will be used as configuration parent
	 * @param tag configuration object will be saved into tag.xml
	 * @param configObj configuration object to be saved
	 */
	@Deprecated
	private <T> void saveConfig(Class<?> clazz, String tag, T configObj){
		Path xmlFile = getDataPath(clazz, tag);
		try {
			BeanMarshal.saveToXML(configObj, xmlFile);
		} catch (JAXBException e) {
			logger.log(e);
		}
	}
	
	/**
	 * Load an instance of <code>configObjClass</code> from tag.xml. In case of any exception occurs, an empty result is what you got.
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
			logger.log(e);
			retval = Optional.empty();
		}
		return retval;
	}
	
	/**
	 * Save <code>configObj</code> as XML file into tag.xml
	 * <p>
	 * Q: What if tag.xml has already existed? 
	 * A: It will be overwritten. This behavior would be modified by specifying {@link ConfigExistedPolicy} of this {@link Config} instance.
	 * </p>
	 * @param tag
	 * @param configObj
	 */
	public <T> void save(String tag, T configObj){
		Path xmlFile = getDataPath(tag);
		try {
			BeanMarshal.saveToXML(configObj, xmlFile);
		} catch (JAXBException e) {
			logger.log(e);
		}
	}
	
	//------------------------------------------------
	/**
	 * The one and only getInstance() method;
	 * @return 
	 */
	@Deprecated
	static Config inst(){
		return instance;
	}
	
	/**
	 * Generate custom <code>Config</code> instances with the <code>configFactory</code>. Subclass is encouraged to 
	 * override this method to provide their own implementation of Config instances. 
	 * @param componentConfigDirStr
	 * @param configFactory factory used to genereate custom <code>Config</code> objects
	 * @return
	 */
	protected static Config of(String componentConfigDirStr, Function<String, Config> configFactory){
		return instances.computeIfAbsent(componentConfigDirStr, configFactory);
	}
	
	/**
	 * @param componentConfigDir directory name for each stand alone component that wants to store configurations;
	 * @return a new {@link Config} instance, or an existing one with the same <code>componentConfigDir</code>
	 */
	public static Config of(String componentConfigDirStr){
		return instances.computeIfAbsent(componentConfigDirStr, Config::new);
//		String configRealPath = getDataPath(componentConfigDirStr).toRealPath().toString();
//		Path configRealPath = getDataPath().resolve(componentConfigDirStr).toRealPath();
//		return instances.computeIfAbsent(configRealPath, key -> new Config(componentConfigDirStr));
	}
	/**
	 * @param clazz clazz.name will be used as <code>componentConfigDir</code>
	 * @return a new {@link Config} instance or existing one;
	 */
	public static Config of(Class<?> clazz){
		Objects.requireNonNull(clazz);
//		if(Arrays.asList(clazz.getInterfaces()).contains(MsgLogger.class)){
//			
//		}
		return of(clazz.getName());
	}
	/**
	 * @param obj obj.class.name will be used as <code>componentConfigDir</code>
	 * @return a new {@link Config} instance or existing one;
	 */
	public static Config of(Object obj){
		Objects.requireNonNull(obj);
//		if(obj instanceof MsgLogger){
//			of(obj.getClass().getName()).setLogger((MsgLogger)obj);
//		}
		return of(obj.getClass().getName());
	}
//	/**
//	 * Invoke {@link #setLogger(MsgLogger)} and return this {@link #Config} instance;
//	 * @param logger
//	 * @return
//	 */
//	public Config withLogger(MsgLogger logger){
//		setLogger(logger);
//		return this;
//	}
	//------------------------------------------------
	
	//---------------------------------------
	/**
	 * Root path that holds all configuration files; for the default {@link #Config} class, the data path
	 * is <code>./data</code>
	 * <pre>
	 * Subclass of {@link Config} should override this method to customize the root directory to store data.
	 * </pre>
	 * @return
	 */
	protected Path getDataRoot(){
		return Paths.get(Msg.get(this, "data.path"));
	}
	public void setLogger(MsgLogger logger) {
		this.logger = logger;
	}

	/**
	 * Parent directory for all configuration files;
	 */
	private Path configDir;
	/**
	 * Path of parent directory for component-specific configurations, will be resolved relative to <code>configDir</code> when new {@link Config} instance is created.
	 */
	private Path componentConfigDir;
//	private static Path configDir = Paths.get(Msg.get(Config.class, "data.path"));
//	static{
//		createIfNotExists(configDir, Files::createDirectories);
//	}
	private MsgLogger logger = MsgLogger.defaultLogger();
	@Deprecated
	private static final Config instance = new Config();
	private static final String CONFIG_FILENAME_PATTERN = Msg.get(Config.class, "config.fileName.pattern");
	public static interface CreatePathMethod{
		Path create(Path p) throws IOException;
	}
	private static Map<String, Config> instances = new HashMap<>();
//	private static Map<Path, Config> instances = new HashMap<>();
	
//	public static class ConfigFactory{
//		private ConfigFactory(){
//			//
//		}
//		
//		public static Config basedOn(String dataPathDir){
//		
//			return new Config(){
//				@OVerride
//				protected Path getDataRoot(){
//					return Paths.get(dataPathDir);
//				}
//			}
//		}
//		
//		private static Map<String, Config> configInstances = new HashMap<>();
//		static{
//			String appConfig = Msg.get(Config.class, "data.path");
//			configInstances.put(appConfig, Config.of(Config.class));
//			String pluginConfig = Msg.get(Plugin.class, "plugin.root.dir");
//			configInstances.put(pluginConfig, PluginConfig.of(Plugin.class));
//		}
//	}
	
	private static class CustomConfig extends Config{

		
		protected CustomConfig(String dataRootDir) {
			super();
			this.dataRoot = Paths.get(dataRootDir);
		}
		@Override
		protected Path getDataRoot() {
			return dataRoot;
		}
		private final Path dataRoot;
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

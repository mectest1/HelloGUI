package com.mec.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.glass.ui.CommonDialogs.Type;

public class Plugins {

	
	//
	/**
	 * Load plugin with the specified plugin name. Jar files will be searched in {@link #PLUGIN_ROOT_DIR}/<code>tag</code>/
	 * 
	 * <p>
	 * After load completes, the plugin will be started with class specified in <code>pluginConfig.xml<code> 
	 * and (as you may have guessed) method annotated with {@link PluginEntryMehtod} as entry point.
	 * </p>
	 * @param pluginName
	 */
	public static void load(String pluginName){
		Plugin plugin = plugins.computeIfAbsent(pluginName, Plugin::new);
		plugin.setLogger(logger);
//		PluginConfig.of(pluginName, PluginConfig::new).setLogger(logger);
		PluginConfig.of(pluginName).setLogger(logger);
//		PluginConfigBean configBean = PluginConfig.of(pluginName).load(PLUGIN_CONFIG_FILE, PluginConfigBean.class).get();
		PluginConfigBean configBean = PluginConfig.of(pluginName).load(Plugin.PLUGIN_CONFIG_FILE, PluginConfigBean.class).get();
		
		try {
			Class<?> entryClass = plugin.loadClass(configBean.getEntryClass());
//			Method entryMethod = Arrays.stream(entryClass.getMethods())	//<- return all the public methods
			Method entryMethod = Arrays.stream(entryClass.getDeclaredMethods())
					.filter(method -> 
						null != method.getAnnotation(PluginEntryMehtod.class)
					)
					.findAny()
					.orElseThrow(() -> new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.noEntryMethod"), entryClass.getName())));
//			Method entryMethod = null;
//			for(Method method : entryClass.getDeclaredMethods()){
//				PluginEntryMehtod pem = method.getAnnotation(PluginEntryMehtod.class);
//				Annotation[] annotations = method.getAnnotations();
//				if(null != method.getAnnotation(PluginEntryMehtod.class)){
//					entryMethod = method;
//					break;
//				}
//			}
//			if(null == entryMethod){
//				throw new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.noEntryMethod"), entryClass.getName()));
//			}
				
//				
//				
//			entryMethod.setAccessible(true);
//			
//			Class<?>[] parameterTypes = entryMethod.getParameterTypes();
//			if(0 == parameterTypes.length){
//				entryMethod.invoke(obj);
//			}else if(1 == parameterTypes.length && PluginContext.class.equals(parameterTypes[0])){
////				PluginContext pc = new PluginContext();
//				entryMethod.invoke(obj, plugin.getPluginContext());
//			}else{
//				throw new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.method.wrongArgsNum"), entryMethod.getName()));
//			}
			Object obj = entryClass.newInstance();
			start(obj, entryMethod, plugin.getPluginContext());
		} catch (ClassNotFoundException
				|IllegalAccessException
				|InstantiationException
				|IllegalArgumentException
				|InvocationTargetException
				e) {
			logger.log(e);
		}
	}
	
	/**
	 * Start the plugin by invoking the <code>entryMethod</code> on <code>entryObj</code>
	 * e.g. One of these method will be invoked (depends on the entryMethod's signature):
	 * <ul>
	 * 
	 * <li><code>entryObj.entryMethod()</code></li>
	 * <li><code>entryObj.entryMethod(pc)</code></li>
	 * </ul>
	 * @param entryObj
	 * @param entryMethod
	 * @param pc
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static void start(Object entryObj, Method entryMethod, PluginContext pc) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		
		entryMethod.setAccessible(true);
		
		Class<?>[] parameterTypes = entryMethod.getParameterTypes();
		if(0 == parameterTypes.length){
			entryMethod.invoke(entryObj);
		}else if(1 == parameterTypes.length && PluginContext.class.equals(parameterTypes[0])){
//			PluginContext pc = new PluginContext();
//			entryMethod.invoke(obj, plugin.getPluginContext());
			entryMethod.invoke(entryObj, pc);
		}else{
			throw new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.method.wrongArgsNum"), entryMethod.getName()));
		}
	}
	
	
//	public static void load(Path pluginPath){
//	}
	
	public static void setLogger(MsgLogger logger){
		Plugins.logger = logger;
	}
	
	private static MsgLogger logger = MsgLogger.defaultLogger();
	private static Map<String, Plugin> plugins = new HashMap<>();
//	private static final String PLUGIN_CONFIG_FILE = Msg.get(Plugins.class, "plugin.config.file");
	/**
	 * Root directory to accommodate all plugins, default to <code>./plugins/</code>
	 */
//	private static final Path PLUGIN_ROOT_DIR = Paths.get(Msg.get(Plugins.class, "plugin.root.dir")); 
	
	/**
	 * Plugin entry class should have at least one of its method annotated with this annotation,
	 * whose arguments may be empty, or one argument with type of <code>{@link PluginContext}</code>.
	 * e.g.: The entry method should have one of these two forms: 
	 * <ul>
	 * <li>{ReturnType} methodName()</li>
	 * <li>{ReturnType} methodName(PluginContext pc}</li>
	 * <ul>
	 * @author MEC
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)	//<- needed, else its default value is RetentionPolicy.CLASS
	@Documented
	@Target({ElementType.METHOD})
	public @interface PluginEntryMehtod{
		
	}
	
	@XmlRootElement(name="pluginConfig")
	static class PluginConfigBean{
		private String entryClass;
		public String getEntryClass() {
			return entryClass;
		}
		public void setEntryClass(String entryClass) {
			this.entryClass = entryClass;
		}
		@Override
		public String toString() {
			return "PluginConfigBean [entryClass=" + entryClass + "]";
		}
	}
	

	
	/**
	 * Provides plugin context for the plugin entry method
	 * @author MEC
	 *
	 */
	public static class PluginContext{
		
	}
	
	
	static class PluginConfig extends Config{
		
//		private PluginConfig(){
//			super();
//		}
		
		public PluginConfig(String pluginDirectory){
			super(pluginDirectory);
		}
		
//		@Override
		public static Config of(String pluginName){
			return Config.of(pluginName, PluginConfig::new);
		}

		@Override
		protected Path getDataRoot() {
//			return super.getDataRoot();
			return PLUGIN_CONFIG_PATH;
		}
		
		private static final Path PLUGIN_CONFIG_PATH = Paths.get(Msg.get(Plugin.class, "plugin.root.dir"));
	}
	
	//----------------------------------------------
	/**
	 * <code>Plugin</code> is a logical entity which resides in <code>./plugins/${pluginName}</code>, and consists of components listed below:
	 * <ul>
	 * <li>multiple <code>/*.jar</code></li>
	 * <li>One and only <code>pluginConfig.xml</code>, which specified which entry class to run for this plugin</li>
	 * <li></li>
	 * </ul>
	 * @author MEC
	 *
	 */
	static class Plugin {

		public Plugin(String pluginName){
			this(PLUGIN_ROOT_DIR.resolve(pluginName));
		}
		
		private Plugin(Path pluginPath){
			this(pluginPath, PLUGIN_ROOT);
			setName(pluginPath.getFileName().toString());
		}
		
		protected Plugin(Path pluginPath, Plugin parentPlugin){
			this(pluginPath, parentPlugin.getClassLoader());
//			this.parentPlugin = parentPlugin;
			this.parentPlugin = Optional.ofNullable(parentPlugin);
			
		}
		
		/**
		 * For each 
		 * @param pluginPath
		 * @param parent
		 */
		private Plugin(Path pluginPath, ClassLoader parent){
			try {
				if(parent.equals(ClassLoader.getSystemClassLoader())){	//If it's PLUGIN_ROOT, then create ./lib if it doesn't exist;
					Config.of(this).createIfNotExists(pluginPath, Files::createDirectories);
				}
				if (!(Files.exists(pluginPath)
//					|| parent.equals(ClassLoader.getSystemClassLoader())	//PLUGIN_ROOT.parent: system class loader
						)) {
					throw new FileNotFoundException(
							String.format(Msg.get(this, "exception.plugin.not.found"), pluginPath.toString()));
				}
				
				//load jars resides directly in the plugin directory
				List<Path> jars = new ArrayList<>();
				try (Stream<Path> ps = Files.list(pluginPath)) {
					ps.filter(JarTool::isJarFile).forEach(jars::add);
				}
//				Consumer<Path> addJars = p -> }try (Stream<Path> ps = Files.list(p)) {
//					ps.filter(JarTool::isJarFile).forEach(jars::add);
//				};
				
				//load jars resides in the ./lib directory
				Path pluginLib = pluginPath.resolve(Msg.get(this, "lib.dir"));
				if(Files.exists(pluginLib) && Files.isDirectory(pluginLib)){
					try (Stream<Path> ps = Files.list(pluginLib)) {
						ps.filter(JarTool::isJarFile).forEach(jars::add);
					}
				}
				
				//initialize the URLClassLoader
//				URL[] jarURLs = (URL[])jars.stream().map(p -> p.toUri().toURL()).toArray();
				List<URL> jarURLs = new ArrayList<>(jars.size());
				for(Path jarFile : jars){
					jarURLs.add(jarFile.toUri().toURL());
				}
				ucl = new URLClassLoader(jarURLs.toArray(new URL[0]), parent);
			} catch (IOException e) {
				logger.log(e);
				throw new IllegalArgumentException(e);
			}
		}
		
		public Class<?> loadClass(String className) throws ClassNotFoundException{
			try{
				Optional<Plugin> parent = getParent();
//				parent.ifPresent(p -> p.loadClass(className));
//				if(null != parent){
				if(parent.isPresent()){
					return parent.get().loadClass(className);
				}
			}catch(ClassNotFoundException e){}
			
			return ucl.loadClass(className);
		}
	
		protected Optional<Plugin> getParent(){
			return parentPlugin;
		}
		private ClassLoader getClassLoader(){
			return ucl;
		}
		/**
		 * Close this plugin, release any .jar files that have been opened by this plugin manager.
		 */
		public void close(){
			try {
				ucl.close();
			} catch (IOException e) {
				logger.log(e);
			}
		}
		
		
//		public static Plugin of(String pluginName){
//			
//			return null;
//		}
		
		protected PluginContext getPluginContext(){
			return new PluginContext();
		}
		private void setName(String name){
			this.name = name;
		}
		
		//-----------------------------
		
		@Override
		public String toString() {
			String urls = Arrays.stream(ucl.getURLs()).map(URL::toString).collect(Collectors.joining(",", "[", "]"));
			return "Plugin [name=" + name + ", urls=" + urls + "]";
		}

		public void setLogger(MsgLogger logger) {
			this.logger = logger;
		}

		private Optional<Plugin> parentPlugin = Optional.empty();
		private MsgLogger logger = MsgLogger.defaultLogger();
		private URLClassLoader ucl;
		private String name;
		private static final Plugin PLUGIN_ROOT = new Plugin(Paths.get(Msg.get(Plugin.class, "lib.dir")), ClassLoader.getSystemClassLoader());
		private static final Path PLUGIN_ROOT_DIR = Paths.get(Msg.get(Plugin.class, "plugin.root.dir")); 
		protected static final String PLUGIN_CONFIG_FILE = Msg.get(Plugin.class, "plugin.config.file");
		static{
			PLUGIN_ROOT.setName(Msg.get(Plugin.class, "plugin.root.name"));
		}
	}
	
}
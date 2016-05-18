package com.mec.resources;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlRootElement;

import com.mec.application.beans.PluginInfo.PluginContext;
import com.mec.application.beans.PluginInfo.PluginStart;
import com.mec.application.beans.PluginService.ViewService;
//import com.mec.application.beans.PluginAnnotations.Start;
import com.mec.resources.Config.ConfigEndpoint;


public class Plugins {

	
	private static void loadSync(String pluginName){
		loadPlugin(pluginName);
	}
	private static void unloadSync(String pluginName){
		unloadPlugin(pluginName);
	}
	
	public static List<String> listPlugins(){
		List<String> retval = Arrays.asList(Plugin.PLUGIN_ROOT_DIR.toFile().list());
		return retval;
	}
	
//	public static Optional<String> getDispName(String pluginName){
//		Plugin plugin = plugins.computeIfAbsent(pluginName, Plugin::new);
//		Optional<String> retval = Optional.ofNullable(plugin).map(p -> 
//			Optional.ofNullable(p.getDispName()).orElse(p.getDispName())
//		);
//		return retval;
//	}
	
	public static void load(String pluginName){
		Plugin plugin = plugins.computeIfAbsent(pluginName, Plugin::new);
		if(plugin.isAsynchronousLoad()){
			loadAsync(pluginName);
		}else{
			loadSync(pluginName);
		}
	}
	
	public static void unload(String pluginName){
		Plugin plugin = plugins.computeIfAbsent(pluginName, Plugin::new);
		if(plugin.isAsynchronousLoad()){
			unloadAsync(pluginName);
		}else{
			unloadSync(pluginName);
		}
	}
	
	//
	/**
	 * Load plugin with the specified name asynchronously.
	 * @param pluginName
	 */
//	@Deprecated
	private static void loadAsync(String pluginName){
		CompletableFuture.runAsync(() -> Plugins.loadPlugin(pluginName))
//			.whenCompleteAsync((v, error) -> Plugins.startPlugin(entryObj, entryMethod, pc));
			;
	}
	
	/**
	 * Unload plugin with the specified name asynchronously.
	 * @param pluginName
	 */
//	@Deprecated
	private static void unloadAsync(String pluginName){
		CompletableFuture.runAsync(() -> Plugins.unloadPlugin(pluginName));
	}
	/**
	 * Load plugin with the specified plugin name. Jar files will be searched in {@link #PLUGIN_ROOT_DIR}/<code>tag</code>/
	 * 
	 * <p>
	 * After load completes, the plugin will be started with class specified in <code>pluginConfig.xml<code> 
	 * and (as you may have guessed) method annotated with {@link PluginStart} as entry point.
	 * </p>
	 * @param pluginName
	 */
	private static void loadPlugin(String pluginName){
		Plugin plugin = plugins.computeIfAbsent(pluginName, Plugin::new);
		plugin.setLogger(logger);
		PluginConfig.of(pluginName).setLogger(logger);
//		Config.of(pluginName).setLogger(logger);
//		Config.of.of(pluginName).setLogger(logger);
		
		try {
			plugin.load();
			plugin.start();
//			PluginConfigBean configBean = PluginConfig.of(pluginName).load(Plugin.PLUGIN_CONFIG_FILE, PluginConfigBean.class)
////			PluginConfigBean configBean = Config.of(pluginName).load(Plugin.PLUGIN_CONFIG_FILE, PluginConfigBean.class)
//					.orElseThrow(() -> new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.pluginConfig.notFound"), pluginName)));
//			Class<?> entryClass = plugin.loadClass(configBean.getEntryClass());
////			Method entryMethod = Arrays.stream(entryClass.getMethods())	//<- return all the public methods
//			Method entryMethod = Arrays.stream(entryClass.getDeclaredMethods())
//					.filter(method -> 
//						null != method.getAnnotation(PluginStart.class)
//					)
//					.findAny()
//					.orElseThrow(() -> new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.noEntryMethod"), entryClass.getName())));
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
//			Object obj = entryClass.newInstance();
//			startPlugin(obj, entryMethod, plugin.getPluginContext());
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
	 * Unload plugin with name <code>pluginName</code>
	 * @param pluginName
	 */
	private static void unloadPlugin(String pluginName){
		Objects.requireNonNull(pluginName);
		Plugin plugin = plugins.get(pluginName);
		if(null == plugin){
			logger.log(Msg.get(Plugins.class, "msg.unload.plugin.missing"), pluginName);
			return;
		}
		plugin.stop();
		plugins.remove(pluginName);
	}
////	private static void startPlugin(Object entryObj, Method entryMethod, PluginContext pc) 
//	private static void startPlugin(Plugin plugin) 
//			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
//		plugin.start();
////		entryMethod.setAccessible(true);
////		
////		Class<?>[] parameterTypes = entryMethod.getParameterTypes();
////		if(0 == parameterTypes.length){
////			entryMethod.invoke(entryObj);
////		}else if(1 == parameterTypes.length && PluginContext.class.equals(parameterTypes[0])){
//////			PluginContext pc = new PluginContext();
//////			entryMethod.invoke(obj, plugin.getPluginContext());
////			entryMethod.invoke(entryObj, pc);
////		}else{
////			throw new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.method.wrongArgsNum"), entryMethod.getName()));
////		}
//	}
	
	
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
	
	@XmlRootElement(name="pluginConfig")
	static class PluginConfigBean{
		private String entryClass;
//		private String dispName;
		/**
		 * Set if this plugin will be loaded asynchronous;
		 * <p>
		 * Note that if FX Stage is required for this plugin, then set <code>loadAsync</code>
		 * to <code>true</false> will result into this error message:
		 * <dd>
		 * IllegalStateException: Not on FX application thread;
		 * </dd>
		 * Thus you may need to set this flag only when no FX Stage is needed, which means
		 * you may barely needs to use it at all.
		 * </p>
		 */
		private boolean loadAsync = false;	//load asynchronous
		public String getEntryClass() {
			return entryClass;
		}
		public void setEntryClass(String entryClass) {
			this.entryClass = entryClass;
		}
//		public String getDispName() {
//			return dispName;
//		}
//		public void setDispName(String name) {
//			this.dispName = name;
//		}
		public boolean isLoadAsync() {
			return loadAsync;
		}
		@Override
		public String toString() {
			return "PluginConfigBean [entryClass=" + entryClass + ", loadAsync=" + loadAsync + "]";
		}
		public void setLoadAsync(boolean loadAsync) {
			this.loadAsync = loadAsync;
		}
	}
	
	
	private static class PluginContextImpl implements PluginContext{

		Plugin plugin;
		ViewService vs;
		PluginContextImpl(Plugin plugin){
			this.plugin = plugin;
			vs = new ViewServiceForPlugin(plugin);
		}
		@Override
		public ViewService getViewService(){
			return vs;
		}
		
	}
	
	private static class ViewServiceForPlugin implements ViewService{
		
		Plugin plugin;
		ViewServiceForPlugin(Plugin plugin){
			this.plugin = plugin;
		}
		@Override
		public void showNewStage(String viewURL, String title) {
			ViewFactory.showNewStage(plugin.getClassLoader(), viewURL, title);
		}

//		@Override
//		public <ViewRoot, Controller> LoadViewResult<ViewRoot, Controller> loadView(String viewURL) {
//			return ViewFactory.loadViewDeluxe(plugin.getClassLoader(), viewURL);
//		}
//		
		
	}
	
	static class PluginConfig{
		
//		private PluginConfig(){
//			super();
//		}
		
//		protected PluginConfig(String pluginDirectory){
//			super(pluginDirectory);
//		}
		
////		@Override
//		public static Config of(String pluginName){
//			return Config.of(pluginName, PluginConfig::new);
//		}
		public static ConfigEndpoint of(String pluginName){
//			return Config.pluginData();
			return Config.plugins().of(pluginName);
		}

//		@Override
//		protected Path getDataRoot() {
////			return super.getDataRoot();
//			return PLUGIN_CONFIG_PATH;
//		}
//		
//		private static final Path PLUGIN_CONFIG_PATH = Paths.get(Msg.get(Plugin.class, "plugin.root.dir"));
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
	public static class Plugin {

		private Plugin(String pluginName){
			this(PLUGIN_ROOT_DIR.resolve(pluginName));
		}
		
		private Plugin(Path pluginPath){
			this(pluginPath, PLUGIN_ROOT);
			setName(pluginPath.getFileName().toString());
			configBean = PluginConfig.of(name).load(Plugin.PLUGIN_CONFIG_FILE, PluginConfigBean.class)
//					PluginConfigBean configBean = Config.of(pluginName).load(Plugin.PLUGIN_CONFIG_FILE, PluginConfigBean.class)
					.orElseThrow(() -> new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.pluginConfig.notFound"), name)));
//			if(null != configBean.getDispName() && !configBean.getDispName().isEmpty()){
//				setName(configBean.getDispName());
//			}else{
//				setName(pluginPath.getFileName().toString());
//			}
//			setName(pluginPath.getFileName().toString());
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
//					Config.of(this).createIfNotExists(pluginPath, Files::createDirectories);
					Config.data().createIfNotExists(pluginPath, Files::createDirectories);
					
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
				
				setLogger(Plugins.logger);
			} catch (IOException e) {
				logger.log(e);
				throw new IllegalArgumentException(e);
			}
		}
		
		/**
		 * Load classes from the plug-in Jars first, so some functions can be overwritten by plug-in itself.
		 * @param className
		 * @return
		 * @throws ClassNotFoundException
		 */
		private Class<?> loadClass(String className) throws ClassNotFoundException{
//			try{
//				Optional<Plugin> parent = getParent();
////				parent.ifPresent(p -> p.loadClass(className));
////				if(null != parent){
//				if(parent.isPresent()){
//					return parent.get().loadClass(className);
//				}
//			}catch(ClassNotFoundException e){}
//			
//			return ucl.loadClass(className);
			try{
				return ucl.loadClass(className);
			}catch(ClassNotFoundException e){}
			
			Optional<Plugin> parent = getParent();
			if(parent.isPresent()){
				return parent.get().loadClass(className);
			}else{
				throw new ClassNotFoundException(className);
			}
		}
	
		protected Optional<Plugin> getParent(){
			return parentPlugin;
		}
		private ClassLoader getClassLoader(){
			return ucl;
		}
		//------------------------------------------------------
		/**
		 * Try to load the <code>entryClass</code> (as specified in <code>pluginConfig.xml</code>) from .jar files or directories;
		 * @throws ClassNotFoundException
		 */
		public void load() throws ClassNotFoundException{
			
			entryClass = loadClass(configBean.getEntryClass());
//					Method entryMethod = Arrays.stream(entryClass.getMethods())	//<- return all the public methods
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
		 * @throws InstantiationException 
		 */
		public void start() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			Method entryMethod = Arrays.stream(entryClass.getDeclaredMethods())
					.filter(method -> 
					null != method.getAnnotation(PluginStart.class)
							)
					.findAny()
					.orElseThrow(() -> new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.noEntryMethod"), entryClass.getName())));
			entryMethod.setAccessible(true);
			
			Object entryObj = this.entryClass.newInstance();
			Class<?>[] parameterTypes = entryMethod.getParameterTypes();
			if(0 == parameterTypes.length){
				entryMethod.invoke(entryObj);
			}else if(1 == parameterTypes.length && PluginContext.class.equals(parameterTypes[0])){
//				PluginContext pc = new PluginContext();
//				entryMethod.invoke(obj, plugin.getPluginContext());
				entryMethod.invoke(entryObj, getPluginContext());
			}else{
				throw new IllegalArgumentException(String.format(Msg.get(Plugins.class, "exception.method.wrongArgsNum"), entryMethod.getName()));
			}
		}
		
		/**
		 * Stop & Close this plugin, release any .jar files that have been opened by this plugin manager.
		 */
		public void stop(){
			try {
				ucl.close();
			} catch (IOException e) {
				logger.log(e);
			}
		}
		//------------------------------------------------------
		
//		public static Plugin of(String pluginName){
//			
//			return null;
//		}
		
		protected PluginContext getPluginContext(){
//			return new PluginContextImpl(this);
			if(null == pctx){
				pctx = new PluginContextImpl(this);
			}
			return pctx;
		}
		private void setName(String name){
			this.name = name;
		}
		public boolean isAsynchronousLoad(){
			return configBean.isLoadAsync();
		}
//		public String getDispName(){
//			return configBean.getDispName();
//		}
		//-----------------------------
		
		@Override
		public String toString() {
			String urls = Arrays.stream(ucl.getURLs()).map(URL::toString).collect(Collectors.joining(",", "[", "]"));
			return "Plugin [name=" + name + ", urls=" + urls + "]";
		}

		public void setLogger(MsgLogger logger) {
			this.logger = logger;
		}

		private PluginConfigBean configBean;
		private PluginContext pctx;
		private Optional<Plugin> parentPlugin = Optional.empty();
		private MsgLogger logger = MsgLogger.defaultLogger();
		private URLClassLoader ucl;
		private String name;
		private Class<?> entryClass;
//		private List<Plugin> pluginList;
		//
		private static final Plugin PLUGIN_ROOT = new Plugin(Paths.get(Msg.get(Plugin.class, "lib.dir")), ClassLoader.getSystemClassLoader());
		private static final Path PLUGIN_ROOT_DIR = Paths.get(Msg.get(Plugin.class, "plugin.root.dir")); 
		protected static final String PLUGIN_CONFIG_FILE = Msg.get(Plugin.class, "plugin.config.file");
		static{
			PLUGIN_ROOT.setName(Msg.get(Plugin.class, "plugin.root.name"));
		}
	}
	
}

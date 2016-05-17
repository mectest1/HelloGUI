package com.mec.app.plugin.filemanager.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * <p>
 * Get messages from the resource bundle file.
 * Common format:
 * <ul>
 * <li>{className}.{messageTag}</li>
 * <li>{className}.exception.{exceptionMessage}</li>
 * </ul>
 * <p>
 * @author MEC
 *
 */
public class Msg {

//	private Msg(String resourceBundlePath){
//		resources = ResourceBundle.getBundle(resourceBundlePath);
//	}
//	
//	private Msg(ResourceBundle resourceBundle){
//		this.resources = resourceBundle;
//	}
	
//	public static Messages getInstance(){
//		return instance;
//	}
	
	/**
	 * Get with full name: className + tagName
	 * @param key
	 * @return
	 */
	protected static Optional<String> getFull(String key){
		
		try {
			return Optional.of(resources.getString(key));
//			return Optional.of(getResources().getString(key));
//			return Optional.of(defaultInstance.getResources().getString(key));
		} catch (MissingResourceException e) {
			return Optional.empty();
		}
	}
	
	
	/**
	 * Note heavy load time this method would impose on program execution
	 * @param tag
	 * @return
	 */
	@Deprecated
	public static String get(String tag){
//		return getFull(String.format("%s.%s", getCaller.getCallerClassName(3), tag));
		String key = String.format(TAG_COMBINE_PATTERN, getCaller.getCallerClassName(3), tag);
		Optional<String> val = getFull(key);
		if(val.isPresent()){
			return val.get();
		}else{
			return key;
		}
	}
	
	public static String get(Class<?> clazz, String tag){
		String key = String.format(TAG_COMBINE_PATTERN, clazz.getName(), tag);
		Optional<String> val = getFull(key);
//		if(val.isPresent()){
//			return val.get();
//		}else{
//			return key;
//		}
		return val.orElse(key);
	}
	/**
	 * Same as {@link #get(Class, String)}, only returns an {@link java.util.Optional} instead of  afull string
	 * @param clazz
	 * @param tag
	 * @return
	 */
	public static Optional<String> getOptional(Class<?> clazz, String tag){
		String key = String.format(TAG_COMBINE_PATTERN, clazz.getName(), tag);
		Optional<String> val = getFull(key);
		return val;
	}
	
	public static String get(Object obj, String tag){
		return get(obj.getClass(), tag);
	}
	
	public static <R> R get(Class<?> clazz, String tag, Function<String, R> converter, R defaultValue){
		return get(clazz, tag, converter).orElse(defaultValue);
	}
	private static <R> Optional<R> get(Class<?> clazz, String tag, Function<String, R> converter){
		if(null == converter){
			return Optional.empty();
		}
		String strval = get(clazz, tag);
		R retval = null;
		try {
			retval = converter.apply(strval);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return Optional.ofNullable(retval);
	}
	
	/**
	 * <p>
	 * Instead of simply return the property value as a string (as specified in the message bundle),
	 * this method will try to convert this string value to a designated type. In case of the converter 
	 * is null, or exception happens in the converting process a null will be returned.
	 * <p>
	 * <strong>Note:</strong> If the tag value is not specified in the message bundle, a null value will 
	 * be passed into converter. It's the converter's responsibility to treat this null value correctly.
	 * </p>
	 * </p>
	 * <p>If what you want is simply string value, use {@link #get(Object, String)} instead.
	 * </p>
	 * @param obj class of this object will be used as prefix in message bundle.
	 * @param tag tag name
	 * @param converter a Function<STring, R> converter, with a single method: <R> R apply(String value)
	 * @param defaultValue defualt return value, which will apply when the converter is null or error occurred during the convert process
	 * @return
	 */
	public static <R> R get(Object obj, String tag, Function<String, R> converter, R defaultValue){
//		return get(obj.getClass(), tag, converter, defaultValue);
		return get(obj.getClass(), tag, converter).orElse(defaultValue);
	}
	
	/**
	 * Get properties as list, e.g.:
	 * <ul>
	 * <li>prop.name</li>
	 * <li>prop.name.1</li>
	 * <li>prop.name.2</li>
	 * </ul>
	 * @param clazz
	 * @param tag
	 * @return
	 */
	public static List<String> getList(Class<?> clazz, String tag){
		List<String> retval = new ArrayList<String>();
		String baseKey = String.format(TAG_COMBINE_PATTERN, clazz.getName(), tag);
		getFull(baseKey).ifPresent(e -> retval.add(e));
		
		int count = 0;
		Optional<String> value = null;
		while(true){
			value = getFull(String.format(TAG_COMBINE_PATTERN, baseKey, ++count));
			if(value.isPresent()){
				retval.add(value.get());
			}else{
				break;
			}
		}
		return retval;
	}
	
	public static List<String> getList(Object obj, String tag){
		return getList(obj.getClass(), tag);
	}
	
	public static String getExpMsg(Object obj, String tag){
		String key = String.format("%s.exception.%s", obj.getClass().getName(), tag);
		
		Optional<String> val = getFull(key);
		if(val.isPresent()){
			return val.get();
		}else{
			return key;
		}
	}
	
	
	//------------------------------------------------------
//	protected ResourceBundle getResources(){
//		return resources;
//	}
//	private static Msg getDefault(){
//		return defaultInstance;
//	}
//	/**
//	 * Get {@link Msg} instance from the specified <code>resourcePath</code>.
//	 * Typical usage:
//	 * <code><pre>
//	 * Msg m = Msg.of("com.foo.bar.Resources");
//	 * m.get(this, "derp");
//	 * </pre></code>
//	 * @param resourcePath
//	 * @return
//	 */
//	public static Msg of(String resourcePath){
//		return msgs.computeIfAbsent(resourcePath, Msg::new);
//	}
//	protected Optional<String> getFull(String key){
//		
//	}
	
//	private static final String MESSAGES = "com.mec.resources.MessagesBundle";
	private static final String MESSAGES = "com.mec.app.plugin.filemanager.resources.MessagesBundle";
	private static ResourceBundle resources = ResourceBundle.getBundle(MESSAGES);
//	private ResourceBundle resources;	//ResourceBundle.getBundle(MESSAGES);
//	private static Msg defaultInstance = new Msg(ResourceBundle.getBundle(MESSAGES));
//	private static final Map<String, Msg> msgs = new HashMap<>();
//	static{
//		msgs.put(MESSAGES, defaultInstance);
//	}
	private static final String TAG_COMBINE_PATTERN = "%s.%s";
	private static final GetCallerClassNameMethod getCaller = new SecurityManagerMethod();
	
	
	
	private static abstract class GetCallerClassNameMethod{
		public abstract String getCallerClassName(int callStackDepth);
		public abstract String getMethodName();
	}
	
	public static class ThreadStackTraceMethod extends GetCallerClassNameMethod{

		@Override
		public String getCallerClassName(int callStackDepth) {
			return Thread.currentThread().getStackTrace()[callStackDepth].getClassName();
		}

		@Override
		public String getMethodName() {
			return "Current Thread Stack Track";
		}
		
	}
	public static class ThrowableStackTrackMethod extends GetCallerClassNameMethod{
		
		@Override
		public String getCallerClassName(int callStackDepth) {
			return new Throwable().getStackTrace()[callStackDepth].getClassName();
		}
		
		@Override
		public String getMethodName() {
			return "Throwable Stack Track";
		}
		
	}
	
	public static class SecurityManagerMethod extends GetCallerClassNameMethod{

		@Override
		public String getCallerClassName(int callStackDepth) {
			return callerManager.getCallerClassName(callStackDepth);
		}

		@Override
		public String getMethodName() {
			return "SecurityManager";
		}
		
		private static final CallerSecurityManager callerManager = new CallerSecurityManager(); 
		
		static class CallerSecurityManager extends SecurityManager{
			public String getCallerClassName(int callStackDepth){
				return getClassContext()[callStackDepth].getName();
			}
		}
		
	}
	
	
}

class Derp{
	class Derp2{
		class Derp3{
			class Derp5{
				
			}
		}
	}
	
	class Derp4{
		
	}
}


/**
 References:
 [1] http://stackoverflow.com/questions/421280/how-do-i-find-the-caller-of-a-method-using-stacktrace-or-reflection
 [2] 
 
 
 
*/

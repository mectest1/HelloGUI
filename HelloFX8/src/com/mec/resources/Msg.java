package com.mec.resources;

import java.util.ArrayList;
import java.util.List;
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

	private Msg(){
//		resources = ResourceBundle.getBundle(MESSAGES);
		
	}
	
//	public static Messages getInstance(){
//		return instance;
//	}
	
	/**
	 * Get with full name: className + tagName
	 * @param key
	 * @return
	 */
//	protected static String getFull(String key){
	protected static Optional<String> getFull(String key){
		
		try {
			return Optional.of(resources.getString(key));
		} catch (MissingResourceException e) {
//			return key;
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
//		return getFull(String.format(getCaller.getCallerClassName(1), tag));
		//Stacks:
//		class com.mec.resources.Msg$SecurityManagerMethod$CallerSecurityManager,
//		class com.mec.resources.Msg$SecurityManagerMethod,
//		class com.mec.resources.Msg,
//		class com.mec.fx.HelloFX8,
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
		if(val.isPresent()){
			return val.get();
		}else{
			return key;
		}
	}
	
	public static String get(Object obj, String tag){
		return get(obj.getClass(), tag);
	}
	
	public static <R> R get(Class<?> clazz, String tag, Function<String, R> converter, R defaultValue){
		if(null == converter){
			return defaultValue;
		}
		String strval = get(clazz, tag);
		R retval;
		try {
			retval = converter.apply(strval);
		} catch (Exception e) {
//			e.printStackTrace();
			retval = null;
		}
		if(null != retval){
			return retval;
		}else{
			return defaultValue;
		}
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
		return get(obj.getClass(), tag, converter, defaultValue);
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
	
//	public static void clearCash(){
//		ResourceBundle.clearCache();
////		resources = ResourceBundle.getBundle(MESSAGES, Locale.getDefault());
//		resources = ResourceBundle.getBundle(MESSAGES);
//		System.out.printf("default locale: %s, resource.locale: %s\n", Locale.getDefault(), resources.getLocale());
//	}
	
	
	public static List<String> getList(Class<?> clazz, String tag){
		List<String> retval = new ArrayList<String>();
//		String value = get(clazz, tag);
//		if(null != value){
//			retval.add(value);
//		}
		String baseKey = String.format(TAG_COMBINE_PATTERN, clazz.getName(), tag);
		getFull(baseKey).ifPresent(e -> retval.add(e));
		
		int count = 1;
//		String tagAppendix = tag;
		Optional<String> value = null;
		while(true){
//			tagAppendix = tag + "." + count;
//			value = get(clazz, tagAppendix);
			value = getFull(String.format(TAG_COMBINE_PATTERN, baseKey, count));
//			if(null == value || value.isEmpty()){
//			if(value.endsWith(tagAppendix)){
//				break;
//			}
//			retval.add(value);
			if(value.isPresent()){
				retval.add(value.get());
			}else{
				break;
			}
			++count;
		}
		
		return retval;
	}
	
	public static List<String> getList(Object obj, String tag){
		return getList(obj.getClass(), tag);
	}
	
	private static final String MESSAGES = "com.mec.resources.MessagesBundle";
	private static final ResourceBundle resources = ResourceBundle.getBundle(MESSAGES);
	private static final String TAG_COMBINE_PATTERN = "%s.%s";
//	private static final Messages instance = new Messages();
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



/**
 References:
 [1] http://stackoverflow.com/questions/421280/how-do-i-find-the-caller-of-a-method-using-stacktrace-or-reflection
 [2] 
 
 
 
*/

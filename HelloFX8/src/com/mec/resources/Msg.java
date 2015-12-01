package com.mec.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
	protected static String getFull(String key){
		
		try {
			return resources.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}
	
	
	/**
	 * Note heavy load time this method would pay
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
		return getFull(String.format("%s.%s", getCaller.getCallerClassName(3), tag));
	}
	
	public static String get(Class<?> clazz, String tag){
		return getFull(String.format("%s.%s", clazz.getName(), tag));
	}
	
	public static String get(Object obj, String tag){
		return get(obj.getClass(), tag);
	}
	
	public static String getExpMsg(Object obj, String tag){
		return getFull(String.format("%s.exception.%s", obj.getClass().getName(), tag));
	}
	
//	public static void clearCash(){
//		ResourceBundle.clearCache();
////		resources = ResourceBundle.getBundle(MESSAGES, Locale.getDefault());
//		resources = ResourceBundle.getBundle(MESSAGES);
//		System.out.printf("default locale: %s, resource.locale: %s\n", Locale.getDefault(), resources.getLocale());
//	}
	
	
	private static final String MESSAGES = "com.mec.resources.MessagesBundle";
	private static ResourceBundle resources = ResourceBundle.getBundle(MESSAGES);
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

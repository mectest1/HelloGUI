package com.mec.resources;

public interface MsgLogger {

	default void log(Exception e){
		log(JarTool.exceptionToStr(e));
	};
	void log(String msg);
	
	/**
	 * Output message with <code>format</code> as pattern and <code>args</code> as parameters.
	 * @param format format pattern, used as format string in {@link String#format(String, Object...)}
	 * @param args output parameters
	 */
	default void log(String format, Object ... args){
		log(String.format(format, args));
	}
	static MsgLogger defaultLogger(){
		return System.out::println; 
	}
	
}

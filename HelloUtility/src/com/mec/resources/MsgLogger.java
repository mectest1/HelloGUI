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
	/**
	 * Append a new line to the original log message
	 * @param msg
	 */
	default void ln(String msg){
		log(msg);
		log(NEW_LINE);
	}
	/**
	 * Append a new line to the original exception log message
	 * @param e
	 */
	default void ln(Exception e){
		log(e);
		log(NEW_LINE);
	}
	static MsgLogger defaultLogger(){
		return System.out::println; 
	}
	
	
	static String NEW_LINE = Msg.get(MsgLogger.class, "newline");
}

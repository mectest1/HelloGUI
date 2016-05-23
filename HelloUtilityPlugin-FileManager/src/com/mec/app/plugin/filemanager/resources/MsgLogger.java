package com.mec.app.plugin.filemanager.resources;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface MsgLogger {

	default void log(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		log(sw.toString());
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
	default void ln(String format, Object ... args){
		log(format, args);
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
	
	//-------------------------------------
	static MsgLogger defaultLogger(){
		return System.out::println; 
	}
	
	public static class StringLogger implements MsgLogger{
		StringWriter logger = new StringWriter();

		@Override
		public void log(String msg) {
			logger.write(msg);
		}
		@Override
		public String toString() {
			return logger.toString();
		}
	}
	
	
	public static class StringPrefixLogger extends StringLogger{
		String logPrefix;
		public StringPrefixLogger(String logPrefix){
			this.logPrefix = logPrefix;
		}
		@Override
		public void log(String msg) {
			logger.write(logPrefix);
			logger.write(msg);
		}
	}
	
	static String NEW_LINE = Msg.get(MsgLogger.class, "newline");
}

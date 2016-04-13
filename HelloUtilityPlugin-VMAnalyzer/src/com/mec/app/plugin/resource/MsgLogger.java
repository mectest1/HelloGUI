package com.mec.app.plugin.resource;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface MsgLogger {

	default void log(Exception e){
		log(expToString(e));
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
	
	static String expToString(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}

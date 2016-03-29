package com.mec.duke.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public interface MsgLogger {

	default void log(Exception e){
//		log(JarTool.exceptionToStr(e));
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		log(sw.toString());
	};
	void log(String msg);
	default void log(String format, Object ... args){
		log(String.format(format, args));
	}
	static MsgLogger defaultLogger(){
		return System.out::println; 
	}
	
}

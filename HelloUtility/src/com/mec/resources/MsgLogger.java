package com.mec.resources;

public interface MsgLogger {

	default void log(Exception e){
		log(JarTool.exceptionToStr(e));
	};
	void log(String msg);
	
	default void log(String format, Object ... args){
		log(String.format(format, args));
	}
	static MsgLogger defaultLogger(){
		return System.out::println; 
	}
	
}

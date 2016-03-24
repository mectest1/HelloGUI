package com.mec.resources;

public interface MsgLogger {

	default void log(Exception e){
		log(JarTool.exceptionToStr(e));
	};
	void log(String msg);
	
	static MsgLogger defaultLogger(){
		return System.out::println; 
	}
	
}

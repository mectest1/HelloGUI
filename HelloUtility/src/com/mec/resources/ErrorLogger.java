package com.mec.resources;

public interface ErrorLogger {

	default void log(Exception e){
		log(JarTool.exceptionToStr(e));
	};
	void log(String msg);
}

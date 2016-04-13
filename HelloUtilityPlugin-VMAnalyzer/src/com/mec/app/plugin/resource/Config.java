package com.mec.app.plugin.resource;

public class Config {

	
	
	private Config(){
		
	}
	
	
	public static final Config me(){
		return instance;
	}
	
	
	private static final Config instance = new Config();
}


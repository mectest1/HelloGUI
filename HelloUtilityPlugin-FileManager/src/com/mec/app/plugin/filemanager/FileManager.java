package com.mec.app.plugin.filemanager;

import java.io.PrintStream;

import com.mec.application.beans.PluginInfo.PluginStart;

public class FileManager {

	
	@PluginStart
	private void sayHello(){
		out.println("Hello, from FileManager");
	}
	
	
	private static final PrintStream out = System.out;
}

package com.mec.app.plugin.filemanager;

import java.io.PrintStream;

import com.mec.resources.Plugins.PluginEntryMehtod;

public class FileManager {

	
	@PluginEntryMehtod
	private void sayHello(){
		out.println("Hello, from FileManager");
	}
	
	
	private static final PrintStream out = System.out;
}

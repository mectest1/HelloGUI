package com.mec.app.plugin.sqlparser;

import java.io.PrintStream;
import java.util.ResourceBundle;

import com.mec.application.beans.PluginInfo.PluginContext;
import com.mec.application.beans.PluginInfo.PluginStart;

public class SQLParser {

	
	@PluginStart
	private void sayHello(PluginContext pc) throws Exception{
		out.println("Hello, SQLParser, " + pc);
		
		
		String resource = "com.mec.resources.MessagesBundle";
		ResourceBundle rb = ResourceBundle.getBundle(resource);
		out.println(rb.getString("com.mec.resources.Plugins.exception.noEntryMethod"));	//<- Derp, can still get the parent resource;
	}
	
	
	
	private static final PrintStream out = System.out;
}

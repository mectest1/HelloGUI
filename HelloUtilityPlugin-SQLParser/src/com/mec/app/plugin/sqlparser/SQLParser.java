package com.mec.app.plugin.sqlparser;

import java.io.PrintStream;

import com.mec.resources.Plugins.PluginContext;
import com.mec.resources.Plugins.PluginEntryMehtod;

public class SQLParser {

	
	@PluginEntryMehtod
	private void sayHello(PluginContext pc){
		out.println("Hello, SQLParser, " + pc);
	}
	
	private static final PrintStream out = System.out;
}

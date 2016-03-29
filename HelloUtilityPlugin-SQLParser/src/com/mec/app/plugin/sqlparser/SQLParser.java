package com.mec.app.plugin.sqlparser;

import java.io.PrintStream;

import com.mec.application.beans.PluginInfo.PluginContext;
import com.mec.application.beans.PluginInfo.PluginStart;

public class SQLParser {

	
	@PluginStart
	private void sayHello(PluginContext pc){
		out.println("Hello, SQLParser, " + pc);
	}
	
	private static final PrintStream out = System.out;
}

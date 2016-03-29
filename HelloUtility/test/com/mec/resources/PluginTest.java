package com.mec.resources;

import org.junit.Ignore;
import org.junit.Test;

import com.mec.resources.Config.ConfigEndpoint;
import com.mec.resources.Plugins.Plugin;
import com.mec.resources.Plugins.PluginConfig;
import com.mec.resources.Plugins.PluginConfigBean;

public class PluginTest {

	
	@Ignore
	@Test
	public void testGeneratePluginConfig(){
		final String fmPlugin = "FileManager";
		PluginConfigBean configBean = new PluginConfigBean();
		configBean.setEntryClass("com.mec.app.plugin.filemanager.FileManager");
//		PluginConfig.of(fmPlugin, PluginConfig::new).save(Plugin.PLUGIN_CONFIG_FILE, configBean);
		
//		PluginConfig.of(fmPlugin).save(Plugin.PLUGIN_CONFIG_FILE, configBean);
		ConfigEndpoint pluginConfig = PluginConfig.of(fmPlugin);
		pluginConfig.save(Plugin.PLUGIN_CONFIG_FILE, configBean);
		
		
		//2
//		Config.of(fmPlugin).save(Plugin.PLUGIN_CONFIG_FILE, configBean);
		
		//3
//		Config.pluginData().save(Plugin.PLUGIN_CONFIG_FILE, configBean);
//		Config.pluginData().of(fmPlugin).save(Plugin.PLUGIN_CONFIG_FILE, configBean);
	}
	
	@Ignore
	@Test
	public void testLoadPlugin() throws Exception{
		final String fmPlugin = "FileManager";
//		Plugins.load(fmPlugin);
//		Thread.sleep(10000L);
		
		Plugins.loadSync(fmPlugin);
	}
	
	
	
	//-------------------------------------------------
	@Ignore
	@Test
	public void testGeneratePluginConfig2(){
		final String fmPlugin = "SQLParser";
		PluginConfigBean configBean = new PluginConfigBean();
		configBean.setEntryClass("com.mec.app.plugin.sqlparser.SQLParser");
//		PluginConfig.of(fmPlugin, PluginConfig::new).save(Plugin.PLUGIN_CONFIG_FILE, configBean);
		PluginConfig.of(fmPlugin).save(Plugin.PLUGIN_CONFIG_FILE, configBean);
	}
	
//	@Ignore
	@Test
	public void testLoadPlugin2(){
		final String fmPlugin = "SQLParser";
//		Plugins.load(fmPlugin);
		Plugins.loadSync(fmPlugin);
	}
	//-------------------------------------------------
}

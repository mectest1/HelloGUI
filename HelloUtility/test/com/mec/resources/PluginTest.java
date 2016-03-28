package com.mec.resources;

import org.junit.Ignore;
import org.junit.Test;

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
		PluginConfig.of(fmPlugin).save(Plugin.PLUGIN_CONFIG_FILE, configBean);
	}
	
//	@Ignore
	@Test
	public void testLoadPlugin(){
		final String fmPlugin = "FileManager";
		Plugins.load(fmPlugin);
		
	}
}

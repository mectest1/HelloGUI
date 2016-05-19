package com.mec.app.plugin.filemanager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;

import org.junit.Ignore;
import org.junit.Test;

import com.mec.app.plugin.filemanager.XMLParamComparatorController.ParamDirectoryExtractor;
import com.mec.app.plugin.filemanager.resources.MsgLogger;

public class XMLParamComparatorControllerTest {

	@Ignore
	@Test
	public void testMatchNumDir() {
//		fail("Not yet implemented");
		Path p = Paths.get("1");
		Matcher m = ParamDirectoryExtractor.NUM_SUB_DIR_PATTERN.matcher(p.getFileName().toString());
		
		logger.log("%s matches pattern %s? %s", p.toString(), 
				ParamDirectoryExtractor.NUM_SUB_DIR_PATTERN.toString(), 
				m.matches());
		
		p = Paths.get("12");
		m = ParamDirectoryExtractor.NUM_SUB_DIR_PATTERN.matcher(p.getFileName().toString());
		logger.log("%s matches pattern %s? %s", p.toString(), 
				ParamDirectoryExtractor.NUM_SUB_DIR_PATTERN.toString(), 
				m.matches());
		
		p = Paths.get("a12");
		m = ParamDirectoryExtractor.NUM_SUB_DIR_PATTERN.matcher(p.getFileName().toString());
		logger.log("%s matches pattern %s? %s", p.toString(), 
				ParamDirectoryExtractor.NUM_SUB_DIR_PATTERN.toString(), 
				m.matches());
		
	}
	
	/*
	 * 2016-05-19, result: 
	 * EE Accounting Rules\Module
		EE Accounting Rules\System
		EE Amount&Rate Format\Module
		EE Amount&Rate Format\System
		EE Attribute\Module
		EE Attribute\System
		EE Batch Manage
		EE Business Unit Config
		EE Calculation
		EE Catalog\Module\Multi
		EE Catalog\Module\Single
		EE Catalog\System\Multi
		EE Catalog\System\Single
		EE Clause\Module
		EE Clause\System
		EE Component Manage
		EE DataSource Manage
		EE DB Dictionary
		EE Event Driven
		EE Field Conversion\Module
		EE Field Conversion\System
		EE Form\Module
		EE Form\System
		EE Function Group
		EE GAPI Setting\Authorize
		EE GAPI Setting\Inquire
		EE GAPI Setting\Release
		EE GAPI Setting\Transaction
		EE Get CUBK\Module
		EE Get CUBK\System
		EE Get DO DATA\Module
		EE Get DO DATA\System
		EE HandWriting
		EE Message Broker Setting
		EE Message Mapping
		EE Module&Event
		EE Protocol Manager
		EE Screen
		EE Screen\Module
		EE Screen\System
		EE Security Parameters
		EE Server Side JS
		EE STD Setting
		EE SWIFT
		EE System Maintain
		EE System Parameter
		EE Transaction Function
		EE Transfer To\Module
		EE Transfer To\System
	 * 
	 */
	@Ignore
	@Test
	public void testGetCharacteristicPath(){
		Path rootDir = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure");
//		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(logger, rootDir);
		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(logger);
		
		List<Path> cps = pe.getCharacteristicPath(rootDir);
//		logger.log(cps.toString());
//		cps.forEach(cp -> logger.log(cp.toString()));
		cps.forEach(cp -> logger.log((rootDir.relativize(cp)).toString()));
	}
	
	
	@Test
	public void testGroupNumDirContents(){
		
	}
	
	MsgLogger logger = MsgLogger.defaultLogger();

}

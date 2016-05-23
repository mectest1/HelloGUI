package com.mec.app.plugin.filemanager.resources;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

public class XMLToolsTest {

	@Ignore
	@Test
	public void testNodeToString() {
		Path xml = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Message Mapping/3/HSBC/US/AP/SCHEMAMAPPING/MAPPING/mapping_root.xml");
		
		Document dom = XMLTools.inst().fileToDom(xml);
		
		String domStr = XMLTools.inst().nodeToStr(dom.getDocumentElement());
		
		logger.log(domStr);
		
	}
	@Test
	public void testStripWhitespaces() {
		Path xml = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Message Mapping/3/HSBC/US/AP/SCHEMAMAPPING/MAPPING/mapping_root.xml");
		
		Document dom = XMLTools.inst().fileToDom(xml);
		
		String domStr = XMLTools.inst().nodeToStr(dom.getDocumentElement());
		
		logger.log(domStr);
		
		
		logger.log("--------------\n");
		logger.log(XMLTools.stripWS(domStr));
	}
	
	
	MsgLogger logger = MsgLogger.defaultLogger();

}

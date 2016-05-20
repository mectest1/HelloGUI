package com.mec.app.plugin.filemanager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
	
	@Ignore
	@Test
	public void testListXmlParamFiles(){
		Path numDir1 = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Accounting Rules/Module/1");
		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(logger);
		
		List<Path> xmlFiles = pe.listXmlParamFiles(numDir1);
		xmlFiles.forEach(rp -> logger.log(rp.toString()));
	}
	
	@Ignore
	@Test
	public void testListAllChildNumDirs(){
		Path rootDir = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Accounting Rules/Module");
		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(logger);
		
		List<Path> numDirs = pe.listChildNumDirs(rootDir);
		numDirs.forEach(p -> logger.log(p.toString()));
	}
	
	@Ignore
	@Test
	public void testGroupNumDirContents(){
		Path rootDir = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure/EE Accounting Rules/Module");
		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(logger);
		
//		List<Path> numDirs = pe.listChildNumDirs(rootDir);
		Map<Path, List<Set<Path>>> groupedNumDirsByXMLStruct = pe.groupNumDirContents(rootDir);
//		logger.log(groupedNumDirsByXMLStruct.toString());
		for(Path paramFile : groupedNumDirsByXMLStruct.keySet()){
			logger.log("==========================================");
			logger.log(paramFile.toString());
			List<Set<Path>> groupedNumDirs = groupedNumDirsByXMLStruct.get(paramFile);
//			String groupedNumDirsStr = groupedNumDirs.stream()
//				.map(sp -> sp.stream()
////						.sorted(Comparator.comparingInt(p -> Integer.parseInt(p.getFileName().toString()))
//						.map(p -> p.getFileName().toString()).collect(Collectors.joining(",", "{", "}")))
//				.collect(Collectors.joining(",", "[", "]"));
			
			
//			String groupedNumDirsStr = groupedNumDirs.stream()
//					.map(sp -> sp.stream()
//							.sorted(Comparator.comparingInt(p -> Integer.parseInt(p.getFileName().toString())))
//							.map(p -> p.getFileName().toString())
//							.collect(Collectors.joining(",", "{", "}"))
//					).collect(Collectors.joining(",", "[", "]"));
			String groupedNumDirsStr = pe.groupedNumDirsToStr(groupedNumDirs);
			logger.log(groupedNumDirsStr);
		}
	}
	
	@Test
	public void testGroupingXmlParamFilesInAllCharacteristicPaths() throws IOException{
		Path rootDir = Paths.get("//10.39.100.2/dev2/Utility Doc/Web Utility Test Information/Original Utility 测试点分析/XML Structure");
//		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(logger, rootDir);
		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(logger);
		
		Path outputLogFile = Paths.get("data/difference_report.log");
		BufferedWriter outputWriter = Files.newBufferedWriter(outputLogFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		List<Path> cps = pe.getCharacteristicPath(rootDir);
		for(Path cp : cps){
//			logger.log("\n\n==========================\n==%s\n==========================", rootDir.relativize(cp).toString());
			logAndWrite(outputWriter, "\n\n==========================\n==%s\n==========================\n", rootDir.relativize(cp).toString());
			Map<Path, List<Set<Path>>> groupedNumDirsByXMLStruct = pe.groupNumDirContents(cp);
			for(Path paramFile : groupedNumDirsByXMLStruct.keySet()){
				List<Set<Path>> groupedNumDirs = groupedNumDirsByXMLStruct.get(paramFile);
				if(1 < groupedNumDirs.size()){
					String groupedNumDirsStr = pe.groupedNumDirsToStr(groupedNumDirs);
//					logger.log("%s: %s", paramFile.toString(), groupedNumDirsStr);
					logAndWrite(outputWriter, "%s: %s", paramFile.toString(), groupedNumDirsStr);
				}
			}
		}
		outputWriter.close();
	}
	
	void logAndWrite(Writer writer, String format, Object ... args) throws IOException{
		String content = String.format(format, args);
		logger.log(content);
		writer.append(content);
	}
	
	
	MsgLogger logger = MsgLogger.defaultLogger();

}

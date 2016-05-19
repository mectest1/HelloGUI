package com.mec.app.plugin.filemanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mec.app.plugin.filemanager.resources.Msg;
import com.mec.app.plugin.filemanager.resources.MsgLogger;
import com.mec.app.plugin.filemanager.resources.XMLStructComparator;

import javafx.fxml.FXML;

public class XMLParamComparatorController {
	
	
	
	@FXML
	private void initialize(){
		Path currentPath = Paths.get(".");
		try {
			//Result: E:\Git\github.com\mectest1\HelloGUI\HelloUtility

			logger.log(currentPath.toRealPath().toString());
		} catch (IOException e) {
			logger.log(e);
		}
	}
	
	
	/**
	 * <h3>Consider the structure in XML Param parent directory</h3>
	 * <p>PARAM_ROOT_DIRECTORY/</p>
	 * <ul>
	 * <li>
	 * 	<h4>Function_Group_Name/</h4>
	 * 	<ul>
	 * 		<li>
	 * 
	 * 		</li>
	 * 		<li>{Module|System}/{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{Module|System}/{<strong>number</strong>}/{ConfigType}/{FuncType}/*.xml</li>
	 * 		<li>{ConfigType}/{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{Module|System}/{Multi|Single}/{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{ConfigType}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{BankGroup}/{CountryCode}/*.js	//<--------</li>
	 * 		<li>{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{ConfigType}/{FuncType}/*.{xml|.xsl}</li>
	 * 		<li>[Module|System]/{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{ConfigType}/{FuncType}/[{SubFuncType}]/*.{xml|.xsl}</li>
	 * 		<li>*.vbs</li>
	 * 		<li></li>
	 * 	</ul>
	 * </li>
	 * <li>
	 * 	<h4>*.xlsx</h4>
	 * </li>
	 * </ul>
	 * 
	 * <h3>Notes:</h3>
	 * <ul>
	 * 	<li>{<strong>number</strong>} may not start from 1</li>
	 * 	<li>Sub-directories in <strong>Function_Group_Name</strong>/{*}/{<strong>number</strong>} are supposed to have the <em>same</em> structure, 
	 * it's under these <em>characteristic directories</em> that we we are supposed to check the structure of directors & XML files.</li>
	 * 	<li>The comparison process should be proceeded for each parent directory of 
	 * <strong>Function_Group_Name</strong>/{*}/{<strong>number</strong>}</li>
	 * 	<li></li>
	 * </ul>
	 * @author MEC
	 *
	 */
	static class ParamDirectoryExtractor{
//		private static final ParamDirectoryExtractor instance = new ParamDirectoryExtractor();
		private Path rootDir;
		private ParamDirectoryExtractor(MsgLogger logger){
			this.logger = logger;
		}
		private ParamDirectoryExtractor(Path root){
			Objects.requireNonNull(root);
			if(!Files.exists(root)){
				logger.log(new FileNotFoundException(Msg.get(XMLParamComparatorController.class, "error.pathNotExist")));
			}
			this.rootDir = root;
		}
		
		public static ParamDirectoryExtractor newInst(MsgLogger logger){
//			ParamDirectoryExtractor retval =  new ParamDirectoryExtractor(paramRootDirectory);
//			retval.setLogger(logger);
			return new ParamDirectoryExtractor(logger);
		}
//		public static ParamDirectoryExtractor newInst(MsgLogger logger, Path paramRootDirectory){
//			ParamDirectoryExtractor retval =  new ParamDirectoryExtractor(paramRootDirectory);
//			retval.setLogger(logger);
//			return retval;
//		}
		
		//
		/**
		 * Function groups directories are those direct children of {@link #rootDir}
		 * @return
		 */
		public List<Path> getFunctionGroups(){
			List<Path> retval = new ArrayList<>();
			try {
				List<Path> subDirs = Files.list(rootDir).filter(Files::isDirectory).collect(Collectors.toList());
				retval.addAll(subDirs);
			} catch (IOException e) {
				logger.log(e);
			}
			return retval;
		}
		
		
		/**
		 * <p><strong>Characteristic path</strong> are those directories 
		 * have <strong><em>direct child directory</em></strong> with name pattern <code>\d+</code>
		 * </p>
		 * <p>
		 * This method will check all the descendant directories of <code>fromDir</code>,
		 * in case any directory matches principles listed above, it will be includedi in
		 * the returned list.
		 * </p>
		 * @param fromDir
		 * @return
		 */
		public List<Path> getCharacteristicPath(Path fromDir){
			List<Path> retval = new ArrayList<>();
			if(!Files.isDirectory(fromDir)){
				return retval;
			}
			
			try {
				
//				Optional<Path> numDir = Files.list(fromDir).filter(p -> {
//					Matcher m = NUM_SUB_DIR_PATTERN.matcher(p.getFileName().toString());
//					
//					return m.matches();
////					return false;
//					
//				}).findAny();
//				
//				if(numDir.isPresent()){
//					retval.add(fromDir);
//				}
				
				List<Path> subDirs = Files.list(fromDir).collect(Collectors.toList());
				for(Path subDir : subDirs){
					if(!Files.isDirectory(subDir)){
						continue;
					}
					
//					Matcher m = NUM_SUB_DIR_PATTERN.matcher(subDir.getFileName().toString());
					
//					if(m.matches()){	//a {number} folder is found
					if(isNumDir(subDir)){
						if(!retval.contains(fromDir)){
							retval.add(fromDir);
						}
					}else{	//check its sub directories
						List<Path> subCharacteristicPaths = getCharacteristicPath(subDir);
						retval.addAll(subCharacteristicPaths);
					}
					
				}
			} catch (IOException e) {
				logger.log(e);
			}
			//
			return retval;
		}
		
		
		/**
		 * Group each descendant XML file in this <code>characteristicPath</code>
		 * according to their structures. Final result would look like this:
		 * <dl>
		 * <dt>Result:</dt>
		 * <dd>{<strong>key</strong> -> <strong>sets</strong>};</dd>
		 * <dt>key:</dt>
		 * <dd>path of the XML file;</dd>
		 * <dt>sets:</dt>
		 * <dd>Looks like {{1}, {2, 3}, {4}}, where numDirs are grouped 
		 * according to XML equality behavior as defined in {@link XMLStructComparator#isXMLFilesEqual(Path, Path)}</dd>
		 * </dl>
		 * 
		 *  
		 * 
		 * @param characteristicPath
		 */
		Map<Path, Set<Set<Path>>> groupNumDirContents(Path characteristicPath){
			Map<Path, Set<Set<Path>>> retval = new HashMap<>();
			Objects.requireNonNull(characteristicPath);
			if(!Files.exists(characteristicPath)
				|| !Files.isDirectory(characteristicPath)){
				logger.log(Msg.get(XMLParamComparatorController.class, "error.invalidCharacteristicPath")
						, characteristicPath.toString());
				return retval;
			}
			
			try {
				List<Path> numDirs = Files.list(characteristicPath)
						.filter(this::isNumDir)
						.collect(Collectors.toList());
				
//				numDirs.stream().collect(Collectors.groupingBy(classifier))
				for(Path numDir : numDirs){
					walkXmlFilesInNumDir(numDir, numDirs);
				}
				
				
				
				
				
				
				
				
				
				
				
				
			} catch (IOException e) {
				logger.log(e);
			}
			
			return retval;
		}
		
		
		private Map<Path, Set<Set<Path>>> walkXmlFilesInNumDir(Path currentNumDir, List<Path> siblingNumDirs){
			Map<Path, Set<Set<Path>>> retval = new HashMap<>();
			
			
			return retval;
			
		}
		
		
		
		
		
		private boolean isNumDir(Path subDir){
			Matcher m = NUM_SUB_DIR_PATTERN.matcher(subDir.getFileName().toString());
			return m.matches();
		}
		
//		public void setLogger(MsgLogger logger) {
//			this.logger = logger;
//		}


		private MsgLogger logger = MsgLogger.defaultLogger();
		static final Pattern NUM_SUB_DIR_PATTERN = Pattern.compile(Msg.get(XMLParamComparatorController.class, "pattern.numSubDir"));
	}

//	static final PrintStream out = System.out;
	MsgLogger logger = MsgLogger.defaultLogger();
}

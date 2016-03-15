package com.mec.resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileParser {

	
	/**
	 * Accepted only normalized modify list content format: one file for each line;
	 * e.g.: 
	 * INPUT:
	 * 	/EximBillsELoan/src/com/cs/eximap/eloan/utility/ASUtilityService.java
	 *  /EximBillWeb/WebContent/screen/eLOAN/IAAC_InqAmzPosting.jsp
	 * OUTPUT:
	 * 	{
	 * 		EximBillsELoan: [com/cs/eximap/eloan/utility/ASUtilityService.java]
	 * 		EximBillWeb: [WebContent/screen/eLOAN/IAAC_InqAmzPosting.jsp]
	 * 	}
	 * You may need to invoke {@link #normalizeModifyList(String)} before invoking this one;
	 * <p>
	 * Note that duplicate items in the result will be elimilated;
	 * </p>
	 * @param content
	 * @return
	 */
//	public static Map<String, List<String>> parseModifyList(String content){
	public static Map<String, Set<String>> parseModifyList(List<String> lines){
		Map<String, Set<String>> retval = new HashMap<>();
//		if(null == content || content.isEmpty()){
//			return retval;
//		}
		if(null == lines || lines.isEmpty()){
			return retval;
		}
		
//		content.replaceAll(CARRIAGE_RETURN, NEWLINE);
//		String[] lines = content.split(NEWLINE);
		for(String line : lines){
//			if(isCommentLine(line)){
			if(isModifyListCommentLine(line)){
				continue;
			}
			
			line = JarTool.normalizePath(line);
			
//			if(line.startsWith(JarTool.NIX_PATH)){
//				line = line.substring(1);
//			}
			line = JarTool.trimLeadingSlash(line);	//line.trim()
			int projectIndex = line.indexOf(JarTool.NIX_PATH);
			String projectName = line.substring(0, projectIndex);
			
			Set<String> projectSourceList = retval.get(projectName);
			if(null == projectSourceList){
				projectSourceList = new HashSet<>();
				retval.put(projectName, projectSourceList);
			}
			
			
			String sourceFile = JarTool.trimLeadingSlash(line.substring(projectIndex + 1));
			sourceFile = trimSourceFolderPrefix(sourceFile);
			projectSourceList.add(sourceFile);
			
			//
		}
		
		return retval;
	}
	
	public static List<String> normalizeModifyList(String content){
		List<String> retval = new ArrayList<String>();
		if(null == content || content.isEmpty()){
			return retval;
		}
		content.replaceAll(CARRIAGE_RETURN, NEWLINE);
		String[] lines = content.split(NEWLINE);
		
		for(String line : lines){
			if(!isModifyListCommentLine(line)){
				line = normalizeModifyListLine(line);
			}
			if(null != line){
				retval.add(line.trim());	//<- trim modify file;
			}
		}
		
		return retval;
	}
	
	
	/**
	 * INPUT:
	 * 	ServerJS.java - CSEECore/src/com/cs/core/parser (2 matches)
	 * OUTPUT:
	 * 	CSEECore/src/com/cs/core/parser/ServerJS.java
	 * @param line
	 * @return
	 */
	public static String normalizeModifyListLine(String line){
		if(null == line || line.trim().isEmpty()){
			return line;
		}
		String retval = null;
		
		line = JarTool.normalizePath(line);
		
		List<Pattern> modifyListPatterns = new ArrayList<>();;
		List<String> modifyListNamePatternStr = Msg.getList(FileParser.class, "pattern.modifyList.pattern");
		for(String pStr : modifyListNamePatternStr){
			modifyListPatterns.add(Pattern.compile(pStr));
		}
		List<String> outputFormat = Msg.getList(FileParser.class, "pattern.modifyList.outputFormat");
		
		List<String> outputIndicesStr = Msg.getList(FileParser.class, "pattern.modifyList.outputFormat.indices");
		final String indicesDelimiter = Msg.get(FileParser.class, "pattern.modifyList.outputIndices.delimiter");
		List<List<Integer>> outputIndices = outputIndicesStr.stream().map(indicesStr -> 
			Arrays.asList(indicesStr.split(indicesDelimiter))
				.stream().map(indexStr -> Integer.parseInt(indexStr.trim())).collect(Collectors.toList())
		).collect(Collectors.toList());
		
		retval = StringTemplate.getRegExtractor().extractAndOutput(
				line, modifyListPatterns, outputFormat, outputIndices, 
				String.format(Msg.get(FileParser.class,  "exception.modifyList.unrecognized"), line)
				);
		
		retval = JarTool.trimLeadingSlash(retval);
		return retval;
	}
	
	
//	public static List<String> getClassesFromJavaFile(File javaFile) throws FileNotFoundException, IOException{
	public static List<String> getClassesFromJavaFile(Path javaFile) throws IOException{
		List<String> retval = new ArrayList<String>();
//		FileReader fis = new FileReader(javaFile);
//		try(BufferedReader br = new BufferedReader(fis)){
//			br.lines().forEach(line -> {
//				extractClassNameFromLine(line).ifPresent(classNames -> retval.addAll(classNames));
//			});
//		}
		Files.readAllLines(javaFile).forEach(line -> {
			extractClassNameFromLine(line).ifPresent(classNames -> retval.addAll(classNames));
		});
		return retval;
	}
	
	private static Optional<List<String>> extractClassNameFromLine(String line){
//		if(null == line || line.isEmpty()){
		if(!isValidJavaCodeLine(line)){
			return Optional.empty();
		}
		List<String> retval = null;
		Matcher m = CLASS_DECLARTION.matcher(line);
		while(m.find()){
			if(null == retval){
				retval = new ArrayList<>();
			}
			retval.add(m.group(1));
		}
		return Optional.ofNullable(retval);
	}
	
	private static boolean isModifyListCommentLine(String line){
		if(null == line || line.trim().isEmpty()){
			return true;
		}
		return isStringStartsWith(line, COMMENT_START);
	}
	
	public static boolean isStringStartsWith(String line, List<String> startsWith){
		if(null == line || line.isEmpty()){
			return false;
		}
		for(String comment : startsWith){
			if(line.trim().startsWith(comment)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isStringEndsWithSuffix(String str, List<String> suffix){
		if(null == str || str.isEmpty() || null == suffix || suffix.isEmpty()){
			return false;
		}
		boolean retval = false;
		for(String s : suffix){
			if(str.endsWith(s)){
				retval = true;
				break;
			}
		}
		
		return retval;
	}
	
	private static String trimSourceFolderPrefix(String line){
		if(null == line || line.trim().isEmpty()){
			return line;
		}
		for(String sourceFolder : SOURCE_FOLDER){
			line = JarTool.trimLeading(line, sourceFolder);
		}
		return line;
	}
	
	private static boolean isValidJavaCodeLine(String codeLine){
//		if(null == codeLine || codeLine.isEmpty() || codeLine.trim().startsWith(JAVA_CODE_COMMENT_LNIE_START)){
//			return false;
//		}
//		return true;
		if(null == codeLine || codeLine.trim().isEmpty()){
			return false;
		}
		return !isStringStartsWith(codeLine, JAVA_CLASS_PARSE_IGNORE_LINE_START);
	}
	
	
	public static ImageView getImage(String imagePath){
//		try {
			Image img = new Image(imagePath);
			return new ImageView(img);
//		} catch (IOException e) {
//			throw new IllegalArgumentException(e);
//		}
	}
	
//	private static final Pattern classDeclartion = Pattern.compile("\\bclass\\b\\w+\\b", Pattern.MULTILINE);
//	private static final String DEFAULT_CLASS_PATTERN = "\\bclass\\s+(\\w+)";
	public static final Pattern CLASS_DECLARTION = Pattern.compile(Msg.get(FileParser.class, "classPattern"));
	public static final String CARRIAGE_RETURN = Msg.get(FileParser.class, "constant.carriageReturn");
	public static final String NEWLINE= Msg.get(FileParser.class, "constant.newLine");
	private static final List<String> COMMENT_START = Msg.getList(FileParser.class, "comment.lineStart");
//	private static final List<String> SOURCE_FOLDER = Msg.getList(JarTool.class, "source.dir");
	private static final List<String> SOURCE_FOLDER = JarTool.SOURCE_FOLDER;
	private static final List<String> JAVA_CLASS_PARSE_IGNORE_LINE_START = Msg.getList(FileParser.class, "java.classParse.ignore.start");
//	private static final Pattern MODIFY_LIST_NAME_PATTERN_CANON = Pattern.compile(Msg.get(FileParser.class, "pattern.modifyList.canon")); 
//	private static final Pattern MODIFY_LIST_NAME_PATTERN_SVN = Pattern.compile(Msg.get(FileParser.class, "pattern.modifyList.svn"));
//	private static final Pattern MODIFY_LIST_NAME_PATTERN_SVN2 = Pattern.compile(Msg.get(FileParser.class, "pattern.modifyList.svn2"));
	
//	private static final List<Pattern> MODIFY_LIST_NAME_PATTERNS = new ArrayList<>();;
//	static{
//		List<String> modifyListNamePatternStr = Msg.getList(FileParser.class, "pattern.modifyList.pattern");
//		for(String pStr : modifyListNamePatternStr){
//			MODIFY_LIST_NAME_PATTERNS.add(Pattern.compile(pStr));
//		}
//	}
	
	/**
	 * Save &amp; load JavaBean to &amp; from XML file.
	 * @author MEC
	 *
	 */
	public static class BeanMarshal{
		public static <T> void saveToXML(T obj, File xmlFile) throws JAXBException, PropertyException{
			JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			//
			m.marshal(obj, xmlFile);
		}
		
		@SuppressWarnings("unchecked")
		public static <T> T loadFromXML(Class<? extends T> clazz, File xmlFile) throws JAXBException{
			JAXBContext ctx = JAXBContext.newInstance(clazz);
			Unmarshaller um = ctx.createUnmarshaller();
			
			//
			return (T) um.unmarshal(xmlFile);
		}
	}

}





//public class BeanMarshal {
//
//	
//	
//	public static <T> void saveToXML(T obj, File xmlFile ) throws Exception{
//		JAXBContext context = JAXBContext.newInstance(obj.getClass());
//		Marshaller m = context.createMarshaller();
//		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//		
//		m.marshal(obj, xmlFile);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public static <T> T loadFromXML(Class<? extends T> clazz, File xmlFile) throws Exception{
////	public static <T> T loadFromXML(File xmlFile) throws Exception{
//		JAXBContext context = JAXBContext.newInstance(clazz);
//		Unmarshaller um = context.createUnmarshaller();
//		
//		return (T) um.unmarshal(xmlFile);
//	}
//}
package com.mec.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {

	
	
	public static List<String> getClassesFromJavaFile(File javaFile) throws FileNotFoundException, IOException{
		List<String> retval = new ArrayList<String>();
		FileReader fis = new FileReader(javaFile);
		try(BufferedReader br = new BufferedReader(fis)){
			br.lines().forEach(line -> {
				extractClassNameFromLine(line).ifPresent(classNames -> retval.addAll(classNames));
			});
		}
		return retval;
	}
	
	private static Optional<List<String>> extractClassNameFromLine(String line){
		if(null == line || line.isEmpty()){
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
	
//	private static final Pattern classDeclartion = Pattern.compile("\\bclass\\b\\w+\\b", Pattern.MULTILINE);
	private static final String DEFAULT_CLASS_PATTERN = "\\bclass\\s+(\\w+)";
	public static final Pattern CLASS_DECLARTION = Pattern.compile(Msg.get(FileParser.class, "classPattern"));
}
package com.mec.resources;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

public class FileParserTest {

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testTestClassPattern() {
//		fail("Not yet implemented");
		
		String c1 = "class derp{";

		Arrays.asList("class derp{", 
				"class    Derp{",
				" class What",
				" public class FileParserTest{}",
				" public class     FileParsertest{ class FileParserTestInnerClass{}}"
				," public classFileParsertest{ class FileParserTestInnerClass{}}"
				," publicclass     FileParsertest{ class FileParserTestInnerClass{}}"
				," publicclass     FileParsertest{ classFileParserTestInnerClass{}}"
				).stream().forEach(s -> FileParserTest.printMatches(classDeclartion, s));
		
		
	}
	

	private static void printMatches(Pattern p, String s){
		Matcher m = p.matcher(s);
		out.printf("\n[%s] pattern found in [%s] ? %s\n", p.pattern(), s, "result: ");
		while(m.find()){
			String g = m.group();
			String className = m.group(1);
			out.printf("\tFound subString: [%s], className: %s\n", g, className);
		}
		
	}
	
	@Ignore
	@Test
	public void testModifyListNamePatterns() throws Exception{
		String[] modifyList = new String[]{
			"ServerJS.java - CSEECore/src/com/cs/core/parser (2 matches)"
			, "/HelloUtility/src/com/mec/resources/ViewFactory.java"
		};
		List<String> modifyListNamePattern = Msg.getList(FileParser.class, "pattern.modifyList");
		for(String pStr : modifyListNamePattern){
			Pattern p = Pattern.compile(pStr);
			Arrays.asList(modifyList).stream().forEach(l -> {
				out.printf("Normalied line: %s\n", FileParser.normalizeModifyListLine(l));
			});
		}
	}
	
	@Ignore
	@Test
	public void testModifyListSVNNamePatterns() throws Exception{
		String[] modifyList = new String[]{
				  "HelloUtility/src/com/mec/resources/ViewFactory.java"
				, "/HelloUtility/src/com/mec/resources/ViewFactory.java"
				, "ServerJS.java - CSEECore/src/com/cs/core/parser (2 matches)"
				, "BTAGGetSessionData.java - EximBillWeb/JavaSource/com/cs/eximweb/usertag (2 matches) 65264 mike.tang 1/4/16, 4:08 PM"
		};
		List<String> modifyListNamePattern = Msg.getList(FileParser.class, "pattern.modifyList.svn");
		for(String pStr : modifyListNamePattern){
			Pattern p = Pattern.compile(pStr);
			Arrays.asList(modifyList).stream().forEach(l -> {
				out.printf("Normalied line: %s\n", FileParser.normalizeModifyListLine(l));
			});
		}
	}
	
//	@Ignore
	@Test
	public void testModifyListSVNName2Patterns() throws Exception{
		String[] modifyList = new String[]{
				"HelloUtility/src/com/mec/resources/ViewFactory.java"
				, "HelloGUI\\HelloUtility\\src\\com\\mec\\resources\\FileParser.java"
				, "BTAGGetSessionData.java - EximBillWeb/JavaSource/com/cs/eximweb/usertag (2 matches) 65264 mike.tang 1/4/16, 4:08 PM"
				, "EximBillWeb/JavaSource/com/cs/eximweb/usertag - BTAGJavaScriptURL.java (4 matches)"
		};
//		List<String> modifyListNamePattern = Msg.getList(FileParser.class, "pattern.modifyList.svn2");
//		for(String pStr : modifyListNamePattern){
//			Pattern p = Pattern.compile(pStr);
//			Arrays.asList(modifyList).stream().forEach(l -> {
//				out.printf("Normalied line: %s\n", FileParser.normalizeModifyListLine(l));
//			});
//		}
		Arrays.asList(modifyList).stream().forEach(l -> 
				out.printf("Normalied line: %s\n", FileParser.normalizeModifyListLine(l)));
	}
	
	
//	@Ignore
	@Test(expected=IllegalArgumentException.class)
//	@Test
	public void testUnrecognizableModifyList() throws Exception{
		String[] modifyList = new String[]{
				"DerpDerp"
		};
		
		Arrays.asList(modifyList).stream().forEach(l -> {
			out.printf("Normalied line: %s\n", FileParser.normalizeModifyListLine(l));
		});
	}
	
	
	private static final PrintStream out = System.out;
//	private static final Pattern classDeclartion = Pattern.compile("\\bclass\\s+(\\w+)", Pattern.MULTILINE);
	private static final Pattern classDeclartion = FileParser.CLASS_DECLARTION;
}

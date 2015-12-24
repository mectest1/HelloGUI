package com.mec.resources;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Test;

public class FileParserTest {

	@After
	public void tearDown() throws Exception {
	}

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
	
	private static final PrintStream out = System.out;
//	private static final Pattern classDeclartion = Pattern.compile("\\bclass\\s+(\\w+)", Pattern.MULTILINE);
	private static final Pattern classDeclartion = FileParser.CLASS_DECLARTION;
}

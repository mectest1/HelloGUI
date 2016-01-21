package com.mec.duke;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

public class StringTest {

	@Test
	@Ignore
	public void testTrimStr() {
//		fail("Not yet implemented");
		String str = "010898";
		str = fillOrSubPreString(0, "0", str);
		out.printf(str);
	}

	
	
	private static String fillOrSubPreString(int length, String preChar, String value) {
		if (value == null) {
			value = "";
		}
		int vLength = value.length();
		if (vLength > length) {
			int bIndex = -1;
			for (int i = 0; i < value.length(); i++) {
				if (value.charAt(i) != '0') {
					bIndex = i;
					break;
				}
			}

			value = value.substring(bIndex);
		} else if (vLength < length) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < length - vLength; i++) {
				buffer.append(preChar);
			}
			value = buffer.append(value).toString();
		}
		return value;
	}
	
	
	@Ignore
	@Test
	public void testChar() throws Exception{
		char c = 1;
		char c2 = 3;
		String s = "" + c + c2;
		
		
		out.printf("%s\n", s);
		
		String s2 = "\01\03";
		out.printf("%s\n", s2);
		
		out.printf("s.equals(s2)? %s", s.equals(s2));
		
		out.printf("replace \\01 with 1: %s\n", s.replace('\01', '1'));
		
		String s3 = "\01Derp\03\01Derpia\03";
		out.printf("replace \\01|\\03 with 2: %s\n", s3.replaceAll("\\01|\\03", "2"));
		out.printf("replace \\01|\\03 with 2: %s\n", s3.replace('\01', '\0').replace('\03', '\0'));	//<- the replacement is not working properly
		
	}
	
	@Ignore
	@Test
	public void testReplaceEfficiency() throws Exception{
		String p = "\\01|\\03";
		Pattern pattern = Pattern.compile(p);
		Path file = Paths.get("E:/Date/2016-01/Defect #38510/form-m-bala.xml");
		long startMilli = System.currentTimeMillis();
		Files.readAllLines(file).stream().forEach(l -> out.println(pattern.matcher(l).replaceAll("")));
		long duration = System.currentTimeMillis() - startMilli;
		out.printf("\n\n\nduration time: %s seconds\n", duration / 1000);	//4 seconds;
		
		
		String s3 = "\01Derp\03\01Derpia\03";
		out.printf("replace \\01|\\03 with 2: %s\n", pattern.matcher(s3).replaceAll("2"));
	}
	
	@Ignore
	@Test
	public void testPatternMatch() throws Exception{
		Pattern p = Pattern.compile("\01.*?\03");
		String s = "\01Derp\03zzzzzz\01Derpia\03zzzz";
		Matcher m = p.matcher(s);
		while(m.find()){
			out.println(m.group());
		}
		
	}
	
	@Ignore
	@Test
	public void testDeduce(){
		Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).stream().reduce((l, r) -> l + r).ifPresent(out::println);
	}
	
	
	
	
	@Test
	public void testEncapsulate(){
//		Arrays.asList("Hello, World").stream().forEach(s -> encapsulateFunc(s, (String s2) -> printWitchException(s2)));	//<- unhandled exception
//		Arrays.asList("Hello, World").stream().forEach(encapsulateFunc(this::printWithException));
	}
	
	private void printWithException(String s) throws Exception{
		out.println(s);
	}
	
//	static <T, R>  Consumer<T, R> encapsulateFunc(Consumer<T, R> func){
//		try{
//			return func;
//		}catch(Exception e){
//			throw new RuntimeException(e);
//		}
//	}
//	static <T>  Consumer<T> encapsulateFunc(Consumer<T> func){
//		try{
//			return func;
//		}catch(Exception e){
//			throw new RuntimeException(e);
//		}
//	}
	
	private static final PrintStream out = System.out;
}

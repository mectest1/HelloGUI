package com.mec.duke;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

public class ClassLoaderFeatureTest {

	@Test
	@Ignore
//	public void testFindClass() throws Exception{
	public void testLoadClass() throws Exception{
		URL st = getClass().getResource("/com/mec/duke/StringTest.class");
		URLClassLoader cl = URLClassLoader.newInstance(new URL[]{
			st
		});
		Class<?> stc = cl.loadClass("com.mec.duke.StringTest");
		Object sti = stc.newInstance();
		Method testTrimStr = stc.getMethod("testTrimStr");
		testTrimStr.invoke(sti);
		out.println("\nURLClassLoader says hello");
	}

	
	@Test
	public void testArrayObjects() throws Exception{
		final String intArrayClassName = "[I";
		Class<?> ic = Class.forName(intArrayClassName);
		final int arraySize = 10;
//		Object ia = ic.newInstance();	//<- InstantiationException; (my guess is that this array constructor doesn't provide the necessary dimension size;
//		Object ia = Array.newInstance(ic, arraySize);	//create instance of [[I
		Object ia = Array.newInstance(ic.getComponentType(), arraySize);	//<- create instance of [I, ic.componentType: int
//		Arrays.fill((int[])ia, 0);
		IntStream.range(0, arraySize).forEach(i -> 
			Array.set(ia, i, i)
		);
		out.println(
			IntStream.range(0, arraySize).boxed().map(i -> (int)(Array.get(ia, i)))
				.map(i -> Integer.toString(i))
				.collect(Collectors.joining(", ", "[", "]"))
		);
	}
	
	
	static final PrintStream out = System.out;
}

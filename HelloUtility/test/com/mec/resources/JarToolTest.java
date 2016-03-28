package com.mec.resources;

import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Test;

public class JarToolTest {

	@Test
	public void testIsJarFile() {
		Stream.of(Paths.get("derp.jar")
				,Paths.get("derp.JAR")
				,Paths.get("1.jar")
				,Paths.get("11.jar")
				,Paths.get("11.jar")
				,Paths.get("1d.jar")
				,Paths.get("1d.ja")
				,Paths.get("1d.jar.what")
				).forEach(p -> 
					out.printf("%s is jar file? %s\n", p, JarTool.isJarFile(p))
				);
		
		
		
		
		
	}
	
	
	private static final PrintStream out = System.out;

}

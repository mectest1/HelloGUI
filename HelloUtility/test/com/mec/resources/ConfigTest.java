package com.mec.resources;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class ConfigTest {

	@Test
	public void testPath() {
		
		
		Path p1 = Paths.get("derp/data");
		Path p2 = Paths.get("derp");
		p2 = p2.resolve("data");
		out.println(p1.equals(p2));
		out.printf("p1.toString equals p2.toString? %s, p1=%s, p2=%s\n", p1.toString().equals(p2.toString()), p1, p2);
	}

	
	private static final PrintStream out = System.out;
}

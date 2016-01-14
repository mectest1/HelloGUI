package com.mec.duke;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

public class FilesTest {

	@Ignore
	@Test
	public void testIsSomething() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FilesTest.java");
		
		out.printf("%s exists? %s\nexecutable? %s\n"
				+ "is directory? %s\n"
				+ "writable? %s\n"
				+ "executable? %s\n"
				+ "regular file? %s\n"
				+ "isHidden? %s\n"
				, p
				, Files.exists(p)
				, Files.isReadable(p)
				, Files.isDirectory(p)
				, Files.isWritable(p)
				, Files.isExecutable(p)
				, Files.isRegularFile(p)
				, Files.isHidden(p)
				);
		
	}
	
	@Ignore
	@Test
	public void testListRoots() throws Exception{
		//The NIO2 way
		out.println("The JDK8 way");
		FileSystems.getDefault().getRootDirectories().forEach(l -> out.println(l.toString()));
		
		out.println("The JDK6 way");
		//The JDK6 way
		Arrays.asList(File.listRoots()).stream().forEach(f -> out.println(f));
	}
	
	@Test
	public void testListDirectory() throws Exception{
		Path p = Paths.get(".");
		Files.newDirectoryStream(p).forEach(f -> {
			out.println(f.normalize().toAbsolutePath());
		});
		out.println("All files: ");

		Files.walk(p).forEach(f -> {
			out.println(f.normalize());
		});
	}
	
	
	private static final PrintStream out = System.out;

}

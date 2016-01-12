package com.mec.duke;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

public class PathTest {

	@Ignore
	@Test
	public void testPath() {
//		fail("Not yet implemented");
	}
	
	@Ignore
	@Test
	public void testFileSystems() throws Exception{
		FileSystem fs = FileSystems.getDefault();
		Path p = Paths.get(".");
		out.println(p.getFileName());
		
		Files.list(p).forEach(f -> list(f, 0));
	}
	
	
	@Ignore
	@Test
	public void testPathParts() throws IOException{
		String file = "E:/Git/github.com/mectest1/HelloGUI/HelloDuke/test/com/mec/duke/PathTest.java";
		
		Path p1 = Paths.get("/Git/github.com/mectest1/HelloGUI/", "/HelloDuke/test/com/mec/duke/PathTest.java");
		out.printf("%s, %s\n", p1.getFileName(), p1.toFile().getCanonicalPath());
		
		//
		//Define a path relative to the current file store root should start with the file delimiter
		Path p2 = Paths.get("/Git/github.com/mectest1/HelloGUI/HelloDuke/test/com/mec/duke/PathTest.java");
		out.printf("%s, %s\n", p2.getFileName(), p2.toFile().getCanonicalPath());
		
		//When you define a path relative to the current work folde, the path should not start with teh file delimiter
		p2 = Paths.get("test/com/mec/duke/PathTest.java");
		out.printf("%s, %s\n", p2.getFileName(), p2.toFile().getCanonicalPath());
		
	}
	
	
	@Ignore
	@Test
	public void testNormalizedPath() throws IOException{
		String file = "E:/Git/github.com/mectest1/HelloGUI/HelloDuke/test/com/mec/duke/PathTest.java";
		Path p1 = Paths.get("././././test/com/mec/duke/PathTest.java");
		Path p2 = Paths.get("././././test/com/mec/duke/PathTest.java").normalize();
		Path p3 = Paths.get("/Git/github.com/mectest1/HelloGUI/HelloDuke/../HelloDuke/test/com/mec/duke/PathTest.java");
		Path p4 = Paths.get("E:/Git/github.com/mectest1/HelloGUI/HelloDuke/../HelloDuke/test/com/mec/duke/PathTest.java").normalize();
		
		out.printf("%s\n%s\n%s\n%s", p1, p2, p3, p4);
	}
	
	@Ignore
	@Test
	public void testURIPath() throws Exception{
		Path p1 = Paths.get(URI.create("file://E:/Git/github.com/mectest1/HelloGUI/HelloDuke/test/com/mec/duke/PathTest.java"));
		Path p2 = Paths.get(URI.create("file:///Git/github.com/mectest1/HelloGUI/HelloDuke/test/com/mec/duke/PathTest.java"));
//		Path p3 = Paths.get(URI.create("http://www.bing.com"));	//provider "http" not installed
		out.printf("%s\n%s\n", p1, p2);
		
		
//		p1 exsites? false
//		p2 exists? true
		out.printf("p1 exsites? %s, p2 exists? %s\n", Files.exists(p1), Files.exists(p2));
	}
	
	@Ignore
	@Test
	public void testGetPathThroughFileSystems() throws Exception{
		Path p1 = FileSystems.getDefault().getPath("test/com/mec/duke", "PathTest.java");
		Path p2 = FileSystems.getDefault().getPath("/Git/github.com/mectest1/HelloGUI/HelloDuke/./", "test/com/mec/duke", "PathTest.java");
//		out.printf("%s\n%s", p1.toFile().getCanonicalPath(), p2.toFile().getCanonicalFile());
		out.printf("%s\n%s", p1, p2);
	}
	
	@Ignore
	@Test
	public void testHomeDirectory(){
		Path p = Paths.get(System.getProperty("user.home"), "Downloads", "ChromeSetup.exe");
//		out.printf("%s", p.toAbsolutePath());
		out.printf("%s", p);
	}
	private void list(Path path, final int indent){
		for(int i = indent; 1 < i; --i){
			out.print(TAB);
		}
//		out.print("----");
		out.println(path.getFileName());
		if(Files.isDirectory(path)){
			try {
				Files.list(path).forEach(file -> list(file, 1 + indent));
			} catch (IOException e) {
				e.printStackTrace(out);
			}
		}
	}

	@Test
	public void testPathElements(){
		Path p1 = FileSystems.getDefault().getPath("test/com/mec/duke", "PathTest.java");
		out.printf("Absolute path: %s\n", p1.toAbsolutePath());
		out.printf("path.root: %s\n", p1.getRoot());
		
		for(int i = 0; i < p1.getNameCount(); ++i){
			out.printf("name[%s] = %s\n", i, p1.getName(i));
		}
		
		out.printf("subpath(1, 2):%s\n", p1.subpath(1, 2));
		
		
		Path p2 = Paths.get("E:/Git/github.com/mectest1/HelloGUI/HelloDuke/test/com/mec/duke/PathTest.java");
		out.printf("Absolute path: %s\n", p2.toAbsolutePath());
		for(int i = 0; i < p2.getNameCount(); ++i){
			out.printf("name[%s] = %s\n", i, p2.getName(i));
		}
		out.printf("subpath(1, 2):%s\n", p2.subpath(1, 2));	//Note that the name elements do not include the root component
		
	}
	
	private static final String TAB = "\t";
	private static final PrintStream out = System.out;
}

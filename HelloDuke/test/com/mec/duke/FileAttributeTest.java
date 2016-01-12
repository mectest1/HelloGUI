package com.mec.duke;

import java.io.PrintStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import org.junit.Ignore;
import org.junit.Test;

public class FileAttributeTest {

	@Ignore
	@Test
	public void test() {
//		fail("Not yet implemented");
	}
	
	
	@Ignore
	@Test
	public void testSupportedAttributeViews(){
		FileSystem fs = FileSystems.getDefault();
		fs.supportedFileAttributeViews().stream().forEach(out::println);
	}
	
	
	@Ignore
	@Test
	public void testFileStoreAttributes() throws Exception{
		FileSystem fs = FileSystems.getDefault();
//		for(FileStore store : fs.getFileStores()){
//			out.printf("%s---%s\n", store.name(), store.supportsFileAttributeView(BasicFileAttributeView.class));
//		}
		fs.getFileStores().forEach(store -> 
			out.printf("%s---%s\n", store.name(), store.supportsFileAttributeView(BasicFileAttributeView.class))
		);
	}
	
	@Ignore
	@Test
	public void testFileAttribute() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class);
		
		out.printf("File size: %sKB\n", attr.size()/1000);
		out.printf("File creation time: %s\n", attr.creationTime());
		out.printf("File was last accessed at: %s\n", attr.lastAccessTime());
		out.printf("File was last modified at: %s\n", attr.lastModifiedTime());
		
		out.printf("Is directory? %s\n", attr.isDirectory());
		out.printf("Is regular file? %s\n", attr.isRegularFile());
		out.printf("Is symbolic link? %s\n", attr.isSymbolicLink());
		out.printf("Is other? %s\n", attr.isOther());
		
	}
	

	@Ignore
	@Test
	public void testSingleAttribute() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		long size = (long) Files.getAttribute(p, "basic:size", LinkOption.NOFOLLOW_LINKS);
		out.printf("Size of file: %sKB\n", size/1000);
	}
	
	
	@Test
	public void testUpdateFileAttribute() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		long time = System.currentTimeMillis();
		FileTime fileTime = FileTime.fromMillis(time);
		Files.getFileAttributeView(p, BasicFileAttributeView.class).setTimes(fileTime, fileTime, null);
		Files.setLastModifiedTime(p, fileTime);
		out.println("File last modified time updated successfully");
	}
	
	private static final PrintStream out = System.out;

}

















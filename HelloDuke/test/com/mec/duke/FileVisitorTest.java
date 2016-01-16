package com.mec.duke;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Test;

public class FileVisitorTest {

	@Test
	public void testWalkPath() throws Exception{
		Path cur = Paths.get(".");
		Files.walkFileTree(cur, ListTree.instance());
	}

	
	
	static class ListTree extends SimpleFileVisitor<Path>{

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			out.printf("Visited directory %s\n", dir.normalize());
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			exc.printStackTrace(out);
			return FileVisitResult.CONTINUE;
		}
		
		public static final FileVisitor<Path> instance(){
			return instance;
		}
		
		private static final ListTree instance = new ListTree();
	}
	
	
	private static final PrintStream out = System.out;
}

package com.mec.duke;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;
import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;

public class FileVisitorTest {

	@Ignore
	@Test
	public void testWalkPath() throws Exception{
		Path cur = Paths.get(".");
		Files.walkFileTree(cur, ListTreeVisitor.instance());
	}

	@Ignore
	@Test
	public void testSearchFile() throws Exception{
		Path searchFile = Paths.get(this.getClass().getSimpleName() + ".java");
		FileVisitor<Path> searchVisitor = new SearchVisitor(searchFile);
		EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		
		Path dirToSearch = Paths.get(".");
		Files.walkFileTree(dirToSearch, opts, Integer.MAX_VALUE, searchVisitor);
	}
	
	@Ignore
	@Test
	public void testSearchGlob() throws Exception{
		Path start = Paths.get(".");
		FileVisitor<Path> searchVisitor = new SearchVisitor("*.java");
		Files.walkFileTree(start, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, searchVisitor);
	}
	
	@Ignore
	@Test
	public void testSearchGlobAndSize() throws Exception{
		Path start = Paths.get(".");
		FileVisitor<Path> searchVisitor = new SearchVisitor("*.java", 2*1000);
		Files.walkFileTree(start, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, searchVisitor);
	}
	
	@Ignore
	@Test
	public void testDeleteDirectory() throws Exception{
		Path tmpDir= createTempDir(Paths.get("."));
		out.printf("Temporary directory creted: %s\n", tmpDir);
		out.println("Temporary directory stucture:");
		Files.walk(tmpDir).forEach(out::println);
		
		//
		out.println("Waiting for 10 seconds...");
		Thread.sleep(10 * 1000);
		out.println("Now starts to delete this temporary directory");
		Files.walkFileTree(tmpDir, DirectoryDeleteVisitor.newInstance());
	}
	
	
	@Ignore
	@Test
	public void testDeleteDirectory2() throws Exception{
		Path tmpDir = createTempDir(Paths.get("."));
		out.printf("Temporary directory creted: %s\n", tmpDir);
		out.println("Temporary directory stucture:");
		Files.walk(tmpDir).forEach(out::println);
		
		//
		out.println("Waiting for 10 seconds...");
//		Thread.sleep(10 * 1000);
		out.println("Now starts to delete this temporary directory");
		deleteFile(tmpDir);
//		Files.deleteIfExists(tmpDir);
	}
	
	@Ignore
	@Test
	public void testCopyDirectory() throws Exception{
		Path tmpDir = createTempDir(Paths.get("."));
		Path copyTo = tmpDir.resolveSibling(tmpDir.getFileName().toString() + "_copy");
	
		Files.walkFileTree(tmpDir, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new DirectoryCopyVisitor(tmpDir, copyTo));
		

		Thread.sleep(5 * 1000);
//		deleteFile(tmpDir);
//		deleteFile(copyTo);
		Files.walkFileTree(tmpDir, DirectoryDeleteVisitor.newInstance());
		Files.walkFileTree(copyTo, DirectoryDeleteVisitor.newInstance());
	}
	
	@Ignore
	@Test
	public void testCopyDirectory2() throws Exception{
		Path tmpDir = createTempDir(Paths.get("."));
		Path copyTo = tmpDir.resolveSibling(tmpDir.getFileName().toString() + "_copy");
		
//		Files.walkFileTree(tmpDir, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new DirectoryCopyVisitor(tmpDir, copyTo));
		copyFile(tmpDir, copyTo);
		
		
		Thread.sleep(5 * 1000);
		deleteFile(tmpDir);
		deleteFile(copyTo);
//		Files.deleteIfExists(tmpDir);
//		Files.deleteIfExists(copyTo);
	}
	

	
	@Ignore
	@Test
	public void testMoveDirectory() throws Exception{
		Path tmpDir = createTempDir(Paths.get("."));
		Path moveTo = tmpDir.resolveSibling(tmpDir.getFileName() + "_move");
		
		Files.move(tmpDir, moveTo);	//<- seems this can totally work
		out.printf("Move %s to %s\n", tmpDir, moveTo);
//		Files.deleteIfExists(moveTo);
		Thread.sleep(5 * 1000);
		Files.walkFileTree(moveTo, DirectoryDeleteVisitor.newInstance());
	}
	
	@Test
	public void testMoveDirectory2() throws Exception{
		Path tmpDir = createTempDir(Paths.get("."));
		Path moveTo = tmpDir.resolveSibling(tmpDir.getFileName() + "_move");
		Files.walkFileTree(tmpDir, new DirectoryMoveVisitor(tmpDir, moveTo));
		
		
		Thread.sleep(5 * 1000);
		Files.walkFileTree(moveTo, DirectoryDeleteVisitor.newInstance());
	}
	
	/**
	 * Would work both on directories and files.
	 * @param fromFile
	 * @param toFile
	 */
	static void moveFile(Path fromFile, Path toFile){
		try{
			Files.move(fromFile, toFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		}catch(IOException e){
			e.printStackTrace(out);
		}
	}
	static void copyFile(Path fromFile, Path toFile){
		try{
			if(Files.isDirectory(fromFile)){
				Files.copy(fromFile, toFile, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
				Files.list(fromFile).forEach(subItem -> copyFile(subItem, toFile.resolve(fromFile.relativize(subItem))));
				Files.setLastModifiedTime(toFile, Files.getLastModifiedTime(fromFile));
				out.printf("Copy directory %s to %s\n", fromFile, toFile);
			}else{
				Files.copy(fromFile, toFile);
				Files.setLastModifiedTime(toFile, Files.getLastModifiedTime(fromFile));
				out.printf("Copy file %s to %s\n", fromFile, toFile);
			}
		}catch(IOException e){
			e.printStackTrace(out);
		}
	}
	
	static void deleteFile(Path p){
//		if(Files.notExists(p)){
//			return;
//		}
		try {
			if(!Files.isDirectory(p)){
				Files.deleteIfExists(p);
//				Files.delete(p);
				out.printf("File %s deleted\n", p);
					
			}else{
				Files.list(p).forEach(FileVisitorTest::deleteFile);
//				for (Path subItem : Files.list(p).collect(Collectors.toList())){
//				deleteFile(subItem);
//				}
//				Files.list(p).forEach(FileVisitorTest::deleteFile);
//				Files.delete(p);		//<-----This will not work, but File.deleteOnExit() will, why?
				p.toFile().deleteOnExit();
//				Files.deleteIfExists(p);
				out.printf("Directory %s deleted\n", p);
			}
//			Files.deleteIfExists(p);
		} catch (IOException e) {
			e.printStackTrace(out);
			throw new RuntimeException(e);
		}
	}
	
	static Path createTempDir(Path start) throws Exception{
		Path p = Files.createTempDirectory(start, "tmp");
		
		for(int i = 0 ; i < 5; ++i){
			Path p2 = Files.createTempDirectory(p, "sub_tmp");
			for(int j = 0; j < 3; ++j){
				Files.createTempFile(p2, "sub2_tmp_file", ".derp2");
			}
			Files.createTempFile(p, "sub_tmp_file", ".derp");
		}
		
		return p;
	}
	
	static class DirectoryMoveVisitor implements FileVisitor<Path>{
		private Path fromDir;
		private Path toDir;
		private Optional<FileTime> dirLastModifiedTime = Optional.empty();
		
		public DirectoryMoveVisitor(Path fromDir, Path toDir){
			this.fromDir = fromDir;
			this.toDir = toDir;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			Path toPath = toDir.resolve(fromDir.relativize(dir));
			dirLastModifiedTime = Optional.of(Files.getLastModifiedTime(dir));
			Files.createDirectories(toPath);
			out.printf("Move directory %s to %s\n", dir, toPath);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Path toFile = toDir.resolve(fromDir.relativize(file));
			Files.copy(file, toFile);
			Files.setLastModifiedTime(toFile, Files.getLastModifiedTime(file));
			Files.deleteIfExists(file);
			out.printf("Move file %s to %s\n", file, toFile);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			exc.printStackTrace(out);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			Path toPath = toDir.resolve(fromDir.relativize(dir));
			if(dirLastModifiedTime.isPresent()){
				Files.setLastModifiedTime(toPath, dirLastModifiedTime.get());
			}
			Files.deleteIfExists(dir);
			return FileVisitResult.CONTINUE;
		}
		
	}
	
	static class DirectoryCopyVisitor implements FileVisitor<Path>{

		private Path fromDir;
		private Path toDir;
		
		public DirectoryCopyVisitor(Path fromDir, Path toDir) {
			super();
			this.fromDir = fromDir;
			this.toDir = toDir;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			Path toFile = toDir.resolve(fromDir.relativize(dir));
			Files.copy(dir, toFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
			out.printf("Copy directory %s to %s\n", dir, toFile);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			Path toFile = toDir.resolve(fromDir.relativize(file));
			Files.copy(file, toFile);
			Files.setLastModifiedTime(toFile, Files.getLastModifiedTime(file));
			out.printf("Copy %s to %s\n", file, toFile);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			exc.printStackTrace(out);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			
			if(null != exc){
				exc.printStackTrace(out);
			}else{
				FileTime time = Files.getLastModifiedTime(dir);
				Files.setLastModifiedTime(dir, time);
			}
			
			return FileVisitResult.CONTINUE;
		}
		
		
	}
	
	static class DirectoryDeleteVisitor implements FileVisitor<Path>{

		public static DirectoryDeleteVisitor newInstance(){
			return new DirectoryDeleteVisitor();
		}
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			
			Files.deleteIfExists(file);
			out.printf("File: %s deleted\n", file);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			exc.printStackTrace(out);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			Files.deleteIfExists(dir);
			out.printf("Directory: %s deleted\n", dir);
			return FileVisitResult.CONTINUE;
		}
		
	}
	
	static class SearchVisitor implements FileVisitor<Path>{

		private Optional<Path> searchedFile = Optional.empty();
		public boolean found;
		private Optional<PathMatcher> matcher = Optional.empty();
		private Optional<Long> acceptedSize = Optional.empty();
		
//		public SearchVisitor
		
		public SearchVisitor(String globStr, long acceptedSize){
//			matcher = Optional.of(FileSystems.getDefault().getPathMatcher("glob:" + globStr));
			this(globStr);
			this.acceptedSize = Optional.of(acceptedSize);
		}
		public SearchVisitor(String globStr){
			matcher = Optional.of(FileSystems.getDefault().getPathMatcher("glob:" + globStr));
		}
		
		public SearchVisitor(Path searchedFile){
			this.searchedFile = Optional.of(searchedFile);
			this.found = false;
		}
		
//		static Path getRealPath(Path file){
//			try{
//				return file.toRealPath();
//			}catch(IOException e){
//				e.printStackTrace(out);
//				return null;
//			}
//		}
		void search(Path file) throws IOException{
			Path name = file.getFileName();
			
			if(null == name){
				return;
			}
			if(matcher.isPresent()){
				PathMatcher m = matcher.get();
				boolean sizeAccepted = true;
				if(acceptedSize.isPresent()){
					long realSize = (long) Files.getAttribute(file, "basic:size");
					sizeAccepted = realSize <= acceptedSize.get();
				}
				if( m.matches(name) && sizeAccepted){
					found = true;
					out.printf("Searched pattern was found in %s\n", file.toRealPath());
				}
			}
			if(searchedFile.isPresent()){
				Path s = searchedFile.get();
				if(s.equals(name)){
					found = true;
					out.printf("Searched file was found: %s in %s\n", searchedFile, file.toRealPath()); //<- toRealPath() is more convenient
//					out.printf("Searched file was found: %s in %s\n", searchedFile, file.toAbsolutePath());
				}
			}
			
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			search(file);
			if(!found){
				return FileVisitResult.CONTINUE;
			}else{
				return FileVisitResult.TERMINATE;
			}
		}

		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			exc.printStackTrace(out);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			out.printf("Visited: %s\n", dir);
			return FileVisitResult.CONTINUE;
		}
		
	}
	
	
	static class ListTreeVisitor extends SimpleFileVisitor<Path>{

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
//			out.printf("Visited directory %s\n", dir.normalize());
//			out.printf("Visited directory %s\n", dir.toRealPath());
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
		
		private static final ListTreeVisitor instance = new ListTreeVisitor();
	}
	
	
	private static final PrintStream out = System.out;
}

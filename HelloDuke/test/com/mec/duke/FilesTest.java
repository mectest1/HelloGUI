package com.mec.duke;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

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
	
	@Ignore
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
	
	
	@Ignore
	@Test
	public void testListDirectory2() throws Exception{
		Path p = Paths.get(".");
		Files.newDirectoryStream(p).forEach(file -> out.println(file.getFileName().toString()));
		
		out.println("\n-----------pictures:------------------");
		Path p2 = getPictureDirectory();
		Files.newDirectoryStream(p2, "**.{jpg, png, bmp}").forEach(out::println);
	}

	private Path getPictureDirectory(){
		Path dp = Paths.get(System.getProperty("user.home"), "Pictures/Date/2014-11-21");
		List<Path> p = Arrays.asList(dp
				, Paths.get("E:/").resolve(dp.getRoot().relativize(dp.toAbsolutePath()))
				);
		Path p2 = p.stream().filter(Files::exists).findAny().get();
		return p2;
	}
	
	@Ignore
	@Test
	public void testFilters() throws Exception{
		Path p = Paths.get(".");
		out.println("\n\nDirectories: --------");	//Get only directories
		Files.newDirectoryStream(p, Files::isDirectory).forEach(out::println);
		
		//Files that only larger than 200KB;
		long size = 1024 * 500;
		Path p2 = getPictureDirectory();
		out.printf("\n\nFiles larger than %sKB in %s\n", size/1024, p2);	//Get only files lars than 100KB
		Files.newDirectoryStream(p2, f -> Files.size(f) > size).forEach(out::println);;
		
		
		Path fileDir = Paths.get(getClass().getResource(".").toURI());
//		Path fileDir = Paths.get("./test", getClass().getPackage().getName().replaceAll("\\.", "/"));
		out.printf("\nFiles only modified today in %s: \n", fileDir);
		Files.newDirectoryStream( fileDir, f -> {
//			long currentTime = FileTime.fromMillis(System.currentTimeMillis()).to(TimeUnit.DAYS);	
			
			//Method 1
//			LocalDate modifiedDate = LocalDate.ofEpochDay(((FileTime) Files.getAttribute(f, "basic:lastModifiedTime")).to(TimeUnit.DAYS));
//			return modifiedDate.equals(LocalDate.now());
			
			long currentTime = FileTime.fromMillis(System.currentTimeMillis()).to(TimeUnit.DAYS);
			long modifiedDate = ((FileTime) Files.getAttribute(f, "basic:lastModifiedTime")).to(TimeUnit.DAYS);
			return currentTime == modifiedDate;
		}).forEach(out::println);
		
		//Filter only hidden files/directories:
//		Path p3 = p.getParent();	//<- p3: null
//		Path p3 = Paths.get("E:/").getParent();	//<- p3: null
		Path p3 = p.toAbsolutePath().getParent().getParent();
		out.printf("\n\nHidden files/directories in %s:\n", p3);
		Files.newDirectoryStream(p3, f -> {
			return Files.isHidden(f);	//Seems not working correctly?
		}).forEach(out::println);
	}
	
	
//	@Ignore
//	@Test
//	public void testGlobFilter() throws Exception{
//		
//	}
	
	

	
	@Ignore
	@Test
	public void testCreateFile() throws Exception{
		Path p = Paths.get(".", "derp.txt");
		
//		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw------");
//		FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
//		Files.createFile(p, attr);
		Files.deleteIfExists(p);
		Files.createFile(p);
		out.printf("%s created successfully.\n", p.toAbsolutePath().normalize());
		
	}
	
	@Ignore
	@Test
	public void testWriteFile() throws Exception{
		Path p = Paths.get(".", "derp.txt");
		
		Files.write(p, "Hello, World!".getBytes());
		out.printf("Write file successfully.\n");
		
		out.printf("Now read from file: %s\n", p.toAbsolutePath().normalize());
		Files.readAllLines(p).forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testWriteBytes() throws Exception{
		Path pin = Paths.get("test", this.getClass().getPackage().getName().replaceAll("\\.", "/")
				, this.getClass().getSimpleName() +  ".java"
				);
		out.printf("%s exists? %s\n", pin.normalize(), Files.exists(pin));
		
		Path pout = Paths.get("derp.txt");
		
		Files.write(pout, Files.readAllLines(pin), StandardOpenOption.APPEND);
		
		out.printf("Content of the %s file: \n", pout.normalize());
		Files.readAllLines(pout).forEach(out::println);
	}
	
	@Test
	public void testReadImage() throws Exception{
		Path pin = Paths.get("src/META-INF/resources/img/", "Kenny_Avatar.jpg");
		Path p2 = Paths.get("derpImage.jpg");
		Path p3 = Paths.get("derpImage.png");
		
//		out.printf("Read from %s and write to %s\n", pin.normalize(), p2.normalize());
//		Files.write(p2, Files.readAllBytes(pin), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
//		Files.deleteIfExists(p2);
		
		//
		out.printf("ImageIO: read from %s and write to %s\n", pin.normalize(), p2.normalize());
		BufferedImage bi = ImageIO.read(pin.toFile());
		ImageIO.write(bi, "png", p3.toFile());	//<-- Still works`
	}
	
	
	
	private static final PrintStream out = System.out;

}

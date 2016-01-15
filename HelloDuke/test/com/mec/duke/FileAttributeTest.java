package com.mec.duke;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Set;

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
//		owner, dos, acl, basic, user
		fs.supportedFileAttributeViews().stream().forEach(s -> out.print(s + ", "));
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
	
	
	@Ignore
	@Test
	public void testUpdateFileAttribute() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		long time = System.currentTimeMillis();
		FileTime fileTime = FileTime.fromMillis(time);
		Files.getFileAttributeView(p, BasicFileAttributeView.class).setTimes(fileTime, fileTime, null);
		Files.setLastModifiedTime(p, fileTime);
		out.println("File last modified time updated successfully");
	}
	
	
	@Ignore
	@Test
	public void testDosFileAttribute() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		DosFileAttributes attr = Files.readAttributes(p, DosFileAttributes.class);
		out.printf("Is read only? %s\n", attr.isReadOnly());
		out.printf("Is Hidden? %s\n", attr.isHidden());
		out.printf("Is archive? %s\n", attr.isArchive());
		out.printf("Is system? %s\n", attr.isSystem());
	}
	
	@Ignore
	@Test
	public void testOwnderFileAttribute() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		FileOwnerAttributeView fv = Files.getFileAttributeView(p, FileOwnerAttributeView.class);
		UserPrincipal owner = fv.getOwner();
		out.printf("UserPrincile.name = %s\n", owner.getName());	//MIKETANG\MEC
		
		String principalName = "MIKETANG\\MEC";
		owner = p.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName(principalName);
		out.printf("Found principal: %s\n", owner);
		
		owner = (UserPrincipal) Files.getAttribute(p, "owner:owner");
		out.printf("Get owner through Files.getAttributes: %s\n", owner);
				
	}
	
	@Ignore
	@Test
	public void testPosixFileAttributes() throws IOException{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		try {
			PosixFileAttributes attr = Files.readAttributes(p, PosixFileAttributes.class);
		} catch (UnsupportedOperationException e) {
			out.println("POSIX File Attributes not supported in current file system.");
			e.printStackTrace(out);
		}
	}
	
	@Ignore
	@Test
	public void testACLAttributes() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		AclFileAttributeView attr = Files.getFileAttributeView(p, AclFileAttributeView.class);
		attr.getAcl().stream().forEach(this::print);
	}
	
	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void testAclAttributes2() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");

		List<AclEntry> attr = (List<AclEntry>) Files.getAttribute(p, "acl:acl");
		attr.stream().forEach(this::print);
	}
	
	private void print(AclEntry entry){
		out.printf("acl: %s\n", entry);
		out.printf("\tprincipal: %s\n", entry.principal());
		out.printf("\tflags: %s\n", entry.flags());
		out.printf("\ttype: %s\n", entry.type());
		out.printf("\tpermissions: %s\n", entry.permissions());
	}
	
	@Ignore
	@Test
	public void testFileStoreAttribute() throws Exception{
		FileSystems.getDefault().getFileStores().forEach(this::print);
	}
	
	private void print(FileStore store){
		try {
			//Note that  FileStore.name is different with rootDirectories;
			out.printf("FileStore %s, type: %s\n", store.name(), store.type());	
			out.printf("\tTotal Spacee: %sGB\n", store.getTotalSpace() / (1024 * 1024 * 2014));
			out.printf("\tUsable Space: %sGB\n", store.getUsableSpace() / (1024 * 1024 * 1024));
			out.printf("\tRead Only? %s\n", store.isReadOnly());
		} catch (Exception e) {
			e.printStackTrace(out);
		}
	}
	
	@Ignore
	@Test
	public void testFileStoreAttribute2() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		print(Files.getFileStore(p));
	}
	
	@Ignore
	@Test
	public void testeUserDefinedAttribute() throws Exception{
		Path p = Paths.get("test/com/mec/duke/FileAttributeTest.java");
		
		FileStore store = Files.getFileStore(p);
		boolean s = store.supportsFileAttributeView(UserDefinedFileAttributeView.class);
		out.printf("FileStore supports user defined file attribute? %s\n", s);
		
	}
	
	@Ignore
	@Test
	public void testUserDefinedAttribute2() throws Exception{
		Path p = Paths.get("test/com/mec/duke/PathTest.java");
		
		UserDefinedFileAttributeView udfav = Files.getFileAttributeView(p, UserDefinedFileAttributeView.class);
		
		int written = udfav.write("file.description", Charset.defaultCharset().encode("This file contains private info -- by mec"));
		out.printf("user defined attribute written successfully. attr size: %s\n", written);
	}
	
	
	@Ignore
	@Test
	public void testUserDefinedAttribute3() throws Exception{
		Path p = Paths.get("test/com/mec/duke/PathTest.java");
		
		UserDefinedFileAttributeView udfav = Files.getFileAttributeView(p, UserDefinedFileAttributeView.class);
		
		udfav.list().stream().forEach(attr -> {
			try {
				int attrSize = udfav.size(attr);
				ByteBuffer attrValue = ByteBuffer.allocateDirect(attrSize);
				udfav.read(attr, attrValue);
				attrValue.flip();
				out.printf("Attribute name: [%s], size of it: [%s], value: [%s]\n", attr, attrSize,
						Charset.defaultCharset().decode(attrValue).toString());
				
				udfav.delete(attr);
				out.printf("User defined attribute: [%s] has been removed.\n", attr);
			} catch (IOException e) {
				e.printStackTrace(out);
			}
		});
	}
	
	
	private static final PrintStream out = System.out;

}

















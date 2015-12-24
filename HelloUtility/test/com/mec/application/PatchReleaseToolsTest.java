package com.mec.application;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.After;
import org.junit.Test;

import com.mec.resources.FileParser;

public class PatchReleaseToolsTest {

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWriteJarFile() throws Exception{
//		fail("Not yet implemented");
		
		File curDir = new File(".");
		
		out.printf("current work directory: %s\n", curDir.getCanonicalPath());
		
		File distDir = new File(curDir, "dist");
		if(!distDir.exists()){
			distDir.mkdir();
		}
		
		final File sourceDir = new File(curDir, "src");
		final File classDir = new File(curDir, "bin");
		String[] packagedFiles = new String[]{
//				"com/mec/resources/"
				 "/com/mec/resources/Msg.java"
//				,"com/mec/resources/"
//				,"com/mec/resources/"
		};
		
		
		File patchFile = new File(distDir, "testPatch.jar");
		if(patchFile.exists()){
			patchFile.renameTo(new File(patchFile.getParentFile(), String.format("testPatch_del%s.jar", System.currentTimeMillis())));
		}
		patchFile = new File(distDir, "testPatch.jar");
//		patchFile.deleteOnExit();
//		patchFile.createNewFile();
		JarOutputStream outputJar = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(patchFile)));
		
		Arrays.asList(packagedFiles).stream().forEach(packageFile -> {
			try{
				if('/' == packageFile.charAt(0)){
					packageFile = packageFile.substring(1);
				}
				
				Set<String> classEntries = getClassEntries(sourceDir, classDir, packageFile);
				out.printf("\t Class Entries: %s\n", classEntries);
				
				String packageDir = packageFile.substring(0, packageFile.lastIndexOf("/"));
//				packageFile = packageFile.replaceAll("\\.java$", ".class");
//				zipFile(outputJar, classDir, packageFile);
				classEntries.stream().forEach(entry -> {
					
				try {
					zipFile(outputJar, classDir, String.format("%s/%s", packageDir, entry));
				} catch (Exception e) {
					e.printStackTrace();
				}
						
				});
			}catch(Exception e){
				e.printStackTrace();
			}
		});
		
//		outputJar.flush();
		outputJar.close();
	}
	
	private static void zipFile(ZipOutputStream outputJar, File classDir, String packageFile) throws IOException{
		if(null == packageFile || packageFile.isEmpty()){
			return;
		}
		try(BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(classDir, packageFile)), BUFFER_SIZE);){
			out.printf("\tAdd zip entry: %s\n", packageFile);
			ZipEntry entry = new ZipEntry(packageFile);
			outputJar.putNextEntry(entry);
			byte[] buffer = new byte[BUFFER_SIZE];
			int count = 0;
			while(-1 < (count = fis.read(buffer))){
				outputJar.write(buffer, 0, count);
			}
		}
	}
	
	

	/**
	 * INPUT: 
	 * 	"com/mec/resources/Msg.java"
	 * 
	 * OUTPUT:
	 * 	"com/mec/resources/Msg.class"
	 * 	"com/mec/resources/Msg$GetCallerClassNameMethod.class"
	 * 	"com/mec/resources/Msg$....class"
	 * ...
	 * @param classDir
	 * @param javaFilePath
	 * @return
	 */
	private static Set<String> getClassEntries(File sourceDir, File classDir, String javaFilePath){
		Set<String> retval = new HashSet<>();
		String classFilePath = javaFilePath.replaceAll("\\.java$", ".class");
		
		
//		String packageDir = javaFilePath.substring(0, javaFilePath.lastIndexOf("/"));
		File classFileName = new File(classDir, classFilePath);
	
		File classParent = classFileName.getParentFile();
		String classFileNameStr = classFileName.getName().replaceAll("\\.class$", "");
		List<String> filteredClasses = extractInnerClasses(classParent, classFileNameStr);
		retval.addAll(filteredClasses);
		
		
		//
		
		File sourceFile = new File(sourceDir, javaFilePath);		
		try {
			List<String> otherClasses = FileParser.getClassesFromJavaFile(sourceFile);
			otherClasses.stream().forEach(c -> {
//				retval.add(c + ".");
				retval.addAll(extractInnerClasses(classParent, c));
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		return retval;
	}
	
	private static List<String> extractInnerClasses(File classParent, String classFileNameStr){

		Pattern cp = Pattern.compile("^" + classFileNameStr + "(\\$\\w+)*.class$");
		String[] filteredClasses = classParent.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return cp.matcher(name).matches();
			}
		});
		return Arrays.asList(filteredClasses);
	}
	
	
	private static final int BUFFER_SIZE = 1024;
	private static final PrintStream out = System.out;


}

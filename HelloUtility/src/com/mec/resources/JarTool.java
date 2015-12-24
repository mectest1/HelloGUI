package com.mec.resources;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JarTool {

	private JarTool(){
//		this.out = out;
	}
	
	public static JarTool newInstance(PrintWriter logWriter){
//		JarTool retval = new JarTool(logWriter);
		JarTool retval = new JarTool();
		return retval;
	}
	
	/**
	 * INPUT:
	 * 	projectDirectory:
	 * 		
	 *  modifyList:
	 *  	"/com/mec/resources/Msg.java"
	 * 
	 * @param workspaceDirectory
	 * @param projectName
	 * @param modifyList
	 * @param patchDistDir
	 * @throws Exception
	 */
	public void writeToJar(String workspaceDirectory, String projectName, String[] modifyList
//			, String patchDistDir
//			, 
			) throws Exception{
//		fail("Not yet implemented");
		workspaceDirectory = normalizPath(workspaceDirectory);
		for(int i = 0; i < modifyList.length; ++i){
			modifyList[i] = normalizPath(modifyList[i]);
		}
		
//		File curDir = new File(".");
		File curDir = new File(workspaceDirectory);
		
//		out.printf("current work directory: %s\n", curDir.getCanonicalPath());
		out.printf(Msg.get(this, "log.currentWorkDir"), curDir.getCanonicalPath());
		
//		File distDir = new File(curDir, "dist");
		File distDir = new File(curDir, Msg.get(this, "default.dist.dir"));
		if(!distDir.exists()){
			distDir.mkdir();
		}
		
//		final File sourceDir = new File(curDir, "src");
//		final File classDir = new File(curDir, "bin");
		final File sourceDir = new File(curDir, Msg.get(this, "default.source.dir"));
		final File classDir = new File(curDir, Msg.get(this, "default.binary.dir"));
//		String[] modifyList = null;11
//		String[] packagedFiles = new String[]{
////				"com/mec/resources/"
//				 "/com/mec/resources/Msg.java"
////				,"com/mec/resources/"
////				,"com/mec/resources/"
//		};
		
		
//		File patchFile = new File(distDir, "testPatch.jar");
//		String projectName = curDir.getPath().substring(curDir.getPath().subString, endIndex)
		File patchFile = new File(distDir, String.format(Msg.get(this, "default.jar.name"), projectName));
		if(patchFile.exists()){
//			patchFile.renameTo(new File(patchFile.getParentFile(), String.format("testPatch_del%s.jar", System.currentTimeMillis())));
			patchFile.renameTo(new File(patchFile.getParentFile(), String.format(Msg.get(this, "default.jar.delName"), projectName, System.currentTimeMillis())));
		}
//		patchFile = new File(distDir, "testPatch.jar");
		patchFile = new File(distDir, String.format(Msg.get(this, "default.jar.name"), projectName));
//		patchFile.deleteOnExit();
//		patchFile.createNewFile();
		JarOutputStream outputJar = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(patchFile)));
		
		Arrays.asList(modifyList).stream().forEach(packageFile -> {
			try{
				if(NIX_PATH.equals(String.valueOf(packageFile.charAt(0)))){
					packageFile = packageFile.substring(1);
				}
				
				Set<String> classEntries = getClassEntries(sourceDir, classDir, packageFile);
				out.printf(Msg.get(this, "log.debug.classEntries"), classEntries);
				
				String packageDir = packageFile.substring(0, packageFile.lastIndexOf(NIX_PATH));
//				packageFile = packageFile.replaceAll("\\.java$", ".class");
//				zipFile(outputJar, classDir, packageFile);
				classEntries.stream().forEach(entry -> {
					
				try {
					zipFile(outputJar, classDir, String.format(Msg.get(this, "config.packagePathAndEntry"), packageDir, entry));
				} catch (Exception e) {
					e.printStackTrace(out);
				}
						
				});
			}catch(Exception e){
				e.printStackTrace(out);
			}
		});
		
//		outputJar.flush();
		outputJar.close();
	}
	
	
	
	/**
	 * 
	 * @param outputJar the JarOutputStream
	 * @param entriesParentDirectory the parent directory that contains all entry files
	 * @param entryName entry name that will be zipped into file;
	 * @throws IOException
	 */
	private void zipFile(ZipOutputStream outputJar, File entriesParentDirectory, String entryName) throws IOException{
		if(null == entryName || entryName.isEmpty()){
			return;
		}
		try(BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(entriesParentDirectory, entryName)), BUFFER_SIZE);){
//			out.printf("\tAdd zip entry: %s\n", packageFile);
			out.printf(Msg.get(this, "log.debug.entryAdded"), entryName);
			ZipEntry entry = new ZipEntry(entryName);
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
	 * 	"src/"
	 * 	"bin/"
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
	private Set<String> getClassEntries(File sourceDir, File classDir, String javaFilePath){
		Set<String> retval = new HashSet<>();
		String classFilePath = javaFilePath.replaceAll(Msg.get(this, "pattern.java2class.java"), Msg.get(this, "pattern.java2class.class"));
		
		
//		String packageDir = javaFilePath.substring(0, javaFilePath.lastIndexOf("/"));
		File classFileName = new File(classDir, classFilePath);
	
		File classParent = classFileName.getParentFile();
		String classFileNameStr = classFileName.getName().replaceAll(Msg.get(this, "pattern.removeClassSuffix"), "");
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
			e.printStackTrace(out);
		} catch (IOException e) {
			e.printStackTrace(out);
		}
		
		
		
		return retval;
	}
	
	/**
	 * Extract inner class according to class name patterns
	 * 
	 * INPUT:
	 * 	"bin/com/mec/resources"
	 *	"Msg"
	 * OUTPUT:
	 * 	"Msg$GetCallerClassNameMethod"
	 * 	"Msg$SecurityManagerMethod$CallerSecurityManager"
	 * 	"Msg$SecurityManagerMethod"
	 * 	...
	 * @param classParent parent directory for class files
	 * @param classFileNameStr all .class files as well as innert .class files;
	 * @return
	 */
	private static List<String> extractInnerClasses(File classParent, String classFileNameStr){

//		Pattern cp = Pattern.compile("^" + classFileNameStr + "(\\$\\w+)*.class$");
		Pattern cp = Pattern.compile(String.format(Msg.get(JarTool.class, "pattern.classFileName"), classFileNameStr));
		String[] filteredClasses = classParent.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return cp.matcher(name).matches();
			}
		});
		return Arrays.asList(filteredClasses);
	}
	
	
	private String normalizPath(String path){
		if(null == path || path.isEmpty()){
			return path;
		}
		return path.replaceAll(WIN_PATH, NIX_PATH);
	}
	
	public String getLog(){
		out.flush();
		return logStr.toString();
	}
	
	private static final int BUFFER_SIZE = 1024;
//	private static final PrintStream out = System.out;
	private StringWriter logStr = new StringWriter();
	private PrintWriter out = new PrintWriter(logStr);
	private static final String NIX_PATH = Msg.get(JarTool.class, "config.nixPath");
	private static final String WIN_PATH = Msg.get(JarTool.class, "config.winPath"); 
}

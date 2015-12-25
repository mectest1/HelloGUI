package com.mec.resources;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
//		initLog();
	}
	
	public static JarTool newInstance(ErrorLogger logger){
//		JarTool retval = new JarTool(logWriter);
		JarTool retval = new JarTool();
		retval.setLogger(logger);
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
//	public void writeToJar(File workspaceDirectory, String projectName, List<String> modifyList
//			
//			, File patchReleaseDirectory
//			){
//		
//	}
	
	public void writeJavaToJar(File workspaceDirectory, String projectName, List<String> modifyList
//			, String patchDistDir
			, File patchReleaseDirectory
			) throws Exception{
//		fail("Not yet implemented");
//		workspaceDirectory = normalizPath(workspaceDirectory);
		for(int i = 0; i < modifyList.size(); ++i){
			modifyList.set(i, normalizPath(modifyList.get(i)));
		}
		
//		File curDir = new File(".");
//		File curDir = new File(workspaceDirectory);
		File curDir = new File(workspaceDirectory, projectName);
//		if(!(curDir.exists() && curDir.isDirectory())){
//			throw new IllegalArgumentException(String.format(Ms, args));
//		}
		validateDirectory(curDir, String.format(Msg.getExpMsg(this, "invalid.projectDirectory"), curDir.getCanonicalPath()));
		
//		out.printf("current work directory: %s\n", curDir.getCanonicalPath());
//		out.printf(Msg.get(this, "log.currentWorkDir"), curDir.getCanonicalPath());
		logger.log(String.format(Msg.get(this, "log.currentWorkDir"), curDir.getCanonicalPath()));
//		File distDir = new File(curDir, "dist");
//		File distDir = new File(curDir, Msg.get(this, "default.dist.dir"));
//		if(!distDir.exists()){
//			distDir.mkdir();
//		}
		File distDir = patchReleaseDirectory;
		
//		final File sourceDir = new File(curDir, "src");
//		final File classDir = new File(curDir, "bin");
//		final File sourceDir = new File(curDir, Msg.get(this, "default.source.dir"));
//		final File classDir = new File(curDir, Msg.get(this, "default.binary.dir"));
		final File sourceDir = getFirstExisted(curDir, SOURCE_FOLDER);
		final File classDir = getFirstExisted(curDir, BINARY_FOLDER);
		validateDirectory(sourceDir, String.format(Msg.getExpMsg(this, "invalid.sourceDirectory"), sourceDir.getCanonicalFile()));
		validateDirectory(classDir, String.format(Msg.getExpMsg(this, "invalid.binaryDirectory"), classDir.getCanonicalFile()));
//		String[] modifyList = null;11
//		String[] packagedFiles = new String[]{
////				"com/mec/resources/"
//				 "/com/mec/resources/Msg.java"
////				,"com/mec/resources/"
////				,"com/mec/resources/"
//		};
		
		
//		File patchFile = new File(distDir, "testPatch.jar");
//		String projectName = curDir.getPath().substring(curDir.getPath().subString, endIndex)
//		File patchFile = new File(distDir, String.format(Msg.get(this, "default.jar.name"), projectName));
//		if(patchFile.exists()){
////			patchFile.renameTo(new File(patchFile.getParentFile(), String.format("testPatch_del%s.jar", System.currentTimeMillis())));
//			File tmpFile = new File(patchFile.getParentFile(), String.format(Msg.get(this, "default.jar.delName"), projectName, System.currentTimeMillis()));
//			patchFile.renameTo(tmpFile);
//			patchFile = new File(distDir, String.format(Msg.get(this, "default.jar.name"), projectName));
//		}
//		if(!patchFile.exists()){
//			patchFile.createNewFile();
//		}
		File patchFile = createNewFile(distDir
				, String.format(Msg.get(this, "default.jar.name"), projectName)
				, String.format(Msg.get(this, "default.jar.delName"), projectName, System.currentTimeMillis()));
//		patchFile = new File(distDir, "testPatch.jar");
//		patchFile.deleteOnExit();
//		patchFile.createNewFile();
		JarOutputStream eelibJar = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(patchFile)));
		JarOutputStream webContentJar = null;
		
		for (String packageFile : modifyList) {
//		modifyList.stream().forEach(packageFile -> {
//			try{
//				if(NIX_PATH.equals(String.valueOf(packageFile.charAt(0)))){
//				if(packageFile.startsWith(NIX_PATH)){
//					packageFile = packageFile.substring(1);
//				}
			packageFile = trimLeadingSlash(packageFile);
			
			if(isJavaFile(packageFile)){
				writeJavaClassToJar(eelibJar, sourceDir, classDir, packageFile);
			}else if(isWebContentFile(packageFile)){
				if(null == webContentJar){
					final File webContentFolder = getFirstExisted(curDir, WEB_CONTENT_FOLDER);
					validateDirectory(webContentFolder, String.format(Msg.get(this, "exception.invalid.webContentDirectory"), webContentFolder.getCanonicalPath()));
					//
					String defaultWebContentJarDir = Msg.get(this, "webContent.dir");
					String webContentJarName = String.format(Msg.get(this, "default.jar.name"), defaultWebContentJarDir);
					File webContentJarFile  = createNewFile(distDir, webContentJarName, String.format(Msg.get(this, "default.jar.delName"), webContentJarName, System.currentTimeMillis()));
					webContentJar = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(webContentJarFile)));
				}
				writeWebContentToJar(webContentJar, curDir, packageFile);
			}else{
//				out.printf(String.format(Msg.get(this, "error.unsupported.fileType"), packageFile));
				logger.log(String.format(Msg.get(this, "error.unsupported.fileType"), packageFile));
			}
//			Set<String> classEntries = getClassEntries(sourceDir, classDir, packageFile);
//			out.printf(Msg.get(this, "log.debug.classEntries"), classEntries);
//			String packageDir = packageFile.substring(0, packageFile.lastIndexOf(NIX_PATH));
//			//				packageFile = packageFile.replaceAll("\\.java$", ".class");
//			//				zipFile(outputJar, classDir, packageFile);
//			classEntries.stream().forEach(entry -> {
//		
//				try {
//					zipFile(outputJar, classDir, String.format(Msg.get(this, "config.packagePathAndEntry"), packageDir, entry));
//				} catch (Exception e) {
//					e.printStackTrace(out);
//				}
//		
//			});
//			}catch(Exception e){
//				e.printStackTrace(out);
//			}
//		});
		}
		
		
		//		outputJar.flush();
		eelibJar.close();
		if(null != webContentJar){
			webContentJar.close();
		}
	}
	
	/**
	 * project
	 * @param projectDirectory
	 * @param packageFile
	 */
	private void writeWebContentToJar(ZipOutputStream webContentJar, File webProjectDirectory, String packageFile) throws IOException{
		final File webContentFolder = getFirstExisted(webProjectDirectory, WEB_CONTENT_FOLDER);
//		validateDirectory(webContentFolder, String.format(Msg.get(this, "exception.invalid.webContentDirectory"), webContentFolder.getCanonicalPath()));
//		//
		String defaultWebContentJarDir = Msg.get(this, "webContent.dir");
//		String webContentJarName = String.format(Msg.get(this, "default.jar.name"), defaultWebContentJarDir);
//		File webContentJarFile =  createNewFile(distDirectory, webContentJarName, null);
//		File webContentJarFile  = new File(distDirectory, webContentJarName);
//		try(JarOutputStream webContentJar = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(webContentJarFile)));){
			String entryName = trimLeadingSlash(trimLeading(packageFile, defaultWebContentJarDir));
			zipFile(webContentJar, webContentFolder, entryName);
//		}
	}
	
	public static File createNewFile(File parentDirectory, String fileName, String bakFileName) throws IOException{
		File patchFile = new File(parentDirectory, fileName);
		if(patchFile.exists()){
//			patchFile.renameTo(new File(patchFile.getParentFile(), String.format("testPatch_del%s.jar", System.currentTimeMillis())));
			if(null !=  bakFileName && !bakFileName.trim().isEmpty()){
				File tmpFile = new File(patchFile.getParentFile(), bakFileName);
				patchFile.renameTo(tmpFile);
				
			}else{
				//
				patchFile.delete();
			}
			patchFile = new File(parentDirectory, fileName);
		}
		if(!patchFile.exists()){
			patchFile.createNewFile();
		}
		return patchFile;
	}
	private void writeJavaClassToJar(ZipOutputStream outputJar, File sourceDir, File classDir, String packageFile){
		Set<String> classEntries = getClassEntries(sourceDir, classDir, packageFile);
//		out.printf(Msg.get(this, "log.debug.classEntries"), classEntries);
		logger.log(String.format(Msg.get(this, "log.debug.classEntries"), classEntries));
		String packageDir = packageFile.substring(0, packageFile.lastIndexOf(NIX_PATH));
		//				packageFile = packageFile.replaceAll("\\.java$", ".class");
		//				zipFile(outputJar, classDir, packageFile);
//		classEntries.stream().forEach(entry -> {
	
		for (String entry : classEntries) {
			try {
				zipFile(outputJar, classDir,
						String.format(Msg.get(this, "config.packagePathAndEntry"), packageDir, entry));
			} catch (Exception e) {
//				e.printStackTrace(out);
				logger.log(e);
			} 
		}
	
//		});
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
//			out.printf(Msg.get(this, "log.debug.entryAdded"), entryName);
			logger.log(String.format(Msg.get(this, "log.debug.entryAdded"), entryName));
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
		} catch (IOException e) {
//			e.printStackTrace(out);
			logger.log(e);
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
	
	private boolean isJavaFile(String fileName){
		return FileParser.isStringEndsWithSuffix(fileName, Arrays.asList(Msg.get(this, "suffix.java")));
	}
	
	private boolean isWebContentFile(String fileName){
		boolean retval = false;
		retval = FileParser.isStringEndsWithSuffix(fileName, Msg.getList(this, "suffix.webContent"));
		return retval;
	}
	

	public static void validateDirectory(File directory, String errorMsg) throws IllegalArgumentException{
		if(!(directory.exists() && directory.isDirectory())){
			throw new IllegalArgumentException(errorMsg);
		}
//		return true;
	}
	
	
	public static File getFirstExisted(File parentDir, List<String> fileCandidates){
		File retval = null;
		for(String fileCandidate : fileCandidates){
			retval = new File(parentDir, fileCandidate);
			if(retval.exists()){
				break;
			}
		}
		
		return retval;
	}
	
	public static String exceptionToStr(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		try {
			sw.close();
		} catch (IOException e1) {
			return e.getMessage();
		}
		return sw.toString();
	}
	public static String normalizPath(String path){
		if(null == path || path.isEmpty()){
			return path;
		}
		return path.replaceAll(WIN_PATH_PATTERN, NIX_PATH);
	}
	
	public static String trimLeadingSlash(String line){
		return trimLeading(line, NIX_PATH);
	}
	
	public static String trimLeading(String line, String trimmedStr){
		if(null == line || line.isEmpty() || null == trimmedStr || trimmedStr.isEmpty()){
			return line;
		}
//		if(line.startsWith(trimmedStr)){
		while(line.startsWith(trimmedStr)){
			line = line.substring(trimmedStr.length());
		}
		return line;
	}
//	public String getLog(){
//		out.flush();
//		String retval = logStr.toString();
//		initLog();
//		return retval;
//	}
//	private void initLog(){
//		logStr = new StringWriter();
//		out = new PrintWriter(logStr);
//	}
	
	private void setLogger(ErrorLogger logger){
		this.logger = logger;
	}
	private static final int BUFFER_SIZE = 1024;
//	private static final PrintStream out = System.out;
//	private StringWriter logStr;
//	private PrintWriter out;
	public static final String NIX_PATH = Msg.get(JarTool.class, "config.nixPath");
	public static final String WIN_PATH_PATTERN = Msg.get(JarTool.class, "config.winPath.pattern"); 
	private ErrorLogger logger;
	
	private static final List<String> BINARY_FOLDER = Msg.getList(JarTool.class, "binary.dir");
	public static final List<String> SOURCE_FOLDER = Msg.getList(JarTool.class, "source.dir");
	private static final List<String> WEB_CONTENT_FOLDER = Msg.getList(JarTool.class, "webContent.dir");
	
	public static Pattern WEB_CONTENT_JAR = Pattern.compile(Msg.get(JarTool.class, "pattern.jarName.WebContent"));
	public static Pattern EE_LIB_JAR = Pattern.compile(Msg.get(JarTool.class, "pattern.jarName.EE_LIB"));
}

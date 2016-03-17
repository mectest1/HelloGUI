package com.mec.resources;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JarTool {

	private JarTool(){
//		this.out = out;
//		initLog();
	}
	
	public static JarTool newInstance(MsgLogger logger){
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
	
	public void writeFilesToJar(Path workspaceDirectory, String projectName, Collection<String> modifyList
//			, String patchDistDir
//			, File patchReleaseDirectory
			, Path patchReleaseDirectory
			) throws Exception{
		Set<String> modifyListSet = new HashSet<>();
//		for(int i = 0; i < modifyList.size(); ++i){
		for(String modifyListItem : modifyList){
//			modifyList.set(i, normalizePath(modifyList.get(i)));
			modifyListSet.add(normalizePath(modifyListItem));
		}
		
//		File curDir = new File(workspaceDirectory, projectName);
		Path curDir = workspaceDirectory.resolve(projectName);
		validateDirectory(curDir, String.format(Msg.getExpMsg(this, "invalid.projectDirectory"), curDir.toAbsolutePath()));
		
		logger.log(String.format(Msg.get(this, "log.currentWorkDir"), curDir.toAbsolutePath()));
//		File distDir = patchReleaseDirectory;
		Path distDir = patchReleaseDirectory;
		
//		final File sourceDir = getFirstExisted(curDir, SOURCE_FOLDER);
//		final File classDir = getFirstExisted(curDir, BINARY_FOLDER);
		final Path sourceDir = getFirstExisted(curDir, SOURCE_FOLDER);
		final Path classDir = getFirstExisted(curDir, BINARY_FOLDER);
//		validateDirectory(sourceDir, String.format(Msg.getExpMsg(this, "invalid.sourceDirectory"), sourceDir.getCanonicalFile()));
//		validateDirectory(classDir, String.format(Msg.getExpMsg(this, "invalid.binaryDirectory"), classDir.getCanonicalFile()));
		validateDirectory(sourceDir, String.format(Msg.getExpMsg(this, "invalid.sourceDirectory"), sourceDir.toAbsolutePath()));
		validateDirectory(classDir, String.format(Msg.getExpMsg(this, "invalid.binaryDirectory"), classDir.toAbsolutePath()));
		
		JarOutputStream eelibJar = null;	
		JarOutputStream webContentJar = null;
		
		for (String packageFile : modifyListSet) {
			packageFile = trimLeadingSlash(packageFile);
			
			if(isInDirectory(packageFile, sourceDir)){	//package file in source directory;
				if(null == eelibJar){
					Path patchFile = createNewFile(distDir
							, String.format(Msg.get(this, "default.jar.name"), projectName)
							, String.format(Msg.get(this, "default.jar.delName"), projectName, System.currentTimeMillis()));
//					eelibJar = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(patchFile)));
					eelibJar = new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(patchFile)));
				}
				if(isJavaFile(packageFile)){
					writeJavaClassToJar(eelibJar, sourceDir, classDir, packageFile);
				}else{
//					writeSourceFileToJar(eelibJar, sourceDir, classDir, packageFile);
					zipFile(eelibJar, classDir, packageFile);
				}
			}else if(isWebContentFile(packageFile)){
				if(null == webContentJar){
					final Path webContentFolder = getFirstExisted(curDir, WEB_CONTENT_FOLDER);
					validateDirectory(webContentFolder, String.format(Msg.get(this, "exception.invalid.webContentDirectory"), webContentFolder.toAbsolutePath()));
					//
					String defaultWebContentJarDir = Msg.get(this, "webContent.dir");
					String webContentJarName = String.format(Msg.get(this, "default.jar.name"), defaultWebContentJarDir);
					Path webContentJarFile  = createNewFile(distDir, webContentJarName, String.format(Msg.get(this, "default.jar.delName"), webContentJarName, System.currentTimeMillis()));
//					webContentJar = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(webContentJarFile)));
					webContentJar = new JarOutputStream(new BufferedOutputStream(Files.newOutputStream(webContentJarFile)));
				}
				writeWebContentToJar(webContentJar, curDir, packageFile);
			}else{
				logger.log(String.format(Msg.get(this, "error.unsupported.fileType"), packageFile));
			}
		}
		
		
//		if(null != eelibJar){
//			eelibJar.close();
//		}
//		if(null != webContentJar){
//			webContentJar.close();
//		}
		closeOutputStream(eelibJar, webContentJar);
	}
	
	/**
	 * Close these OutputStream instances when they're not null.
	 * @param oses
	 * @throws IOException
	 */
	public static void closeOutputStream(OutputStream ... oses) throws IOException{
		for(OutputStream os : oses){
			if(null != os){
				os.close();
			}
		}
	}
	/**
	 * project
	 * @param projectDirectory
	 * @param packageFile
	 */
	private void writeWebContentToJar(ZipOutputStream webContentJar, Path webProjectDirectory, String packageFile) throws IOException{
		final Path webContentFolder = getFirstExisted(webProjectDirectory, WEB_CONTENT_FOLDER);
		String defaultWebContentJarDir = Msg.get(this, "webContent.dir");
		String entryName = trimLeadingSlash(trimLeading(packageFile, defaultWebContentJarDir));
		zipFile(webContentJar, webContentFolder, entryName);
	}
	
	public static Path createNewFile(Path parentDirectory, String fileName, String bakFileName) throws IOException{
//		File patchFile = new File(parentDirectory, fileName);
		Path patchFile = parentDirectory.resolve(fileName);
//		if(patchFile.exists()){
		if(Files.exists(patchFile)){
//			patchFile.renameTo(new File(patchFile.getParentFile(), String.format("testPatch_del%s.jar", System.currentTimeMillis())));
			if(null !=  bakFileName && !bakFileName.trim().isEmpty()){
				Path tmpFile = patchFile.getParent().resolve(bakFileName);
//				patchFile.renameTo(tmpFile);
				Files.move(patchFile, tmpFile);
			}else{
				//
//				patchFile.delete();
				Files.deleteIfExists(patchFile);
			}
//			patchFile = new File(parentDirectory, fileName);
			patchFile = parentDirectory.resolve(fileName);
		}
//		if(!patchFile.exists()){
//			patchFile.createNewFile();
//		}
		if(!Files.exists(patchFile)){
			Files.createFile(patchFile);
		}
		return patchFile;
	}
	
	private void writeJavaClassToJar(ZipOutputStream outputJar, Path sourceDir, Path classDir, String packageFile){
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
	 * INPUT:
	 * <dl>
	 * 	<dt>outputJar:</dt> <dd>JarOutputStream("E:\Date\2015-12\Defect #36895\_testPatch\EE_LIB\EximBillsBusBase.jar")</dd>
	 * 	<dt>entriesParentDirectory:</dt> <dd>E:\SVN\EximbillsEnterprise\branches\EECore262.1_20140307\EximBillsFactoringObjects\bin</dd>
	 * 	<dt>entryName:</dt>
	 * 				<dd>com/cs/eximap/busiintf/factoring/bo/ASEFactoringBaseBO.java<br/>
	 * 				com/cs/eximap/busiintf/factoring/bo/trxref.xml<br/>
	 * 				SYS_JS/SYS_BaseFunc_CAL.js
	 * 				</dd>
	 * </dl>
	 * @param outputJar the JarOutputStream
	 * @param entriesParentDirectory the parent directory that contains all entry files
	 * @param entryName entry name that will be zipped into file;
	 * @throws IOException
	 */
	private void zipFile(ZipOutputStream outputJar, Path entriesParentDirectory, String entryName) throws IOException{
		if(null == entryName || entryName.isEmpty()){
			return;
		}
		
//		try(BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(entriesParentDirectory, entryName)), BUFFER_SIZE);){
		Path entryFile = entriesParentDirectory.resolve(entryName);
		try(InputStream fis = new BufferedInputStream(Files.newInputStream(entryFile))){
//			out.printf("\tAdd zip entry: %s\n", packageFile);
//			out.printf(Msg.get(this, "log.debug.entryAdded"), entryName);
			logger.log(String.format(Msg.get(this, "log.debug.entryAdded"), entryName));
			ZipEntry entry = new ZipEntry(entryName);
			entry.setLastModifiedTime(Files.getLastModifiedTime(entryFile));	//<-- set the entry's last modified time
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
	 * 	"Msg.class"
	 * 	"Msg$GetCallerClassNameMethod.class"
	 * 	"Msg$....class"
	 * ...
	 * @param classDir
	 * @param javaFilePath
	 * @return
	 */
//	private Set<String> getClassEntries(File sourceDir, File classDir, String javaFilePath){
	private Set<String> getClassEntries(Path sourceDir, Path classDir, String javaFilePath){
		Set<String> retval = new HashSet<>();
		String classFilePath = javaFilePath.replaceAll(Msg.get(this, "pattern.java2class.java"), Msg.get(this, "pattern.java2class.class"));
		
		
//		String packageDir = javaFilePath.substring(0, javaFilePath.lastIndexOf("/"));
//		File classFileName = new File(classDir, classFilePath);
		Path classFileName = classDir.resolve(classFilePath);
	
//		File classParent = classFileName.getParentFile();
		Path classParent = classFileName.getParent();
		String classFileNameStr = classFileName.getFileName().toString().replaceAll(Msg.get(this, "pattern.removeClassSuffix"), "");
		try {
			List<String> filteredClasses = extractInnerClasses(classParent, classFileNameStr);
			retval.addAll(filteredClasses);
			
//			File sourceFile = new File(sourceDir, javaFilePath);		
			Path sourceFile = sourceDir.resolve(javaFilePath);		
				List<String> otherClasses = FileParser.getClassesFromJavaFile(sourceFile);
				otherClasses.stream().forEach(c -> {
//					retval.add(c + ".");
					try {
						retval.addAll(extractInnerClasses(classParent, c));
					} catch (Exception e) {
						logger.log(e);
					}
			});
		
		} catch (IOException e) {
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
	 * @throws IOException 
	 */
	private static List<String> extractInnerClasses(Path classParent, String classFileNameStr) throws IOException{

//		Pattern cp = Pattern.compile("^" + classFileNameStr + "(\\$\\w+)*.class$");
		Pattern cp = Pattern.compile(String.format(Msg.get(JarTool.class, "pattern.classFileName"), classFileNameStr));
//		String[] filteredClasses = classParent.list(new FilenameFilter() {
//			@Override
//			public boolean accept(File dir, String name) {
//				return cp.matcher(name).matches();
//			}
//		});
		return Files.list(classParent)
				.filter(f -> cp.matcher(f.getFileName().toString()).matches())
				.map(f -> f.getFileName().toString()).collect(Collectors.toList());
//		return Arrays.asList(filteredClasses);
	}
	
	private boolean isJavaFile(String fileName){
		return FileParser.isStringEndsWithSuffix(fileName, Arrays.asList(Msg.get(this, "suffix.java")));
	}
	
	/**
	 * Check if the specified file is in directory
	 * @param filePath the specified file
	 * @param directory dose the file reside in this directory?
	 * @return
	 */
	private boolean isInDirectory(String filePath, Path directory){
//		boolean retval = false;
//		File file = new File(directory, filePath);
//		retval = file.exists();
//		return retval;
		return Files.exists(directory.resolve(filePath));
	}
	
	private boolean isWebContentFile(String fileName){
		boolean retval = false;
		retval = FileParser.isStringEndsWithSuffix(fileName, Msg.getList(this, "suffix.webContent"));
		return retval;
	}
	

	public static void validateDirectory(Path directory, String errorMsg) throws IllegalArgumentException{
//		if(!(directory.exists() && directory.isDirectory())){
		if(! (Files.exists(directory) && Files.isDirectory(directory))){
			throw new IllegalArgumentException(errorMsg);
		}
//		return true;
	}
	
	
	public static Path getFirstExisted(Path parentDir, List<String> fileCandidates){
		Path retval = null;
		for(String fileCandidate : fileCandidates){
//			retval = new File(parentDir, fileCandidate);
//			if(retval.exists()){
//				break;
//			}
			/*
			 * INPUT:
			 * parentDir: E:\foo\bar\
			 * fileCandidate: /
			 * 
			 * OUTPUT:
			 * retval: E:/		//interesting, huh? 
			 */
			retval = parentDir.resolve(fileCandidate);
			if(Files.exists(retval)){
				break;
			}
		}
		
		return retval;
	}
	
	/**
	 * Convert exception to full stack trace string. The output would be same as {@link Exception#printStackTrace()}
	 * @param e convert this exception to string with fill stack trace info
	 * @return
	 */
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
	/**
	 * Replace all '\' with '/' in directory path;
	 * @param path directory path
	 * @return
	 */
	public static String normalizePath(String path){
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
	
	public static String trimTrailing(String line, String trimmedStr){
		if(null == line || line.isEmpty() || null ==trimmedStr || trimmedStr.isEmpty()){
			return line;
		}
		//
		while(line.endsWith(trimmedStr)){
			line = line.substring(0, line.length() - trimmedStr.length());
		}
		return line;
	}

	private void setLogger(MsgLogger logger){
		this.logger = logger;
	}
	private static final int BUFFER_SIZE = 1024;
//	private static final PrintStream out = System.out;
//	private StringWriter logStr;
//	private PrintWriter out;
	public static final String NIX_PATH = Msg.get(JarTool.class, "config.nixPath");
	public static final String WIN_PATH_PATTERN = Msg.get(JarTool.class, "config.winPath.pattern"); 
	private MsgLogger logger;
	
	private static final List<String> BINARY_FOLDER = Msg.getList(JarTool.class, "binary.dir");
	public static final List<String> SOURCE_FOLDER = Msg.getList(JarTool.class, "source.dir");
	private static final List<String> WEB_CONTENT_FOLDER = Msg.getList(JarTool.class, "webContent.dir");
	
	public static Pattern WEB_CONTENT_JAR = Pattern.compile(Msg.get(JarTool.class, "pattern.jarName.WebContent"));
	public static Pattern EE_LIB_JAR = Pattern.compile(Msg.get(JarTool.class, "pattern.jarName.EE_LIB"));
}

package com.mec.application.views;

import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mec.resources.FileParser;
import com.mec.resources.JarTool;
import com.mec.resources.Msg;
import com.mec.resources.MsgLogger;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PatchReleaseController implements MsgLogger{

	@FXML
	private TextField workSpaceDirectory;
	
	@FXML
	private TextField patchReleaseDirectory;
	
	@FXML
	private TextArea modifyList;
	
	
	
	@FXML
	private TextArea logMsg;
	
	
	@FXML
	private Button startPatch;
	@FXML
	private Button clearLogBtn;
	
	@FXML
	private void initialize(){
//		logMsg.textProperty().bind(logs);
		fieldEmpty.bind(
				Bindings.or(workSpaceDirectory.textProperty().isEmpty(), patchReleaseDirectory.textProperty().isEmpty())
				.or(modifyList.textProperty().isEmpty())
				);
		startPatch.disableProperty().bind(fieldEmpty);
		clearLogBtn.disableProperty().bind(logMsg.textProperty().isEmpty());
		
		workSpaceDirectory.setPromptText(Msg.get(this, "prompt.workspaceDir"));
		modifyList.setPromptText(Msg.get(this, "prompt.modifyList"));
		patchReleaseDirectory.setPromptText(Msg.get(this, "prompt.patchReleaseDir"));
	}
	
	
	@FXML
	private void onStartPatch(){
		try{
//			if(!validateParams()){
//				throw new IllegalArgumentException(Msg.get(this, "error.filedsRequired"));
//			}
			
			String workspaceDirStr = JarTool.normalizePath(workSpaceDirectory.getText());
//			File workspaceDir = new File(workspaceDirStr);
			Path workspaceDir = Paths.get(workspaceDirStr);
//			if(!(workspaceDir.exists() && workspaceDir.isDirectory())){
//				throw new IllegalArgumentException(String.format(Msg.getExpMsg(this, "invalid.workspace"), workspaceDirStr));
//			}
			JarTool.validateDirectory(workspaceDir, String.format(Msg.getExpMsg(this, "invalid.workspace"), workspaceDirStr));
			
			String patchReleaseDirStr = JarTool.normalizePath(patchReleaseDirectory.getText());
//			File patchReleaseDir = new File(patchReleaseDirStr);
			Path patchReleaseDir = Paths.get(patchReleaseDirStr);
			if(!(Files.exists(patchReleaseDir))){
				log(String.format(Msg.get(this, "info.patchReleaseDir.create"), patchReleaseDir));
			}
			
			String modifyListContent = modifyList.getText();
			List<String> modifyList = FileParser.normalizeModifyList(modifyListContent);
			Map<String, Set<String>> modifyListMap = FileParser.parseModifyList(modifyList);
			
			for(String projectName : modifyListMap.keySet()){
				Set<String> sourceFileList = modifyListMap.get(projectName);
				jarTool.writeFilesToJar(workspaceDir, projectName, sourceFileList, patchReleaseDir);
				log(FileParser.NEWLINE);
			}
			
			relocateJars(patchReleaseDir);
			writeReadMe(patchReleaseDir, modifyList);
//			appendLog(jarTool.getLog());
		}catch(Exception e){
//			appendLog(Msg.getExpMsg(e, e.getMessage()));
//			log(String.format(Msg.get(this, "exception.log"), e.getClass().getSimpleName(), JarTool.exceptionToStr(e)));
			log(e);
		}
	}
	
//	private boolean validateParams(){
//		if(workSpaceDirectory.getText().isEmpty())
//		if(workSpaceDirectory.getText().isEmpty()
//			|| patchReleaseDirectory.getText().isEmpty()
//			|| modifyList.getText().isEmpty()
//				){
//			
//		}
			
//	}
	
	private void relocateJars(Path patchReleaseDir) throws IOException{
		Path eeLibDir = null;
//		log(FileParser.NEWLINE);
		for(Path jarFile : Files.list(patchReleaseDir).collect(Collectors.toList())){
			String jarFileName = jarFile.getFileName().toString();
			if(JarTool.WEB_CONTENT_JAR.matcher(jarFileName).matches()){
				//
			}else if(JarTool.EE_LIB_JAR.matcher(jarFileName).matches()){
				if(null == eeLibDir){
//					eeLibDir = new File(patchReleaseDir, Msg.get(this, "path.EE_LIB"));
					eeLibDir = patchReleaseDir.resolve(Msg.get(this, "path.EE_LIB"));
					if(Files.exists(eeLibDir)){
//						File tmp = new File(patchReleaseDir, String.format(Msg.get(this, "path.EE_LIB.bak"), System.currentTimeMillis()));
//						eeLibDir.renameTo(tmp);
						Files.move(eeLibDir, patchReleaseDir.resolve(String.format(Msg.get(this, "path.EE_LIB.bak"), System.currentTimeMillis())));
//						eeLibDir = new File(patchReleaseDir, Msg.get(this, "path.EE_LIB"));
						eeLibDir =patchReleaseDir.resolve(Msg.get(this, "path.EE_LIB"));
					}
//					if(!eeLibDir.exists()){
//						eeLibDir.mkdir();
					if(Files.notExists(eeLibDir)){
						Files.createDirectory(eeLibDir);
						JarTool.validateDirectory(eeLibDir, String.format(Msg.get(this, "path.EE_LIB.error"), eeLibDir.toAbsolutePath()));
					}
				}
				log(String.format(Msg.get(this, "info.moveJar"), jarFileName, eeLibDir.toAbsolutePath()));
//				Files.move(jarFile.toPath(), new File(eeLibDir, jarFileName).toPath());
				Files.move(jarFile,eeLibDir.resolve(jarFileName));
			}else{
				log(String.format(Msg.get(this, "info.dontMove"), jarFileName, patchReleaseDir));
			}
		}
	}
	
	private void writeReadMe(Path patchReleaseDir, List<String> contentLines) throws IOException{
//		File readMe = new File(patchReleaseDir, Msg.get(this, "path.README"));
//		if(readMe.exists()){
//			File tmp = new File(patchReleaseDir, String.format(Msg.get(this, "path.README.bak"), System.currentTimeMillis()));
//			readMe.renameTo(tmp);
//			readMe = new File(patchReleaseDir, Msg.get(this, "path.README"));
//		}
//		if(!readMe.exists()){
//			readMe.createNewFile();
//		}
		Path readMe = JarTool.createNewFile(patchReleaseDir, 
				Msg.get(this, "path.README"), 
				String.format(Msg.get(this, "path.README.bak"), Msg.get(this, "path.README"), System.currentTimeMillis()));
//		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(readMe)));){
		try(PrintWriter writer = new PrintWriter((Files.newBufferedWriter(readMe)))){
			for(String line : contentLines){
				if(null == line){
					continue;
				}
				writer.println(line);
			}
		}
		
	}
	
	@FXML
	private void onClearLog(){
		logMsg.clear();
	}
	
//	private void appendLog(String msg){
////		logs.concat("\n" + msg);
////		logMsg.setText(new StringBuilder(logMsg.getText()).append(msg).toString());
////		logMsg.positionCaret(logMsg.getText().length());
//		logMsg.appendText(msg);
////		logMsg.setScrollTop(Double.MAX_VALUE);	//scroll to the bottom;
//	}
	
	@Override
	public void log(String msg) {
//		appendLog(msg);
		logMsg.appendText(msg);
	}
	

//	@Override
//	public void log(Exception e) {
//		MsgLogger.super.log(e);	//<- use this syntax to invoke the defualt method in interface
//	}





	//	private StringWriter jarToolLogs = new StringWriter();
	private JarTool jarTool = JarTool.newInstance(this);
//	private StringProperty logs = new SimpleStringProperty();
	private BooleanProperty fieldEmpty = new SimpleBooleanProperty();
}

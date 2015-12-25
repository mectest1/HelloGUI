package com.mec.application.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import com.mec.resources.ErrorLogger;
import com.mec.resources.FileParser;
import com.mec.resources.JarTool;
import com.mec.resources.Msg;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PatchReleaseController implements ErrorLogger{

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
			
			String workspaceDirStr = workSpaceDirectory.getText();
			File workspaceDir = new File(workspaceDirStr);
//			if(!(workspaceDir.exists() && workspaceDir.isDirectory())){
//				throw new IllegalArgumentException(String.format(Msg.getExpMsg(this, "invalid.workspace"), workspaceDirStr));
//			}
			JarTool.validateDirectory(workspaceDir, String.format(Msg.getExpMsg(this, "invalid.workspace"), workspaceDirStr));
			
			String patchReleaseDirStr = patchReleaseDirectory.getText();
			File patchReleaseDir = new File(patchReleaseDirStr);
			if(!(patchReleaseDir.exists())){
				appendLog(String.format(Msg.get(this, "info.patchReleaseDir.create"), patchReleaseDir));
			}
			
			String modifyListContent = modifyList.getText();
			List<String> modifyList = FileParser.normalizeModifyList(modifyListContent);
			Map<String, List<String>> modifyListMap = FileParser.parseModifyList(modifyList);
			
			for(String projectName : modifyListMap.keySet()){
				List<String> sourceFileList = modifyListMap.get(projectName);
				
				jarTool.writeJavaToJar(workspaceDir, projectName, sourceFileList, patchReleaseDir);
			}
			
			relocateJars(patchReleaseDir);
			writeReadMe(patchReleaseDir, modifyList);
//			appendLog(jarTool.getLog());
		}catch(Exception e){
//			appendLog(Msg.getExpMsg(e, e.getMessage()));
			appendLog(String.format(Msg.get(this, "exception.log"), e.getClass().getName(), JarTool.exceptionToStr(e)));
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
	
	private void relocateJars(File patchReleaseDir) throws IOException{
		File eeLibDir = new File(patchReleaseDir, Msg.get(this, "path.EE_LIB"));
		if(eeLibDir.exists()){
			File tmp = new File(patchReleaseDir, String.format(Msg.get(this, "path.EE_LIB.bak"), System.currentTimeMillis()));
			eeLibDir.renameTo(tmp);
			eeLibDir = new File(patchReleaseDir, Msg.get(this, "path.EE_LIB"));
		}
		if(!eeLibDir.exists()){
			eeLibDir.mkdir();
		}
		JarTool.validateDirectory(eeLibDir, String.format(Msg.get(this, "path.EE_LIB.error"), eeLibDir.getCanonicalPath()));
		
		for(File jarFile : patchReleaseDir.listFiles()){
			String jarFileName = jarFile.getName();
			if(JarTool.WEB_CONTENT_JAR.matcher(jarFileName).matches()){
				//
			}else if(JarTool.EE_LIB_JAR.matcher(jarFileName).matches()){
				log(String.format(Msg.get(this, "info.moveJar"), jarFileName, eeLibDir.getCanonicalPath()));
				Files.move(jarFile.toPath(), new File(eeLibDir, jarFileName).toPath());
			}else{
				log(String.format(Msg.get(this, "info.dontMove"), jarFileName, patchReleaseDir));
			}
		}
	}
	
	private void writeReadMe(File patchReleaseDir, List<String> contentLines) throws IOException{
//		File readMe = new File(patchReleaseDir, Msg.get(this, "path.README"));
//		if(readMe.exists()){
//			File tmp = new File(patchReleaseDir, String.format(Msg.get(this, "path.README.bak"), System.currentTimeMillis()));
//			readMe.renameTo(tmp);
//			readMe = new File(patchReleaseDir, Msg.get(this, "path.README"));
//		}
//		if(!readMe.exists()){
//			readMe.createNewFile();
//		}
		File readMe = JarTool.createNewFile(patchReleaseDir, 
				Msg.get(this, "path.README"), 
				String.format(Msg.get(this, "path.README.bak"), Msg.get(this, "path.README"), System.currentTimeMillis()));
		try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(readMe)));){
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
	
	private void appendLog(String msg){
//		logs.concat("\n" + msg);
		logMsg.setText(new StringBuilder(logMsg.getText()).append(msg).toString());
//		logMsg.positionCaret(logMsg.getText().length());
		logMsg.setScrollTop(Double.MAX_VALUE);	//scroll to the bottom;
	}
	
	@Override
	public void log(String msg) {
		appendLog(msg);
	}



	//	private StringWriter jarToolLogs = new StringWriter();
	private JarTool jarTool = JarTool.newInstance(this);
//	private StringProperty logs = new SimpleStringProperty();
	private BooleanProperty fieldEmpty = new SimpleBooleanProperty();
}

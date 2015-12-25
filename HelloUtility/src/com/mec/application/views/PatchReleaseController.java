package com.mec.application.views;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.mec.resources.FileParser;
import com.mec.resources.JarTool;
import com.mec.resources.Msg;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PatchReleaseController {

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
			Map<String, List<String>> modifyListMap = FileParser.parseModifyList(modifyListContent);
			
			for(String projectName : modifyListMap.keySet()){
				List<String> sourceFileList = modifyListMap.get(projectName);
				
				jarTool.writeJavaToJar(workspaceDir, projectName, sourceFileList, patchReleaseDir);
			}
			
			appendLog(jarTool.getLog());
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
	
	@FXML
	private void onClearLog(){
		logMsg.clear();
	}
	
	private void appendLog(String msg){
//		logs.concat("\n" + msg);
		logMsg.setText(new StringBuilder(logMsg.getText()).append(msg).toString());
	}
	
//	private StringWriter jarToolLogs = new StringWriter();
	private JarTool jarTool = JarTool.newInstance();
	private StringProperty logs = new SimpleStringProperty();
	private BooleanProperty fieldEmpty = new SimpleBooleanProperty();
}

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
		
//		workSpaceDirectory.setPromptText(Msg.get(this, "prompt.workspaceDir"));
//		modifyList.setPromptText(Msg.get(this, "prompt.modifyList"));
//		patchReleaseDirectory.setPromptText(Msg.get(this, "prompt.patchReleaseDir"));
	}
	
	
	@FXML
	private void onStartPatch(){
		try{
			
			String workspaceDirStr = JarTool.normalizePath(workSpaceDirectory.getText());
			Path workspaceDir = Paths.get(workspaceDirStr);
			JarTool.validateDirectory(workspaceDir, String.format(Msg.getExpMsg(this, "invalid.workspace"), workspaceDirStr));
			
			String patchReleaseDirStr = JarTool.normalizePath(patchReleaseDirectory.getText());
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
		}catch(Exception e){
			log(e);
		}
	}
	
	
	private void relocateJars(Path patchReleaseDir) throws IOException{
		Path eeLibDir = null;
		for(Path jarFile : Files.list(patchReleaseDir).collect(Collectors.toList())){
			String jarFileName = jarFile.getFileName().toString();
			if(JarTool.WEB_CONTENT_JAR.matcher(jarFileName).matches()){
				//
			}else if(JarTool.EE_LIB_JAR.matcher(jarFileName).matches()){
				if(null == eeLibDir){
					eeLibDir = patchReleaseDir.resolve(Msg.get(this, "path.EE_LIB"));
					if(Files.exists(eeLibDir)){
						Files.move(eeLibDir, patchReleaseDir.resolve(String.format(Msg.get(this, "path.EE_LIB.bak"), System.currentTimeMillis())));
						eeLibDir =patchReleaseDir.resolve(Msg.get(this, "path.EE_LIB"));
					}
					if(Files.notExists(eeLibDir)){
						Files.createDirectory(eeLibDir);
						JarTool.validateDirectory(eeLibDir, String.format(Msg.get(this, "path.EE_LIB.error"), eeLibDir.toAbsolutePath()));
					}
				}
				log(String.format(Msg.get(this, "info.moveJar"), jarFileName, eeLibDir.toAbsolutePath()));
				Files.move(jarFile,eeLibDir.resolve(jarFileName));
			}else{
				log(String.format(Msg.get(this, "info.dontMove"), jarFileName, patchReleaseDir));
			}
		}
	}
	
	private void writeReadMe(Path patchReleaseDir, List<String> contentLines) throws IOException{
		Path readMe = JarTool.createNewFile(patchReleaseDir, 
				Msg.get(this, "path.README"), 
				String.format(Msg.get(this, "path.README.bak"), Msg.get(this, "path.README"), System.currentTimeMillis()));
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
	
	@Override
	public void log(String msg) {
		logMsg.appendText(msg);
	}
	
	private JarTool jarTool = JarTool.newInstance(this);
	private BooleanProperty fieldEmpty = new SimpleBooleanProperty();
}

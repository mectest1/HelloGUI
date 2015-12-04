package com.mec.fx.views;

import java.io.File;
import java.util.Optional;

import com.mec.fx.PersonInfoViewer;
import com.mec.resources.Msg;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

public class RootLayoutController {

	
	@FXML
	private void handleNew(){
		personInfoViewer.getPersonData().clear();
		personInfoViewer.setPersonFilePath(null);
	}
	
	
	@FXML
	private void handleOpen(){
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(Msg.get(this, "extension.xml.description"), Msg.get(this, "extension.xml"));
		fileChooser.getExtensionFilters().add(extensionFilter);
		File selectedFile = fileChooser.showOpenDialog(personInfoViewer.getPrimaryStage());
		if(null != selectedFile){
			personInfoViewer.loadPersonDataFromFile(selectedFile);
		}
		
	}
	
	@FXML
	private void handleSave(){
		File personFile = personInfoViewer.getPersonFilePath();
		if(null != personFile){
			personInfoViewer.savePersonDataToFile(personFile);
		}else{
			handleSaveAs();
		}
	}
	
	@FXML
	private void handleSaveAs(){
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(Msg.get(this, "extension.xml.description"), Msg.get(this, "extension.xml"));
		fileChooser.getExtensionFilters().add(extensionFilter);
		File currentDir = personInfoViewer.getPersonFileDirectory();
		if(null != currentDir){
			fileChooser.setInitialDirectory(currentDir);
		}
		File saveFile = fileChooser.showSaveDialog(personInfoViewer.getPrimaryStage());
		if(null != saveFile){
			if(!saveFile.getPath().endsWith(Msg.get(this, "extension.xml.suffix"))){
				saveFile = new File(String.format(Msg.get(this, "extension.xml.fileNamePattern"), 
						saveFile.getPath(), Msg.get(this, "extension.xml.suffix")));
			}
			personInfoViewer.savePersonDataToFile(saveFile);
			personInfoViewer.setPersonFilePath(saveFile);
		}
	}
	

	
	@FXML
	private void handleShowBirthdayStatistics(){
		personInfoViewer.showBirthdayStatistics();
	}
	
	@FXML
	private void handleAbout(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(Msg.get(this, "about.title"));
		alert.setHeaderText(Msg.get(this, "about.header"));
		alert.setContentText(Msg.get(this, "about.content"));
		
		alert.showAndWait();
	}
	
	@FXML
	public void handleExit(){
//		System.exit(0);
		if(!personInfoViewer.isPersonDataSaved()){
			Alert alert = new Alert(AlertType.WARNING, Msg.get(this, "exit.unsaved.content"), ButtonType.OK, ButtonType.CANCEL, ButtonType.NO);
			alert.setTitle(Msg.get(this, "exit.unsaved.title"));
			alert.setHeaderText(Msg.get(this, "exit.unsaved.header"));
//			alert.setContentText();
			
			alert.initOwner(this.personInfoViewer.getPrimaryStage());
			alert.initModality(Modality.WINDOW_MODAL);
			Optional<ButtonType> result = alert.showAndWait();
			if(ButtonType.CANCEL == result.get()){
				return;
			}else if(ButtonType.OK == result.get()){
				handleSave();
			}//else if ButtonType.NO == result.get();
		}
		Platform.exit();
	}
	public void setPersonInfoViewer(PersonInfoViewer personInfoViewer) {
		this.personInfoViewer = personInfoViewer;
	}

	private PersonInfoViewer personInfoViewer;
}

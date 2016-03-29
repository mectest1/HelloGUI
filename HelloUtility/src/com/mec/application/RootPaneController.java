package com.mec.application;

import com.mec.resources.Msg;
import com.mec.resources.MsgLogger;
import com.mec.resources.ViewFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
public class RootPaneController implements MsgLogger{
	
	@FXML
	private TextArea logMsg;
	
	@FXML
	private void onExit(){
		Platform.exit();
	}
	
	@FXML
	private void onAbout(){
		Alert about = ViewFactory.newAlert(AlertType.INFORMATION, 
				Msg.get(this, "about.title"), 
				Msg.get(this, "about.header"), 
				Msg.get(this, "about.content")
		);
		
		about.getButtonTypes().clear();
		
		ButtonType okButton = new ButtonType(Msg.get(this, "about.ok"), ButtonData.OK_DONE);
		about.getButtonTypes().add(okButton);
		about.showAndWait();
	}
	
	@FXML
	private void initialize(){
		ViewFactory.setLogOutput(this);
	}
	

	//Note: static method cannot be FXML method?
	@FXML
	private void viewMenuItemClicked(ActionEvent event){
		ViewFactory.onClickedViewMenuItem(event);
	}

	
	@FXML
	private void onClearLog(){
		logMsg.clear();
	}
	
	@Override
	public void log(String msg) {
		logMsg.appendText(msg);
	}
}

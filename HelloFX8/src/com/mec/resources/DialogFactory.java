package com.mec.resources;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogFactory {

	private DialogFactory(){
		
	}
	
	public static Alert newAlert(AlertType alertType, String title, String header, String content){
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		return alert;
	}
}

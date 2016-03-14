package com.mec.fx.beans;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Supplier;

import com.mec.resources.Msg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LogInControl extends GridPane {

	@FXML
	private TextField userId;
	
	@FXML
	private PasswordField pwd;
	
	public LogInControl(){
		//load the FXML
		URL view = getClass().getResource(Msg.get(this, "view"));
		Objects.requireNonNull(view);
		
		FXMLLoader loader = new FXMLLoader(view);
		loader.setRoot(this);
		loader.setController(this);
		try{
			loader.load();
		}catch(IOException e){
			throw new RuntimeException(e);
		}
	}
	
	@FXML
	private void initialize(){
		//initialization work goes here
	}
	
	@FXML
	private void okClicked(){
		showAlert(() -> Msg.get(this, "ok.info"));
	}
	
	@FXML
	private void cancelClicked(){
		showAlert(() -> Msg.get(this, "cancel.info"));
	}
	
	private void showAlert(Supplier<String> msg){
		Alert alert = new Alert(AlertType.INFORMATION, msg.get());
		alert.showAndWait();
	}
	
	public String getUserId(){
		return userId.getText();
	}
	
	
	public String getPassword(){
		return pwd.getText();
	}
}

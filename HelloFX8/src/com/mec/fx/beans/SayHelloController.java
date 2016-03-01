package com.mec.fx.beans;

import java.net.URL;
import java.util.ResourceBundle;

import com.mec.resources.Msg;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class SayHelloController {
	
	@FXML
	private Label msgLbl;
	@FXML
	private Button sayHelloBtn;
	@FXML
	private TextArea info;
	
	@FXML
	private URL location;
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private void initialize(){
		info.appendText(location.toExternalForm());
//		info.appendText(resources.toString());
	}
	
	@FXML
	private void sayHello(){
		msgLbl.setText(String.join(Msg.get(this, "delimiter"), Msg.get(this, "hello") , Integer.toString(count++)));
	}
	
	int count = 0;

}

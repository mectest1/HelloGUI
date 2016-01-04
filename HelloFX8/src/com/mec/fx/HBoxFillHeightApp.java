package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HBoxFillHeightApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox root = new HBox(10);
		
		Label descLbl = new Label(Msg.get(this, "description"));
		TextArea desc = new TextArea();
		desc.setPrefColumnCount(10);
		desc.setPrefRowCount(3);
		
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		
		
	}

}

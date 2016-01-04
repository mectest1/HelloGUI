package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HBoxAlignmentApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
	}

}

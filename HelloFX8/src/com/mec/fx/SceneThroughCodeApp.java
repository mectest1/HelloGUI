package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneThroughCodeApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox();
		root.getChildren().addAll(new Label(Msg.get(this, "info"))
				, new Button(Msg.get(this, "button"))
				);
		
		
	}

	
}

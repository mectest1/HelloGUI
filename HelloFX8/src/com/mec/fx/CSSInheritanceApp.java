package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CSSInheritanceApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button okBtn = new Button(Msg.get(this, "ok"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		HBox root = new HBox(10);
		root.getChildren().addAll(okBtn, cancelBtn);
		
		//Set styles for the OK button and its parent HBox
		root.setStyle(Msg.get(this, "rootStyle"));
		okBtn.setStyle(Msg.get(this, "okStyle"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

	
	
}

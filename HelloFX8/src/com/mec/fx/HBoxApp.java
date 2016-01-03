package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HBoxApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label nameLabel = new Label(Msg.get(this, "name"));
		TextField nameFld = new TextField();
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//
		HBox root = new HBox(10);	//10px spacing
		root.getChildren().addAll(nameLabel, nameFld, okBtn, cancelBtn);
		root.setStyle(Msg.get(this, "style"));

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class HBoxHGrowApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label nameLabel = new Label(Msg.get(this, "name"));
		TextField nameField = new TextField();
		
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		HBox root = new HBox(10);
		root.getChildren().addAll(nameLabel, nameField, okBtn, cancelBtn);
		
		//Let the TextField always grow horizontally
		HBox.setHgrow(nameField, Priority.ALWAYS);
		
		root.setStyle(Msg.get(this, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	

}

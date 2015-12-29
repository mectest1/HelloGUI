package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StyleClassApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label nameLabel = new Label(Msg.get(this, "name"));
		TextField nameIf  = new TextField(Msg.get(this, "if"));
		Button closeBtn = new Button(Msg.get(this, "close"));
		
		
		HBox root = new HBox();
		root.getChildren().addAll(nameLabel, nameIf, closeBtn);
		root.getStyleClass().add(Msg.get(this, "styleClass"));
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(Msg.get(this, "styleSheet"));
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	
}

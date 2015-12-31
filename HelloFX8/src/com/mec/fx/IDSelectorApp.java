package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class IDSelectorApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button openBtn = new Button(Msg.get(this, "open"));
		Button saveBtn = new Button(Msg.get(this, "save"));
		
		Button closeBtn = new Button(Msg.get(this, "close"));
		closeBtn.setId(Msg.get(this, "id"));
		
		HBox root = new HBox();
		root.getChildren().addAll(openBtn, saveBtn, closeBtn);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(Msg.get(this, "styleSheet"));
		
		//
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();

	}

}

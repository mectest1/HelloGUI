package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		okBtn.relocate(10, 10);
		cancelBtn.relocate(60, 10);
		
		//
		Pane root = new Pane();
		root.getChildren().addAll(okBtn, cancelBtn);
		root.setStyle(Msg.getList(this, "style").stream().reduce((r, s) -> r+s).get()); 
		
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

}

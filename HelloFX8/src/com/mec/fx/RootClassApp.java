package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RootClassApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label nameLbl = new Label(Msg.get(this, "name"));
		TextField nameIf = new TextField(Msg.get(this, "nameIf"));
		Button closeBtn = new Button(Msg.get(this, "close"));
		
		HBox root = new HBox();
		root.getChildren().addAll(nameLbl, nameIf, closeBtn);
		
		Scene scene = new Scene(root);
		
		scene.getStylesheets().add(Msg.get(this, "styleSheet"));
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	

}

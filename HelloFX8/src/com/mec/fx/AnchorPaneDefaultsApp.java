package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AnchorPaneDefaultsApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button bigBtn = new Button(Msg.get(this, "big"));
		Button smallBtn = new Button(Msg.get(this, "small"));
		
		//Create an AnchorPane with two buttons
		AnchorPane root = new AnchorPane(bigBtn, smallBtn);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

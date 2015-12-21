package com.mec.fx;

import java.net.URL;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ButtonStyleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button yesBtn = new Button(Msg.get(this, "yes"));
		Button noButton = new Button(Msg.get(this, "no"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		HBox root = new HBox(5);
		root.getChildren().addAll(yesBtn, noButton, cancelBtn);
		
		
		//add a stylesheet to the scene
		
		Scene scene = new Scene(root);
//		URL url = getClass().getResource(Msg.get(this, "stylePath"));
//		scene.getStylesheets().add(url.toExternalForm());
		//Same effect:
		scene.getStylesheets().add(Msg.get(this, "stylePath"));
		
		Application.setUserAgentStylesheet(null);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

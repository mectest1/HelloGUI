package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StylesPrioritiesApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button yesBtn = new Button(Msg.get(this, "yes"));
		Button noBtn = new Button(Msg.get(this, "no"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//
		yesBtn.setStyle(Msg.get(this, "style"));
		yesBtn.setFont(new Font(10));
		
		noBtn.setFont(new Font(8));
		
		HBox root = new HBox();
		root.setSpacing(10);;
		root.getChildren().addAll(yesBtn, noBtn, cancelBtn);
		
		//
		Scene scene = new Scene(root);
		scene.getStylesheets().add(Msg.get(this, "stylesheet"));
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
}

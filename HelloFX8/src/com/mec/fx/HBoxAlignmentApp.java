package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HBoxAlignmentApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		HBox hbox = new HBox(10);
		hbox.setPrefSize(200, 100);
		hbox.getChildren().addAll(okBtn, cancelBtn);
		
		//Set the alignment to the bottom right
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		
		//
		hbox.setStyle(Msg.get(this, "style"));
		
		
		Scene scene = new Scene(hbox);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

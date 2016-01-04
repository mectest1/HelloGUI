package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VBoxAlignmentApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		VBox root = new VBox(10);
		root.setPrefSize(200, 100);
		root.getChildren().addAll(okBtn, cancelBtn);
		
		//Set the alignment to bottom right
		root.setAlignment(Pos.BOTTOM_RIGHT);
		
		root.setStyle(Msg.get(this, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

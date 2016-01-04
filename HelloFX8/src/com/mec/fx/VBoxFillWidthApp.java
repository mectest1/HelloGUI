package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VBoxFillWidthApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button b1 = new Button(Msg.get(this, "new"));
		Button b2 = new Button(Msg.get(this, "newModified"));
		Button b3 = new Button(Msg.get(this, "notModified"));
		Button b4 = new Button(Msg.get(this, "dataModified"));
		
		//Set the max width of the buttons to Double.MAX_VALUE,
		//so they can grow horizontally
		b1.setMaxWidth(Double.MAX_VALUE);
		b2.setMaxWidth(Double.MAX_VALUE);
		b3.setMaxWidth(Double.MAX_VALUE);
		b4.setMaxWidth(Double.MAX_VALUE);
		//
		VBox root = new VBox(10, b1, b2, b3, b4);
		root.setStyle(Msg.get(this, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

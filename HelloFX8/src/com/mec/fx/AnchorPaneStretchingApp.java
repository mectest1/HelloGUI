package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AnchorPaneStretchingApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button leftRight = new Button(Msg.get(this, "button"));
		AnchorPane.setTopAnchor(leftRight, 10d);
		AnchorPane.setLeftAnchor(leftRight, 10d);
		AnchorPane.setRightAnchor(leftRight, 10d);
		
		//
		AnchorPane root = new AnchorPane();
		root.getChildren().addAll(leftRight);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

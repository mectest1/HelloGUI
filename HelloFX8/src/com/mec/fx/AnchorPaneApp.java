package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AnchorPaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button topLeft = new Button(Msg.get(this, "topLeft"));
		AnchorPane.setTopAnchor(topLeft, 10d);
		AnchorPane.setLeftAnchor(topLeft, 10d);
		
		//
		Button bottomRight = new Button(Msg.get(this, "bottomRight"));
		AnchorPane.setBottomAnchor(bottomRight, 10d);
		AnchorPane.setRightAnchor(bottomRight, 10d);
		
		//
		AnchorPane root = new AnchorPane();
		root.getChildren().addAll(topLeft, bottomRight);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

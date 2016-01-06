package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class GridPaneChildrenListApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button b1 = new Button(Msg.get(this, "one"));
		Button b2 = new Button(Msg.get(this, "two"));
		Button b3 = new Button(Msg.get(this, "three"));
		
		GridPane root = new GridPane();
		//Add the three buttons to he list of children of the GridPane directly
		root.getChildren().addAll(b1, b2, b3);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		primaryStage.show();
	}

	
}

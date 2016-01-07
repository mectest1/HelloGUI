package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneChildrenPositionsApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button b1 = new Button(Msg.get(GridPaneChildrenListApp.class, "one"));
		Button b2 = new Button(Msg.get(GridPaneChildrenListApp.class, "two"));
		Button b3 = new Button(Msg.get(GridPaneChildrenListApp.class, "three"));
		
		GridPane root = new GridPane();
		//Add three buttons to the list of children of the GridPane directly
		root.getChildren().addAll(b1, b2, b3);
		root.setGridLinesVisible(true);
		//Set the cells of the buttons
		GridPane.setConstraints(b1, 0, 0);
		GridPane.setMargin(b1, new Insets(10));
		GridPane.setConstraints(b2, 1, 0);
		GridPane.setMargin(b2, new Insets(10));
		GridPane.setConstraints(b3, 2, 0);
		GridPane.setMargin(b3, new Insets(10));
		
		//
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
}

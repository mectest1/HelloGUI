package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class GridPaneHVgrowApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane root = new GridPane();
		root.setStyle(Msg.get(this, "style"));
		root.setGridLinesVisible(true);
		
		//Add three buttons to a column
		Button b1 = new Button(Msg.get(this, "one"));
		Button b2 = new Button(Msg.get(this, "two"));
		Button b3 = new Button(Msg.get(this, "three"));
		Button b4 = new Button(Msg.get(this, "four"));
		Button b5 = new Button(Msg.get(this, "five"));
		Button b6= new Button(Msg.get(this, "six"));
		
		root.addColumn(0, b1, b2, b3);
		root.addColumn(1, b4, b5, b6);
		
		//Set the column constraints
		ColumnConstraints cc1 = new ColumnConstraints();
		cc1.setHgrow(Priority.NEVER);
		root.getColumnConstraints().add(cc1);
		
		//Set three different hgrow priorities for children in the second
		//column. The highest priority, ALWAYS, will be used.
		GridPane.setHgrow(b4, Priority.ALWAYS);
		GridPane.setHgrow(b5, Priority.NEVER);
		GridPane.setHgrow(b6, Priority.SOMETIMES);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

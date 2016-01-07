package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneLineApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane root = new GridPane();
		root.setStyle(Msg.get(this, "style"));
		
		root.setGridLinesVisible(true);
		
		//Make the gridLines
		Button b1 = new Button(Msg.get(this, "one"));
		Button b2 = new Button(Msg.get(this, "two"));
		Button b3 = new Button(Msg.get(this, "three"));
		Button b4 = new Button(Msg.get(this, "four"));
		
		//Make all children invisible
		b1.setVisible(false);
		b2.setVisible(false);
		b3.setVisible(false);
		b4.setVisible(false);
		
		//Add children to teh GridPane
		root.addRow(0, b1, b2);
		root.addRow(2, b3, b4);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
				
		
		
		
	}

}

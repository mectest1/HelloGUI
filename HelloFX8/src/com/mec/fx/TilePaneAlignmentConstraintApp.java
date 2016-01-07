package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class TilePaneAlignmentConstraintApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button b12 = new Button(Msg.get(this, "oneTwo"));
		Button b3 = new Button(Msg.get(this, "three"));
		Button b4 = new Button(Msg.get(this, "four"));
		Button b5 = new Button(Msg.get(this, "five"));
		Button b6 = new Button(Msg.get(this, "six"));
		
		//Set the tile alignment constraint on b3 to BOTTMO_RIGHT
		TilePane.setAlignment(b3,  Pos.BOTTOM_RIGHT);
		
		TilePane root = new TilePane(b12, b3, b4, b5, b6);
		root.setPrefColumns(3);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

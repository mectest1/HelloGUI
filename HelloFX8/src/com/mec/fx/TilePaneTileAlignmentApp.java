package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class TilePaneTileAlignmentApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TilePane tileAlignCenter = createTilePane(Pos.CENTER);
		TilePane tileAlignTopright = createTilePane(Pos.TOP_LEFT);
			
		//
		HBox root = new HBox(tileAlignCenter, tileAlignTopright);
		root.setFillHeight(false);
	
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	public TilePane createTilePane(Pos tileAlignment){
		Button[] buttons = new Button[]{
			new Button(Msg.get(this, "b1")),
			new Button(Msg.get(this, "b2")),
			new Button(Msg.get(this, "b3")),
			new Button(Msg.get(this, "b4")),
			new Button(tileAlignment.toString())
		};
		
		TilePane tpane = new TilePane(5, 5, buttons);
		tpane.setTileAlignment(tileAlignment);
		tpane.setPrefColumns(3);
		tpane.setStyle(Msg.get(FlowPaneApp.class, "style"));
		return tpane;
	}
}

package com.mec.fx;

import java.time.Month;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class TilePaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		double hgap = 5.0;
		double vgap = 5.0;
		TilePane root = new TilePane(hgap, vgap);
		root.setPrefColumns(5);
		
		//Add 12 buttons -- each having he name of the 12 month
		for(Month month : Month.values()){
			Button b = new Button(month.toString());
			b.setMaxHeight(Double.MAX_VALUE);
			b.setMaxWidth(Double.MAX_VALUE);
			root.getChildren().add(b);
		}
		
		//
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

}

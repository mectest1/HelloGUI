package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StackPaneAlignmentApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane topLeft =  createStackPane(Pos.TOP_LEFT);
		StackPane topRight =  createStackPane(Pos.TOP_RIGHT);
		StackPane bottomLeft =  createStackPane(Pos.BOTTOM_LEFT);
		StackPane bottomRight =  createStackPane(Pos.BOTTOM_RIGHT);
		StackPane center = createStackPane(Pos.CENTER);
		
		double spacing = 10.0;
		HBox root = new HBox(spacing, topLeft, topRight, bottomLeft, bottomRight, center);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	private StackPane createStackPane(Pos alignment){
		Rectangle rect = new Rectangle(80, 50);
		rect.setFill(Color.LAVENDER);
		
		Text text = new Text(alignment.toString());
		text.setStyle(Msg.get(this, "style.text"));
		
		//
		StackPane spane = new StackPane(rect, text);
		spane.setAlignment(alignment);
		spane.setStyle(Msg.get(FlowPaneApp.class, "style"));
		return spane;
	}

}

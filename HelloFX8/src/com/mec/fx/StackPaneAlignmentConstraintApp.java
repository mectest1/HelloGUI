package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StackPaneAlignmentConstraintApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Rectangle rect = new Rectangle(200, 60);
		rect.setFill(Color.LAVENDER);
		
		//Create a text node with the default CENTER alignment
		Text center = new Text(Msg.get(this, "center"));
		
		//Create a Text node with TOP_LEFT alignment constraint
		Text topLeft = new Text(Msg.get(this, "topLeft"));
		StackPane.setAlignment(topLeft, Pos.TOP_LEFT);
		
		//Create a text node with a bottom_left alignment constraint
		Text bottomRight = new Text(Msg.get(this, "bottomRight"));
		StackPane.setAlignment(bottomRight, Pos.BOTTOM_RIGHT);
		
		//
		StackPane root = new StackPane(rect, center, topLeft, bottomRight);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		

	}

}

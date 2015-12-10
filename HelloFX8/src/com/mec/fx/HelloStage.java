package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HelloStage extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(Msg.get(this, "title"));
		
		primaryStage.setOpacity(Msg.get(this, "stage.opacity", Double::parseDouble, 1.0));
		
		Group root = new Group();
		Button btnHello = new Button(Msg.get(this, "btnHello.text"));
		root.getChildren().add(btnHello);
		
		Scene scene = new Scene(root, Msg.get(this, "scene.width", Integer::parseInt, 300),
				Msg.get(this, "scene.height", Integer::parseInt, 300)
				);
		primaryStage.setScene(scene);
//		primaryStage.sizeToScene();
		primaryStage.setWidth(Msg.get(this, "stage.width", Integer::parseInt, 300));
		primaryStage.setHeight(Msg.get(this, "stage.height", Integer::parseInt, 300));
		primaryStage.show();
		
		//Center the stage to window only after the stage has been shown
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		double x = bounds.getMinX() + (bounds.getWidth() - primaryStage.getWidth())/2; 
		double y = bounds.getMinY() + (bounds.getHeight() - primaryStage.getHeight())/2;
		
		//
		primaryStage.setX(x);
		primaryStage.setY(y);
	}

	
	
}

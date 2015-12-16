package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MaximizedStage extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button fullScreenButton = new Button(Msg.get(this, "fullScreenButton"));
		fullScreenButton.setOnAction(e -> primaryStage.setFullScreen(!primaryStage.isFullScreen()));
		primaryStage.setScene(new Scene(new Group(fullScreenButton)));
		primaryStage.setTitle(Msg.get(this, "title"));
		
		//Set the position and size of the stage equal to the position
		//and size of the screen
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(visualBounds.getMinX());
		primaryStage.setY(visualBounds.getMinY());
		primaryStage.setWidth(visualBounds.getWidth());
		primaryStage.setHeight(visualBounds.getHeight());
		
		//
		primaryStage.setFullScreen(true);
		primaryStage.show();
	}

}

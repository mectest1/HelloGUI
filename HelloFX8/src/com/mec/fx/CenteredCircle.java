package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CenteredCircle extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle c = new Circle();
		Group root = new Group(c);
		Scene scene = new Scene(root, 300, 300);
		
		//Bind the centerX, centerY, and radius to the scene width and height
		c.centerXProperty().bind(scene.widthProperty().divide(2));
		c.centerYProperty().bind(scene.heightProperty().divide(2));
		c.radiusProperty().bind(Bindings.min(scene.widthProperty(), scene.heightProperty()).divide(2));
		
		//
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

}

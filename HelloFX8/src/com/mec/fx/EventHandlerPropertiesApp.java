package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class EventHandlerPropertiesApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(100, 100, 50);
		circle.setFill(Color.valueOf(Msg.get(this, "color")));
		
		//
		HBox root = new HBox();
		root.getChildren().add(circle);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		primaryStage.sizeToScene();
		
		//
		EventHandler<MouseEvent> eventFilter = e -> out.println(Msg.get(this, "filterMsg"));
		EventHandler<MouseEvent> eventHandler = e -> out.println(Msg.get(this, "handlerMsg"));
		
		circle.addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter);
		
		//Register the handler using the setter method fo the 
		//onMouseClicked convenience event property
		circle.setOnMouseClicked(eventHandler);

	}

	private static final PrintStream out = System.out;
}

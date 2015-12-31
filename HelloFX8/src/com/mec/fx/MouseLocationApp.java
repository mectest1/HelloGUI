package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MouseLocationApp extends Application {

	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(50, 50, 50);
		circle.setFill(Msg.get(this, "circleColor", Color::valueOf, Color.WHITE));
		
		Rectangle rect = new Rectangle(100, 100);
		rect.setFill(Msg.get(this, "rectColor", Color::valueOf, Color.WHITE));
		
		HBox root = new HBox();
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		root.getChildren().addAll(circle, rect);
		
		
		//Add a MOUSE_CLICKED event handler to the stage
		primaryStage.addEventHandler(MouseEvent.MOUSE_CLICKED, ConsumingEventsApp::print);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	private static final PrintStream out = System.out;
}

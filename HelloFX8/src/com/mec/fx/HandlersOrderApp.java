package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class HandlersOrderApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(50, 50, 50);
		circle.setFill(Color.valueOf(Msg.get(this, "color")));
		
		HBox root = new HBox();
		root.getChildren().addAll(circle);
		Scene scene = new Scene(root);
		
		//Register three handlers for the circle that can handle mouse-clicked events
		//This will be called last
		circle.addEventHandler(MouseEvent.ANY, e -> handleAnyMouseEvent(e));
		
		
		//This will be called first
		circle.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> handleMouseClicked(Msg.get(this, "add"), e));
		
		//This will be called second
		circle.setOnMouseClicked(e -> handleMouseClicked(Msg.get(this, "onClicked"), e));
		
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	private void handleMouseClicked(String registrationMethod, MouseEvent e){
		out.printf(Msg.get(this, "info"), registrationMethod);
	}
	private void handleAnyMouseEvent(MouseEvent e){
		//Print a mesage only for mouse-clicked events, ignoring 
		//other mouse events such as mouse-pressed, mouse-released, etc.
		if(MouseEvent.MOUSE_CLICKED == e.getEventType()){
			out.println(Msg.get(this, "anyInfo"));
		}
	}
	
	
	private static final PrintStream out = System.out;
}

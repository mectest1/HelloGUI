package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CaptureBubblingOrderApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(50, 50, 50);
		circle.setFill(Color.valueOf(Msg.get(this, "circleColor")));
		
		Rectangle rect = new Rectangle(100, 100);
		rect.setFill(Color.valueOf(Msg.get(this, "rectColor")));
		
		HBox root = new HBox();
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		root.getChildren().addAll(circle, rect);
		
		//
		Scene scene = new Scene(root);
		
		//
		EventHandler<MouseEvent> filter = e -> handleEvent(Msg.get(this, "phaseCapture"), e);
		EventHandler<MouseEvent> handler = e -> handleEvent(Msg.get(this, "phaseBubbling"), e);
		
		//Register filters
		primaryStage.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
		root.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
		circle.addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
		
		primaryStage.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
		scene.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
		root.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
		circle.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	private void handleEvent(String phase, MouseEvent e){
		String  type = e.getEventType().getName();
		String source = e.getSource().getClass().getSimpleName();
		String target = e.getTarget().getClass().getSimpleName();
		
		//Get coordinates of the mouse cursor relative to the event source
		double x = e.getX();
		double y = e.getY();
		
		//
		out.printf(Msg.get(this, "info"), phase, type, target, source, x, y);
	}
	
	
	private static final PrintStream out = System.out;
}

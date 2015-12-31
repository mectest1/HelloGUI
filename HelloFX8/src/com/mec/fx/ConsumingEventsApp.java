package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ConsumingEventsApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(50, 50, 50);
		circle.setFill(Msg.get(this, "circleColor", Color::valueOf, Color.WHITE));
		
		Rectangle rect = new Rectangle(100, 100);
		rect.setFill(Msg.get(this, "rectColor", Color::valueOf, Color.WHITE));
		
		HBox root = new HBox();
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		root.getChildren().addAll(circle, rect, consumeEventCbx);
		
		//
		Scene scene = new Scene(root);
		
		//Register mouse-clicked event handlers to all nodes
		//except the rectangle and checkbox
		EventHandler<MouseEvent> handler = e -> handleEvent(e);
		EventHandler<MouseEvent> circleMeHandler = e -> handleEventForCircle(e);
		
		primaryStage.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
		scene.addEventFilter(MouseEvent.MOUSE_CLICKED, handler);
		root.addEventHandler(MouseEvent.MOUSE_CLICKED, handler);
		circle.addEventHandler(MouseEvent.MOUSE_CLICKED, circleMeHandler);
		
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	public void handleEvent(MouseEvent e){
		print(e);
	}
	
	private void handleEventForCircle(MouseEvent e){
		print(e);
		if(consumeEventCbx.isSelected()){
			e.consume();
		}
	}
	
	public static void print(MouseEvent e){
		String type = e.getEventType().getName();
		String source = e.getSource().getClass().getSimpleName();
		String target = e.getTarget().getClass().getSimpleName();
		
		
		//Get coordinates of the mouse curosr relative to the event source
		double x = e.getX();
		double y = e.getY();
		
		//
		out.printf(Msg.get(ConsumingEventsApp.class, "info"), type, target, source, x, y);
	}
	private CheckBox consumeEventCbx = new CheckBox(Msg.get(ConsumingEventsApp.class, "consumeCheckBox"));
	private static final PrintStream out = System.out;
}

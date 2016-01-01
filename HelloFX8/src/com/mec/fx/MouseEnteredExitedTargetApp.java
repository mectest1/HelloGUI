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
import javafx.stage.Stage;

public class MouseEnteredExitedTargetApp extends Application {
	private CheckBox consumeBox = new CheckBox(Msg.get(MouseEnteredExitedTargetApp.class, "checkBox"));
	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(50, 50, 50);
		circle.setFill(Msg.get(this, "color", Color::valueOf, Color.WHITE));
		
		HBox root = new HBox(circle, consumeBox);
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		
		
		//Create Mouse event handlers
		EventHandler<MouseEvent> circleHandler = e -> handleCircle(e);
		EventHandler<MouseEvent> circleTargetHandler = e -> handleCircleTarget(e);
		EventHandler<MouseEvent> hboxTargetHandler = e -> handleHBoxTarget(e);
		
		//Add mouse-entered-target and mouse-exited-target event
		//handlers to HBox
		root.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, hboxTargetHandler);
		root.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, hboxTargetHandler);
		
		//Add mouse-entered-target and ouse-exited-target handlers to the circle
		circle.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, circleTargetHandler);
		circle.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, circleTargetHandler);
		
		//Add mouse-entered and mouse-exited event handlers to the circle
		circle.addEventHandler(MouseEvent.MOUSE_ENTERED, circleHandler);
		circle.addEventHandler(MouseEvent.MOUSE_EXITED, circleHandler);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		
	}
	
	private void handleCircle(MouseEvent e){
		print(e, Msg.get(this, "circleHandler"));
	}

	private void handleCircleTarget(MouseEvent e){
		print(e, Msg.get(this, "circleTarget"));
	}
	
	private void handleHBoxTarget(MouseEvent e){
		print(e, Msg.get(this, "hboxTarget"));
		if(consumeBox.isSelected()){
			e.consume();
			out.printf(Msg.get(this, "info.consumed"), e.getEventType());
		}
	}
	
	private void print(MouseEvent e, String msg){
		String type = e.getEventType().getName();
		String source = e.getSource().getClass().getSimpleName();
		String target = e.getTarget().getClass().getSimpleName();
		
		out.printf(Msg.get(this, "info.print"), msg, type, target, source);
	}
	
	private static final PrintStream out = System.out;
}

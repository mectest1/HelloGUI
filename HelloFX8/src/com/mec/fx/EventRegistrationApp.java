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

public class EventRegistrationApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(100, 100, 50);
		circle.setFill(Color.CORAL);
		
		EventHandler<MouseEvent> mouseEventFilter = e -> out.println(Msg.get(this, "filterInfo"));
		EventHandler<MouseEvent> mouseEventHandler = e -> out.println(Msg.get(this, "handlerInfo"));
		
		circle.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventFilter);
		circle.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandler);
		
		HBox root = new HBox();
		root.getChildren().add(circle);
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();

	}

	private static final PrintStream out = System.out;
}

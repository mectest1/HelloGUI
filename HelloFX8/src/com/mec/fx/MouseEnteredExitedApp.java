package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MouseEnteredExitedApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(50, 50, 50);
		circle.setFill(Msg.get(this, "color", Color::valueOf, Color.ANTIQUEWHITE));
		
		HBox root = new HBox();
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		root.getChildren().add(circle);
		
		//Add mouse-entered and mouse-existed event handlers to the HBox
		root.addEventHandler(MouseEvent.MOUSE_ENTERED, ConsumingEventsApp::print);
		root.addEventHandler(MouseEvent.MOUSE_EXITED, ConsumingEventsApp::print);
		
		//Add mouse-entered and mouse-existed event handlers to the Circle
		circle.addEventHandler(MouseEvent.MOUSE_ENTERED, ConsumingEventsApp::print);
		circle.addEventHandler(MouseEvent.MOUSE_EXITED, ConsumingEventsApp::print);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		
	}

}

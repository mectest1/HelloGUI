package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
//import com.mec.fx.PickOnBoundsApp;

public class MouseTransparencyApp extends Application {

	private CheckBox pickOnBoundsCbx = new CheckBox(Msg.get(MouseTransparencyApp.class, "checkBox"));
	@Override
	public void start(Stage primaryStage) throws Exception {
		Circle circle = new Circle(50, 50, 50, Msg.get(this, "circleColor", Color::valueOf, Color.WHITE));
//		circle.pickOnBoundsProperty().bind(pickOnBoundsCbx.selectedProperty());
		circle.mouseTransparentProperty().bind(pickOnBoundsCbx.selectedProperty());
		
		Rectangle rect = new Rectangle(100, 100);
		rect.setFill(Msg.get(this, "rectColor", Color::valueOf, Color.WHITE));
		
		Group group = new Group();
//		group.getChildren().addAll(circle, rect);	//the circle will be overriden by the rectangle
		group.getChildren().addAll(rect, circle);
		
		HBox root = new HBox();
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		root.getChildren().addAll(group, pickOnBoundsCbx);
		
		circle.setOnMouseClicked(MouseTransparencyApp::handleMouseClickec);
		rect.setOnMouseClicked(MouseTransparencyApp::handleMouseClickec);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		

	}

	private static void handleMouseClickec(MouseEvent e){
		String target = e.getTarget().getClass().getSimpleName();
		String type = e.getEventType().getName();
//		out.printf(Msg.get(PickOnBoundsApp.class, "info"), type, target);
	}
	
	private static final PrintStream out = System.out;
}

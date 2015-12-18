package com.mec.fx;


import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ResizableNodeApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button btn = new Button(Msg.get(this, "button"));
		Rectangle rect = new Rectangle(100, 50);
		rect.setFill(Color.WHITE);
		rect.setStrokeWidth(Msg.get(this, "strokeWidth", Integer::parseInt, 1));
		rect.setStroke(Color.BLACK);
		
		HBox root = new HBox();
		root.setSpacing(20);;
		root.getChildren().addAll(btn, rect);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		out.printf(Msg.get(this, "output"), btn.isResizable(), rect.isResizable());
	}

	
	private static final PrintStream out = System.out;
}

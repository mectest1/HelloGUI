package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StackPaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Rectangle rect = new Rectangle(200, 50);
		rect.setStyle(Msg.get(this, "style.rect"));
		
		Text text = new Text(Msg.get(this, "text"));
		
		
		//Create a StackPane with a Rectangle and a Text
		StackPane root = new StackPane(rect, text);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

package com.mec.fx;

import java.util.Locale;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloFX8 extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Group root = new Group();
		Scene scene = new Scene(root, 200, 150);
//		scene.setFill(Color.LIGHTGREY);
		scene.setFill(Color.LIGHTYELLOW);
		
		Circle c = new Circle(60, 40, 30, Color.LIGHTGREEN);
		
//		Text text = new Text(10, 90, "JavaFX Scene");
//		Text text = new Text(10, 90, Msg.get(this, "text"));
		Text text = new Text(10, 90, Msg.get("text"));
		text.setFill(Color.LIGHTCORAL);
		
		Font font = new Font(20);
		text.setFont(font);
		
		root.getChildren().add(c);
		root.getChildren().add(text);
		
//		primaryStage.setTitle("Hello FX8");
//		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.setTitle(Msg.get("title"));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	
	public static void main(String[] args) {
//		Locale.setDefault(Locale.CHINESE);
//		Locale.setDefault(Locale.CHINA);
//		Locale.setDefault(new Locale("zh", "CN"));
//		Locale.setDefault(new Locale("en", "US"));
//		Msg.clearCash();
		launch(args);
	}
	

	

	
}

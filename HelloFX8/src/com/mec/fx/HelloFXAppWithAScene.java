package com.mec.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloFXAppWithAScene extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Text msg = new Text("Hello, JavaFx");
//		msg.setFont(new Font(20));
		VBox root = new VBox();
		root.getChildren().add(msg);
		
		Scene scene = new Scene(root, 300, 50);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Hello JavaFX Application with a Scene");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

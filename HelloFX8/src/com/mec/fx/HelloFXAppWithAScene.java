package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloFXAppWithAScene extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Text msg = new Text(Msg.get(this, "msg"));
//		msg.setFont(new Font(20));
		VBox root = new VBox();
		root.getChildren().add(msg);
		
		Scene scene = new Scene(root, 300, 50);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SayHelloWithFXMLApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = FXMLLoader.load(getClass().getResource(Msg.get(this, "fxml")));
	
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

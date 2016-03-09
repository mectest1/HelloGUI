package com.mec.fx;

import java.net.URL;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReusableObjectsInFXMLApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		final URL view = getClass().getResource(Msg.get(this, "view"));
		
		FXMLLoader loader = new FXMLLoader(view);
		Scene scene = loader.load();
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();

	}

}

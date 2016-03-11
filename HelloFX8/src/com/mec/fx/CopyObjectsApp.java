package com.mec.fx;

import java.net.URL;
import java.util.Objects;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CopyObjectsApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		URL view = getClass().getResource(Msg.get(this, "view"));
		Objects.requireNonNull(view);
		
		FXMLLoader loader = new FXMLLoader(view);
		VBox root = loader.load();
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();

	}

}

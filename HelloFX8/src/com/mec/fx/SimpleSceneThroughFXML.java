package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleSceneThroughFXML extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		final String url = "/META-INF/resources/fxml/SimpleSceneThroughFXML.fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
		VBox root = loader.load();
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

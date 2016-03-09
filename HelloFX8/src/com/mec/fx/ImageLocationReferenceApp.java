package com.mec.fx;

import java.net.URL;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImageLocationReferenceApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		URL view = getClass().getResource(Msg.get(this, "view"));
		
		FXMLLoader loader = new FXMLLoader(view);
		ImageView imageView = loader.load();
		
		Scene scene = new Scene(new VBox(imageView));
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

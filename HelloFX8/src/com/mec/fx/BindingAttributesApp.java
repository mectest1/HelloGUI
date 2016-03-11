package com.mec.fx;

import java.net.URL;
import java.util.Objects;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BindingAttributesApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		URL view = getClass().getResource(Msg.get(this, "view"));
		Objects.requireNonNull(view);
		
		FXMLLoader loader = new FXMLLoader(view);
		VBox bind = loader.load();
		
//		URL BiView = getClass().getResource(Msg.get(this, "BiView"));	//<-Bi-Directional Binding is not supported currently; 
////		loader.setLocation(BiView);
//		VBox biBind = FXMLLoader.load(BiView);
		
//		VBox root = new VBox(5, bind, biBind);
		VBox root = new VBox(5, bind);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();

	}

}

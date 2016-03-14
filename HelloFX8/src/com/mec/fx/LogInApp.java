package com.mec.fx;

import com.mec.fx.beans.LogInControl;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LogInApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create the Login Custom Control
		GridPane root = new LogInControl();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

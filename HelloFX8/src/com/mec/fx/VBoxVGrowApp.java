package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VBoxVGrowApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label descLabel = new Label(Msg.get(this, "desc"));
		TextArea desc = new TextArea();
		desc.setPrefColumnCount(10);
		desc.setPrefRowCount(3);
		
		VBox root = new VBox(10);
		root.getChildren().addAll(descLabel, desc);
		
		
		//Let the TextArea always grow vertically
		VBox.setVgrow(desc, Priority.ALWAYS);
		
		root.setStyle(Msg.get(this, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NodesLayoutInGroupApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Button okBtn = new Button(Msg.get(this, "ok"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//Set the location of the OK button
		okBtn.setLayoutX(10);
		cancelBtn.setLayoutY(10);
		
		//Set the location of the Cancel button relative to the OK button
		NumberBinding layoutXBinding = okBtn.layoutXProperty().add(okBtn.widthProperty().add(10));
		cancelBtn.layoutXProperty().bind(layoutXBinding);
		
		//
		cancelBtn.layoutYProperty().bind(okBtn.layoutYProperty());
		
		//
		Group root = new Group();
		root.getChildren().addAll(okBtn, cancelBtn);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

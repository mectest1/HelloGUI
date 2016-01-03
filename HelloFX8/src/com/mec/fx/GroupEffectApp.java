package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

public class GroupEffectApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button okBtn = new Button(Msg.get(this, "ok"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//Set the locations of the buttons
		okBtn.setLayoutX(10);
		okBtn.setLayoutX(10);
		cancelBtn.setLayoutX(80);
		cancelBtn.setLayoutY(10);
		
		
		//
		Group root = new Group();
		root.setEffect(new DropShadow());	//Set a drop shadow effect
		root.setRotate(10);					//Rotate by 10 degrees clockwise
		root.setOpacity(0.8);				//Set the opacity to 80%
		
		root.getChildren().addAll(okBtn, cancelBtn);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

}

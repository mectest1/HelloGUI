package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HBoxFillHeightApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox root = new HBox(10);
		
		Label descLbl = new Label(Msg.get(this, "description"));
		TextArea desc = new TextArea();
		desc.setPrefColumnCount(10);
		desc.setPrefRowCount(3);
		
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//Let the cancel button expand vertically
		cancelBtn.setMaxHeight(Double.MAX_VALUE);
		
		CheckBox fillHeightCbx = new CheckBox(Msg.get(this, "fillHeight"));
		fillHeightCbx.setSelected(true);
		
		//Add an event handler to the CheckBox, so the user can set the 
		//fillHeight property using the CheckBox
		root.fillHeightProperty().bind(fillHeightCbx.selectedProperty());
		
		//
		root.getChildren().addAll(descLbl, desc, fillHeightCbx, okBtn, cancelBtn);
		
		//
		root.setStyle(Msg.get(this, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		
	}

}

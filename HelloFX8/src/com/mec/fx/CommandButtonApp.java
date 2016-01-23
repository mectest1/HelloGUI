package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CommandButtonApp extends Application{

	private Label msgLabel = new Label(Msg.get(this, "msg.initial"));
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		//A normal button with N as its mnemonic
		Button newBtn = new Button(Msg.get(this, "button.new"));
		newBtn.setOnAction(e -> newDocument());
		
		//A default button with S as its mnemonic
		Button saveBtn = new Button(Msg.get(this, "button.save"));
		saveBtn.setDefaultButton(true);
//		saveBtn.setCancelButton(true);
		saveBtn.setOnAction(e -> save());
		
		//A cancel button with C as its mnemonic
		Button cancelBtn = new Button(Msg.get(this, "button.cancel"));
		cancelBtn.setCancelButton(true);
		cancelBtn.setOnAction(e -> cancel());
		
		//
		HBox buttonBox = new HBox(15, newBtn, saveBtn, cancelBtn);
//		buttonBox.setSpacing(15);
		VBox root = new VBox(15, msgLabel, buttonBox);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	private void newDocument(){
		msgLabel.setText(Msg.get(this, "msg.new"));
	}
	
	
	private void save(){
		msgLabel.setText(Msg.get(this, "msg.save"));
	}
	
	private void cancel(){
		msgLabel.setText(Msg.get(this, "msg.cancel"));
	}
}

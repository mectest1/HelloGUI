package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class KeyPressedReleaseApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label nameLabel = new Label(Msg.get(this, "name"));
		TextField nameField = new TextField();
		
		//
		HBox root = new HBox();
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		root.getChildren().addAll(nameLabel, nameField);
		
		//Add key pressed and released events to the TextField
		nameField.setOnKeyPressed(KeyPressedReleaseApp::handle);
		nameField.setOnKeyReleased(KeyPressedReleaseApp::handle);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();

	}

	
	private static void handle(KeyEvent e){
		String type = e.getEventType().getName();
		KeyCode keyCode = e.getCode();
		
		out.printf(Msg.get(KeyPressedReleaseApp.class, "info"), type, keyCode.getName(), e.getText());
		
		//Show the help window when theF1 key is pressed
		if(KeyEvent.KEY_PRESSED == e.getEventType() && KeyCode.F1 == e.getCode()){
			displayHelp();
			e.consume();
		}
	
	}
	
	private static  void displayHelp(){
		Text helpText = new Text(Msg.get(KeyPressedReleaseApp.class, "help"));
		HBox root = new HBox();
		root.setStyle(Msg.get(KeyPressedReleaseApp.class, "help.style"));
		root.getChildren().add(helpText);
		
		Scene scene = new Scene(root, 200, 100);
		Stage helpStage = new Stage();
		helpStage.setScene(scene);
		helpStage.setTitle(Msg.get(KeyPressedReleaseApp.class, "help.title"));
		helpStage.show();
	}
	
	private static final PrintStream out = System.out;
}

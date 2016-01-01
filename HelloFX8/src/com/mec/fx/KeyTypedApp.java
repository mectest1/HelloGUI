package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class KeyTypedApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label nameLabel = new Label(Msg.get(this, "name"));
		TextField nameField = new TextField();
		
		HBox root = new HBox();
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		root.getChildren().addAll(nameLabel, nameField);
		
		//Add key-typed event to the TextField
		nameField.setOnKeyTyped(e -> handle(e));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get("title"));
		primaryStage.show();
		
	}
	
	
	private void handle(KeyEvent e){
		//Consume the event if it is not a letter
		String str = e.getCharacter();
		int len = str.length();
		for(int i = 0; i < len; ++i){
			Character c = str.charAt(i);
			if(!Character.isLetter(c)){
				e.consume();
			}
		}
		
		//Print the details if it is not consumed
		if(!e.isConsumed()){
			out.printf(Msg.get(this, "info"), 
					e.getEventType().getName(), e.getCharacter()
					);
		}
		
	}

	private static final PrintStream out = System.out;
}

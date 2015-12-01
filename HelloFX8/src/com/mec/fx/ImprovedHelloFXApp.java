package com.mec.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImprovedHelloFXApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label nameLbl = new Label("Name: ");
		TextField nameFld = new TextField();
		nameFld.setPromptText("Enter you name here");
		
		Label msg = new Label();
		msg.setStyle("-fx-text-fill: blue;");
		
		//Create buttons
		Button sayHelloBtn = new Button("Say Hello");
		Button exitBtn = new Button("Exit");
		
		//Add the event handler for the Say Hello button
		sayHelloBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String name = nameFld.getText();
				if(name.trim().isEmpty()){
					name = "there";
				}
				msg.setText(String.format("Hello %s", name));
			}
		});
		
		//Add the event handler for the Exit button
		exitBtn.setOnAction(e -> Platform.exit());
		
		//Create the root node
		VBox root = new VBox();
		HBox buttons = new HBox();
		//Set the vertical spacing between children to 5px
		root.setSpacing(5);
		buttons.setSpacing(5);
		
		//Add children to the root node.
		root.getChildren().addAll(msg, nameLbl, nameFld, buttons);
		buttons.getChildren().addAll(sayHelloBtn, exitBtn);
		
		Scene scene = new Scene(root, 350, 150);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Improved Hello JavaFX Application");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

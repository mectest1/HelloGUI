package com.mec.fx;

import com.mec.resources.Msg;

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
		Label nameLbl = new Label(Msg.get(this, "nameLbl"));
		TextField nameFld = new TextField();
		nameFld.setPromptText(Msg.get(this, "nameFld"));
		
		Label msg = new Label();
		msg.setStyle("-fx-text-fill: blue;");
		
		//Create buttons
		Button sayHelloBtn = new Button(Msg.get(this, "sayHelloBtn"));
		Button exitBtn = new Button(Msg.get(this, "exitBtn"));
		
		//Add the event handler for the Say Hello button
		sayHelloBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				String name = nameFld.getText();
				if(name.trim().isEmpty()){
					name = "there";
				}
				msg.setText(String.format(Msg.get(ImprovedHelloFXApp.this, "msg"), name));
				nameFld.clear();
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
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

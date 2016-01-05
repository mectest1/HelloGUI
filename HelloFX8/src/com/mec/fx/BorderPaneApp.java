package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BorderPaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Node top = null;
		Node left = null;
		
		//Build the content nodes for the center region
		VBox center = getCenter();
		
		//Create the right child node
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//Make the OK and cancel buttons the same size;
		okBtn.setMaxWidth(Double.MAX_VALUE);
		VBox right = new VBox(okBtn, cancelBtn);
		right.setStyle(Msg.get(this, "style.right"));
		
		//Create teh bottom child node
		Label statusLabel = new Label(Msg.get(this, "status"));
		HBox bottom = new HBox(statusLabel);
		BorderPane.setMargin(bottom, new Insets(10, 0, 0, 0));
		bottom.setStyle(Msg.get(this, "style.bottom"));
		
		BorderPane root = new BorderPane(center, top, right, bottom, left);
		root.setStyle(Msg.get(this, "style.root"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	private VBox getCenter(){
		//A label adn a text field in an HBox
		Label nameLbl = new Label(Msg.get(this, "name"));
		TextField nameField = new TextField();
		HBox.setHgrow(nameField, Priority.ALWAYS);
		HBox nameFields = new HBox(nameLbl, nameField);
		
		//A label and a TextArea
		Label descLabel = new Label(Msg.get(this, "desc"));
		TextArea descText = new TextArea();
		descText.setPrefColumnCount(20);
		descText.setPrefColumnCount(20);
		descText.setPrefRowCount(5);
		VBox.setVgrow(descText, Priority.ALWAYS);
		
		VBox center = new VBox(nameFields, descLabel, descText);
		
		return center;
	}
}

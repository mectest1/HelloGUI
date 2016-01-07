package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class GridPaneFormApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		//A lable and a TextField
		Label nameLbl = new Label(Msg.get(this, "name"));
		TextField nameField = new TextField();
		
		//
		//A Label and a TextArea
		Label descLabel = new Label(Msg.get(this, "desc"));
		TextArea descText = new TextArea();
		descText.setPrefColumnCount(20);
		descText.setPrefRowCount(5);
		
		//Two buttons
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//A label used as a status bar
		Label statusBar = new Label(Msg.get(this, "status"));
		statusBar.setStyle(Msg.get(this, "style.status"));
		
		//Create a GridPane and set its background color to lightgray
		GridPane root = new GridPane();
		root.setStyle(Msg.get(this, "style.root"));
		
		//Add children to teh GridPane
//		root.add(nameLbl, 0, 0, 1, 1);
//		root.add(nameField, 1, 0, 1, 1);
		root.addRow(0, nameLbl, nameField, okBtn);
		root.add(descLabel, 0, 1, 3, 1);
		root.add(cancelBtn, 2, 1, 1, 1);
		root.add(descText, 0, 2, 2, 1);
		root.add(statusBar, 0, 3, GridPane.REMAINING, 1);
		
		//Set constraints for children to customize their resizing behaviour
		
		//The max width of the OK button should be big enough,
		//so it can fill the width of its cell
		okBtn.setMaxWidth(Double.MAX_VALUE);
		
		//The name field in the first row should grow horizontally
		GridPane.setHgrow(nameField, Priority.ALWAYS);
		
		//The description field in the thrid row should grow vertically
		GridPane.setVgrow(descText, Priority.ALWAYS);
		
		//The staus bar in the last shold fill its cell
		statusBar.setMaxWidth(Double.MAX_VALUE);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

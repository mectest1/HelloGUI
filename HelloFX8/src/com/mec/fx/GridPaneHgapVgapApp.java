package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneHgapVgapApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label fnameLbl = new Label(Msg.get(this, "firstName"));
		TextField fnameFld = new TextField();
		Label lnameLbl = new Label(Msg.get(this, "lastName"));
		TextField lnameFld = new TextField();
		Button okBtn = new Button(Msg.get(this, "OK"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//The OK button should fill its cell
		okBtn.setMaxWidth(Double.MAX_VALUE);
		
		//Create a GridPane and set its background color to lightgray
		GridPane root = new GridPane();
		root.setGridLinesVisible(true);
		root.setHgap(10);
		root.setVgap(5);
		root.setStyle(Msg.get(this, "style"));
		
		//Add children to teh GridPane
		root.addRow(0, fnameLbl, fnameFld, okBtn);
		root.addRow(1, lnameLbl, lnameFld, cancelBtn);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

}

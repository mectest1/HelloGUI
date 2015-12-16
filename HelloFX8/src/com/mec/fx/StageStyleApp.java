package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageStyleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//A label to display the style type:
		Label styleLabel = new Label(Msg.get(this, "label"));
		
		//A button to close the stage
		Button closeButton = new Button(Msg.get(this, "button"));
		closeButton.setOnAction(e -> primaryStage.close());
		
		//
		VBox root = new VBox();
		root.getChildren().addAll(styleLabel, closeButton);
		Scene scene = new Scene(root, 100, 70);
		primaryStage.setScene(scene);
		
		//The title of the stage if not visible for all styles
		primaryStage.setTitle(Msg.get(this, "title"));
		
		//
//		show(primaryStage, styleLabel, StageStyle.DECORATED);
//		show(primaryStage, styleLabel, StageStyle.UNDECORATED);
//		show(primaryStage, styleLabel, StageStyle.TRANSPARENT);
//		show(primaryStage, styleLabel, StageStyle.UNIFIED);
		show(primaryStage, styleLabel, StageStyle.UTILITY);
		
		

	}
	
	private void show(Stage stage, Label styleLabel, StageStyle style){
		styleLabel.setText(style.toString());
		
		//Set the style
		stage.initStyle(style);
		//
		
		//For the transparent style, set the scene fill to null.
		//Otherwise, the content area will have the default white background
		//of the scene.
		if(StageStyle.TRANSPARENT == style){
			stage.getScene().setFill(null);
			//
			stage.getScene().getRoot().setStyle("-fx-background-color: transparent;");
		}else if(StageStyle.UNIFIED == style){
			stage.getScene().setFill(Color.TRANSPARENT);
		}
		
		//Show hte stage
		stage.show();
	}

}

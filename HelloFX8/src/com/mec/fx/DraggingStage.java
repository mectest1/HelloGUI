package com.mec.fx;


import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DraggingStage extends Application {

	private Stage stage;
	private double dragOffsetX;
	private double dragOffsetY;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		
		//
		Label msgLabel = new Label(Msg.get(this, "label"));
		Button closeButton = new Button(Msg.get(this, "button"));
		closeButton.setOnAction(e -> primaryStage.close());
		
		//
		VBox root = new VBox();
		root.getChildren().addAll(msgLabel, closeButton);
		
		Scene scene = new Scene(root, 300, 200);
		scene.setOnMousePressed(e -> handleMousePressed(e));
		scene.setOnMouseDragged(e -> handleMouseDragged(e));
		
		//
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		
		//Set mouse pressed and dragged event handlers for the scene.
	}

	protected void handleMousePressed(MouseEvent e){
//		this.dragOffsetX = e.getScreenX() - stage.getX();
//		this.dragOffsetY = e.getScreenY() - stage.getY();
		this.dragOffsetX = e.getSceneX();
		this.dragOffsetY = e.getSceneY();
	}
	
	protected void handleMouseDragged(MouseEvent e){
		stage.setX(e.getScreenX() - this.dragOffsetX);
		stage.setY(e.getScreenY() - this.dragOffsetY);
	}
}

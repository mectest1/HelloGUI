package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NodeSizesApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button btn = new Button(Msg.get(this, "button"));
		
		VBox root = new VBox();
		
		HBox bar = new HBox(btn);
		root.getChildren().add(bar);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		//
		StringBuilder msg = new StringBuilder(Msg.get(this, "before"));
		printSize(msg, btn);
		
		//Change the button's properties
		btn.setWrapText(true);
		btn.setPrefWidth(80);
		
		msg.append(Msg.get(this, "after"));
		printSize(msg, btn);
		
		Label labelInfo = new Label(msg.toString());
		root.getChildren().add(labelInfo);
		primaryStage.sizeToScene();
	}

	
	private void printSize(StringBuilder msg, Region btn){
		msg.append(String.format(Msg.get(this, "output")
				, btn.getContentBias()
				
				,btn.getPrefWidth(), btn.getPrefHeight()
				,btn.getMinWidth(), btn.getMinHeight()
				,btn.getMaxWidth(), btn.getMaxHeight()
				
				,btn.prefWidth(-1), btn.prefHeight(btn.prefWidth(-1))
				,btn.minWidth(-1), btn.minHeight(btn.prefWidth(-1))
				,btn.maxWidth(-1), btn.maxHeight(btn.maxWidth(-1))
				
				,btn.getWidth(), btn.getHeight()
				));
	}
}

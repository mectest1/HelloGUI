package com.mec.fx;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ScrollPaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		StringBuilder fileStr = new StringBuilder();
		Files.readAllLines(Paths.get(Msg.get(this, "srcFolder")
				, getClass().getName().replace(".", "/") + Msg.get(this, "fileExt")))
//			.forEach(fileStr::append);;	//<- newline characters have been stripped off
			.forEach(l -> fileStr.append(l).append(Msg.get(this, "newline")));
		Label content = new Label(fileStr.toString());
		
		//Create a scroll pane with the label as its content
		ScrollPane sPane = new ScrollPane(content);
		sPane.setPannable(true);
		sPane.setPrefViewportWidth(300);
		sPane.setPrefViewportHeight(200);
		
//		sPane.setPrefWidth(1600);
//		sPane.setPrefHeight(1200);
		
		//
		HBox root = new HBox(sPane);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

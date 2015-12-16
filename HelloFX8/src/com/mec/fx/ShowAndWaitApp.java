package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ShowAndWaitApp extends Application {
	protected static int counter = 0;
	protected Stage lastOpenStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox();
		Button openButton = new Button(Msg.get(this, "open"));
		openButton.setOnAction(e -> open(++counter));
		
		root.getChildren().add(openButton);
		Scene scene = new Scene(root, 400, 400);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		this.lastOpenStage = primaryStage;
		
	}

	
	private void open(int stageNumber){
		Stage stage = new Stage();
		stage.setTitle(String.format(Msg.get(this, "subStage.title"), stageNumber));
		
		//
		Button sayHelloButton = new Button(Msg.get(this, "subStage.sayHello"));
		sayHelloButton.setOnAction(e -> out.printf(Msg.get(this, "subStage.helloFrom"), stageNumber));
		
		Button openButton = new Button(Msg.get(this, "open"));
		openButton.setOnAction(e -> open(++counter));
		
		//
		VBox root = new VBox();
		root.getChildren().addAll(sayHelloButton, openButton);
		Scene scene = new Scene(root, 200, 200);
		
		stage.setScene(scene);
		stage.setX(this.lastOpenStage.getX() + 50);
		stage.setY(this.lastOpenStage.getY() + 50);
		this.lastOpenStage = stage;
		
		out.printf(Msg.get(this, "subStage.before"), stageNumber);
		//Show the stage and wait for it to close
		stage.showAndWait();
		out.printf(Msg.get(this, "subStage.after"), stageNumber);
	}
	
	
	private static PrintStream out = System.out;
}

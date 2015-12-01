package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxLifeCycleApp extends Application {

	public FxLifeCycleApp(){
		out.printf(Msg.get(this, "constructor"), Thread.currentThread().getName());
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		out.printf(Msg.get(this, "start"), Thread.currentThread().getName());
		
		Group p = new Group();
		Scene scene = new Scene(p, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	@Override
	public void init() throws Exception {
		out.printf(Msg.get(this, "init"), Thread.currentThread().getName());
	}

	@Override
	public void stop() throws Exception {
		out.printf(Msg.get(this, "stop"), Thread.currentThread().getName());
	}
	
	private static final PrintStream out = System.out;
	

	public static void main(String[] args) {
		launch(args);
	}
}

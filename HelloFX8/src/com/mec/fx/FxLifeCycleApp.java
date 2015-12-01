package com.mec.fx;

import java.io.PrintStream;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxLifeCycleApp extends Application {

	public FxLifeCycleApp(){
		out.printf("FxLifeCycleApp() method: %s\n", Thread.currentThread().getName());
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		out.printf("start() method: %s\n", Thread.currentThread().getName());
		
		Group p = new Group();
		Scene scene = new Scene(p, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX Application Life Cycle");
		primaryStage.show();
	}

	@Override
	public void init() throws Exception {
		out.printf("init() method: %s\n", Thread.currentThread().getName());
	}

	@Override
	public void stop() throws Exception {
		out.printf("stop() method: %s\n", Thread.currentThread().getName());
	}
	
	private static final PrintStream out = System.out;
	

	public static void main(String[] args) {
		launch(args);
	}
}

package com.mec.fx;

import java.io.PrintStream;
import java.util.stream.IntStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BadUIApp extends Application {
	Label statusLbl = new Label(Msg.get(UnresponsiveUIApp.class, "label.init"));
	Button startBtn = new Button(Msg.get(UnresponsiveUIApp.class, "button.start"));
	Button exitBtn = new Button(Msg.get(UnresponsiveUIApp.class, "button.exit"));

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Add event handlers to the buttons
		startBtn.setOnAction(e -> startTask());
		exitBtn.setOnAction(e -> Platform.exit());
		
		//
		HBox buttonBox = new HBox(5, startBtn ,exitBtn);
		VBox root = new VBox(10, statusLbl, buttonBox);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	void startTask(){
		//Create a runnable
		Runnable task = this::runTask;
		
		//Run the task in a background thread
		Thread backgroundThread = new Thread(task);
		
		//Terminate teh running thread if the application exists
		backgroundThread.setDaemon(true);
		
		//Start the thread
		backgroundThread.start();
	}
	
	void runTask(){
		IntStream.rangeClosed(0, 10).forEach(i -> {
			try{
				String status = String.format(Msg.get(UnresponsiveUIApp.class, "info"), i);
				statusLbl.setText(status);	//<- java.lang.IllegalStateException: Not on FX application thread; currentThread
											//status of node in scene can only be modified in JavaFX Application Thread
				out.println(status);
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace(out);
			}
		});
	}

	
	
	private static final PrintStream out = System.out;

}

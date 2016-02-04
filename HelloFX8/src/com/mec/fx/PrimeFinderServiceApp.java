package com.mec.fx;

import com.mec.fx.OneShotTaskApp.PrimeFinderTask;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PrimeFinderServiceApp extends Application {

	Button startBtn = new Button(Msg.get(this, "button.start"));
	Button cancelBtn = new Button(Msg.get(this, "button.cancel"));
	Button resetBtn = new Button(Msg.get(this, "button.reset"));
	Button exitBtn = new Button(Msg.get(this, "buttno.exit"));
	boolean onceSarted = false;
	
	//Create the service
	Service<ObservableList<Long>> service = new Service<ObservableList<Long>>(){

		@Override
		protected Task<ObservableList<Long>> createTask() {
			return new PrimeFinderTask();
		}
		
	};
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Add event handlers to the buttons
		addEventHandlers();
		
		//Enable disable buttons based on the service state
		bindButtonsState();
		
		//
		GridPane pane = new WorkerStateUI();
		HBox buttonBox = new HBox(5, startBtn, cancelBtn, resetBtn, exitBtn);
		BorderPane root = new BorderPane(pane);
		root.setBottom(buttonBox);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	void addEventHandlers(){
		//Add event handlers to the buttons
		startBtn.setOnAction(e ->{
			if(onceSarted){
				service.restart();
			}else{
				service.start();
				onceSarted = true;
				startBtn.setText(Msg.get(this, "button.restart"));
			}
		});
		
		cancelBtn.setOnAction(e -> service.cancel());
		resetBtn.setOnAction(e -> service.reset());
		exitBtn.setOnAction(e -> Platform.exit());
	}


	void bindButtonsState(){
		cancelBtn.disableProperty().bind(service.stateProperty().isNotEqualTo(State.RUNNING));
		resetBtn.disableProperty().bind(Bindings.or(service.stateProperty().isEqualTo(State.RUNNING), service.stateProperty().isEqualTo(State.SCHEDULED)));
	}
}

















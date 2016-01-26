package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ProgressApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		ProgressIndicator indeterminateInd = new ProgressIndicator();
		ProgressIndicator determinateInd = new ProgressIndicator();
		
		//
		ProgressBar indeterminateBar = new ProgressBar();
		ProgressBar determinateBar = new ProgressBar();
		
		//
		Button completeIndBtn = new Button(Msg.get(this, "button.complete"));
		completeIndBtn.setOnAction( e -> indeterminateInd.setProgress(1.0));
		
		Button completeBarBtn = new Button(Msg.get(this, "button.complete"));
		completeBarBtn.setOnAction(e -> indeterminateBar.setProgress(1.0));
		
		//
		Button makeProgressIndBtn = new Button(Msg.get(this, "button.progress"));
		makeProgressIndBtn.setOnAction(e -> makeProgress(determinateInd));
		
		//
		Button makeProgressBarBtn = new Button(Msg.get(this, "button.progress"));
		makeProgressBarBtn.setOnAction(e -> makeProgress(determinateBar));
		
		//
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(5);
		
		root.addRow(0, new Label(Msg.get(this, "label.indeterminate")), 
				indeterminateInd, completeIndBtn
				);
		root.addRow(1, new Label(Msg.get(this, "label.determinate")), 
				determinateInd, makeProgressIndBtn
				);
		root.addRow(2, new Label(Msg.get(this, "label.indeterminate")),
				indeterminateBar, completeBarBtn
				);
		root.addRow(3, new Label(Msg.get(this, "label.determinate")), 
				determinateBar, makeProgressBarBtn
				);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	void makeProgress(ProgressIndicator ind){
		double progress = ind.getProgress();
		if(0 >= progress){
			progress = 0.1;
		}else{
			progress += 0.1;
			if(1.0 <= progress){
				progress = 1.0;
			}
		}
		ind.setProgress(progress);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}

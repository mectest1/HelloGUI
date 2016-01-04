package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FlowPaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		double hgap = 5;
		double vgap = 10;
		
		FlowPane root = new FlowPane(hgap, vgap);
		
		//Add then buttons to the flow pane;
		for(int i = 1; i<=10; ++i){
			root.getChildren().add(new Button(String.format(Msg.get(this, "button"), i)));
		}
		root.setStyle(Msg.get(this, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		

	}

}

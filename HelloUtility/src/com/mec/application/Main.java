package com.mec.application;
	
import com.mec.resources.Msg;
import com.mec.resources.ViewFactory;
import com.mec.resources.ViewFactory.LoadViewResult;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	

	@Override
	public void start(Stage primaryStage) {
		try {
			
			LoadViewResult<BorderPane, RootPaneController> loadResult = ViewFactory.loadViewDeluxe(Msg.get(this, "rootPane"));
			BorderPane root = loadResult.getViewRoot();
			RootPaneController rootController = loadResult.getController();
			Scene scene = new Scene(root,400,400);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle(Msg.get(this, "title"));
			try {
				primaryStage.show();
				root.requestFocus();	//Clear focus for log msg panel;
			} catch (Exception e) {
				rootController.log(e);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

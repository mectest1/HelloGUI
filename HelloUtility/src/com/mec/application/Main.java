package com.mec.application;
	
import com.mec.resources.JarTool;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	

	@Override
	public void start(Stage primaryStage) {
		try {
//			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("RootPane.fxml"));
			FXMLLoader loader = new FXMLLoader(getClass().getResource(Msg.get(this, "rootPane")));
			BorderPane root = (BorderPane)loader.load();
			RootPaneController rootController = (RootPaneController)loader.getController();
			
			Label hello = new Label(Msg.get(this, "hello"));
			root.setCenter(hello);
			
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource(Msg.get(this, "rootStyle")).toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setTitle(Msg.get(this, "title"));
			try {
				primaryStage.show();
				root.requestFocus();	//Clear focus for log msg panel;
			} catch (Exception e) {
//				rootController.appendLog(JarTool.exceptionToStr(e));
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

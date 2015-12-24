package com.mec.resources;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ViewFactory {

	
	
	@SuppressWarnings("unchecked")
	private static <T> T loadView(String viewURL){
		T retval = null;
		try {
			retval = (T) FXMLLoader.load(ViewFactory.class.getResource(viewURL));
		} catch (IOException e) {
//			e.printStackTrace();
//			retval =  null;
		}
		return retval;
	}
	
	private static Stage newStage(Parent sceneRoot, String stageTitle){
		Scene scene = new Scene(sceneRoot);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setTitle(stageTitle);
		return stage;
	}
	
	public static void showNewStage(String viewUrl, String stageTitle){
		Pane viewPane = ViewFactory.loadView(viewUrl);
		Stage stage = ViewFactory.newStage(viewPane, stageTitle);
		stage.show();
	}
}

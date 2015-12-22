package com.mec.resources;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewFactory {

	
	
	@SuppressWarnings("unchecked")
	public static <T> T loadView(String viewURL){
		T retval = null;
		try {
			retval = (T) FXMLLoader.load(ViewFactory.class.getResource(viewURL));
		} catch (IOException e) {
//			e.printStackTrace();
//			retval =  null;
		}
		return retval;
	}
	
	public static Stage newStage(Parent sceneRoot, String stageTitle){
		Scene scene = new Scene(sceneRoot);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setTitle(stageTitle);
		return stage;
	}
}

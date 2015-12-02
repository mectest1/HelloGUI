package com.mec.fx;

import com.mec.fx.views.TetrisPaneController;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TetrisPaneApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/mec/fx/views/TetrisPane.fxml"));
		BorderPane root = (BorderPane) loader.load();
		
		Scene scene = new Scene(root, 
				Msg.get(this, "scene.width", Integer::parseInt, 400),
				Msg.get(this, "scene.height", Integer::parseInt, 300)
				);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		
		primaryStage.show();
		
		
		//
		TetrisPaneController controller = loader.getController();
		controller.setTetrisApp(this);
		
	}

	
	
	public void sayHello(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(Msg.get(this, "alert.title"));
		alert.setHeaderText(Msg.get(this, "alert.header"));
		alert.setContentText(Msg.get(this, "alert.content"));
//		alert.initModality(Modality.WINDOW_MODAL);
		alert.initOwner(this.primaryStage);
		
//		alert.show();
		alert.showAndWait();
	}
	
	
	private Stage primaryStage;
	
}

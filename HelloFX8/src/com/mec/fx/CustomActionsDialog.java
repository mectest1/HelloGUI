package com.mec.fx;

import java.util.Optional;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class CustomActionsDialog extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(Msg.get(this, "title"));
		alert.setHeaderText(Msg.get(this, "header"));
		alert.setContentText(Msg.get(this, "content"));
		
		ButtonType buttonTypeOne = new ButtonType(Msg.get(this, "buttonType.one"));
		ButtonType buttonTypeTwo = new ButtonType(Msg.get(this, "buttonType.two"));
		ButtonType buttonTypeThree = new ButtonType(Msg.get(this, "buttonType.three"));
		ButtonType buttonTypeCancel = new ButtonType(Msg.get(this, "buttonType.cancel"), ButtonData.CANCEL_CLOSE);
		
		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);
		
		Optional<ButtonType> result = alert.showAndWait();
		
//		if(buttonTypeOne == result.get()){
//			
//		}else if(buttonTypeTwo == result.get()){
//			
//		}else if(buttonTypeThree == result.get()){
//			
//		}else{
//			//user chose CANCEL or closed the dialog
//		}
		System.out.printf("Use chosed button: %s\n", result.get());

	}

}

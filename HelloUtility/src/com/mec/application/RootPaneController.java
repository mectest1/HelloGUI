package com.mec.application;

import com.mec.resources.DialogFactory;
import com.mec.resources.Msg;
import com.mec.resources.ViewFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class RootPaneController {
	
	@FXML
	private void onExit(){
		Platform.exit();
	}
	
	@FXML
	private void onAbout(){
//		Alert about = new Alert(AlertType.INFORMATION);
//		about.setTitle(Msg.get(this, "about.title"));
//		about.setHeaderText(Msg.get(this, "about.header"));
//		about.setContentText(Msg.get(this, "about.content"));
		Alert about = DialogFactory.newAlert(AlertType.INFORMATION, 
				Msg.get(this, "about.title"), 
				Msg.get(this, "about.header"), 
				Msg.get(this, "about.content")
		);
		
		about.getButtonTypes().clear();
		
		ButtonType okButton = new ButtonType(Msg.get(this, "about.ok"), ButtonData.OK_DONE);
		about.getButtonTypes().add(okButton);
		about.showAndWait();
	}
	
	@FXML
	private void onBase64Decoder(){
		
//		com.sun.org.apache.xml.internal.security.Init.init();
		
		String viewUrl = Msg.get(this, "menu.view.base64decoder.url");
		Pane viewPane = ViewFactory.loadView(viewUrl);
		
//		Stage stage = new Stage();
//		stage.setScene(new Scene(viewPane));
		Stage stage = ViewFactory.newStage(viewPane, Msg.get(this, "menu.view.base64decoder.title"));
		
		stage.show();
	}
}

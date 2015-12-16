package com.mec.fx;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class KnowingDetailsApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		String url = Msg.get(this, "url");
		Button openUrlButton = new Button(Msg.get(this, "openButton"));
		openUrlButton.setOnAction(e -> getHostServices().showDocument(url));
		
		//
		Button showAlert = new Button(Msg.get(this, "showAlert"));
		showAlert.setOnAction(e -> showAlert());
		
		VBox root = new VBox();
		
		//Add buttons and all host related details to the VBox;
		root.getChildren().addAll(openUrlButton, showAlert);
		
		//
		Map<String, String> hostDetails = getHostDetails();
		for(Map.Entry<String, String> entry : hostDetails.entrySet()){
			String desc = String.format(Msg.get(this, "descLabel"), entry.getKey(), entry.getValue());
			root.getChildren().add(new Label(desc));
		}
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	private Map<String, String> getHostDetails(){
		HashMap<String, String> retval = new HashMap<>();
		HostServices host = getHostServices();
		
		//
		retval.put(Msg.get(this, "codeBase"), host.getCodeBase());
		retval.put(Msg.get(this, "documentBase"), host.getDocumentBase());
		retval.put(Msg.get(this, "environment"), null == host.getWebContext()? Msg.get(this, "environment.nonWeb") : Msg.get(this, "environment.web"));
		retval.put(Msg.get(this, "resourceURI"), host.resolveURI(host.getDocumentBase(), 
				String.format(Msg.get(this, "resourceURI.urlPattern"), getClass().getName())
				));
		return retval;
	}
	private void showAlert(){
		HostServices host = getHostServices();
		JSObject js = host.getWebContext();
		if(null == js){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initModality(Modality.WINDOW_MODAL);
			
			//
			alert.setTitle(Msg.get(this, "alert.title"));
			alert.setHeaderText(Msg.get(this, "alert.FX"));
			Optional<ButtonType> result = alert.showAndWait();
			
		}else{
			js.eval(Msg.get(this, "alert.JS.script"));
		}
	
	}
	
//	private static final 
}

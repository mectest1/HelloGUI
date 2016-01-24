package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckBoxApp extends Application {

	Label userSelectionMsg = new Label(String.format(Msg.get(this, "msg.pattern"), Msg.get(this, "msg.3")));
	CheckBox agreeCbx;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create a CheckBox to support only two states
		CheckBox hungryCbx = new CheckBox(Msg.get(this, "checkbox.hungry"));
		
		//Create a CheckBox to support three states
		agreeCbx = new CheckBox(Msg.get(this, "checkbox.agree"));
//		agreeCbx.setIndeterminate(true);
		agreeCbx.setAllowIndeterminate(true);
		
		//Track the state change for the "I agree" CheckBox
		//Text for the Label userSelectionMsg will be updated
		agreeCbx.selectedProperty().addListener(this::changed);
		agreeCbx.indeterminateProperty().addListener(this::changed);
		
		//
		VBox root = new VBox(20, userSelectionMsg, hungryCbx, agreeCbx);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root, 200, 130);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}
	
	//A change listener to track the state change in agreeCbx
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldVal, Boolean newVal){
		String msg;
		if(agreeCbx.isIndeterminate()){
			msg = Msg.get(this, "msg.1");
		}else if(agreeCbx.isSelected()){
			msg = Msg.get(this, "msg.2");
		}else{
			msg = Msg.get(this, "msg.3");
		}
		userSelectionMsg.setText(String.format(Msg.get(this, "msg.pattern"), msg));
	}

}

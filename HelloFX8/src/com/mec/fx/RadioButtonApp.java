package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RadioButtonApp extends Application {

	Label userSelectionMsg = new Label(String.format(Msg.get(this, "msg.pattern"), Msg.get(this, "toggle.initial")));
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create four toggle buttons
		ToggleGroup group = new ToggleGroup();
		Msg.getList(this, "toggle").forEach(t -> group.getToggles().add(new RadioButton(t)));	//<- Change ToggleButton to RadioButton
		
		//Track the selection changes and display the currently selected season
		group.selectedToggleProperty().addListener(this::changed);
		group.selectToggle(group.getToggles().get(0));	//Set the default choice
		//
		Label msg = new Label(Msg.get(this, "select"));
		
		//Add ToggleButtons to an HBox
		HBox buttonBox = new HBox(10);
//		buttonBox.getChildren().addAll(group.getToggles());
		group.getToggles().forEach(t -> buttonBox.getChildren().add((ToggleButton)t));
		
		//
		VBox root = new VBox(10, userSelectionMsg, msg, buttonBox);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	
	//A change listener to track the selection in the group
	public void changed(ObservableValue<? extends Toggle> observable, Toggle oldBtn, Toggle newBtn){
		String selectedLabel = Msg.get(this, "toggle.initial");
		if(null != newBtn){
			selectedLabel = ((Labeled)newBtn).getText();
		}
		userSelectionMsg.setText(String.format(Msg.get(this, "msg.pattern"), selectedLabel));
	}

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class ChoiceBoxApp extends Application {

	TextArea logMsg = new TextArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//
		Label seasonLbl = new Label(Msg.get(this, "label.select"));
		ChoiceBox<String> seasons = new ChoiceBox<>();
		seasons.getItems().addAll(Msg.getList(this, "season"));
		
		//Select the first season from the list;
		seasons.getSelectionModel().selectFirst();
		
		//Add ChnageListeners to track change in selected index and item,
		//Only one listener is necessary if you want ot track change in selection
		seasons.getSelectionModel().selectedItemProperty().addListener(this::itemChanged);
		seasons.getSelectionModel().selectedIndexProperty().addListener(this::indexChanged);
		
		//
		Label selectedmsgLbl = new Label(Msg.get(this, "msg.select"));
		Label selectedValueLbl = new Label(Msg.get(this, "msg.initial"));
		
		//Bind the value property to the text property of the label
		selectedValueLbl.textProperty().bind(seasons.valueProperty());
		
		//Display controls in a GridPane
		GridPane root = new GridPane();
		root.setVgap(10);
		root.setHgap(10);
		root.addRow(0, seasonLbl, seasons);
		root.addRow(1, selectedmsgLbl, selectedValueLbl);
		
		GridPane.setColumnSpan(logMsg, 2);
		root.addRow(2, logMsg);
		
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene= new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	
	private void itemChanged(ObservableValue<? extends String> observable, String oldVal, String newVal){
		logMsg.appendText(String.format(Msg.get(this, "msg.change.item"), oldVal, newVal));
	}
	
	private void indexChanged(ObservableValue<? extends Number> observable, Number oldVal, Number newVal){
		logMsg.appendText(String.format(Msg.get(this, "msg.change.index"), oldVal, newVal));
	}
	
	

}

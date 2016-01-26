package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TextFieldApp extends Application {

	TextArea info = new TextArea(Msg.get(this, "info.initial"));
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create a TextField with an empty string as its initial text
		TextField firstNameFld = new TextField();
		TextField lastNameFld = new TextField();
		
		//Both fields should be wide enough to display 15 chars
		firstNameFld.setPrefColumnCount(15);
		lastNameFld.setPrefColumnCount(15);
		
		//Add a ChangeListener to the text property
		firstNameFld.textProperty().addListener(this::onChanged);
		lastNameFld.textProperty().addListener(this::onChanged);
		
		//Add a dummy custom context menu for the firstName field
		ContextMenu cm = new ContextMenu();
		MenuItem dummyItem = new MenuItem(Msg.get(this, "context.item"));
//		dummyItem.setDisable(true);
		cm.getItems().add(dummyItem);
		firstNameFld.setContextMenu(cm);	//<- replace the default context menu
		
		//Set ActionEvent handers for both fields
		firstNameFld.setOnAction(e -> onAction(Msg.get(this, "label.firstName")));
		lastNameFld.setOnAction(e -> onAction(Msg.get(this, "label.lastName")));
		
		//
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(5);
		root.addRow(0, new Label(Msg.get(this, "label.firstName")), firstNameFld);
		root.addRow(1, new Label(Msg.get(this, "label.lastName")), lastNameFld);
		root.add(info, 0, 2, 2, 1);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

	
	
	
	void onAction(String fieldName){
		info.appendText(String.format(Msg.get(this, "action.format"), fieldName));
	}
	
	void onChanged(ObservableValue<? extends String> prop, String oldVal, String newVal){
		info.appendText(String.format(Msg.get(this, "changed.format"), oldVal, newVal));
	}
}

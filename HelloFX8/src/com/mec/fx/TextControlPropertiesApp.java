package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TextControlPropertiesApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField nameFld = new TextField();
		Label anchorLbl = new Label();
		Label caretLbl = new Label();
		Label lengthLbl = new Label();
		Label selectedTextLbl = new Label();
		Label selectionLbl = new Label();
		Label textLbl = new Label();
		
		//Bind text property o the labels to the properties of the TextField
		anchorLbl.textProperty().bind(nameFld.anchorProperty().asString());
		caretLbl.textProperty().bind(nameFld.caretPositionProperty().asString());
		lengthLbl.textProperty().bind(nameFld.lengthProperty().asString());
		selectedTextLbl.textProperty().bind(nameFld.selectedTextProperty());
		selectionLbl.textProperty().bind(nameFld.selectionProperty().asString());
		textLbl.textProperty().bind(nameFld.textProperty());
		
		//
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(5);
		root.addRow(0, new Label(Msg.get(this, "label.name")), nameFld);
		root.addRow(1, new Label(Msg.get(this, "label.anchorPos")), anchorLbl);
		root.addRow(2, new Label(Msg.get(this, "label.caretPos")), caretLbl);
		root.addRow(3, new Label(Msg.get(this, "label.length")), lengthLbl);
		root.addRow(4, new Label(Msg.get(this, "label.selected")), selectedTextLbl);
		root.addRow(5, new Label(Msg.get(this, "label.selection")), selectionLbl);
		root.addRow(6, new Label(Msg.get(this, "label.text")), textLbl);
		
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

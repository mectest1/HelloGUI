package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LabelApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField fNameFld = new TextField();
		Label fNameLbl = new Label(Msg.get(this, "firstName"));
		fNameLbl.setLabelFor(fNameFld);
		fNameLbl.setMnemonicParsing(true);
		
		TextField lNameFld = new TextField();
		Label lNameLbl = new Label(Msg.get(this, "lastName"));
		lNameLbl.setLabelFor(lNameFld);
		lNameLbl.setMnemonicParsing(true);
		
		//
		GridPane root = new GridPane();
		root.addRow(0,  fNameLbl, fNameFld);
		root.addRow(1, lNameLbl, lNameFld);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

}

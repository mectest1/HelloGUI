package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sun.util.locale.provider.AvailableLanguageTags;

public class GridPaneFormApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		//A lable and a TextField
		Label nameLbl = new Label(Msg.get(this, "name"));
		TextField nameField = new TextField();
		
		//
		//A Label and a TextArea
		Label descLabel = new Label(Msg.get(this, "desc"));
//		TextArea descText = new TextArea();
//		descText.
	}

}

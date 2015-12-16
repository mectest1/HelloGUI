package com.mec.fx;

import java.io.PrintStream;
import java.util.Optional;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class TextInputDialogApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextInputDialog dialog = new TextInputDialog(Msg.get(this, "initialText"));
		
		dialog.setTitle(Msg.get(this, "title"));
		dialog.setHeaderText(Msg.get(this, "header"));
		dialog.setContentText(Msg.get(this, "content"));
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> out.printf(Msg.get(this, "out"), result.get()));
	}

	private static final PrintStream out = System.out;
}

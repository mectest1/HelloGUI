package com.mec.fx;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

public class ChoiceDialogApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		List<String> choices = Arrays.asList(
				Msg.get(this, "choice.1"),
				Msg.get(this, "choice.2"),
				Msg.get(this, "choice.3")
				);

		ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(1), choices);
		dialog.setTitle(Msg.get(this, "title"));
		dialog.setHeaderText(Msg.get(this, "header"));
		dialog.setContentText(Msg.get(this, "content"));
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(choice -> out.printf(Msg.get(this, "output"), choice));
	}

	private static final PrintStream out = System.out;
}

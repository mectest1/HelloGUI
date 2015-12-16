package com.mec.fx;

import java.io.PrintStream;
import java.util.Optional;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class LoginDialogApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(Msg.get(this, "title"));
		dialog.setHeaderText(Msg.get(this, "header"));
		
		//Set the icon (must be included in the project
//		dialog.setGraphic(new ImageView(getClass().getResource(Msg.get(this, "icon")).toString()));
		
		ButtonType loginButtonType = new ButtonType(Msg.get(this, "login"), ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
		
		
		//Create the user name and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);;
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		//
		TextField username = new TextField();
		username.setPromptText(Msg.get(this, "username.prompt"));
		PasswordField password = new PasswordField();
		password.setPromptText(Msg.get(this, "password.prompt"));
		
		//
		grid.add(new Label(Msg.get(this, "username.label")), 0, 0);
		grid.add(new Label(Msg.get(this, "password.label")), 0, 1);
		grid.add(username, 1, 0);
		grid.add(password, 1, 1);
		
		//Enable/Diable login button depending on whether a user name was entered
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);
		
		//Do some validation (using the Java 8 lambda expression
		username.textProperty().addListener((observable, oldVal, newVal) -> {
			loginButton.setDisable(newVal.trim().isEmpty());
		}); 

		//
		dialog.getDialogPane().setContent(grid);
		
		//Request focus on the username field by default
		Platform.runLater(() -> username.requestFocus());
		
		//Convert the result to a username-password pair when the login
		//button is clicked
		dialog.setResultConverter(dialogButton -> {
			if(loginButtonType == dialogButton){
				return new Pair<>(username.getText(), password.getText());
			}
			
			return null;
		});
		
		dialog.initStyle(StageStyle.UTILITY);
		Optional<Pair<String, String>> result = dialog.showAndWait();
		
		
		//
		result.ifPresent(pair -> out.printf(Msg.get(this, "output"), pair.getKey(), pair.getValue()));
		
	}

	private static final PrintStream out = System.out;
}

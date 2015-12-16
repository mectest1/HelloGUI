package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class StageModalityApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button ownedNoneButton = new Button(Msg.get(this, "button.ownedNone"));
		ownedNoneButton.setOnAction(e -> showDialog(primaryStage, Modality.NONE));
		VBox.setVgrow(ownedNoneButton, Priority.ALWAYS);

		Button nonOwnedNoneButton = new Button(Msg.get(this, "button.nonOwnedNone"));
		nonOwnedNoneButton.setOnAction( e -> showDialog(null, Modality.NONE));
		VBox.setVgrow(nonOwnedNoneButton, Priority.ALWAYS);
		
		Button ownedWindowModal = new Button(Msg.get(this, "button.ownedWindowModal"));
		ownedWindowModal.setOnAction( e -> showDialog(primaryStage, Modality.WINDOW_MODAL));
		VBox.setVgrow(ownedWindowModal, Priority.ALWAYS);
		
		Button nonOwnedWindowModal = new Button(Msg.get(this, "button.nonOwnedWindowModal"));
		nonOwnedWindowModal.setOnAction( e -> showDialog(null, Modality.WINDOW_MODAL));
		VBox.setVgrow(nonOwnedWindowModal, Priority.ALWAYS);
		
		Button ownedApplicationModal = new Button(Msg.get(this, "button.ownedApplicationModal"));
		ownedApplicationModal.setOnAction( e -> showDialog(primaryStage, Modality.APPLICATION_MODAL));
		VBox.setVgrow(ownedApplicationModal, Priority.ALWAYS);
		
		Button nonOwnedApplicationModal = new Button(Msg.get(this, "button.nonOwnedApplicationModal"));
		nonOwnedApplicationModal.setOnAction( e -> showDialog(null, Modality.APPLICATION_MODAL));
		VBox.setVgrow(nonOwnedApplicationModal, Priority.ALWAYS);
		
		VBox root = new VBox();
		root.getChildren().addAll(
				ownedNoneButton, nonOwnedNoneButton,
				ownedWindowModal, nonOwnedWindowModal,
				ownedApplicationModal, nonOwnedApplicationModal
				);
		Scene scene = new Scene(root, 300, 200);
		root.prefWidthProperty().bind(scene.widthProperty());
		root.prefHeightProperty().bind(scene.heightProperty());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	
	
	private void showDialog(Window owner, Modality modality){
		//Create a stage with specified owner and modality
		Stage stage = new Stage();
		stage.initOwner(owner);
		stage.initModality(modality);
		
		//
		Label modalityLabel = new Label(modality.toString());
		Button closeButton = new Button(Msg.get(this, "button.close"));
		closeButton.setOnAction(e -> stage.close());
		
		//
		VBox root = new VBox();
		root.getChildren().addAll(modalityLabel, closeButton);
		
		Scene scene = new Scene(root, 200, 100);
		stage.setScene(scene);
		stage.setTitle(Msg.get(this, "subStage.title"));
		stage.show();
	}
}

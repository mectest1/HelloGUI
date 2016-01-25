package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ComboBoxApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label seasonLbl= new Label(Msg.get(this, "label.season"));
		ComboBox<String> seasons = new ComboBox<>();
		seasons.getItems().addAll(Msg.getList(ChoiceBoxApp.class, "season"));
		
		//
		Label breakfastLbl = new Label(Msg.get(this, "label.breakfast"));
		ComboBox<String> breakfasts = new ComboBox<>();
		breakfasts.getItems().addAll(Msg.getList(this, "breakfast"));
		breakfasts.setEditable(true);
		
		//Show the user's selections in a label
		Label selectionLbl = new Label(Msg.get(this, "label.selection"));
		StringProperty str = new SimpleStringProperty(Msg.get(this, "info.selection"));
		selectionLbl.textProperty().bind(
				str.concat(Msg.get(this, "label.selection.1"))
				.concat(seasons.valueProperty())
				.concat(Msg.get(this, "label.selection.2"))
				.concat(breakfasts.valueProperty())
		);
		
		
		HBox row1 = new HBox(10, seasonLbl, seasons, breakfastLbl, breakfasts);
		VBox root = new VBox(10, row1, selectionLbl);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

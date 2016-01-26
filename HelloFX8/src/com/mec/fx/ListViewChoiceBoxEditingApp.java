package com.mec.fx;

import java.util.Arrays;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.ChoiceBoxListCell;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListViewChoiceBoxEditingApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ListView<String> breakfasts = new ListView<>();
		breakfasts.setPrefSize(200, 120);
		breakfasts.setEditable(true);
		breakfasts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//Let the user select a maximum of four breakfast items
		for(int i = 0; i < Msg.get(this, "item.count", Integer::parseInt, 1); ++i){
			breakfasts.getItems().add(Msg.get(this, "item.none"));
		}
		
		//Set a ChoiceBox cell factory for editing
		breakfasts.setCellFactory(ChoiceBoxListCell.forListView(
//		breakfasts.setCellFactory(ComboBoxListCell.forListView(	//<- both ComboBox and ChoiceBox will work
			FXCollections.observableList(
				Arrays.asList(Msg.get(ListViewEditingApp.class, "breakfasts").split(Msg.get(ListViewEditingApp.class, "breakfast.delimiter")))
			)
		));//<- even after the cell factory is set, we still need to add list items.
		
		
		VBox root = new VBox(10, new Label(Msg.get(this, "label.select"))
				, new Label(Msg.get(this, "label.breakfast"))
				, breakfasts
				);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

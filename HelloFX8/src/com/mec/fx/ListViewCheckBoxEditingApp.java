package com.mec.fx;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ListViewCheckBoxEditingApp extends Application {
	Map<String, ObservableValue<Boolean>> map = new HashMap<>();
	TextArea info = new TextArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//populate the map with ListView items as its keys and
		//their selected state as the value
//		Arrays.stream(Msg.get(ListViewEditingApp.class, "breakfasts").split(Msg.get(ListViewEditingApp.class, "breakfast.delimiter")).
		Stream.of(Msg.get(ListViewEditingApp.class, "breakfasts").split(Msg.get(ListViewEditingApp.class, "breakfast.delimiter")))
			.forEach(b -> map.put(b.trim(), new SimpleBooleanProperty(false)));
		
		ListView<String> breakfasts = new ListView<>();
		breakfasts.setPrefSize(200, 120);
		breakfasts.setEditable(true);
		breakfasts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//		breakfasts.setOrientation(Orientation.HORIZONTAL);	//<- Change the list orientation to horizontal
		
		//Add all keys from the map as items to the list view
		breakfasts.getItems().addAll(map.keySet());
		
		//Create a Callback object
		Callback<String, ObservableValue<Boolean>> itemToBoolean = item -> map.get(item);
		
		//Set the cell factory
		breakfasts.setCellFactory(CheckBoxListCell.forListView(itemToBoolean));
		
		//
		Button printBtn = new Button(Msg.get(this, "button.print"));
		printBtn.setOnAction(e -> printSelection());
		
		//
		VBox root = new VBox(10, new Label(Msg.get(this, "label.breakfast")), 
				breakfasts, printBtn, info
				);
		VBox.setVgrow(info, Priority.SOMETIMES);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	void printSelection(){
		int i = 0;
		info.appendText(
			String.format(Msg.get(this, "info.format"), 
					map.keySet().stream().filter(k -> map.get(k).getValue())
					.collect(Collectors.joining(Msg.get(this, "info.join")))
			)
		);
	}

}

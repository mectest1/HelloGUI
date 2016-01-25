package com.mec.fx;

import java.util.Arrays;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListViewSelectionModelApp extends Application {

	private ListView<String> seasons;
	private final Label selectedItemsLbl = new Label(String.format(Msg.get(this, "selection.format"), Msg.get(this, "season.none")));
	private final Label lastSelectedItemLbl = new Label(selectedItemsLbl.getText());
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Label seasonsLbl = new Label(Msg.get(this, "label.select"));
		seasons = new ListView<>();
		seasons.setPrefSize(120, 120);
		Arrays.asList(Msg.get(this, "season.items").split(Msg.get(this, "season.delimiter")))
			.forEach(s -> seasons.getItems().add(s.trim()));
		
		//Enable multiple selection
		seasons.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		//Add a selection change listener
		seasons.getSelectionModel().selectedItemProperty().addListener(this::selectionChanged);
		
		//Add some buttons to assist in selection
		Button selectAllBtn = new Button(Msg.get(this, "button.selectAll"));
		selectAllBtn.setOnAction(e -> seasons.getSelectionModel().selectAll());
		
		//
		Button clearAllBtn = new Button(Msg.get(this, "button.clearAll"));
		clearAllBtn.setOnAction(e -> seasons.getSelectionModel().clearSelection());
		
		//
		Button selectFirstBtn = new Button(Msg.get(this, "button.selectFirst"));
//		selectFirstBtn.setOnAction(e -> seasons.getSelectionModel().selectFirst());
		selectFirstBtn.setOnAction(e -> {
			seasons.getSelectionModel().clearSelection();
			seasons.getSelectionModel().selectFirst();
		});
		
		//
		Button selectLastBtn = new Button(Msg.get(this, "button.selectLast"));
//		selectLastBtn.setOnAction(e -> seasons.getSelectionModel().selectLast());
		selectLastBtn.setOnAction(e -> {
//			seasons.getSelectionModel().clearSelection();
			seasons.getSelectionModel().selectLast();
		});
		
		//
		Button selectNextBtn = new Button(Msg.get(this, "button.selectNext"));
//		selectNextBtn.setOnAction(e -> seasons.getSelectionModel().selectNext());
		selectNextBtn.setOnAction(e -> {
//			seasons.getSelectionModel().clearSelection();
			int index = seasons.getSelectionModel().getSelectedIndex();
			seasons.getSelectionModel().selectNext();
			seasons.getSelectionModel().clearSelection(index);
		});
		
		
		//
		Button selectPreviousBtn = new Button(Msg.get(this, "button.selectPrevious"));
//		selectPreviousBtn.setOnAction(e -> seasons.getSelectionModel().selectPrevious());
		selectPreviousBtn.setOnAction(e -> {
			int index = seasons.getSelectionModel().getSelectedIndex();
//			seasons.getSelectionModel().clearSelection();
			seasons.getSelectionModel().selectPrevious();
			seasons.getSelectionModel().clearSelection(index);
		});
		
		//Let all buttons expand as needed
		Arrays.asList(selectAllBtn, clearAllBtn, selectFirstBtn, selectLastBtn, selectNextBtn, selectPreviousBtn).forEach(b -> b.setMaxWidth(Double.MAX_VALUE));		

		//Add buttons to two VBox objects
		VBox singleSelectionBtns = new VBox(10, selectFirstBtn, selectPreviousBtn, selectNextBtn, selectLastBtn);
		
		//
		VBox allSelectionBtns = new VBox(10, selectAllBtn, clearAllBtn);

		
		GridPane root = new GridPane();
		root.setHgap(10);
		root.setVgap(5);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		//
		root.addColumn(0, seasonsLbl, seasons);
		root.add(singleSelectionBtns, 1, 1, 1, 1);
		root.add(allSelectionBtns, 2, 1, 1, 1);
		
		//Add controls to display the user selection
		Label selectionLbl = new Label(Msg.get(this, "label.selection"));
		root.add(selectionLbl, 0, 2);
		root.add(selectedItemsLbl, 1, 2, 2, 1);
		
		//
		Label lastSelectionLbl = new Label(Msg.get(this, "label.selection"));
		root.add(lastSelectionLbl, 0, 3);
		root.add(lastSelectedItemLbl, 1, 3, 2, 1);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	//A change listener to track the change in item selection
	void selectionChanged(ObservableValue<? extends String> observable, String oldVal, String newVal){
		String lastItem = null == newVal 
					? String.format(Msg.get(this, "selection.format"), Msg.get(this, "season.none"))
					: String.format(Msg.get(this, "selection.format"), newVal);
		lastSelectedItemLbl.setText(lastItem);
		
		//
		ObservableList<String> selectedItems = seasons.getSelectionModel().getSelectedItems();
		String selectedValues = selectedItems.isEmpty()
				? String.format(Msg.get(this, "selection.format"), Msg.get(this, "season.none"))
						: selectedItems.toString();
		selectedItemsLbl.setText(selectedValues);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

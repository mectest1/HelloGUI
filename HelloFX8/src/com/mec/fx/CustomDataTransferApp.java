package com.mec.fx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomDataTransferApp extends Application {

	
	ListView<Item> lv1 = new ListView<>();
	ListView<Item> lv2 = new ListView<>();
	static final DataFormat ITEM_LIST = new DataFormat(Msg.get(CustomDataTransferApp.class, "mime.format"));
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Build the UI
		GridPane root = getUIs();
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		//Add event handlers for the source and target
		//text fields of the DnD operation
		addDnDEventHandlers();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	GridPane getUIs(){
		Label msgLbl = new Label(Msg.get(this, "label.message"));
		
		lv1.setPrefSize(200, 200);
		lv2.setPrefSize(200, 200);
		lv1.getItems().addAll(this.getList());
		
		//Allow multi-select in lists
		lv1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lv2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		
		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		pane.add(msgLbl, 0, 0, 2, 1);
		pane.addRow(1, new Label(Msg.get(this, "label.list1")), new Label(Msg.get(this, "label.list2")));
		pane.addRow(2, lv1, lv2);
		
		return pane;
	}
	
	ObservableList<Item> getList(){
		ObservableList<Item> list = FXCollections.observableArrayList();
		Arrays.stream(Msg.get(this, "items").split(Msg.get(this, "item.delimiter"))).forEach(item -> {
			list.add(new Item(item.trim()));
		});
		return list;
	}
	
	void addDnDEventHandlers(){
		Stream.of(lv1, lv2).forEach(lv -> {
			lv.setOnDragDetected(e -> this.dragDetected(e,  lv));
			lv.setOnDragOver(e -> this.dragOver(e, lv));
			lv.setOnDragDropped(e -> this.dragDropped(e, lv));
			lv.setOnDragDone(e -> this.dragDone(e, lv));
			
			lv.setCellFactory(listView -> {
				return new ListCell<Item>(){

					@Override
					protected void updateItem(Item item, boolean empty) {
						super.updateItem(item, empty);
						if(!empty){
							this.setText(item.getName());
						}else{
							this.setText("");	//<--- Guess what will happen without this line?
						}
					}
				};
			});
		});
	}
	
	void dragDetected(MouseEvent e, ListView<Item> listView){
		//Make sure at least one item is selected
		int selectedCount = listView.getSelectionModel().getSelectedIndices().size();
		if(0 == selectedCount){
			e.consume();
			return;
		}
		
		//Initiate a drag-and-drop gesture
		Dragboard dragboard = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
		
		//Put the selected items to the dragboard
		ArrayList<Item> selectedItems = getSelectedItems(listView);
		ClipboardContent content = new ClipboardContent();
		content.put(ITEM_LIST, selectedItems);
		dragboard.setContent(content);
		
		e.consume();
	}
	
	void dragOver(DragEvent e, ListView<Item> listView){
		//If drag board has an ITEM_LIST and it is not being dragged
		//over itself, we accept the MOVe transfer mode
		Dragboard dragboard = e.getDragboard();
		
		if(e.getGestureSource() != listView
			&& dragboard.hasContent(ITEM_LIST)
		){
			e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		e.consume();
	}
	
	@SuppressWarnings("unchecked")
	void dragDropped(DragEvent e, ListView<Item> listView){
		boolean dragCompleted = false;
		
		//Transfer the data to the target
		Dragboard dragboard = e.getDragboard();
		
		//
		if(dragboard.hasContent(ITEM_LIST)){
			ArrayList<Item> list = (ArrayList<Item>) dragboard.getContent(ITEM_LIST);
			listView.getItems().addAll(list);
			//Data transfer is successful
			dragCompleted = true;
		}
		
		e.setDropCompleted(dragCompleted);
		e.consume();
	}
	
	
	void dragDone(DragEvent e, ListView<Item> listView){
		//Check how data was transfered to the target
		//If it was moved, clear the selected items
		TransferMode tm = e.getTransferMode();
		if(TransferMode.MOVE == tm){
			removeSelectedItems(listView);
		}
		e.consume();
	}
	
	ArrayList<Item> getSelectedItems(ListView<Item> listView){
		//Return the list of selected item in an ArrayList, so 
		//it is serializable and can be stored in a Dragboard
		ArrayList<Item> list = new ArrayList<>(listView.getSelectionModel().getSelectedItems());
		return list;
	}
	
	
	void removeSelectedItems(ListView<Item> listView){
		//Get all selected items in a separate list to avoid the shared list issue
		List<Item> selectedList = new ArrayList<>();
		
		listView.getSelectionModel().getSelectedItems().forEach(item -> selectedList.add(item));
		//Clear the selection
		listView.getSelectionModel().clearSelection();
		listView.getItems().removeAll(selectedList);
		
	}
	
	

	
	
	static class Item implements Serializable{
		private String name = Msg.get(CustomDataTransferApp.class, "name.initial");

		public Item(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Item [name=" + name + "]";
//			return name;
		}
	}
}

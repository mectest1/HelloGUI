package com.mec.fx;

import java.io.PrintStream;
import java.time.LocalDate;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeTableView.TreeTableViewSelectionModel;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

public class TreeTableViewAddDeleteRowsApp extends Application {

	private TreeTableView<Person> treeTable;
	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {
		TreeItem<Person> rootNode = TreeTableUtil.getModel();
		rootNode.setExpanded(true);
		
		//Create a TreeTableView with a model
		treeTable = new TreeTableView<>(rootNode);
		treeTable.setPrefWidth(400);
		treeTable.getSelectionModel().selectFirst();
		
		//Must make the TreTableView editable
		treeTable.setEditable(true);
		
		//Set appropriate cell factories for treeTable;
		TreeTableColumn<Person, String> fNameCol = TreeTableUtil.getfirstNameColumn();
		fNameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
		
		//
		TreeTableColumn<Person, String> lNameCol = TreeTableUtil.getLastNameColumn();
		lNameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
		
		//
		LocalDateStringConverter converter = new LocalDateStringConverter();
		TreeTableColumn<Person, LocalDate> birthDateCol = TreeTableUtil.getBirthDate();
		birthDateCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn(converter));
		
		//Add columns
		treeTable.getColumns().addAll(fNameCol, lNameCol, birthDateCol);
		
		//Add a placeholder to the TreeTableView
		//It is displayed when the root node is deleted
		treeTable.setPlaceholder(new Label(Msg.get(this, "treetable.placeholder")));
		
		Label msgLbl = new Label(Msg.get(this, "msg.general"));
		VBox root = new VBox(5, msgLbl, getButtons(), treeTable);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	private HBox getButtons(){
		Button addBtn = new Button(Msg.get(this, "add"));
		addBtn.setOnAction(e -> addRow());
		
		//
		Button deleteBtn = new Button(Msg.get(this, "delete"));
		deleteBtn.setOnAction(e -> deleteRow());
		
		//
		return new HBox(10, addBtn, deleteBtn);
	}
	
	private void addRow(){
		if(0 == treeTable.getExpandedItemCount()){
			//There is no row in the TreeTableView
			addNewRootItem();
		}else if(treeTable.getSelectionModel().isEmpty()){
			out.println(Msg.get(this, "add.error"));
			return;
		}else{
			addNewChildItem();
		}
	}
	
	private void addNewRootItem(){
		//Add a root Item
		TreeItem<Person> item = new TreeItem<>(new Person(Msg.get(this, "person.dummy"), 
				Msg.get(this, "person.dummy"),
				null
				));
		treeTable.setRoot(item);
		
		//Edit the item
		this.editItem(item);
	}
	private void addNewChildItem(){
		//Prepare a new TreeItem with a new Person object
		TreeItem<Person> item = new TreeItem<>(new Person(Msg.get(this, "person.dummy"),
				Msg.get(this, "person.dummy"), null));
		
		//Get the selection model
		TreeTableViewSelectionModel<Person> sm = treeTable.getSelectionModel();
		
		//Get the selected row index
		int rowIndex = sm.getSelectedIndex();
		
		//Get the selected TreeItem
		TreeItem<Person> selectedItem = sm.getModelItem(rowIndex);
		
		//Add the new item as children to the selected item.
		selectedItem.getChildren().add(item);
		
		//Make sure the new item is visible
		selectedItem.setExpanded(true);
		
//		treeTable.scrollTo(rowIndex);
		this.editItem(item);
	}
	private  void editItem(TreeItem<Person> item){
		//Scroll to the enw item
		int newRowIndex = treeTable.getRow(item);
		treeTable.scrollTo(newRowIndex);
		
		//Put the first column in editing mode
		TreeTableColumn<Person, ?> firstCol = treeTable.getColumns().get(0);
		treeTable.getSelectionModel().select(item);
		treeTable.getFocusModel().focus(newRowIndex, firstCol);
		treeTable.edit(newRowIndex, firstCol);
	}
	
	private void deleteRow(){
		//Get the selection model
		TreeTableViewSelectionModel<Person> sm = treeTable.getSelectionModel();
		if(sm.isEmpty()){
			out.println(Msg.get(this, "delete.error"));
			return;
		}
		
		int rowIndex = sm.getSelectedIndex();
		TreeItem<Person> selectedItem = sm.getModelItem(rowIndex);
		
		TreeItem<Person> parent = selectedItem.getParent();
		if(null != parent){
			parent.getChildren().remove(selectedItem);
		}else{
			//Muse be deleting the root item
			treeTable.setRoot(null);
		}
		
	}
	
	private static final PrintStream out = System.out;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

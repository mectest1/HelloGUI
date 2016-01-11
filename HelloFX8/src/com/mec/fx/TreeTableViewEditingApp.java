package com.mec.fx;

import java.time.LocalDate;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

public class TreeTableViewEditingApp extends Application {

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {
		//Create the model
		TreeItem<Person> rootNode = TreeTableUtil.getModel();
		rootNode.setExpanded(true);
		
		//Create a TreeTableView with a model
		TreeTableView<Person> treeTable = new TreeTableView<>(rootNode);
		treeTable.setPrefWidth(400);
		
		
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
		
		//
		HBox root = new HBox(treeTable);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.mec.fx;

import java.io.PrintStream;
import java.util.Arrays;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableViewAddDeleteRowsApp extends Application {

	private final TextField fNameField = new TextField();
	private final TextField lNameField = new TextField();
	private final DatePicker dobField = new DatePicker();
	
	TableView<Person> table = new TableView<>(PersonTableUtil.getPersonList());
	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {

//		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//		table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);	//default value
//		table.setColumnResizePolicy(resizeFeatures -> true);	//disable column resize
		
		//Trn on multi-row selection for the TableView
		TableViewSelectionModel<Person> tsm = table.getSelectionModel();
		tsm.setSelectionMode(SelectionMode.MULTIPLE);
		
		//Add columns to the TableView
		table.getColumns().addAll(PersonTableUtil.getIdColumn(), 
				PersonTableUtil.getFirstNameColumn(),
				PersonTableUtil.getLastNameColumn(),
				PersonTableUtil.getBirthDateColumn()
				);
		
		
		GridPane newDatePane = getNewPersonDatePane();
		
		Button restoreBtn = new Button(Msg.get(this, "restore"));
		restoreBtn.setOnAction(e -> restoreRows());
		
		Button deleteBtn = new Button(Msg.get(this, "delete"));
		deleteBtn.setOnAction(e -> deleteSelectedRows());
		
		//
		VBox root = new VBox(newDatePane, new HBox(10, restoreBtn, deleteBtn), table);
		root.setSpacing(5);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	private GridPane getNewPersonDatePane(){
		GridPane pane = new GridPane();
		
		pane.setHgap(10);
		pane.setVgap(5);
		pane.addRow(0, new Label(Msg.get(this, "firstName")), fNameField);
		pane.addRow(1, new Label(Msg.get(this, "lastName")), lNameField);
		pane.addRow(2, new Label(Msg.get(this, "birthDate")), dobField);
		
		//
		Button addBtn = new Button(Msg.get(this, "add"));
		addBtn.setOnAction(e -> addPerson());
		
		pane.add(addBtn, 2, 2);
		
		return pane;
	}
	private void deleteSelectedRows(){
		TableViewSelectionModel<Person> tsm = table.getSelectionModel();
		if(tsm.isEmpty()){
			out.println(Msg.get(this, "error"));
			return;
		}
		
		//Get all selected row indices in an array
		ObservableList<Integer> list = tsm.getSelectedIndices();
		Integer[] selectedIndices = list.toArray(new Integer[0]);
		Arrays.sort(selectedIndices);
		
		for(int i = selectedIndices.length - 1; 0 <= i; --i){
			tsm.clearSelection(selectedIndices[i]);
//			table.getItems().remove(selectedIndices[i]);	//<- this will not work as you expected
														//the ObservableList.remove() method is overloaded with remove(Object) and remove(int)
														//Thus remove(Integer) will result into remove(Object) being invoked;
			table.getItems().remove(selectedIndices[i].intValue());
		}
	}
	
	private void restoreRows(){
		table.getItems().clear();
		table.getItems().addAll(PersonTableUtil.getPersonList());
	}
	public Person getPerson(){
		return new Person(fNameField.getText(), lNameField.getText(), dobField.getValue());
	}
	
	public void addPerson(){
		Person p = getPerson();
		table.getItems().add(p);
		clearFields();
	}
	
	public void clearFields(){
		fNameField.clear();
		lNameField.clear();
		dobField.setValue(null);
	}
	
	private static final PrintStream out = System.out;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

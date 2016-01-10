package com.mec.fx;

import java.io.PrintStream;
import java.time.LocalDate;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;

public class TableViewEditingApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TableView<Person> table = new TableView<>(PersonTableUtil.getPersonList());
		
		//
		table.setEditable(true);
		
		
		addIdColumn(table);
		addFirstNameColumn(table);
		addLastNameColumn(table);
		addBirthDateColumn(table);
		addBabyColumn(table);
		addGenderColumn(table);
		
		HBox root = new HBox(table);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	public void addIdColumn(TableView<Person> table){
		table.getColumns().add(PersonTableUtil.getIdColumn());
	}
	
	
	public void addFirstNameColumn(TableView<Person>  table){
		TableColumn<Person, String> fNameCol = PersonTableUtil.getFirstNameColumn();
		
		fNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		table.getColumns().add(fNameCol);
	}
	
	public void addLastNameColumn(TableView<Person> table){
		TableColumn<Person, String> lNameCol = PersonTableUtil.getLastNameColumn();
		
		lNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		table.getColumns().add(lNameCol);
	}
	
	public void addBirthDateColumn(TableView<Person> table){
		TableColumn<Person, LocalDate> birthDateCol = PersonTableUtil.getBirthDateColumn();
		
		//
		LocalDateStringConverter converter = new LocalDateStringConverter();
		birthDateCol.setCellFactory(TextFieldTableCell.forTableColumn(converter));
		
		table.getColumns().add(birthDateCol);
	}
	
	public void addBabyColumn(TableView<Person> table){
		TableColumn<Person, Boolean> babyCol = new TableColumn<>(Msg.get(this, "column.baby.header"));
		babyCol.setEditable(false);
		
		//
		babyCol.setCellValueFactory(cellData -> {
			Person p = cellData.getValue();
			Boolean v = Person.AgeCategory.BABY == p.getAgeCategory();
			return new ReadOnlyBooleanWrapper(v);
		});
		
		babyCol.setCellFactory(CheckBoxTableCell.forTableColumn(babyCol));
		table.getColumns().add(babyCol);
	}
	
	//
	public void addGenderColumn(TableView<Person> table){
		TableColumn<Person, String> genderCol = new TableColumn<>(Msg.get(this, "column.gender.header"));
		genderCol.setMinWidth(80);
		
		//
		genderCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(null));
		
		
		//
		genderCol.setCellFactory(ComboBoxTableCell.forTableColumn(
				FXCollections.observableList(Msg.getList(this, "column.gender.values"))));
		
		genderCol.setOnEditCommit( e -> {
			int row = e.getTablePosition().getRow();
			Person p = e.getRowValue();
			out.printf(Msg.get(this, "msg.gender"), 
					p.getFirstName(),
					p.getLastName(),
					row + 1,
					e.getNewValue()
					);
		});
		
		table.getColumns().add(genderCol);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static final PrintStream out = System.out;
	
}

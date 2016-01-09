package com.mec.fx;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TableViewDataApp extends Application {

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {
		TableView<Person> table = new TableView<>(PersonTableUtil.getPersonList());
		
		//
		TableColumn<Person, String> ageCol = new TableColumn<>(Msg.get(this, "column.age"));
		ageCol.setCellValueFactory(cellData -> {
			Person p = cellData.getValue();
			LocalDate dob = p.getBirthDate();
			String ageInYear = Msg.get(this, "age.unknown");
			if(null != dob){
				long years = ChronoUnit.YEARS.between(dob, LocalDate.now());
				if(0 == years){
					ageInYear = Msg.get(this, "age.type1");
				}else if(1 == years){
					ageInYear = String.format(Msg.get(this, "age.type2"), years);
				}else{
					ageInYear = String.format(Msg.get(this, "age.type3"), years);
				}
			}
			
			
			return new ReadOnlyStringWrapper(ageInYear);
		});
		
		
		//Create an "Age Category" column
		TableColumn<Person, Person.AgeCategory> ageCategoryCol = new TableColumn<>(Msg.get(this, "column.ageCategory.header"));
		ageCategoryCol.setCellValueFactory(new PropertyValueFactory<>(Msg.get(this, "column.ageCategory.property")));
		
		//Add columns to teh Table View
		table.getColumns().addAll(PersonTableUtil.getIdColumn(), 
				PersonTableUtil.getFirstNameColumn(),
				PersonTableUtil.getLastNameColumn(),
				PersonTableUtil.getBirthDateColumn(),
				ageCol,
				ageCategoryCol
				);
		
		
		HBox root = new HBox(table);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

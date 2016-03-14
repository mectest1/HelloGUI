package com.mec.fx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TableViewCellFactoryApp extends Application {

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {
		TableView<Person> table = new TableView<>(PersonTableUtil.getPersonList());
		
		//Create the birth date column
		TableColumn<Person, LocalDate> birthDateCol = PersonTableUtil.getBirthDateColumn();
		
		//Set a custom cell factory for Birth Date column
		birthDateCol.setCellFactory(col -> {
			TableCell<Person, LocalDate> cell = new TableCell<Person, LocalDate>(){

				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					
					//Clear the cell before populating it
					this.setText(null);
					this.setGraphic(null);
					
					if(!empty){
						String formattedDob = DateTimeFormatter.ofPattern(Msg.get(TableViewCellFactoryApp.class, "date.pattern")).format(item);
						this.setText(formattedDob);
					}
				}
				
			};
			
			return cell;
		});
	
		//Create and configure the baby column
		TableColumn<Person, Boolean> babyCol = new TableColumn<>(Msg.get(this, "column.baby.header"));
		babyCol.setCellValueFactory(cellDate -> {
			Person p = cellDate.getValue();
			Boolean v = Person.AgeCategory.BABY == p.getAgeCategory();
			return new ReadOnlyBooleanWrapper(v);
		});
		
		//Set a custom cell factory for the baby column
		babyCol.setCellFactory(CheckBoxTableCell.forTableColumn(babyCol));
		
		//Add columns to thetable
		table.getColumns().addAll(PersonTableUtil.getIdColumn(), 
				PersonTableUtil.getFirstNameColumn(),
				PersonTableUtil.getLastNameColumn(),
				birthDateCol,
				babyCol
				);
		
		HBox root = new HBox(table);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));;
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

package com.mec.fx;

import java.time.LocalDate;
import java.time.format.FormatStyle;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class CustomTableCellApp extends Application {

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {
		TableView<Person> table = new TableView<>(PersonTableUtil.getPersonList());
		
		//Make sure the TableView is editable
		table.setEditable(true);
		
		//Set up the Brith Date column to use DatePickerTableCell
		TableColumn<Person, LocalDate> birthDateCol = PersonTableUtil.getBirthDateColumn();
//		StringConverter<LocalDate> converter = new LocalDateStringConverter(
//				FormatStyle.MEDIUM 
//				, DateTimeFormatter.ofPattern(Msg.get(this, "date.format"))
//				);
		StringConverter<LocalDate> converter = new LocalDateStringConverter(FormatStyle.MEDIUM);
		birthDateCol.setCellFactory(DatePickerTableCell.forTableColumn(converter, false));
		table.getColumns().addAll(PersonTableUtil.getIdColumn(), 
				PersonTableUtil.getFirstNameColumn(), 
				PersonTableUtil.getLastNameColumn(), 
				birthDateCol
				);
		
		HBox root = new HBox(table);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

























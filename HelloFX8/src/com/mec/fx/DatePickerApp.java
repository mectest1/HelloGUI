package com.mec.fx;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class DatePickerApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		DatePicker datePicker = new DatePicker();
		
		//
		datePicker.setOnAction(event -> {
			LocalDate date = datePicker.getValue();
			out.printf("Selected date: %s\n", date);
		});
		datePicker.setConverter(new StringConverter<LocalDate>(){

			@Override
			public String toString(LocalDate date) {
				if(null != date){
					out.println(dateFormatterStr.get());
					return DateTimeFormatter.ofPattern(dateFormatterStr.get()).format(date); 
				}
				return "";
			}

			@Override
			public LocalDate fromString(String string) {
				if(null == string || string.isEmpty()){
					return null;
				}
//				return DateTimeFormatter.ofPattern(dateFormatterStr.get()).parse(string);
				return LocalDate.parse(string, DateTimeFormatter.ofPattern(dateFormatterStr.get()));
			}
			
		});
		
		//
//		StackPane root = new StackPane();
		VBox root = new VBox(5);
		HBox dateRow = new HBox(5);
//		AnchorPane root = new AnchorPane();
//		root.getChildren().add(dateRow);
		
		Scene scene= new Scene(root, Msg.get(this, "scene.width", Integer::parseInt, 300), Msg.get(this, "scene.height", Integer::parseInt, 300));
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		CheckBox showWeekNumbers = new CheckBox(Msg.get(this, "showWeekNumbers"));
		datePicker.showWeekNumbersProperty().bind(showWeekNumbers.selectedProperty());
		dateRow.getChildren().addAll(datePicker, showWeekNumbers);
		//-> The same effect;
//		showWeekNumbers.setOnAction( e -> datePicker.setShowWeekNumbers(!datePicker.isShowWeekNumbers()));
		

		//Date formatter now
		HBox dateFormatterRow = new HBox(5);
//		ChoiceBox formatter1 = new ChoiceBox()
		ToggleGroup formatterGroup = new ToggleGroup();
		formatterGroup.selectedToggleProperty().addListener((Observable, oldVal, newVal) -> {
//			out.println(newVal);
			dateFormatterStr.set(((RadioButton) newVal).getText());
//			out.println(dateFormatterStr.get());
//			datePicker.setValue(datePicker.getValue());
			datePicker.getEditor().setText(datePicker.getConverter().toString(datePicker.getValue()));		//<- too ugly;
		});
//		dateFormatterStr.bind(formatterGroup.selectedToggleProperty().get);
	
		RadioButton formatter1 = new RadioButton(Msg.get(this, "formatter.1"));
		formatter1.setToggleGroup(formatterGroup);
//		formatter1.setOnAction(e -> {
//			if(formatter1.isSelected()){
////				dateFormatterRow.set(formatter1.getText());
//				dateFormatterStr.set(formatter1.getText());
//			}
//		});
		formatter1.setSelected(true);
		
		RadioButton formatter2 = new RadioButton(Msg.get(this,"formatter.2"));
		formatter2.setToggleGroup(formatterGroup);
//		formatter2.setOnAction(e -> {
//			if(formatter2.isSelected()){
////				dateFormatterRow.set(formatter1.getText());
//				dateFormatterStr.set(formatter2.getText());
//			}
//		});
		
		dateFormatterRow.getChildren().addAll(formatter1, formatter2);
		
		
		//
		root.getChildren().addAll(dateFormatterRow, dateRow);
//		dateRow.getChildren().addAll(root);
	}
	
	
	
	public static void main(String[] args) {
		Application.launch(DatePickerApp.class);
	}
	
	
	
	private static PrintStream out = System.out;
	private StringProperty dateFormatterStr = new SimpleStringProperty();

}



/**
 * References:
 * [1] http://code.makery.ch/blog/javafx-8-date-picker/
 * [2] 
 */

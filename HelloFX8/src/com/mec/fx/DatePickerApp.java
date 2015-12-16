package com.mec.fx;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.IsoChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.MinguoChronology;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
		
		VBox root = new VBox(5);
		HBox dateRow = new HBox(5);
		
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
			dateFormatterStr.set(((RadioButton) newVal).getText());
			datePicker.getEditor().setText(datePicker.getConverter().toString(datePicker.getValue()));		//<- too ugly;
		});
	
		RadioButton formatter1 = new RadioButton(Msg.get(this, "formatter.1"));
		formatter1.setToggleGroup(formatterGroup);
		formatter1.setSelected(true);
		
		RadioButton formatter2 = new RadioButton(Msg.get(this,"formatter.2"));
		formatter2.setToggleGroup(formatterGroup);
		
		//Chronology list
		ChoiceBox<Chronology> chronologyList = new ChoiceBox<>(FXCollections.observableArrayList(
				 IsoChronology.INSTANCE
				,JapaneseChronology.INSTANCE
				,HijrahChronology.INSTANCE
				,MinguoChronology.INSTANCE
				,ThaiBuddhistChronology.INSTANCE
				));
		chronologyList.setOnAction(action -> {
			Chronology selectedChrono = chronologyList.getSelectionModel().getSelectedItem();
			datePicker.setChronology(selectedChrono);
//			formatterGroup.selectToggle(null);
//			dateFormatterStr.set(selectedChrono.);
		});
		chronologyList.getSelectionModel().select(IsoChronology.INSTANCE);
		HBox chronologyBar = new HBox(5);
		chronologyBar.getChildren().add(chronologyList);
		
		dateFormatterRow.getChildren().addAll(formatter1, formatter2);
		
		
		//
		root.getChildren().addAll(dateFormatterRow, chronologyBar, dateRow);
		primaryStage.sizeToScene();
	}
	
	
	
//	public static void main(String[] args) {
//		Application.launch(DatePickerApp.class);
//	}
	
	
	
	private static PrintStream out = System.out;
	private StringProperty dateFormatterStr = new SimpleStringProperty();

}



/**
 * References:
 * [1] http://code.makery.ch/blog/javafx-8-date-picker/
 * [2] 
 */

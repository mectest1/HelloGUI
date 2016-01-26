package com.mec.fx;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class DatePickerApp2 extends Application{

	TextArea info = new TextArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		DatePicker birthDate = new DatePicker();
		birthDate.setEditable(false);
		
		//Print the new date in the info panel
		birthDate.setOnAction(e -> info.appendText(String.format(Msg.get(this, "info.format"), birthDate.getValue())));
		
		birthDate.setConverter(new LocalDateStringConverter(Msg.get(this, "date.pattern")));
		birthDate.setPromptText(Msg.get(this, "date.pattern"));

		//Create the cell factory
		Callback<DatePicker, DateCell> dayCellFactory = datePicker -> {
			return new DateCell(){

				@Override
				public void updateItem(LocalDate item, boolean empty) {
					//Muse call super
					super.updateItem(item, empty);
					
					//Disable all future date cells
					if(item.isAfter(LocalDate.now())){
						setDisable(true);
					}
					//Show weekends in blue
					DayOfWeek day = DayOfWeek.from(item);
					if(DayOfWeek.SUNDAY == day || DayOfWeek.SATURDAY == day){
						setTextFill(Color.valueOf(Msg.get(DatePickerApp2.class, "weekend.backgroundColor")));
					}
				}
				
			};
			
			
		};
		birthDate.setDayCellFactory(dayCellFactory);
		
		//
//		HBox root = new HBox(10, new Label(Msg.get(this, "label.birthDate"))
		HBox dateRow = new HBox(10, new Label(Msg.get(this, "label.birthDate")), birthDate);
//		dateRow.setPrefWidth(Double.MAX_VALUE);
//		dateRow.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(birthDate, Priority.SOMETIMES);
		VBox root = new VBox(10, 
				dateRow
				, info
				);
		VBox.setVgrow(info, Priority.SOMETIMES);
		
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	

	
	
	static class LocalDateStringConverter extends StringConverter<LocalDate>{

		private DateTimeFormatter dtFormatter;	// = DateTimeFormatter.ofPattern(Msg.get(DatePickerApp.class, "date.patter"));
		
		public LocalDateStringConverter(){
			dtFormatter = DateTimeFormatter.ofPattern(Msg.get(DatePickerApp.class, "date.patter"));
		}
		
		public LocalDateStringConverter(String datePattern){
			dtFormatter = DateTimeFormatter.ofPattern(datePattern);
		}
		
		
		
		@Override
		public String toString(LocalDate object) {
			String retval = null;
			if(null !=object){
				retval = dtFormatter.format(object);
			}
			return retval;
		}

		@Override
		public LocalDate fromString(String string) {
			LocalDate retval = null;
			if(null != string && !string.trim().isEmpty()){
				retval = LocalDate.parse(string, dtFormatter);
			}
			
			return retval;
		}
		
		
	}
}

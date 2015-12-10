package com.mec.fx;

import java.io.PrintStream;
import java.time.LocalDate;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DatePickerApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		DatePicker datePicker = new DatePicker();
		
		//
		datePicker.setOnAction(event -> {
			LocalDate date = datePicker.getValue();
			out.printf("Selected date: %s\n", date);
		});
		
		//
		StackPane root = new StackPane();
//		AnchorPane root = new AnchorPane();
		root.getChildren().add(datePicker);
		Scene scene= new Scene(root, Msg.get(this, "scene.width", Integer::parseInt, 300), Msg.get(this, "scene.height", Integer::parseInt, 300));
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
//		CheckBox b1 = new CheckBox(Msg.get(this, "showWeekNumbers"));
//		datePicker.showWeekNumbersProperty().bind(b1.selectedProperty());
//		Group hideMonth = new Group(b1);
//		root.getChildren().add(hideMonth);
	}
	
	
	
	public static void main(String[] args) {
		Application.launch(DatePickerApp.class);
	}
	
	
	
	private static PrintStream out = System.out;
	
	

}

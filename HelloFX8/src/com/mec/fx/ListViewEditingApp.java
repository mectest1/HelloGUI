package com.mec.fx;

import java.util.Arrays;

import com.mec.fx.ComboBoxWithConverterApp.PersonStringConverter;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ListViewEditingApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ListView<String> breakfasts = getBreakfastListView();
		ListView<Person> persons = getPersonListView();
		
		//
		GridPane root = new GridPane();
		root.setHgap(20);
		root.setVgap(10);
		root.add(new Label(Msg.get(this, "label.edit")), 0, 0, 2, 1);
		root.addRow(1, new Label(Msg.get(this, "label.person")), new Label(Msg.get(this, "label.breakfast")));
		root.addRow(2, persons, breakfasts);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	ListView<Person> getPersonListView(){
		ListView<Person> persons = new ListView<>();
		persons.setPrefSize(200, 120);
		persons.setEditable(true);
		Msg.getList(ListViewDomainObjectApp.class, "person").forEach(s -> {
			String[] fields = s.split(Msg.get(ListViewDomainObjectApp.class, "person.delimiter"));
			persons.getItems().add(new Person(fields));
		});
		
		//Set a TextField cell factory to edit the Person items. Also 
		//use a StringConverer to convert a String ot a Person and vice-versa
		persons.setCellFactory(TextFieldListCell.forListView(new PersonStringConverter()));
		
		return persons;
	}

	
	ListView<String> getBreakfastListView(){
		ListView<String> breakfasts = new ListView<>();
		breakfasts.setEditable(true);
		breakfasts.setPrefSize(200, 120);
		Arrays.asList(Msg.get(this, "breakfasts").split(Msg.get(this, "breakfast.delimiter")))
			.forEach(b -> breakfasts.getItems().add(b.trim()));
		
		//Set a TextField cell factory to edit the String items
		breakfasts.setCellFactory(TextFieldListCell.forListView());

		return breakfasts;
	}
}

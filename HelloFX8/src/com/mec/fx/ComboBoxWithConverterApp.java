package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class ComboBoxWithConverterApp extends Application {

	Label userSelectionMsgLbl = new Label(Msg.get(this, "label.selection"));
	Label userSelectionDataLbl =new Label("");
	TextArea msgInfo = new TextArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Label personLbl = new Label(Msg.get(this, "label.selectEnter"));
		
		ComboBox<Person> persons = new ComboBox<>();
//		persons.setEditable(true);							//Seems this line conflicts with the "converter" property, Why?
		persons.setConverter(new PersonStringConverter());	//<- Children: duplicate children added: parent = ComboBox
		Msg.getList(this, "person").stream().forEach(s -> {
			String[] fields = s.split(Msg.get(this, "person.delimiter"));
			persons.getItems().add(new Person(fields));
		});
		
//		persons.setConverter(new PersonStringConverter());
		
		
		//Add ChagneListeners to the selectedItem and selectedIndex
		//properties of the selection model
		persons.getSelectionModel().selectedIndexProperty().addListener(this::indexChanged);
		persons.getSelectionModel().selectedItemProperty().addListener(this::itemChanged);
		
		//Update the message label when the value changes
		persons.setOnAction(e -> valueChanged(persons));
		
		//
		GridPane root = new GridPane();
		root.addRow(0, personLbl, persons);
		root.addRow(1, userSelectionMsgLbl, userSelectionDataLbl);
		GridPane.setColumnSpan(msgInfo, 2);
		root.addRow(2, msgInfo);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

	void valueChanged(ComboBox<Person> list){
		Person p = list.getValue();
		String name = String.join(Msg.get(this, "person.delimier"), p.getFirstName(), p.getLastName());
		userSelectionDataLbl.setText(name);
	}
	
	void itemChanged(ObservableValue<? extends Person> observable, Person oldVal, Person newVal){
		msgInfo.appendText(String.format(Msg.get(this, "msg.item"), oldVal, newVal));
	}
	
	void indexChanged(ObservableValue<? extends Number> observable, Number oldVal, Number newVal){
		msgInfo.appendText(String.format(Msg.get(this, "msg.index"), oldVal, newVal));
	}
	
	
	public static class PersonStringConverter extends StringConverter<Person>{

		@Override
		public String toString(Person object) {
			Person p = (Person) object;
			return String.join(Msg.get(ComboBoxWithConverterApp.class, "person.delimiter"), 
					p.getFirstName(),
					p.getLastName()
					);
		}

		@Override
		public Person fromString(String string) {
			String[] fields = string.split(Msg.get(ComboBoxWithConverterApp.class, "person.delimiter"));
			Person p = new Person(fields);
			return p;
		}
		
	}
}

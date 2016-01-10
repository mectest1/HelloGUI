package com.mec.fx;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.mec.resources.Msg;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class PersonTableUtil {

	
	public static ObservableList<Person> getPersonList(){
		List<Person> personList =  Msg.getList(PersonTableUtil.class, "person")
				.stream().map(s ->{
					String[] fields = s.split(Msg.get(PersonTableUtil.class, "person.delimiter"));
					return new Person(fields[0].trim(), 
							fields[1].trim(), 
							LocalDate.of(Integer.parseInt(fields[2].trim()), 
									Integer.parseInt(fields[3].trim()), 
									Integer.parseInt(fields[4].trim()))
							);
					
					}
				).collect(Collectors.toList());
		return FXCollections.observableList(personList);
	}
	
	
	//Return Person Id TableColumn
	public static TableColumn<Person, Integer> getIdColumn(){
		TableColumn<Person, Integer> personIdCol = new TableColumn<>(Msg.get(PersonTableUtil.class, "id.header"));
		personIdCol.setCellValueFactory(new PropertyValueFactory<>(Msg.get(PersonTableUtil.class, "id.property")));
		return personIdCol;
	}
	
	public static TableColumn<Person, String> getFirstNameColumn(){
		TableColumn<Person, String> fNameCol = new TableColumn<>(Msg.get(PersonTableUtil.class, "firstName.header"));
		fNameCol.setCellValueFactory(new PropertyValueFactory<>(Msg.get(PersonTableUtil.class, "firstName.property")));
		return fNameCol;
	}
	
	public static TableColumn<Person, String> getLastNameColumn(){
		TableColumn<Person, String> lNameCol = new TableColumn<>(Msg.get(PersonTableUtil.class, "lastName.header"));
		lNameCol.setCellValueFactory(new PropertyValueFactory<>(Msg.get(PersonTableUtil.class, "lastName.property")));
		return lNameCol;
	}
	
	public static TableColumn<Person, LocalDate> getBirthDateColumn(){
		TableColumn<Person, LocalDate> lNameCol = new TableColumn<>(Msg.get(PersonTableUtil.class, "birthDate.header"));
		lNameCol.setCellValueFactory(new PropertyValueFactory<>(Msg.get(PersonTableUtil.class, "birthDate.property")));
		return lNameCol;
	}
	
	public static TableColumn<Person, CheckBox> getSelectColumn(){
		TableColumn<Person, CheckBox> selectCol = new TableColumn<>();
		selectCol.setCellValueFactory(cellData -> new SimpleObjectProperty<CheckBox>(new CheckBox()));
		return selectCol;
	}
	
}

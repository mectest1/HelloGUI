package com.mec.fx.views;

import com.mec.fx.PersonInfoViewer;
import com.mec.fx.beans.Person;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PersonOverviewController {
	@FXML
	private TableView<Person> personTable;
	
	@FXML
	private TableColumn<Person, String> firstNameColumn;

	@FXML
	private TableColumn<Person, String> lastNameColumn;
	
	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label birhtdayLabel;
	
	//Reference to the main application
	private PersonInfoViewer personInfoViewer;
	
	/**
	 * This method is automatically called after the fxml file has been loaded
	 */
	@FXML
	private void initialize(){
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
	}

	public void setPersonInfoViewer(PersonInfoViewer personInfoViewer) {
		this.personInfoViewer = personInfoViewer;
		
		personTable.setItems(personInfoViewer.getPersonData());
	}
}

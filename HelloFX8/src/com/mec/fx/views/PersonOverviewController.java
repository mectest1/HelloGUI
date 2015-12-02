package com.mec.fx.views;

import java.time.LocalDate;

import com.mec.fx.PersonInfoViewer;
import com.mec.fx.beans.DateUtil;
import com.mec.fx.beans.Person;
import com.mec.resources.Msg;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	@FXML	//Append this annotation or this method will not be invoked;
	private void initialize(){	//initialize method name defined in FXMLLoader;
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		
		//
		showPersonDetails(null);
		
		//
		personTable.getSelectionModel().selectedItemProperty()
			.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
		
		
		
	}

	public void setPersonInfoViewer(PersonInfoViewer personInfoViewer) {
		this.personInfoViewer = personInfoViewer;
		
		personTable.setItems(personInfoViewer.getPersonData());
	}
	
	private void showPersonDetails(Person person){
		if(null != person){
			//fill the labels with info from the person object
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			streetLabel.setText(person.getStreet());
			postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
			cityLabel.setText(person.getCity());
			//As for the birthdayLabel, we need a way to convert it automatically.
			birhtdayLabel.setText(DateUtil.format(person.getBirthday()));
		}else{
			firstNameLabel.setText("");
			lastNameLabel.setText("");
			streetLabel.setText("");
			cityLabel.setText("");
			postalCodeLabel.setText("");
			birhtdayLabel.setText("");
		}
	}
	
	@FXML
	private void handleDeletePerson(){
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		if(-1 < selectedIndex){
			personTable.getItems().remove(selectedIndex);
		}else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(this.personInfoViewer.getPrimaryStage());
			alert.setTitle(Msg.get(this, "deleteBtn.alert.title"));
			alert.setHeaderText(Msg.get(this, "deleteBtn.alert.headerText"));
			alert.setContentText(Msg.get(this, "deleteBtn.alert.contentText"));
			
			alert.showAndWait();
		}
	}
	
	
	@FXML
	private void handleEditPerson(){
		Person person = personTable.getSelectionModel().getSelectedItem();
		if(null == person){
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(this.personInfoViewer.getPrimaryStage());
			alert.setTitle(Msg.get(this, "editBtn.alert.title"));
			alert.setHeaderText(Msg.get(this, "editBtn.alert.header"));
			alert.setContentText(Msg.get(this, "editBtn.alert.content"));
			
			alert.showAndWait();
			return;
		}
		boolean isOK = this.personInfoViewer.showPersonEditDialog(person);
		if(isOK){
			showPersonDetails(person);
			personTable.getSelectionModel().select(person);
		}
	}
	
	@FXML
	private void handleNewPerson(){
		Person newPerson = new Person();
//		newPerson.setBirthday(LocalDate.of(1900, 1, 1));
		boolean isOK = this.personInfoViewer.showPersonEditDialog(newPerson);
		if(isOK){
			personInfoViewer.getPersonData().add(newPerson);
			personTable.getSelectionModel().select(newPerson);
		}
	}
}

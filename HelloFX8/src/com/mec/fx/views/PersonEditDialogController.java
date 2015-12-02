package com.mec.fx.views;

import com.mec.fx.beans.DateUtil;
import com.mec.fx.beans.Person;
import com.mec.resources.Msg;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PersonEditDialogController {

	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField streetField;
	@FXML
	private TextField postalCodeField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField birthdayField;
	
	private Stage dialogStage;
	private Person person;
	private boolean okClicked = false;
	
	@FXML
	private void initiailzed(){
		
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setPerson(Person person) {
		this.person = person;
		
		firstNameField.setText(person.getFirstName());
		lastNameField.setText(person.getLastName());
		streetField.setText(person.getStreet());
		postalCodeField.setText(Integer.toString(person.getPostalCode()));
		cityField.setText(person.getCity());
		birthdayField.setText(DateUtil.format(person.getBirthday()));
		birthdayField.setPromptText(DateUtil.DATE_PATTERN);
	}
	
	@FXML
	private void onOK(){
		if(!isInputValid()){
			return;
		}
		
		person.setFirstName(firstNameField.getText());
		person.setLastName(lastNameField.getText());
		person.setStreet(streetField.getText());
		person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
		person.setCity(cityField.getText());
		person.setBirthday(DateUtil.parse(birthdayField.getText()));
		
		okClicked = true;
		dialogStage.close();
	}
	
	@FXML
	private void onCancel(){
//		okClicked = false;
		dialogStage.close();
	}
	
	private boolean isInputValid(){
		StringBuilder errorMessage = new StringBuilder();
		
		if(!isTextFieldValid(firstNameField)){
			errorMessage.append(Msg.get(this, "errorMessage.invalid.firstName"));
		}
		if(!isTextFieldValid(lastNameField)){
			errorMessage.append(Msg.get(this, "errorMessage.invalid.lastName"));
		}
		if(!isTextFieldValid(this.cityField)){
			errorMessage.append(Msg.get(this, "errorMessage.invalid.city"));
		}
		if(!isTextFieldValid(this.streetField)){
			errorMessage.append(Msg.get(this, "errorMessage.invalid.street"));
		}
		if(!isTextFieldValid(this.postalCodeField)){
			errorMessage.append(Msg.get(this, "errorMessage.invalid.postalCode"));
		}else{
			try{
				Integer.parseInt(this.postalCodeField.getText());
			}catch(NumberFormatException e){
				errorMessage.append(Msg.get(this, "errorMessage.invalid.postalCode.formatError"));
			}
		}
		if(!isTextFieldValid(this.birthdayField)){
			errorMessage.append(Msg.get(this, "errorMessage.invalid.birthday"));
		}else{
			if(!DateUtil.validDate(birthdayField.getText())){
				errorMessage.append(String.format(Msg.get(this, "errorMessage.invalid.birthday.formatError"), DateUtil.DATE_PATTERN));
			}
		}
		
		if(0 >= errorMessage.length()){
			return true;
		}else{
			//Show the error message
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle(Msg.get(this, "errorMessage.alert.title"));
			alert.setHeaderText(Msg.get(this, "errorMessage.alert.header"));
			alert.setContentText(errorMessage.toString());
			
			alert.showAndWait();
			return false;
		}
	}
	
	private boolean isTextFieldValid(TextField textField){
		String txt = textField.getText();
		if(null ==  txt|| txt.isEmpty()){
			return false;
		}
		return true;
	}
	
//	public void derp(){	//<- public method can be recognized by the SceneBuilder && in the FXML.
//		
//	}
}

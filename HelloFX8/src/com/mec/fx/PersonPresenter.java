package com.mec.fx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import com.mec.resources.Msg;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class PersonPresenter {
	private final Person model;
	private final PersonView view;
	
	public PersonPresenter(Person model, PersonView view){
		this.model = model;
		this.view = view;
		attachEvents();
	}
	
	private void attachEvents(){
		//
		view.bDateFld.setOnAction(e -> handleBirthDateChange());
		view.bDateFld.getScene().focusOwnerProperty().addListener(this::focusChanged);
		
		//Save the data
		view.saveBtn.setOnAction(e -> saveDate());
		

		//
		view.closeBtn.setOnAction(e -> view.getScene().getWindow().hide());
	}
	
	
	public void focusChanged(ObservableValue<? extends Node> value, 
			Node oldNode, Node newNode
			){
		//The birth date field has lost focus
		if(view.bDateFld == oldNode){
			handleBirthDateChange();
		}
	}
	
	private void handleBirthDateChange(){
		String bdateStr = view.bDateFld.getText();
		if(null == bdateStr || bdateStr.isEmpty()){
			model.setBirthDate(null);
			view.syncBirthDate();
		}else{
			try{
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(view.dateFormat);
				LocalDate bdate = LocalDate.parse(bdateStr, formatter);
						
				List<String> errorList= new ArrayList<>();
				if(model.isValidBirthDate(bdate, errorList)){
					model.setBirthDate(bdate);
					view.syncAgeCategory();
				}else{
					this.showError(errorList);
					view.syncBirthDate();
				}
			}catch(DateTimeParseException e){
				//Birth date is not in the specified date format
				List<String> errorList = new ArrayList<>();
				errorList.add(String.format(Msg.get(this, "error.dateParse"), view.dateFormat.toLowerCase()));
				this.showError(errorList);
				
				//Refresh the view
				view.syncBirthDate();
				
			}
		}
	}
	
	//
	private void saveDate(){
		List<String> errorList = new ArrayList<>();
		boolean isSaved = model.save(errorList);
		if(!isSaved){
			this.showError(errorList);
		}
	}
	
	public void showError(List<String> errorList){
		String msg = "";
		if(errorList.isEmpty()){
			msg = Msg.get(this, "error.empty");
		}else{
			msg = errorList.stream().reduce((r, s) -> r + Msg.get(this, "error.join") + s).get();
		}
		
		
		Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
		alert.setHeaderText(Msg.get(this, "error.header"));
		alert.setTitle(Msg.get(this, "error.title"));
		alert.showAndWait();
	}
}





































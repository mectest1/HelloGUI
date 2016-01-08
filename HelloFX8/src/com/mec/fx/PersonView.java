package com.mec.fx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.mec.resources.Msg;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class PersonView extends GridPane {

	private final Person model;
	
	//Labels
	Label personIdLbl = new Label(Msg.get(this, "personId"));
	Label fNameLbl = new Label(Msg.get(this, "firstName"));
	Label lNameLbl = new Label(Msg.get(this, "lastName"));
	Label bDateLbl = new Label(Msg.get(this, "birthDate"));
	Label ageCategoryLbl = new Label(Msg.get(this, "ageCategory"));
	
	//Fields:
	TextField personIdFld = new TextField();
	TextField fNameFld = new TextField();
	TextField lNameFld = new TextField();
	TextField bDateFld = new TextField();
	TextField ageCategoryFld = new TextField();
	
	
	//Butons 
	Button saveBtn = new Button(Msg.get(this, "button.save"));
	Button closeBtn = new Button(Msg.get(this, "button.close"));
	
	//Date format
	String dateFormat;
	
	public PersonView(Person model, String dateFormat){
		this.model = model;
		this.dateFormat = dateFormat;
		layoutForm();
		initFieldData();
		bindFieldsToModel();
	}
	
	
	private void initFieldData(){
		//Id and names are populated using bindings
		//populate birth date and age category
		syncBirthDate();
	}
	
	private void layoutForm(){
		this.setHgap(5);
		this.setVgap(5);
		
		//
		this.addColumn(1, personIdLbl, fNameLbl, lNameLbl, bDateLbl, ageCategoryLbl);
		this.addColumn(2, personIdFld, fNameFld, lNameFld, bDateFld, ageCategoryFld);
		
		//Add buttons and make them the same width
		VBox buttonBox = new VBox(saveBtn, closeBtn);
		saveBtn.setMaxWidth(Double.MAX_VALUE);
		closeBtn.setMaxWidth(Double.MAX_VALUE);
		
		this.add(buttonBox, 3, 1, 1, 5);
		
		//Disable the persoId field
		personIdFld.setDisable(true);
		ageCategoryFld.setDisable(true);
		
		//Set the prompt text for the birth date field
		bDateFld.setPromptText(dateFormat.toLowerCase());
	}
	

	public void bindFieldsToModel(){
		personIdFld.textProperty().bind(model.personIdProperty().asString());
		fNameFld.textProperty().bindBidirectional(model.firstNameProperty());;
		lNameFld.textProperty().bindBidirectional(model.lastNameProperty());
	}
	
	public void syncBirthDate(){
		LocalDate bdate = model.getBirthDate();
		if(null != bdate){
			bDateFld.setText(bdate.format(DateTimeFormatter.ofPattern(dateFormat)));
		}
		syncAgeCategory();
	}
	
	public void syncAgeCategory(){
		ageCategoryFld.setText(model.getAgeCategory().toString());
	}
}

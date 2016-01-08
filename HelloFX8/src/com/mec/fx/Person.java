package com.mec.fx;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mec.resources.Msg;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person{
	
	
	public enum AgeCategory{
		BABY, CHILD, TEEN, ADULT, SENIOR, UNKNOWN
	}
	
	private final ReadOnlyIntegerWrapper personId = new ReadOnlyIntegerWrapper(this, "personId", personSequence.incrementAndGet());
	private final StringProperty firstName = new SimpleStringProperty(this, "firstName", null);
	private final StringProperty lastName = new SimpleStringProperty(this, "lastName", null);
	private final ObjectProperty<LocalDate> birthDate = new SimpleObjectProperty<>(this, "birthDate", null);
			
	//Keeps track of last generated person id
	private static AtomicInteger personSequence = new AtomicInteger(0);
	
	public Person(){
		this(null, null, null);
	}
	
	public Person(String firstName, String lastName, LocalDate birthDate){
		this.firstName.set(firstName);
		this.lastName.set(lastName);
		this.birthDate.set(birthDate);
	}
	
	//personId property
	public final int getPersonId(){
		return personId.get();
	}
	
	public final ReadOnlyIntegerProperty personIdProperty(){
		return personId.getReadOnlyProperty();
	}
	
	//firstZName Property
	public final String getFirstName(){
		return firstName.get();
	}
	
	public final void setFirstName(String firstName){
		firstNameProperty().set(firstName);
	}
	
	public final StringProperty firstNameProperty(){
		return firstName;
	}
	
	//lastName property
	public final String getLastName(){
		return lastName.get();
	}
	
	public final void setLastName(String lastName){
		lastNameProperty().set(lastName);
	}
	public final StringProperty lastNameProperty(){
		return lastName;
	}
	
	
	//birthDate Property
	public final LocalDate getBirthDate(){
		return birthDate.get();
	}
	
	public final void setBirthDate(LocalDate birthDate){
		birthDateProperty().set(birthDate);
	}
	
	
	public final ObjectProperty<LocalDate> birthDateProperty(){
		return birthDate;
	}
	
	//Domain specific business rules
	public boolean isValidBirthDate(LocalDate bdate){
		return isValidBirthDate(bdate, new ArrayList<>());
	}
	
	public boolean isValidBirthDate(LocalDate bdate, List<String> errorList){
		if(null == bdate){
			return true;
		}
		
		//Birth date cannot be in the future
		if(bdate.isAfter(LocalDate.now())){
//			throw new IllegalArgumentException(Msg.get(this, "error.birthDate"));
			errorList.add(Msg.get(this, "error.birthDate"));
			return false;
		}
		
		return true;
	}
	
	//Domain specific business rules
	public boolean isValidPerson(List<String> errorList){
		return isValidPerson(this, errorList);
	}
	
	public boolean isValidPerson(Person p, List<String> errorList){
		boolean isValid = true;
		String fn = p.firstName.get();
		if(null == fn || fn.trim().isEmpty()){
			errorList.add(Msg.get(this, "error.firstName"));
			isValid = false;
		}
		
		String ln = p.lastNameProperty().get();
		if(null == ln || ln.trim().isEmpty()){
			errorList.add(Msg.get(this, "error.lastName"));
			isValid = false;
		}
		
		if(!isValidBirthDate(this.birthDate.get(), errorList)){
			isValid = false;
		}
		
		return isValid;
	}
	
	
	//Domian specific business rules
	public AgeCategory getAgeCategory(){
		if(null == birthDate.get()){
			return AgeCategory.UNKNOWN;
		}
		
		long years = ChronoUnit.YEARS.between(birthDate.get(), LocalDate.now());
		
		if(0 <= years && years< 2){
			return AgeCategory.BABY;
		}else if(years < 13){
			return AgeCategory.CHILD;
		}else if(years <= 19){
			return AgeCategory.TEEN;
		}else if(years <= 50){
			return AgeCategory.ADULT;
		}else if(50 < years){
			return AgeCategory.SENIOR;
		}else{
			return AgeCategory.UNKNOWN;
		}
		
	}
	
	
	//Domain specific busness rules
	public boolean save(List<String> errorList){
		boolean isSaved = false;
		if(isValidPerson(errorList)){
			out.printf(Msg.get(this, "error.save"), this.toString());
			isSaved = true;
		}
		return isSaved;
	}
	
	@Override
	public String toString(){
		return String.format(Msg.get(this, "pattern.toString"),
					personId.get(), firstName.get(), lastName.get(), birthDate.get());
	}
	
	
	
	
	
	
	
	
	private static final PrintStream out = System.out;
	
	
}

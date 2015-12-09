package com.mec.fx.beans;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person implements Comparable<Person>{

	private final StringProperty firstName;
	private final StringProperty lastName;
	private final StringProperty street;
	private final IntegerProperty postalCode;
	private final StringProperty city;
	private final ObjectProperty<LocalDate> birthday;
	public Person(){
		this("John", "Doe");
	}
	public Person(String firstName, String lastName){
		this(firstName, lastName, "Unknown", -1, "Unknown", LocalDate.of(1900, 1, 1));
	}
	public Person(String firstName, String lastName, String street, int postalCode, String city, LocalDate birthday) {
		super();
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.street = new SimpleStringProperty(street);
		this.postalCode = new SimpleIntegerProperty(postalCode);
		this.city = new SimpleStringProperty(city);
		this.birthday = new SimpleObjectProperty<LocalDate>(birthday);
	}
	public String getFirstName() {
		return firstName.get();
	}
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}
	public String getLastName() {
		return lastName.get();
	}
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}
	public String getStreet() {
		return street.get();
	}
	public void setStreet(String street) {
		this.street.set(street);
	}
	public int getPostalCode() {
		return postalCode.get();
	}
	public void setPostalCode(int postalCode) {
		this.postalCode.set(postalCode);
	}
	public String getCity() {
		return city.get();
	}
	public void setCity(String city) {
		this.city.set(city);
	}
	@XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
	public LocalDate getBirthday() {
		return birthday.get();
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday.set(birthday);
	}
	public StringProperty firstNameProperty(){
		return firstName;
	}
	public StringProperty lastNameProperty(){
		return lastName;
	}
	public IntegerProperty postalCodeProperty(){
		return postalCode;
	}
	public StringProperty streetProperty(){
		return street;
	}
	public StringProperty cityProperty(){
		return city;
	}
	public ObjectProperty<LocalDate> objectProperty(){
		return birthday;
	}
	@Override
	public String toString() {
//		return "Person [firstName=" + firstName.get() + ", lastName=" + lastName.get() + "]";
		return String.format("Person(%s %s)", firstName.get(), lastName.get());
	}
	@Override
	public int compareTo(Person o) {
		//Assum that the first and last name are always not null
		int diff = this.getFirstName().compareTo(o.getFirstName());
		if(0 == diff){
			diff = this.getLastName().compareTo(o.getLastName());
		}
		return diff;
	}
	
}


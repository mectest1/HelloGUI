package com.mec.fx.beans;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Book {
	private StringProperty title = new SimpleStringProperty(this, "title", "Unknown");
	private DoubleProperty price = new SimpleDoubleProperty(this, "price", 0.0);
	private ReadOnlyStringWrapper ISBN = new ReadOnlyStringWrapper(this, "ISBN", "Unknown");
	
	
	
	public Book(String title, double price, String ISBN) {
		this.title.set(title);
		this.price.set(price);
		this.ISBN.set(ISBN);
	}
//	private String title;
//	private String ISBN;
	public String getTitle() {
		return title.get();
	}
	public void setTitle(String title) {
		this.title.set(title);
	}
	public String getISBN() {
		return ISBN.get();
	}
//	private void setISBN(String ISBN) {
//		this.ISBN.set(ISBN);
//	}
	public double getPrice() {
		return price.get();
	}
	public void setPrice(double price) {
		this.price.set(price);
	}
	public ReadOnlyStringProperty ISBNProperty(){
		return ISBN.getReadOnlyProperty();
	}
	public StringProperty titleProperty(){
		return title;
	}
	public DoubleProperty priceProperty(){
		return price;
	}
}

package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

public class PropertyChangeTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Ignore
	@Test
	public void testPropertyChange() {
		IntegerProperty counter = new SimpleIntegerProperty(100);
		
//		counter.addListener(PropertyChangeTest::invlidated);
		counter.addListener(PropertyChangeTest::propertyChanged);
		
		out.println("Before changing the counter value-1");
		counter.set(101);
		out.println("After changing the counter value-1");
		
		out.println("Before changing the counter value-2");
		counter.set(102);
		out.println("After changing the counter value-2");
		
		int value = counter.get();		//<- property value becomes valid again;
		out.printf("Counter value = %d \n", value);
//		out.printf("Counter value = %s \n", value);	//%s would also work
		
		out.println("Before changing the counter value-3");
		counter.set(102);
		out.println("After changing the counter value-3");
		
		out.println("Before changing the counter value-4");
		counter.set(103);
		out.println("After changing the counter value-4");
	}

	@Test
	public void testObjectPropertyChange(){
		Person p = new Person("John", "Smith");
		ObjectProperty<Person> objectProperty = new SimpleObjectProperty<>(p);
		
		objectProperty.addListener(PropertyChangeTest::propertyChanged);
		
//		p.setFirstName("Josh");	//-> Change event not fired;
		
//		p.firstNameProperty().set("Josh");	//-> Change event no fired;
		
		Person p2 = new Person("Josh", "Derpia");
		objectProperty.set(p2);
	}
	
//	public static void invlidated(Observable observable){
//		out.println("Counter is invalidated.");
//	}
	
	public static <T> void propertyChanged(ObservableValue<? extends T> prop, T oldValue, T newValue){
		out.printf("Counter changed: Old = %s, now = %s\n", oldValue, newValue);
	}
	
	
	private static final PrintStream out = System.out;
}

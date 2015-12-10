package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Test;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class MapBindingTest {

	@Test
	public void testMapBinding() {
//		fail("Not yet implemented");
		MapProperty<String, Double> mp1 = new SimpleMapProperty<>(FXCollections.observableHashMap());
		
		//Create an object binding to bind keySalary to the value of the key "Ken";
		ObjectBinding<Double> kenSalary = mp1.valueAt("Ken");
		out.printf("Ken Salary: %s\n", kenSalary.get());
		
		//Bind the size of empty properties of the MapProperty
		//to create a description of the map
		StringProperty initStr = new SimpleStringProperty("Size: ");
		StringProperty desc = new SimpleStringProperty();
		desc.bind(initStr.concat(mp1.sizeProperty())
				.concat(", Empty: ")
				.concat(mp1.emptyProperty())
				.concat(", Map: ")
				.concat(mp1.asString())
				.concat(", Ken Salary: ")
				.concat(kenSalary)
				);
		out.printf("Before mp1.put(): %s\n", desc.get());
		
		
		//Add some entries to mp1
		mp1.put("Ken", 7890.90);
		mp1.put("Jim", 9800.80);
		mp1.put("Lee", 6000.20);
		out.printf("After mp1.put(): %s\n", desc.get());
		
		
		//Create a new MapProperty
		MapProperty<String, Double> mp2 = new SimpleMapProperty<>(FXCollections.observableHashMap());
		
		//Bind the content of mp1 to the content of mp2
		mp1.bindContent(mp2);
		out.println("Called mp1.bindContent(mp2)...");
		
		/*
		 * At this point, you can change the content of mp1. However,
		 * that will defeat the purpose of content binding, because the content of 
		 * mp1 is no longer in sync with teh content of mp2.
		 * DO NOT DO THIS:
		 * mp1.put("k1", 8989.90)
		 */
		
		out.printf("Before mp2.put(): %s\n", desc.get());
		mp2.put("Ken", 7500.90);
		mp2.put("Cindy", 7800.20);
		out.printf("After mp2.put(): %s\n", desc.get());
		
	}
	
	private static final PrintStream out = System.out;

}

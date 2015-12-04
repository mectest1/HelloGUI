package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Test;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;

public class ChangeAndInvalidationTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testChangeAndInvalidation() {
		IntegerProperty counter = new SimpleIntegerProperty(100);
		
		//
		counter.addListener(ChangeAndInvalidationTest::invalidated);
		counter.addListener(ChangeAndInvalidationTest::changed);
		
		//
		out.println("Before changing the counter value -1");
		counter.set(101);
		out.println("After changing the counter value-1");
		out.println("Before changing the counter value -2");
		counter.set(102);
		out.println("After changing the counter value-2");
		
		
		//Try to set  the same value
		out.println("Before changing the counter value -3");
		counter.set(102);
		out.println("After changing the counter value-3");
		//Try to set a different value
		out.println("Before changing the counter value -4");
		counter.set(103);
		out.println("After changing the counter value-4");
	}

	private static void invalidated(Observable prop){
		out.printf("Counter is invalid.\n");
	}
	
	private 
	static void changed(ObservableValue<? extends Number> prop,
			Number oldValue, Number newValue
			){
		out.printf("Counter changed: old = %s, new = %s\n", oldValue, newValue);
	}
	
	
	private static final PrintStream out = System.out;
}

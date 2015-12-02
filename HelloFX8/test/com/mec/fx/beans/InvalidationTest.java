package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Test;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class InvalidationTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testInvaildation() {
		IntegerProperty counter = new SimpleIntegerProperty(100);
		
		counter.addListener(InvalidationTest::invlidated);
		
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

	
	public static void invlidated(Observable observable){
		out.println("Counter is invalidated.");
	}
	private static final PrintStream out = System.out;
}

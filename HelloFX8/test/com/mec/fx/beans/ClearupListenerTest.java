package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ClearupListenerTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testClearnUpListener() {
		ChangeListener<Number> listener = ClearupListenerTest::changed;
		
		counter.addListener(listener);
		
		//
		counter.set(200);
		
	}

	
	public static void changed(ObservableValue<? extends Number> prop, 
			Number oldValue,
			Number newValue
			){
		out.printf("Counter changed: old = %s, new = %s\n", oldValue, newValue);
	}
	
	private static final PrintStream out = System.out;
	public static IntegerProperty counter = new SimpleIntegerProperty(100);
}

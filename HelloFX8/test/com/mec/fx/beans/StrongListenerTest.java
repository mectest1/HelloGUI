package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class StrongListenerTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testStrongListener() {
//		ChangeListener<Number> listener = StrongListenerTest::changed;
		
//		counter.addListener(listener);
		addChangeListener();
		//
		counter.set(200);
		
		counter.set(300);
	}

	private static void addChangeListener(){
		ChangeListener<Number> listener = StrongListenerTest::changed;
		counter.addListener(listener);
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

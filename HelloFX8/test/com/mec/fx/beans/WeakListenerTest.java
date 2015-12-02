package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.Test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;

public class WeakListenerTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testWeakListener() {
//		ChangeListener<Number> listener = StrongListenerTest::changed;
		
//		counter.addListener(listener);
		addChangeListener();
		//
		out.println("Change value-1");
		counter.set(200);
		
		out.println("Change value-2");
		System.gc();
		out.printf("Is change listener already garbage collected? %s\n", listener.wasGarbageCollected());
		counter.set(300);
		
		out.println("Change value-3");
		changeListener = null;
		counter.set(400);
		
		out.println("Change value-4");
		System.gc();
		out.printf("Is change listener already garbage collected? %s\n", listener.wasGarbageCollected());	//<- funny, it's not working, just like the book showed;
		counter.set(500);
	}

	private static void addChangeListener(){
		changeListener = WeakListenerTest::changed;
		
		listener = new WeakChangeListener<>(changeListener);
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
	private static ChangeListener<Number> changeListener;
	private static WeakChangeListener<Number> listener;
}

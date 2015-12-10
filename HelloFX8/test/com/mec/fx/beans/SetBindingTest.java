package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Test;

import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class SetBindingTest {

	@Test
	public void testSetBinding() {
//		fail("Not yet implemented");
		SetProperty<String> sp1 = new SimpleSetProperty<>(FXCollections.observableSet());
		
		//Bind the size and empty properties of the SetProperty
		//to create a description of the set
		StringProperty initStr = new SimpleStringProperty("Size: ");
		StringProperty desc = new SimpleStringProperty();
		desc.bind(initStr.concat(sp1.sizeProperty()).concat(", Empty: ")
				.concat(sp1.emptyProperty())
				.concat(", Set: ")
				.concat(sp1.asString())
				);
		
		
		out.printf("Before sp1.add(): %s\n", desc.get());
		sp1.add("John");
		sp1.add("Jacobs");
		out.printf("After sp1.add(): %s\n", desc.get());
		
		SetProperty<String> sp2 = new SimpleSetProperty<>(FXCollections.observableSet());
		
		//Bind the content of sp1 to the content of sp2
		sp1.bindContent(sp2);
		out.println("Called s1p.bindContent(sp2)...");
		
		/*
		 * At this poiont, you can change the content of sp1.
		 * However, that will defeat the purpose of content binding,
		 * because the content of sp1 si no longer in sync with 
		 * the content of sp2. 
		 * DO NOT DO THIS:
		 * sp1.add("X");
		 */
		
		print("Before sp2.add():", sp1, sp2);
		sp2.add("1");
		print("After sp2.add():", sp1, sp2);
		
		sp1.unbindContent(sp2);
		print("After sp1.unbindContent(sp2): ", sp1, sp2);
		
		//Bind sp1 and sp2 contents bidirectionally
		sp1.bindContentBidirectional(sp2);
		
		//
		print("Befroe sp1.add():", sp1, sp2);
		sp2.add("2");
		print("After sp1.add():", sp1, sp2);
		
		
	}

	
	private static final void print(String msg, SetProperty<String> sp1, SetProperty<String> sp2){
		out.printf("%s sp1: %s, sp2: %s\n", msg, sp1.get(), sp2.get());
	}
	private static final PrintStream out = System.out;
}

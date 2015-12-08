package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Test;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ObjectBindingTest {

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testObjectBinding() {
//		fail("Not yet implemented");
		Book b1 = new Book("J1", 90, "1234567890");
		Book b2 = new Book("J1", 90, "1234567891");
		
		ObjectProperty<Book> book1 = new SimpleObjectProperty<>(b1);
		ObjectProperty<Book> book2 = new SimpleObjectProperty<>(b2);
		
		//
		BooleanBinding isEqual = book1.isEqualTo(book2);
		out.println(isEqual.get());
		
		//
		book2.set(b1);
		out.println(isEqual.get());
	}

	
	private static final PrintStream out = System.out;
}

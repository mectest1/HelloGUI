package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BidirectionalBindingTest {

	@Test
	public void testBidrectionalBinding() {
//		fail("Not yet implemented");
		IntegerProperty x = new SimpleIntegerProperty(1);
		IntegerProperty y = new SimpleIntegerProperty(2);
		IntegerProperty z = new SimpleIntegerProperty(3);
		
		out.println("Before binding");
		out.printf("x = %s, y = %s, z = %s\n", x.get(), y.get(), z.get());
		
		x.bindBidirectional(y);
		out.println("After binding-1");
		out.printf("x = %s, y = %s, z = %s\n", x.get(), y.get(), z.get());
		
		x.bindBidirectional(z);
		out.println("After binding-2");
		out.printf("x = %s, y = %s, z = %s\n", x.get(), y.get(), z.get());
		
		out.println("After changing z:");
		z.set(19);
		out.printf("x = %s, y = %s, z = %s\n", x.get(), y.get(), z.get());
		
		//Remove bindings;
		x.unbindBidirectional(y);
		x.unbindBidirectional(z);
		out.println("After unbinding and changing them separately");
		x.set(100);
		y.set(200);
		z.set(300);
		out.printf("x = %s, y = %s, z = %s\n", x.get(), y.get(), z.get());
	}

	
	
	private static final PrintStream out = System.out;
}

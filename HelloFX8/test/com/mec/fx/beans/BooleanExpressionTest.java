package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Test;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BooleanExpressionTest {

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBooleanExpression() {
//		fail("Not yet implemented");
		IntegerProperty x = new SimpleIntegerProperty(1);
		IntegerProperty y = new SimpleIntegerProperty(2);
		IntegerProperty z = new SimpleIntegerProperty(3);
		
		//Create a boolean expression for x > y && y <> z
		BooleanExpression condition = x.greaterThan(y).and(y.isNotEqualTo(z));
		
		out.println(condition.get());
		
		//Make the condition true by setting x to 3
		x.set(3);
		out.println(condition.get());
	}

	
	private static final PrintStream out = System.out;
}

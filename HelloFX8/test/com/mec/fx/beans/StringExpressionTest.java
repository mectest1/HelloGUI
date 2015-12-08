package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Test;

import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringExpressionTest {

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStringExpressino() {
//		fail("Not yet implemented");
		DoubleProperty radius = new SimpleDoubleProperty(7.0);
		DoubleProperty area = new SimpleDoubleProperty(0);
		StringProperty initStr = new SimpleStringProperty("Radius = ");
		
		//Bind area to an expression aht computes the area of the circle.
		area.bind(radius.multiply(radius).multiply(Math.PI));
		
		//Create a string expression to describe he circle.
		StringExpression desc = initStr.concat(radius.asString())
								.concat(", Area = ")
								.concat(area.asString());
		
		out.println(desc.getValue());
		
		//Change the radius.
		radius.set(14.0);
		out.println(desc.getValue());
		
	}

	
	private static final PrintStream out = System.out;
}

package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Test;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class CirculeAreaTest {

	@Test
	public void testCircleArea() {
//		fail("Not yet implemented");
		DoubleProperty radius = new SimpleDoubleProperty(7.0);
		DoubleBinding area = radius.multiply(radius).multiply(Math.PI);
		
		out.printf("Radius = %s, Area = %s\n", radius, area.get());
		
		radius.set(14.0);
		out.printf("Radius = %s, Area = %s\n", radius, area.get());
		
		DoubleProperty area2 = new SimpleDoubleProperty();
		
		//Create a double property and bind it to an expression
		//that computes the area of the circle
		area2.bind(radius.multiply(radius).multiply(Math.PI));
		out.printf("Radius = %s, Area2 = %s\n", radius.get(), area2.get());
		
	}
	
	private static PrintStream out = System.out;

}

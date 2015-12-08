package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Test;

import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TernaryExpressionTest {

	@Test
	public void test() {
//		fail("Not yet implemented");
		IntegerProperty num = new SimpleIntegerProperty(10);
		StringBinding desc = new When(num.divide(2).multiply(2).isEqualTo(num)).then("even").otherwise("odd");
		
		out.printf("%s is %s\n", num.get(), desc.get());
		
		//
		num.set(19);
		out.printf("%s is %s\n", num.get(), desc.get());
	}
	
	private static final PrintStream out = System.out;

}

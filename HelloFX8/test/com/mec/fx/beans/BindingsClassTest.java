package com.mec.fx.beans;

import java.io.PrintStream;
import java.util.Locale;

import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BindingsClassTest {

	@Ignore
	@Test
	public void test() {
//		fail("Not yet implemented");
		DoubleProperty radius = new SimpleDoubleProperty(7.0);
		DoubleProperty area = new SimpleDoubleProperty(0.0);
		
		//Bind area to an expressino taht computes the area of the circle
		area.bind(Bindings.multiply(Bindings.multiply(radius, radius), Math.PI));
		
		StringExpression desc = Bindings.format(Locale.US, "Radius = %.2f, Area = %.2f\n", radius, area);
		
		//
		out.println(desc.get());
		
		//
		radius.set(14);
		out.println(desc.getValue());
	}
	
	
	@Ignore
	@Test
	public void testSelectProperty(){
		ObjectProperty<Person> p = new SimpleObjectProperty<>(new Person());
		
		//Bind p.addr.zip
		StringBinding zip = Bindings.selectString(p, "addr", "zip");
		
		out.printf("zip code = %s\n", zip.get());
		
		
		//Change the zip code;
		p.get().addrProperty().get().zipProperty().set("35217");
		out.printf("zip code = %s\n", zip.get());
		
		
		
		////java.lang.NoSuchMethodException: com.mec.fx.beans.BindingsClassTest$Address.getState()
		StringBinding stateBinding = Bindings.selectString(p, "addr", "state");
		out.println(stateBinding.get());
	}

	@Test
	public void testMixAPI(){
		DoubleProperty radius = new SimpleDoubleProperty(7.0);
		DoubleProperty area = new SimpleDoubleProperty(0);
		
		//Combine teh FLuent API and Bindings class API
		area.bind(Bindings.multiply(Math.PI, radius.multiply(radius)));
		
		out.printf("Area = %s\n", area.get());
		
		radius.set(11.0);
		out.printf("Area = %s\n", area.get());
	}
	public static class Address{	//private class will fail
		private StringProperty zip = new SimpleStringProperty("36106");
		
		public StringProperty zipProperty(){
			return zip;
		}
	}
	
	
	public static class Person{
		private ObjectProperty<Address> addr = new SimpleObjectProperty(new Address());
		
		
		public ObjectProperty<Address> addrProperty(){	//<- it's addrProperty(), not addProperty(), notice the difference;
			return addr;
		}
	}
	private static final PrintStream out = System.out;
}

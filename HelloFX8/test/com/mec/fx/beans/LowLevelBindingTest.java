package com.mec.fx.beans;

import java.io.PrintStream;
import java.util.Formatter;
import java.util.Locale;

import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LowLevelBindingTest {

	@Ignore
	@Test
	public void testDoubleBinding() {
//		fail("Not yet implemented");
		final DoubleProperty radius = new SimpleDoubleProperty(7.0);
		DoubleProperty area = new SimpleDoubleProperty(0);
		
		DoubleBinding areaBinding = new DoubleBinding(){
			{
				this.bind(radius);
			}

			@Override
			protected double computeValue() {
				double r=  radius.get();
				double area = Math.PI * r * r;
				return area;
			}

		};
		
		area.bind(areaBinding);
		
		out.printf("Area = %s\n", area.get());
		
		
		radius.set(11);
		out.printf("Area = %s\n", area.get());
	}

	
	@Test
	public void testStringBinding(){
		final DoubleProperty radius = new SimpleDoubleProperty(7);
		final DoubleProperty area = new SimpleDoubleProperty(0);
		
		//
		DoubleBinding areaBinding = new DoubleBinding(){
			{
				this.bind(radius);
			}


			@Override
			protected double computeValue() {
				double r = radius.get();
				double area = Math.PI * r * r;
				return area;
			}
			
			
		};
		area.bind(areaBinding);
		
		//
		StringBinding desc = new StringBinding(){
			{
				this.bind(radius, area);
//				this.bind(area);	//<- seems this will work too;
			}
			@Override
			protected String computeValue() {
				try(Formatter f = new Formatter();){
					f.format(Locale.US, "Radius = %s, Area = %s", radius.get(), area.get());
					return f.toString();
				}
			}
			@Override
			public void dispose() {
				out.println("Description binding is disposed");
			}
			@Override
			public ObservableList<?> getDependencies() {
				return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(radius, area));
				
				//<- will also work, since area has been bound to radius
//				return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(area)); 
			}
			@Override
			protected void onInvalidating() {
				out.println("Description is invalid");
			}
			
		};
		out.println(desc.getValue());
		
		//Change the radius;
		radius.set(14.0);
		out.println(desc.get());
	}
	private static final PrintStream out = System.out;
}

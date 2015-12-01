package com.mec.fx.beans;

import java.beans.PropertyChangeEvent;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testPropertyChange() {
		final Employee e1 = new Employee("John Jacobs", 2000.0);
		
		computeTax(e1.getSalary());
		
		//Add a property change listener to e1
		e1.addPropertyChangeListener(EmployeeTest::handlePropertyChange);
		
		//Change the salary
		e1.setSalary(3000.00);
		e1.setSalary(3000.00);	//No change notification is sent
		e1.setSalary(6000.00);
		
	}
	
	public static void handlePropertyChange(PropertyChangeEvent e){
		String propertyName = e.getPropertyName();
		if("salary".equals(propertyName)){
			out.println("Salary has changed");
			out.printf("Old: %s, New: %s\n", e.getOldValue(), e.getNewValue());
			computeTax((Double)e.getNewValue());
		}
	}
	
	public static void computeTax(double salary){
		double tax = salary * TAX_PERCENT/100.0;
		out.printf("Salary: %s, Tax: %s\n", salary, tax);
	}
	private static final PrintStream out = System.out;
	private static final double TAX_PERCENT = 20.0;
}

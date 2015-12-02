package com.mec.fx;

import org.junit.Ignore;
import org.junit.Test;

import javafx.application.Application;

public class FXParamAppTest {

//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}

//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}

	@Ignore
	@Test
	public void testUnnamedParams() {
//		FXParamApp.launch("Anna Lola");
		Application.launch(FXParamApp.class, new String[]{"Anna", "Lola"});
	}

	@Ignore
	@Test
	public void testFaieldNamedParams(){
		//Using an equals sign in a parameter value on teh command line does not
		//make the parameter a named parameter.
//		FXParamApp.launch("Anna Lola width=200 height=100");
		Application.launch(FXParamApp.class, new String[]{"Anna", "Lola", "width=200", "height=100"});
	}
	
//	@Ignore
	@Test
	public void testNamedParams(){
//		FXParamApp.launch("Anna Lola --width=200 --height=100");
		Application.launch(FXParamApp.class, new String[]{"Anna", "Lola", "--width=200", "--height=100"});
	}
	
	@Ignore
	@Test
	public void testParseDouble() throws Exception{
		Double.parseDouble("7,000,000.000");
	}
}

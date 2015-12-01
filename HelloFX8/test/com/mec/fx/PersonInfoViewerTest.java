package com.mec.fx;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javafx.application.Application;

public class PersonInfoViewerTest {

//	@Before
//	public void populateData(){
//	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testMain() {
		Application.launch(PersonInfoViewer.class);
	}

}

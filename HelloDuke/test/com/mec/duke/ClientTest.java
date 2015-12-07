package com.mec.duke;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import com.mec.duke.tut.Client;

import javafx.application.Application;

public class ClientTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testClientStart() {
//		fail("Not yet implemented");
		Application.launch(Client.class);
	}

}

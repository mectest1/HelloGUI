package com.mec.duke;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import com.mec.duke.tut.Server;

public class ServerTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testServerStart() throws Exception{
		new Server(10019);
	}

}

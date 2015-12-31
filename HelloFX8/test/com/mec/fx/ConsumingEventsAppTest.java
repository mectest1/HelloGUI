package com.mec.fx;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import javafx.application.Application;

public class ConsumingEventsAppTest {

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
//		fail("Not yet implemented");
		Application.launch(ConsumingEventsApp.class);
	}

}

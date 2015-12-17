package com.mec.fx;

import org.junit.Ignore;
import org.junit.Test;

import javafx.application.Application;


public class LayoutBoundsAppTest{

	@Ignore
	@Test
	public void testNotGrouped() {
//		fail("Not yet implemented");
		Application.launch(LayoutBoundsApp.class);
	}
	
//	@Ignore
	@Test
	public void testGrouped(){
		Application.launch(LayoutBoundsApp.class, "--grouped=true");
	}
}

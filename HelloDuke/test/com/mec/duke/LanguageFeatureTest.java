package com.mec.duke;

import static org.junit.Assert.*;

import org.junit.Test;

public class LanguageFeatureTest {

	@Test
	public void testInterfaceConversion() {
		C2 c2 = (C2)new A();	//<- Come on, A is not event a type of C2, right?
								//No complie-time exception
								//Get runtime exception: ClassCastException
	}
	interface B{}
	interface C{}
	interface C2 extends C{}
	class A implements B, C{}

}

package com.mec.duke;

import java.io.PrintStream;

import org.junit.Ignore;
import org.junit.Test;

public class LanguageFeatureTest {

	@Ignore
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

	
	//Example from http://www.oracle.com/technetwork/articles/java/architect-lambdas-part1-2080972.html
	@Ignore
	@Test
	public void testInnertClasses(){

	    InstanceOuter io = new InstanceOuter(12);

	    // Is this a compile error? Seems nope.
	    InstanceOuter.InstanceInner ii = io.new InstanceInner();

	    // What does this print?
	    ii.printSomething(); // prints 12

	    // What about this?
	    StaticOuter.StaticInner si = new StaticOuter.StaticInner();
	    si.printSomething(); // prints 24
	}
	
	static class InstanceOuter {
	  public InstanceOuter(int xx) { x = xx; }

	  private int x;

	  class InstanceInner {
	    public void printSomething() {
	      System.out.println("The value of x in my outer is " + x);
//		      ++x;	//would compile;
	    }
	  }
	}

	static class StaticOuter {
	  private static int x = 24;

	  static class StaticInner {
	    public void printSomething() {
	      System.out.println("The value of x in my outer is " + x);
//		      ++x;	//would compile
	    }
	  }
	}

	
	
	@Test
	public void testClassBlockCode(){
		D d = new D();
		D d2 = new D();
	}
		
	static class D{
		static{
			out.println("Static Code Executed\n");
		}
		{
			out.println("Instance Code Executed");
		}
		public D(){
			out.println("Constructor Executed\n");
		}
	}
		
	
		
	private static final PrintStream out = System.out;
		
}

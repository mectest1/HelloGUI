package com.mec.duke;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	
	@Ignore
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
		
	
	/**
	 * Method Reference Types in Java8:
	 * 1, Method.invoke(null, args ...) 
	 * 2, Method.invoke(inst, args ...)
	 * 3, Method.invoke(obj1, [obj2,] args...)
	 * 3, Constructor.invoke(args, args ...)
	 */
	@Ignore
	@Test
	public void testMethodReference(){
		File[] hiddenFiles = new File("c:/").listFiles(File::isHidden);	//<- Note that File.isHidden() is not static method -- it's only a normal instance method
							
		Arrays.stream(hiddenFiles).forEach(out::println);
	}
	
	
	@FunctionalInterface
	interface FuncInterfaceWithMultipleAbstractMethods{
		void derp();
		boolean equals(Object obj);	//<- extract from java.lang.Object
//		void derp2();	//<-Nah
		String toString();	//<- Still OK
//		Class<?> getClass();	//<- final method cannot be overridden;
//		Object clone();	//<- Object.clone() is protected, thus means this method is abstract~?
	}
	
		
	class Outer{
		private int x;			//<-
		class Inner{
			private int x;		//<-
			void derp(){
				out.println(Outer.this.x);	//<-
			}
			
			void derp2(){
				out.println(x);
			}
		}
		
		void derp(Inner inner){
			out.println(inner.x);	//<- 
		}
	}
	
	

//	@Ignore
	@Test
	public void testGenerics(){
//		List<? super ? extends Object> derp  = new ArrayList<>();	//<- derp, this fails;
		
//		Collector joinList = Collectors.joining(",", "[", "]");
		List l1 = new ArrayList();
		l1.add("what");
		l1.add(1);
		out.println(l1.stream().collect(Collectors.mapping(Object::toString, Collectors.joining(","))));
		
		//
		List<? extends Object> l2 = new ArrayList<>();
//		l2.add("what");	//not applicable 
//		l2.add(1);		//not applicable
//		out.println(l1.stream().collect(Collectors.mapping(Object::toString, Collectors.joining(","))));
		
		List<?> l3 = new ArrayList<>();
//		l3.add("what");		//<- not applicable
//		l3.add(1);			//<- not applicable
//		l3.add(new Object());	//<- not applicable
		
		List<Integer> ints = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
		List<? extends Object> l4 = ints;	//valid expression;
		List<? extends Number> l5 = ints;
//		List<? super Number> l6 = ints;		//<- conversion failed.
		
		Object l40 = l4.get(0);	//<- get type: Object;
		Number l50 = l5.get(0);	//<- get type: Number
		
		
		Optional.of(l4.getClass() == l5.getClass()).ifPresent(out::println);
	}
	
	//Due to type erasure, subclass of Throwable cannot be generic;
//	static class GenericException<T> extends Exception{} //<- Generic class cannot sub-class java.lang.Throwable
		
		
	
	@Ignore
	@Test
	public void testShadowVariables(){
		int a = 10;
		Runnable r1 = ()->{
//			int a = 2;	//<- Compile error
//			a = 2;		//<- Compile error 2
		};
		
		Runnable r2 = new Runnable(){
			int a = 10;				//<- Totally OK
			@Override
			public void run() {
				int a = 10;			//<- Also OK;
				//
			}
			
		};
	}
	
	@Test
	public void testArrayConstructor(){
		int[] ints = {1, 2, 3, 4, 5};	//<-
		Object[] objs = {"der", 1};		//<--
		List[] lists = {new ArrayList<Object>(), new LinkedList<Object>(), new Vector<Object>()};
//		SuppressWarnings[] annotations = {@SuppressWarnings("what"), @SuppressWarnings("is")};
	}
	
	
	static class Derp{
		static{
			out.println("static");	//1, and only once;
		}
		
		{
			out.println("Block Code");	//2, every time a new instance is created;
		}
		
		Derp(){
			out.println("Constructor");	//3, A lot has happened before constructor 
		}
	}
	
	static class Derp2 extends Derp{
		static{
			out.println("static 2");
		}
		{
			out.println("Block Code 2");
		}
		
		Derp2(){
			out.println("Constructor 2");
		}
		static void doItStatically(){
			
		}
		
		void doIt(){
			
		}
	}
	
	static class Derp3 extends Derp2{
//		@Override
//		static void doIt(){	//<- compilation error
//			
//		}
	}
	private static final PrintStream out = System.out;
		
}

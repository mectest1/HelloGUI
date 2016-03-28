package com.mec.duke;

import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
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
	
	

	@Ignore
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
	
	@Ignore
	@Test
	public void testArrayConstructor(){
		int[] ints = {1, 2, 3, 4, 5};	//<-
		Object[] objs = {"der", 1};		//<--
		List[] lists = {new ArrayList<Object>(), new LinkedList<Object>(), new Vector<Object>()};
//		SuppressWarnings[] annotations = {@SuppressWarnings("what"), @SuppressWarnings("is")};
	}
	
	@Ignore
	@Test
	public void testSynchronizeOnNull(){
		Object what = null;
//		synchronized(null){	//<- not a valid argument for the synchronized statement
		synchronized(what){	//<- throws NullPointException
			out.println("sunchronized on null successfully");
		}
	}
	
	@Ignore
	@Test
	public void testPathResolve() throws Exception{
		Path cur = Paths.get(".");
		out.println(cur.toRealPath());
		
		
		Integer i = 0;
		increment(i);
		out.println(i);	//it's 0 or 1?
						/**
						 * Note: 
							1, Java is pass by value, not by reference;
								(Although normal objects values are references themselves)
							2, Integers are immutable 
								
						 */
						
									
		
						
	}
	
	
	@Ignore
	@Test
	public void testExceptionsInThreads(){
		IntStream.range(0, 5).forEach(i -> {
			new Thread(){

				@Override
				public void run() {
					RuntimeException e = new RuntimeException("derp" + i);
					e.printStackTrace(out);
//					throw new RuntimeException("derp" + i);
					throw e;
				}
				
			}.start();
		});
	}
	
	//Question 83 (modified)
	@Ignore
	@Test
	public void testIntegerIncrement(){
		 class TestDeclare implements DeclareStuff{
		 	int x = 5;
			@Override
			public void doStuff(int s) {
				s += EASY + ++s;
				out.printf("s %s\n", s);
			}
		 }
		 int x = 5;
		 new TestDeclare().doStuff(++x);	//<- Q: why is the result "s 16" instead of "s 17"?
		 					//Possible answer: from VM's perspective of view: 
		 					/*
		 					 * expression: 
		 					 * s += EASY + ++s 
		 					 * can be rewritten to:
		 					 * 	s = s + EASY + (++s)
		 					 * 
		 					 * Thus to evaluate this expression, 
		 					 * variable_s = value_of_s + value_of_EASY + value_of_++s
		 					 * 
		 					 * results into
		 					 * 
		 					 * variable_s = 6 + 3 + 7
		 					 * 
		 					 * Thus variable_s now holds value of 16 (instead of 17) -> 
		 					 * since the value stack is pushed one by one from left to right for the addition expression;
		 					 */
		 					//
	}
	
	@Ignore
	@Test
	public void testRecursiveExtend(){
		out.println(new B2());	//<-----StackOverflowError
	}
	
	class A2{
		A2(){
			new B2(); //<- LOL, still available
		}
	}
	class B2 extends A2{
		
	}
	
	//Question 118
	enum Element{
		EARTH, WIND,
		FIRE{	//<- override method on the fly
			public String info(){
				return "Hot";
			}
		};
		public String info(){
			return "element";
		}
	}
	

	@Ignore
	@Test
	public void testEnumInClass(){
		Navi.Direction d = Navi.Direction.NORTH;
		
		abstract class Z{
			
		}
	}
	
	static class Navi{
		enum Direction{NORTH, SOUTH, EAST, WEST;
			
			Direction(){	//only allowable modifier (if there is any) for enum is "private";
				
			}
		}
		
		
		//<- no need to be declared as static --
							//enums are static automatically;
	}
	
	@Ignore
	@Test
	public void testConflictMethods(){
//		interface i{	// Interface, as well as enum(since they're basically the same) can only be defined in top-level class or static context
//			
//		}
		
//		class Derp implements I1, I2{
//
//			@Override
//			public void doIt() {	//<-- The return type is in-compatible wiht I3.doIt();
//				// TODO Auto-generated method stub
//				
//			}
//			
//		}
	}
	interface I1{
		void doIt();
	}
	
	interface I2{
		int doIt();
	}
	
	//====================================================================
	@Ignore
	@Test
	public void testStaticMethods(){
	
	}
	
	static class A3{
		static void doIt(){
			derp();
		}
		
		private static void derp(){
			
		}
	}
	
	static class B3 extends A3{
//		static int doIt(){	//<- return type is incompatible with A3.doIt();
//			
//		}
		
		static int derp(){	//<- it's OK;
			return 0;
		}
	}
	//====================================================================
	
	@Ignore
	@Test
	public void testTreeSet(){
		class Drink implements Comparable<Drink>{
			String name;

			public Drink(String name) {
				super();
				this.name = name;
			}

			@Override
			public int compareTo(Drink o) {
				return 0;
			}

			@Override
			public String toString() {
				return "Drink [name=" + name + "]";
			}
		}
		
		TreeSet<Drink> drinks = new TreeSet<Drink>(); //<- TreeSet: backed by keys in TreeMap;
		drinks.add(new Drink("Coffee"));
		drinks.add(new Drink("Tea"));
		out.println(drinks);	//result:[Drink [name=Coffee]]

	}
	
	@Test
	public void testSuperInterfaces(){
		class D1 implements I1{
			@Override
			public void doIt() {
				// TODO Auto-generated method stub
				
			}
		}
		
		class D2 extends D1 implements Serializable{
			
		}
		
		Arrays.stream(D1.class.getInterfaces()).forEach(c -> out.println(c.getTypeName()));
		Arrays.stream(D2.class.getGenericInterfaces()).forEach(c -> out.println(c.getTypeName()));	//<- only prints out direct interfaces;
		Arrays.stream(D2.class.getAnnotatedInterfaces()).forEach(c -> out.println(c.getClass().getName()));
	}
	
	static interface DeclareStuff{
		public static final int EASY = 3;
		void doStuff(int t);
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
	
	static class Derp5{
		Derp5(String name){
			
		}
	}
	
	static class Derp6 extends Derp5{
		Derp6(){
			super("derp6");
		}
	}
	
	static class Derp7{
		static interface Foo{
			int bar();
		}
		
		class A implements Foo{

			@Override
			public int bar() {
				return 0;
			}
			
		}
		
		void foobar(Foo foo){
			out.println(foo.bar());
		}
		
		void testFoo(){
			class A implements Foo{	//<- The Type A is hiding the type Derp7.A;

				@Override
				public int bar() {
					return 2;
				}
				
			}
			
			foobar(new A());
		}
		
	}
	
	static class Derp8{
		static final int[] a1 = {1, 2};
//		static final int[] a2 = new int[2]{1, 2};//cannot define array dimensions when array initializer is provided
		static final int[] a3 = new int[]{1, 2};
//		static final int[] a4 = new int[];//variable must provide either dimension expressions or an array initializer;
		static final int[] a5 = new int[2]; static{a5[0]=1; a5[1]=2;
						a5[3]=3;	//<- derp ~.~
						}
		
		
	}
	
	static Integer increment(Integer i){
		return i++;
	}
	
	static class Derp9{
		int a;
	}
	
	static class Derp10 extends Derp9{
//		int a;
		{
			this.a = 0;	//<- still can be referenced;
			a = 0;
			super.a = 0;
		}
	}
	
	private static final PrintStream out = System.out;
		
}

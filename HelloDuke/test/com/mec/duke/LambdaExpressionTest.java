package com.mec.duke;

import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

public class LambdaExpressionTest {

	@Ignore
	@Test
	public void testFilter() {
		Stream.of(What.newInstance()).forEach(What::print);
	}
	
	@Ignore
	@Test
	public void testDerp(){
		Stream.of(What.newInstance()).forEach(a -> a.derp(s -> s.print()));
		Function<Object, Object> func = (derp)  -> {return null;};
		
//		Function<Object, Void> func2 = d -> {return (void) null;};	//Error
//		Function<Object, Void> func2 = d -> {return;};	//Error
		
		Consumer<Object> func3 = d -> {++instVariable;};	//That's OK;	//<- instance variable can be accessed here
		
//		int localVariable = 0;
//		Consumer<Object> func4 = d -> {out.println(localVariable);};	//local variable should be effectively final
//		++localVariable;
		
		
	}
	
	
	@Ignore
	@Test
	public void testComparator(){
		Stream.of("A Quick Brown fox Jumps over the lazy dog".split("\\s+"))
			.sorted(Comparator.comparing((String s) -> s.length())		//<- use comparator generator
						.reversed()
						.thenComparing(Comparator.naturalOrder())
					)	
			.forEach(out::println);
		
//		IntStream.range(0, 5).forEach(out::println);
	}
	
	

	//Method reference is a grammar sugar for lambda expressions;
	@Ignore
	@Test
	public void testhMethodReference(){
		BiConsumer<List<String>, Comparator<String>> sortList = List::sort;	//<- class instance method is a BiConsumer
		List<String> list = Arrays.asList("3", "4", "1", "2");
		sortList.accept(list, Comparator.naturalOrder());
//		list.forEach(out::println);
		
		Consumer<String> parse = Integer::parseInt;	//static method is consumer;
		ToIntFunction<String> parseInt = Integer::parseInt;	//static method is consumer;
		Function<String, Integer> parseInt2 = Integer::parseInt;
		BiFunction<String, Integer, Integer> parseInt3 = Integer::parseInt;
		
		Consumer<Object> println = out::println;	//instance method as normal consumer;
		
		BiPredicate<List<? extends Object>, Object> contains = List::contains;
		Consumer<List<?>> clearList = List::clear;
		
		Function<URI, Path> newPath = Paths::get;
		Function<Path, Path> toRoot = Path::getRoot;
		BiPredicate<Path, Path> startsWith = Path::startsWith;
		
		BinaryOperator<Path> resolve = Path::resolve;
		BinaryOperator<Path> relativize = Path::relativize;
		
		
		
		Supplier<Object> newObj = Object::new;
		Supplier<LocalDateTime> now = LocalDateTime::now;
		ToIntBiFunction<LocalDateTime, TemporalField> temporalFieldOf = LocalDateTime::get;
		out.println(temporalFieldOf.applyAsInt(now.get(), ChronoField.MONTH_OF_YEAR));
		
		
	}
	
	@Ignore
	@Test
	public void testFunctionInterfaceConversion(){
		Callable<Object> s = Object::new;
		Supplier<Object> s2 = Object::new;
		
		Supplier<Object> s3 = (Supplier<Object>) (s);	//<- ClassCastException
	}
	
	@Ignore
	@Test
	public void testPredicateComposition(){
		ToIntFunction<Integer> f = x -> x + 1;	//<- not composition ability for ToIntFunction
		Function<Integer, Integer> f2 = x -> x + 1;
		Function<Integer, Integer> f2$ = x -> ++x;
		Function<Integer, Integer> f3 = x -> x + 2;
		UnaryOperator<Integer> f4 = x -> x + 3;	//<- UnaryOperator is also function;
		
		Function<Integer, Integer> f5 = f2.andThen(f3);
		Function<Integer, Integer> f6 = f2.compose(f4);
		Function<Integer, Integer> f7 = f2$.compose(f4);
		
		int seed = 3;
		Stream.of(f5, f6, f7).forEach(fn -> out.println(fn.apply(seed)));
	}
	
	@Ignore
	@Test
	public void testFunctionComposition(){
//		UnaryOperator<String> addHeader$Error = String::concat;	//Error: Cannot make a static reference to non-status reference;
		BinaryOperator<String> addHeader = String::concat;	//instance method should reserver the first argument as current instance; 
		BinaryOperator<String> addBody = String::concat;
		BinaryOperator<String> addFooter = String::concat;
		
		
//		BinaryOperator<String> headBodyFooter = addHeader.andThen(s -> addBody.apply(s, ""));
		
		Consumer<String> println = out::println;
	}
	
	
	@Ignore
	@Test
	public void testCollectionFactory(){
		Map<String, Supplier<Collection<? extends Object>>> f = new HashMap<>();
		f.put("list", ArrayList::new);
		f.put("set", HashSet::new);
		f.put("treeSet", TreeSet::new);
		
		List<Object> l = (List<Object>)f.get("list").get();
		l.add("what");
		l.forEach(out::println);
	}
	
	
	@Ignore
	@Test
	public void testLambdaStackTrace(){
		Stream.of((String)null).forEach(s -> out.println(s.length())); //test for StackTrace through lambda expression
	}
	
	
	
	double integrate(DoubleFunction<Double> f, double a, double b){
		return (f.apply(a) + f.apply(b)) * (b-a) / 2.0;
	}

	void innerClass(Stream<What> what, WhatToDo toDo){
		
		what.forEach(toDo::doIt);
		
	}
	static class What{
		static What newInstance(){
			return new What();
		}
		
		@Override
		public String toString() {
			return String.format("What [%s]", LocalDateTime.now());
		}

		void print(){
			out.println(this);
		}
		
		void derp(Derp derp){
			derp.doWhateverWith(this);
		}
		
		@FunctionalInterface
		interface Derp{
			void doWhateverWith(What a);
		}
	}
	@FunctionalInterface
	interface WhatToDo{
		void doIt(What a);
	}
	
	public static class ObjectFilter{
		/**
		 * Filter the specified objects with the given filter;
		 * @param objects
		 * @param filter
		 * @return
		 */
		public <T> Collection<T> filter(Collection<T> objects, Predicate<T> filter){
			return objects.stream().filter(filter).collect(Collectors.toList());
		}
		
		void $(){	//<- valid function name;
			
		}
	}
	
	int instVariable = 0;
	
	
	
	
	@Test
	public void testDefaultMethods(){
		new C().hello();	//<- default method from B:
		new C2().hello();
	}
	
	static interface A{
		default void hello(){
			out.println("Hello from A");
		}
	}
	
	static interface B extends A{
		default void hello(){
			out.println("Hello from B");
		}
	}
	
	static class C implements B, A{
		
	}
	
	static class D implements A{
		
	}
	
	
	static class C2 extends D implements A, B{
		
	}
	
	
	
	
	
	
	static final PrintStream out = System.out;
}


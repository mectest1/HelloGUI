package com.mec.duke;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

public class StreamTest {

	//ref: http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html
	//ref2: http://www.oracle.com/technetwork/articles/java/architect-streams-pt2-2227132.html
	@Ignore
	@Test
	public void testIntRange() {
		IntStream.range(0, 100).filter(i -> {
			boolean retval =  0 == i % 5;
			if(retval){
				out.printf("#%s - ", i);	//<- lazy evaluation for intermediate operations;
			}
			return retval;
		}).forEach(i -> out.println("Hello, Stream!"));	
	}

	
	@Ignore
	@Test
	public void testNumberGenerator(){
		IntStream.iterate(0, i -> i + 10).limit(5).filter(i -> i > 0).forEach(out::println);	//print out [10, 20, 30, 40]
	}
	
	@Ignore
	@Test
	public void testNumberGenerator2(){
		IntStream.generate(new Random()::nextInt).limit(5).forEach(out::println);	//print 5 random integers
	}
	
	@Ignore
	@Test
	public void testComparing(){
		Stream.generate(() -> String.valueOf(new Random().nextInt()))
			.limit(5)
//			.sorted(Comparator.<String, Integer>comparing(Objects::hashCode))	//<- Compare with the hashCode of the string;
			.sorted(Comparator.<String, Integer>comparing(s -> s.indexOf(s.length()-1)))	//<- Compare with the last character
//			.limit(5)					//put .limit() here and you'll get hanged in the Stream.generate() step. What a surprise;
//			.forEach(out::println);
			.forEachOrdered(out::println);	//Doesn't work as expected, why? 
	}
	
	
	@Ignore
	@Test
	public void testFibonacci(){
		final int seed = 1;
		prev2 = 0L;
		Stream.iterate((long)seed, previous -> {
			if(0 >= prev2){
				prev2 = (long) seed;
				return (long)seed;
			}
			long retval =  previous + prev2;
			prev2 = previous;
			return retval;
		}).limit(100).forEach(out::println);	//<- overflow in the 93th number;
	}
	
	Long prev2;	//instance variable can be modified in lambda expression, since "this" has been pssed in implicitly;
	
	
	@Test
	public void testFilter(){
		menus().filter(d -> d.getCalories() < 500).map(Dish::getName).limit(3).forEach(out::println);
	}
	
	Stream<Dish> menus(){
		return Stream.of(
			new Dish("prok", false, 800, Dish.Type.MEAT)
			, new Dish("beef", false, 700, Dish.Type.MEAT)
			, new Dish("chicken", false, 400, Dish.Type.MEAT)
			, new Dish("french fries", true, 530, Dish.Type.OTHER)
			, new Dish("rice", true, 350, Dish.Type.OTHER)
			, new Dish("season fruit", true, 120, Dish.Type.OTHER)
			, new Dish("pizza", true, 550, Dish.Type.OTHER)
			, new Dish("prawns", false, 300, Dish.Type.FISH)
			, new Dish("salmon", false, 450, Dish.Type.FISH)
			);
	}
	
	static class Dish{
		final String name;
		final boolean vegetarian;
		final int calories;
		final Type type;
		public Dish(String name, boolean vegetarian, int calories, Type type) {
			super();
			this.name = name;
			this.vegetarian = vegetarian;
			this.calories = calories;
			this.type = type;
		}
		
		public String getName() {
			return name;
		}

		public boolean isVegetarian() {
			return vegetarian;
		}

		public int getCalories() {
			return calories;
		}

		public Type getType() {
			return type;
		}

		enum Type{MEAT, FISH ,OTHER}
	}
	
	
	
	
	
	
	
	
	
	
	private static final PrintStream out = System.out;
	
	class FibonacciGenerator{
		Stream<Integer> fibonacci(){
			final int seed = 1;
			prev2 = 0;
			return Stream.iterate(seed, previous -> {
				if(0 >= prev2){
					prev2 = seed;
					return seed;
				}
				int retval =  previous + prev2;
				prev2 = previous;
				return retval;
			});
		}
		
		int prev2;
	}
	
	
}



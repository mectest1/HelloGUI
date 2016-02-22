package com.mec.duke;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
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
	
	@Ignore
	@Test
	public void testFilter(){
		menus().filter(d -> d.getCalories() < 500).map(Dish::getName).limit(3).forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testOperateOnConsumedStream(){
		Stream<String> s = Stream.of("Hello, World!".split("\\s+"));
		s.forEach(out::printf);
		s.forEach(out::printf);	//<- IllegalStateException: stream has already been operated on or closed 
	}
	
	@Ignore
	@Test
	public void testGeneratePrimeNumbers(){
		primes()
		.limit(1000)	//<- test the result on https://primes.utm.edu/lists/small/1000.txt
		.forEach(out::println);
	}
	
	LongStream primes(){
		return LongStream.generate(() -> {
			if(primes.isEmpty()){
				primes.add(2L);
				return 2L;
			}else if(1 == primes.size()){
				primes.add(3L);
				return 3L;
			}
			long lastPrime = primes.get(primes.size() - 1);
			NEXT_NUMBER:
			for(long number = lastPrime + 2; ; number = number + 2){
				final long numberSnapshot = number;
				if(primes.stream().anyMatch(p -> 0 == numberSnapshot % p)){	//<- "number" cannot be used here directly;
					continue;
				}
				for(long factor = lastPrime; factor < Math.sqrt(number); factor = factor + 2){
					if(0 == number % factor){
						continue NEXT_NUMBER;
					}
				}
				primes.add(number);	//<- must be a prime number when it comes here;
				return number;
			}
		});
//		return LongStream.
	}
	
	List<Long> primes = new ArrayList<Long>();
//	List<Long> primes = Stream.of(2L, 3L).collect(Collectors.toList());
	

	@Ignore
	@Test
	public void testLoopFunction(){
		menus().filter(m -> {
			out.printf("filtering: %s\n", m.getName());
			return m.getCalories() > 300;
		}).map(m -> {
			out.printf("mapping %s\n\n", m.getName());
			return m.getName();
		})
		.limit(3)
		.collect(Collectors.toList());
	}
	
	@Ignore
	@Test
	public void testFitlerStream(){
		menus().filter(Dish::isVegetarian)
			.forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testUnique(){
		Random rand = new Random();
		IntStream randNumbers = IntStream.generate(() -> rand.nextInt(100))
				.limit(10)	
				.limit(100)	//Multiple limits can exist at the same time
				.distinct();
		List<Integer> randList = randNumbers.boxed().collect(Collectors.toList());
		out.println(randList.stream().count());
//		randNumbers.forEach(out::println);	//<- IllegalStateExcepton: Stream has been closed;
		randList.stream().forEach(out::print);
	}
	
	@Ignore
	@Test
	public void testSkip(){
//		Random rand = new Random();
		IntStream.range(0, 100).skip(50).limit(3).forEach(out::println);
		IntStream.range(0, 100).limit(3).forEach(out::println);
	}
	
	@Ignore
	@Test
	public void tetMap() throws Exception{
//		menus().map(Dish::getName).forEach(out::println);
		
		Path p = Paths.get("test", getClass().getName().replaceAll("\\.", "/") + ".java");
		Files.readAllLines(p)
			.stream()
//			.map(s -> s.split("\\s+"))
			.flatMap(s -> Arrays.stream(s.split("\\s+")))
			.collect(Collectors.groupingBy(String::length))
			.entrySet().stream()
//			.sorted()	//<- Error: HashMap$Node cannot be cast to Comparable
			.sorted(Comparator.comparingInt(Map.Entry::getKey))
			.forEach(entry -> out.printf("%s - %s\n", entry.getKey(), entry.getValue()));
//			.forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testFlatMap(){
		Stream.of("Hello", "World!").map(w -> w.split(""))
			.flatMap(Arrays::stream)	//<- flatMap: receives a FunctionalInterface: T -> Stream<R>
										//unlike map, which receives T -> R;
			.distinct()
			.forEach(out::println);
	}
	
	Stream<Dish> menus(){
		return Stream.of(
			new Dish("pork", false, 800, Dish.Type.MEAT)
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
		
		@Override
		public String toString() {
			return "Dish [name=" + name + ", vegetarian=" + vegetarian + ", calories=" + calories + ", type=" + type
					+ "]";
		}

		enum Type{MEAT, FISH ,OTHER}
	}
	
	

	@Test
	public void testFlatMap2(){
		List<Integer> numbers1 = Arrays.asList(1, 2, 3);
		List<Integer> numbers2 = Arrays.asList(4, 5);
//		numbers1.stream().map(i -> {
		numbers1.stream().flatMap(i -> {
			return numbers2.stream().map(j -> new int[]{i, j});
		})
		.forEach(pair -> out.printf("%s, %s\n", pair[0], pair[1]));
		;
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



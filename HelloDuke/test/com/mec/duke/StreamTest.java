package com.mec.duke;

import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
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
	
	

	@Ignore
	@Test
	public void testFlatMap2(){
		List<Integer> numbers1 = Arrays.asList(1, 2, 3);
		List<Integer> numbers2 = Arrays.asList(4, 5);
//		numbers1.stream().map(i -> {
		numbers1.stream().flatMap(i -> {
			return numbers2.stream()
					.filter(j -> 0 == (i + j) % 3)	//<- filter specific elements combination
					.map(j -> new int[]{i, j});
		})
		.forEach(pair -> out.printf("%s, %s\n", pair[0], pair[1]));
		;
	}
	
	@Ignore
	@Test
	public void testAnyMatch(){
		if(menus().anyMatch(Dish::isVegetarian)){
			out.println("The menu is (somewhat) vegetarian friendly");
		}
	}
	
	@Ignore
	@Test
	public void testAllMathes(){
		if(menus().allMatch(d -> d.getCalories() < 1000)){
			out.println("Calories of all dishes are under 1000");
		}
	}
	
	@Ignore
	@Test
	public void testNoneMatch(){
		if(menus().noneMatch(d -> d.getCalories() > 1000)){	//<- Complementary 
			out.println("No calories of dish is over 1000");
		}
	}
	
	@Ignore
	@Test
	public void testFindAny(){
		menus().filter(Dish::isVegetarian).findAny().ifPresent(out::println);
	}
	@Ignore
	@Test
	public void testFindFirst(){
		menus().findFirst().ifPresent(out::println);
	}
	
	@Ignore
	@Test
	public void testReduce(){
		IntBinaryOperator sum = (x, y) -> x + y;
		IntStream.rangeClosed(1, 100).reduce(sum).ifPresent(out::println);
//		LongBinaryOperator multiply = (x, y) -> 
//			x * y;
//		LongStream.rangeClosed(1, 100).reduce(multiply).ifPresent(out::println);	//<- the result is 0; Why? it has overflown around 70
		
//		LongStream.rangeClosed(1, 100).reduce((x, y) -> {
//			BigInteger xl = BigInteger.valueOf(x);
//			BigInteger yl = BigInteger.valueOf(y);
//			
//		});
		
		LongStream.rangeClosed(1, 100).forEach(x -> {
//			BigInteger xl = BigInteger.valueOf(x);
			longMultiply = longMultiply.multiply(BigInteger.valueOf(x));
		});
		out.printf("Multiple from 1 to 100 gives %s\n", longMultiply.toString());
		
		IntStream.rangeClosed(1, 100).boxed().map(BigInteger::valueOf).reduce(BigInteger::multiply).ifPresent(out::println);	//<- more concise way to calculate multiplication;
		
		IntStream.rangeClosed(1, 100).reduce(Integer::sum).ifPresent(out::println);
		out.println(IntStream.rangeClosed(1, 100).sum());
	}
	
	BigInteger longMultiply = BigInteger.ONE;
	
	@Ignore
	@Test
	public void testExtractMaxMin(){
		Random rand = new Random();
		List<Integer> ints = IntStream.generate(() -> rand.nextInt()).limit(10).boxed().collect(Collectors.toList());
		ints.stream().reduce(Integer::max).ifPresent(out::println);
		ints.stream().reduce(Integer::min).ifPresent(out::println);	//<- Stream has already been operated upon or closed;
	}
	
	
	@Ignore
	@Test
	public void testCountElements(){
		menus().map(d -> 1).reduce(Integer::sum).ifPresent(out::println);
		out.printf("Total number of dishes: %s\n", menus().count());
	}
	
	@Ignore
	@Test
	public void testLimitedSort(){
		Random rand = new Random();
		IntStream.generate(() -> rand.nextInt()).limit(10).sorted().forEach(out::print);	//<- limit first, sort next;
		IntStream.generate(() -> rand.nextInt()).sorted().limit(10).forEach(out::print);	//<- sort before limit: this code will keep running;
	}
	
	@Ignore
	@Test
	public void testDistinct(){
		Random rand = new Random();
		IntStream.generate(() -> rand.nextInt(10)).limit(20).distinct().sorted().forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testTransactions(){
		List<Transaction> transactions = transactions();
		//1, Find all transactions in the year 2011 and sort them by value;
		transactions.stream().filter(t -> 2011 == t.getYear()).sorted(Comparator.comparing(Transaction::getValue)).forEach(out::println);
		
		//2, What are all the unique cities where the traders work
		transactions.stream().map(Transaction::getTrader).map(Trader::getCity).distinct().forEach(out::println);
		//or:
		transactions.stream().map(t -> t.getTrader().getCity()).distinct().forEach(out::println);
		//or
		transactions.stream().map(t -> t.getTrader().getCity()).collect(Collectors.toSet()).stream().forEach(out::println);
		
		//3, Find all traders from Cambridge and sort them by name;
		transactions.stream().map(Transaction::getTrader).filter(t -> "Cambridge".equals(t.getCity())).distinct().sorted(Comparator.comparing(Trader::getName)).forEach(out::println);
		
		//4, Return a string of all traders' names sorted alphabetically
		transactions.stream().map(Transaction::getTrader).distinct().map(Trader::getName).sorted().forEach(out::println);
		//or: 
		out.println(transactions.stream().map(t -> t.getTrader().getName()).distinct().sorted()
//			.reduce(String::concat);
//			.reduce("", (l, r) -> l + ", " + r) //or format the result better;
			.collect(Collectors.joining(", "))
				);	
		
		
		//5, Are any traders based in Milan
		if(transactions.stream().map(Transaction::getTrader).anyMatch(t -> "Milan".equals(t.getCity()))){
			out.println("Yes, there is at least one trader based in Milan.");
		};
		//or:
		transactions.stream().anyMatch(t -> "Milan".equals(t.getTrader().getCity()));
		
		//6, Print all transaction's values from the traders living in Cambridge
		transactions.stream().filter(t -> "Cambridge".equals(t.getTrader().getCity())).map(Transaction::getValue).forEach(out::println);
		
		//7, What's highest value of all the transactions
		transactions.stream().map(Transaction::getValue).reduce(Integer::max).ifPresent(out::println);
		//or:
		transactions.stream().max(Comparator.comparing(Transaction::getValue)).ifPresent(t -> out.println(t.getValue()));
		
		//8, Find the transaction with the smallest value
//		transactions.stream().map(Transaction::getValue).reduce(Integer::min).ifPresent(out::println);	/It is the transaction record we needed here
		transactions.stream().reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2).ifPresent(out::println);	//<- this is the record we want here;
		transactions.stream().min(Comparator.comparing(Transaction::getValue)).ifPresent(out::println);
	}
	
	@Ignore
	@Test
	public void testFibonacci2(){
		Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]}).map(t -> t[0])
			.limit(10).forEach(out::println);
	}
	

	//------------------------------
	//---Domain: Transactions-------
	//------------------------------
	List<Transaction> transactions(){
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");
		List<Transaction> transactions = Arrays.asList(
			new Transaction(brian, 2011, 300),
			new Transaction(raoul, 2012, 1000),
			new Transaction(raoul, 2011, 400),
			new Transaction(mario, 2012, 710),
			new Transaction(mario, 2012, 700),
			new Transaction(alan, 2012, 950)
		);
		return transactions;
	}
	
	class Trader{
		final String name;
		final String city;
		public Trader(String name, String city) {
			super();
			this.name = name;
			this.city = city;
		}
		public String getName() {
			return name;
		}
		public String getCity() {
			return city;
		}
		@Override
		public String toString() {
			return "Trader [name=" + name + ", city=" + city + "]";
		}
	}
	class Transaction{
		final Trader trader;
		final int year;
		final int value;
		public Transaction(Trader trader, int year, int value) {
			super();
			this.trader = trader;
			this.year = year;
			this.value = value;
		}
		public Trader getTrader() {
			return trader;
		}
		public int getYear() {
			return year;
		}
		public int getValue() {
			return value;
		}
		@Override
		public String toString() {
			return "Transaction [trader=" + trader + ", year=" + year + ", value=" + value + "]";
		}
	}
	
	//-----------------------
	
	@Ignore
	@Test
	public void testGeneratePythagoreanTriples(){
		final int upperBound = 100;
		Stream<int[]> pythagoreanTriples = IntStream.rangeClosed(1, upperBound).boxed().flatMap(a -> 	
			//<- IntStream.flatMap can only return IntStream, thus IntStream should be boxed first
			IntStream.rangeClosed(a, upperBound).filter(b -> 0 == (Math.sqrt(a * a + b * b) % 1))
				.mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
		);
		
		pythagoreanTriples.forEach(t -> out.printf("[%s, %s, %s]\n",t[0], t[1], t[2]));
	}
	
	@Ignore
	@Test
	public void testPythagoreanTriples2(){
		final int upperBound = 100;
		IntStream.rangeClosed(1, upperBound).boxed().flatMap(a -> 
			IntStream.rangeClosed(a, upperBound).mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
				.filter(t -> 0 == t[2] % 1)	//check if the number is an integer
		).forEach(t -> out.printf("[%s, %s, %s]\n", (int)t[0], (int)t[1], (int)t[2]));
	}
	
	@Ignore
	@Test
	public void testStreamFromValues(){
		Stream.of("Hello", "World").map(String::toUpperCase).forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testStreamNumbers(){
		IntSummaryStatistics is = Arrays.stream(new int[]{2, 3, 5, 7, 11, 13}).summaryStatistics();
		out.printf("Sum of the array: %s\n", is.getSum());
	}
	
	@Ignore
	@Test
	public void testStreamFromFile() throws IOException{
		Path p = Paths.get("test", getClass().getName().replaceAll("\\.", "/") + ".java");
		try(Stream<String> lines = Files.lines(p, Charset.defaultCharset());){
			List<String> words = lines.flatMap(l -> Arrays.stream(l.split("\\s+"))).collect(Collectors.toList());
			out.printf("Word counts for file %s is %s, \nunique words: %s (counted with distinct())\nOr %s (counted with set)", 
						p, words.stream().count(), words.stream().distinct().count(), 
						words.stream().collect(Collectors.toSet()).size()
						);
		}
	}
	
	@Ignore
	@Test
	public void testFibonacciThroughIterate(){
		Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
			.map(t -> t[0])
			.limit(10).forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testGenerate(){
		Stream.generate(Math::random)
			.limit(10)	//<- comment out this line to print out random number forever
			.forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testGenerateWithState(){
		IntSupplier s = new IntSupplier() {
			int previous = 0;
			int current = 1;	//<- record the states;
			
			@Override
			public int getAsInt() {
				int retval = previous;
				int next = current + previous;
				previous = current;
				current = next;
				return retval;
			}
		};
		
		IntStream.generate(s).limit(10).forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testGrouping(){
		transactions().stream().collect(Collectors.groupingBy(
				Transaction::getTrader, 	
				Collectors.mapping(Transaction::getValue, Collectors.toList()))
			).forEach((t, lv) -> out.printf("%s from %s - %s\n", t.getName(), t.getCity(), lv));
		
		transactions().stream().collect(Collectors.groupingBy(
				t -> t.getTrader().getName(), 	
//				(Supplier<TreeMap<String, List<Integer>>>)	//In case the Compiler failed to recognize the FunctionalInterface, may need to specify it explicitly;
				TreeMap::new,
				Collectors.mapping(Transaction::getValue, Collectors.toList()))
				).forEach((t, lv) -> out.printf("%s - %s\n", t, lv));
	}
	
	@Ignore
	@Test
	public void testCollect(){
//		IntStream.range(0, 10).collect(() -> 0, (l, i) -> l + i, (Integer l, Integer r) -> l + r);
		out.println(s.get().collect(Collectors.summarizingInt(i -> i)));
		
//		Function<Integer, Integer> id = Function.<Integer>identity();
//		Function<Integer, Integer> id2 = Function.identity();
		out.println(s.get().collect(Collectors.counting()));
		out.println(s.get().collect(Collectors.summingInt(i -> i)));
		out.println(s.get().collect(Collectors.minBy(Integer::compare)).get());
		out.println(s.get().collect(Collectors.averagingInt(i -> i)));
		out.println(s.get().collect(Collectors.maxBy(Integer::compare)).get());
		
		out.println(s.get().map(i -> Integer.toString(i)).collect(Collectors.joining(", ", "[", "]")));
		out.println(s.get().map(i -> i.toString()).collect(Collectors.reducing((i, j) -> i + ", " + j)).get());
		
	}
	
	@Ignore
	@Test
	public void testReducing(){

		//
		out.println(s.get().reduce(Integer::sum).get());
		out.println(s.get().collect(Collectors.summingInt(i -> i)));
		out.println(s.get().collect(Collectors.reducing(Integer::sum)).get());	//<------------------------
	}
	
	
	@Ignore
	@Test
	public void testGroupingBy(){
		out.println(menus().mapToInt(Dish::getCalories).sum());
//		IntStream.range(0, 10).boxed().mapToInt(Function.identity());	//<- compile error: cannot convert from Function to ToIntFunction;
		IntStream.range(0, 10).boxed().mapToInt(i -> i);
		
		menus().collect(
				Collectors.collectingAndThen(
						Collectors.groupingBy(Dish::getType, Collectors.partitioningBy(Dish::isVegetarian))
						,
						m -> {
							m.entrySet().forEach(out::println); 
							return m.keySet();
						}
					)
				
			).forEach(out::println);
		//
//		Collectors.groupingB
		menus().collect(Collectors.groupingBy(Dish::getType, Collectors.summingInt(Dish::getCalories))).entrySet().forEach(out::println);
		//
	}
	
	@Ignore
	@Test
	public void testGroupingBy2(){
//		Function<Dish, CalorieLevel> classifier = d -> {
//			if(400 >= d.getCalories()){
//				return CalorieLevel.DIET;
//			}else if(700 >= d.getCalories()){
//				return CalorieLevel.NORMAL;
//			}else{
//				return CalorieLevel.FAT;
//			}
//		};
		Function<Dish, CalorieLevel> classifier = CalorieLevel::getCalorieLevel;
		menus().collect(Collectors.groupingBy(classifier)).entrySet().forEach(out::println);;
		out.println();
		menus()
//		.collect(Collectors.groupingBy(Dish::getType, 
//				Collectors.groupingBy(classifier)
//				))
//		.collect(Collectors.groupingBy(Dish::getType
//				,
//				Collectors.collectingAndThen(
//							Collectors.groupingBy(classifier), 
//							m -> m.values().stream().map(ld -> ld.stream().map(Dish::getName).collect(Collectors.toList())).collect(Collectors.toList())
//						)
//				
//				))
		.collect(Collectors.groupingBy(Dish::getType, 
				Collectors.groupingBy(classifier,
						Collectors.mapping(Dish::getName, Collectors.toList())
						)
				))
		.entrySet().forEach(out::println);
//		.forEach((k, v) -> out.printf("%s = {%s}\n", k, 
//				v.values().stream().map(Dish::getName).collect(Collectors.joining(", ", "{", "}"))
//				));
		
		
		out.println();
		out.println();
		menus().collect(Collectors.groupingBy(Dish::getType, Collectors.summingInt(Dish::getCalories))).entrySet().forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testGroupingBy3(){
		//<- Find Dish with highest calories for each Dish Type
		menus().collect(Collectors.groupingBy(Dish::getType,  Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)))).entrySet().forEach(out::println);
		
		out.println();
		menus().collect(Collectors.groupingBy(Dish::getType, 
				Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)	//<- Collect and extract the optinoal value
				)).entrySet().forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testGroupingAndMapping(){
		menus().collect(Collectors.groupingBy(Dish::getType,
				Collectors.mapping(CalorieLevel::getCalorieLevel, 
//						Collectors.toSet()
						Collectors.toCollection(HashSet::new)	//<- specify the type of Set
					)
			)).entrySet().forEach(out::println);
//		HashSet.class;
	}
	
	
	@Ignore
	@Test
	public void testPartition(){
		menus().collect(Collectors.partitioningBy(Dish::isVegetarian
				, Collectors.groupingBy(Dish::getType
						, 
						Collectors.mapping(Dish::getName, Collectors.toList())
						)
				)).entrySet().forEach(out::println);
		
		menus().collect(Collectors.partitioningBy(Dish::isVegetarian	//partition with isVegetarian
//				, Collectors.collectingAndThen(
//						Collectors.maxBy(Comparator.comparing(Dish::getCalories))
//						, Optional::get)
				, Collectors.collectingAndThen(
					Collectors.collectingAndThen(
						Collectors.maxBy(Comparator.comparing(Dish::getCalories))	//<- collect through "maxBy";
						, Optional::get) 											//Then get the optional value
					, Dish::getName													//Then get the dish name
					)
				)).entrySet().forEach(out::println);;
				
		menus().collect(Collectors.partitioningBy(Dish::isVegetarian
				, Collectors.counting()
				)).entrySet().forEach(out::println);
	}
	
	@Ignore
	@Test
	public void testPartition2(){	//Parition prime numbers;
		Predicate<Integer> isPrime = i -> !IntStream.rangeClosed(2, (int) Math.sqrt(i)).anyMatch(j -> 0 == i%j);
		IntStream.rangeClosed(2, 100).boxed().collect(Collectors.partitioningBy(isPrime))
			.entrySet().forEach(out::println);
	}
	
	
	@Ignore
	@Test
	public void testCollectorsFactoryMethodSummary(){
		//toList
		menus().collect(Collectors.toList());
		
		//toSet
		menus().collect(Collectors.toSet());
		
		//toCollection
//		menus().collect(Collectors.toCollection(() -> new TreeSet<>()));
		menus().collect(Collectors.toCollection(TreeSet<Dish>::new));
		
		//counting
		menus().collect(Collectors.counting());
		
		//summingInt
		menus().collect(Collectors.summingInt(Dish::getCalories));
		
		//averageInt
		menus().collect(Collectors.averagingInt(Dish::getCalories));
		
		//summarizingInt
		menus().collect(Collectors.summarizingInt(Dish::getCalories));
		
		//join
		menus().map(Dish::getName).collect(Collectors.joining(", "));
		
		//maxBy
		menus().collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)));
		
		//minBy
		menus().collect(Collectors.minBy(Comparator.comparingInt(Dish::getCalories)));
		
		//reducing
		menus().collect(Collectors.reducing(0, Dish::getCalories, Integer::sum));
		
		//collectingAndThen
		menus().collect(Collectors.collectingAndThen(Collectors.toList(), List::size));
		
		//groupingBy
		menus().collect(Collectors.groupingBy(Dish::getType));
		
		//partitioningBy
		menus().collect(Collectors.partitioningBy(Dish::isVegetarian));
		
	}
	
	@Ignore
	@Test
	public void testCollecting(){
//		menus().collect(
//				ArrayList::new,
//				List::add,
//				(l1, l2) -> {l1.addAll(l2); return l1;}
//				);
//		menus().collect(Collections::emptyList, List::add, List::add);	//<- the compiler cannot deduce the generic type here, since thre is not type to return;
		
		List<Dish> dishes = menus().collect(ArrayList::new, List::add, List::addAll);	//<- this will work;
	}
	
	@Ignore
	@Test
	public void testCollectorInterface(){
		DoubleStream.generate(Math::random).limit(10).boxed()
			.collect(new ToListCollector<Double>())			//1
			.stream().forEach(out::println);
		;
		
		DoubleStream.generate(Math::random).limit(10).boxed()
		.collect(new ToListCollector2<Double>())			//2
		.stream().forEach(out::println);
		;
		
		Collector<Double, List<Double>, List<Double>> toListCollector3 = Collector.of(ArrayList::new, 
				List::add, 
				(t1, t2) -> {t1.addAll(t2); return t1;}, 
				Characteristics.IDENTITY_FINISH);
		DoubleStream.generate(Math::random).limit(10).boxed()
		.collect(toListCollector3)							//3
		.stream().forEach(out::println);
		;
		
		
	}
	
	static class ToListCollector2<T> implements Collector<T, List<T>, List<T>>{

		@Override
		public Supplier<List<T>> supplier() {
			return ArrayList::new;
		}

		@Override
		public BiConsumer<List<T>, T> accumulator() {
//			return List<T>::add;
			return List::add;
		}

		@Override
		public BinaryOperator<List<T>> combiner() {
			return (t1, t2) -> {
				t1.addAll(t2);
				return t1;
			};
		}

		@Override
		public Function<List<T>, List<T>> finisher() {
			return Function.identity();
		}

		@Override
		public Set<Characteristics> characteristics() {
//			return EnumSet.<Collector.Characteristics>of(Characteristics.IDENTITY_FINISH);	//or
			return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));	//<- this set should be immutable
		}
		
	}
	
	static class ToListCollector<T> implements Collector<T, List<? super T>, List<? super T>>{

		@Override
		public Supplier<List<? super T>> supplier() {
			return ArrayList::new;
		}

		@Override
		public BiConsumer<List<? super T>, T> accumulator() {
			return (a, t) -> a.add(t);
		}

		@Override
		@SuppressWarnings("unchecked")
		public BinaryOperator<List<? super T>> combiner() {
			return (t1, t2) -> {
				t2.stream().forEach(e -> t1.add((T)e));
				return t1;
			};
		}

		@Override
		public Function<List<? super T>, List<? super T>> finisher() {
			return Function.identity();
		}

		@Override
		public Set<Characteristics> characteristics() {
			return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
		}

		
	}
	
	enum CalorieLevel{
		DIET, NORMAL, FAT
		;
		
		static CalorieLevel getCalorieLevel(Dish d){
			if(400 >= d.getCalories()){
				return CalorieLevel.DIET;
			}else if(700 >= d.getCalories()){
				return CalorieLevel.NORMAL;
			}else{
				return CalorieLevel.FAT;
			}
		}
	
	};
	
	private Supplier<Stream<Integer>> s = () -> IntStream.range(0, 100).boxed();
	
	
	@Ignore
	@Test
	public void testCollector2(){
		IntStream.rangeClosed(2, 100).boxed().collect(new PrimeNumberCollector()).entrySet().forEach(out::println);
	}
	
	/**
	 * i: number to test whether it is prime;
	 * primes: previously tested prime number
	 */
	final BiPredicate<Integer, Collection<Integer>> isPrime = (i, primes) -> {
		return !primes.stream().filter(j -> j <= Math.sqrt(i)).anyMatch(j -> 0 == i %j);
	};
	
	class PrimeNumberCollector implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>>{

		@Override
		public Supplier<Map<Boolean, List<Integer>>> supplier() {
			return () -> new HashMap<Boolean, List<Integer>>(){
				private static final long serialVersionUID = 8686539634523436098L;

				{
					put(true, new ArrayList<Integer>());
					put(false, new ArrayList<Integer>());
				}
			};
		}

		@Override
		public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
			return (m, i) -> m.get(isPrime.test(i, m.get(true))).add(i);
		}

		@Override
		public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
//			throw new UnsupportedOperationException();	//<- This collector doesn't support parallelism;
			return (m1, m2) -> {
				m1.get(true).addAll(m2.get(true));
				m1.get(false).addAll(m2.get(false));
				return m1;
			};
		}

		@Override
		public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
			return Function.identity();
		}

		@Override
		public Set<Characteristics> characteristics() {
//			return null;
			return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
		}
		
	}
	
	
	@Ignore
	@Test
	public void testParallelStream(){
		Stream.iterate(1L, i -> i + 1).limit(100_000).parallel().reduce(Long::sum).ifPresent(out::println);
		out.println(LongStream.rangeClosed(0, 1000_000_000).parallel().sum());										//Totally OK
//		LongStream.iterate(1, i -> i + 1).limit(100_000_000).parallel().reduce(Long::sum).ifPresent(out::println);	//OutOfMemoryError
				//<- note that iterate is highly in-effective for parallelism;
		
		//Compare the used time 
//		out.println(LongStream.rangeClosed(0, 1000_000_000).sum());
	}
	
	
	@Test
	public void testSideEffectParallelSum(){
		class Accumulator{
			long sum = 0;
			void add(long x){
				sum += x;
			}
		}
		Accumulator accumulator = new Accumulator();
		
		IntStream.range(0, 10).forEach(i -> {
			Accumulator a = new Accumulator();
			LongStream.rangeClosed(1, 1000_000).parallel().forEach(a::add);
			out.println(a.sum);	//<- WARNING: wrong result for side effect;
		});
	}
	
	private static final PrintStream out = System.out;
	
	class FibonacciGenerator{
		@Deprecated
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
		
		
		//
		Stream<Integer> fibonacci2(){
			return Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]}).map(t -> t[0])
//					.mapToInt(i -> i).boxed()
					;
		}
		
		//
		Stream<Integer> fibonacci3(){
			IntSupplier s = new IntSupplier() {
				int previous = 0;
				int current = 1;	//<- record the states;
				
				@Override
				public int getAsInt() {
					int retval = previous;
					int next = current + previous;
					previous = current;
					current = next;
					return retval;
				}
			};
			
			return IntStream.generate(s).boxed();
		}
	}
	
	
}



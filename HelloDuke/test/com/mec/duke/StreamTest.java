package com.mec.duke;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
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
	
	private static final PrintStream out = System.out;
}



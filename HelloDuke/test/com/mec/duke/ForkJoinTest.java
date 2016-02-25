package com.mec.duke;

import java.io.PrintStream;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

public class ForkJoinTest {

	@Ignore
	@Test
	public void testForJoin() {
		ForkJoinPool pool = ForkJoinTask.getPool();
		ForkJoinTask<?> t1 = ForkJoinTask.adapt(() -> out.println("Hello "));
//		ForkJoinTask<Object> t2 = ForkJoinTask.adapt(() -> out.println("Hello "));
		out.println(t1.fork().join());	//<- prints null;
		ForkJoinTask.invokeAll();
		ForkJoinTask.invokeAll(
//				Stream.generate(() -> ForkJoinTask.adapt(() -> out.println(Math.random()))
				Stream.iterate(1, i -> i + 1).map(i -> ForkJoinTask.adapt(() -> out.println(i))
				).limit(10).collect(Collectors.toList()))
			;
		ForkJoinTask.invokeAll(ForkJoinTask.adapt(() -> out.println(", World!")));
	}

	
	
	private static final PrintStream out = System.out;
}

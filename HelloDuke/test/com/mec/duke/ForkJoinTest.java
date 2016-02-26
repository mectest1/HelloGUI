package com.mec.duke;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
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

	@Test
	public void testForkJoinSumCalculator(){
		long[] numbers = LongStream.rangeClosed(0, 10_000_120).toArray();
		ForkJoinSumCalculator calcTask = new ForkJoinSumCalculator(0, numbers.length, numbers);
//		out.println(calc.compute());	//StackOverflowError
		out.println(new ForkJoinPool().invoke(calcTask));
		
		//
		out.println(Arrays.stream(numbers).reduce(Long::sum).getAsLong());	//<- Another calculate method;
	}
	
	
	private static final PrintStream out = System.out;
	
	
	static class ForkJoinSumCalculator extends RecursiveTask<Long>{
		private static final long serialVersionUID = -5129270376710207717L;
		
		int start;
		int end;
		long[] numbers;
		
		final static int SPLIT_THRESHOLD = 10_000;
		
		ForkJoinSumCalculator(long[] numbers){
			this(0, numbers.length, numbers);
		}

		ForkJoinSumCalculator(int start, int end, long[] numbers){
			this.start = start;
			this.end = end;
			this.numbers = numbers;
		}
		
		@Override
		protected Long compute() {
			int length = end - start;
			if(SPLIT_THRESHOLD >= length){
				return computeSequentially();
			}
//			ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(start, length/2, numbers);	//<-- error
			ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(start, start + length/2, numbers);
			leftTask.fork();
			
//			ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(length/2, end, numbers);
			ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(start + length/2, end, numbers);	//<---- error
			long rightResult = rightTask.compute();
//			long rightResult = rightTask.fork().join();
			long leftResult = leftTask.join();
			
			return leftResult + rightResult;
		}

		
		long computeSequentially(){
			long sum = 0;
			for(int i = start; i < end; ++i){
				sum += numbers[i];
			}
			return sum;
		}
	}
	
}




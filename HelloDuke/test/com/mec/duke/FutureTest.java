package com.mec.duke;

import java.io.PrintStream;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

public class FutureTest {

	@Test
	public void testFuture() {
//		fail("Not yet implemented");
//		CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> "Hello, World!");
//		cf.thenAccept(out::println);
		//ref: https://community.oracle.com/docs/DOC-921264
		CompletableFuture.supplyAsync(this::greeting).thenAccept(out::println);
	}

	private String greeting(){
		return "Hello, World!";
	}
	
	private static final PrintStream out = System.out;
}

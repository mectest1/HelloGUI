package com.mec.duke;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

public class CompletableFutureTest {

	@Ignore
	@Test
	public void testClassicFutureCall() {
		Shop shop = new Shop("BestShop");
		long start = System.nanoTime();
		Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
		long invocationTime = (System.nanoTime() - start) / 1_000_000;
		out.printf("Invication returned after %s msecs\n", invocationTime);
		
		//Do some more tasks, like query other shops;
		//doSomethingElse();
		//while the price ofthe product is being caslculated
		try{
			double price = futurePrice.get();		//<- The client code will be blocked here for a long time
			out.printf("Price is %.2f%n", price);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		long retrievalTime = (System.nanoTime() - start) / 1_000_000;
		out.printf("Price returned after %s msecs\n", retrievalTime);
	}
	
	@Ignore
	@Test
	public void testClassicFutureCall2() {
		Shop shop = new Shop("BestShop");
		long start = System.nanoTime();
//		Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
		Future<Double> futurePrice = shop.getPriceAsync2("my favorite product");
		long invocationTime = (System.nanoTime() - start) / 1_000_000;
		out.printf("Invication returned after %s msecs\n", invocationTime);
		
		//Do some more tasks, like query other shops;
		//doSomethingElse();
		//while the price ofthe product is being caslculated
		try{
			double price = futurePrice.get();		//<- The client code will be blocked here for a long time
			out.printf("Price is %.2f%n", price);
		}catch(Exception e){
//			throw new RuntimeException(e);
			e.printStackTrace(out);
		}
		
		long retrievalTime = (System.nanoTime() - start) / 1_000_000;
		out.printf("Price returned after %s msecs\n", retrievalTime);
	}

	@Ignore
	@Test
	public void testFindPrices(){
		final String product = "myPhone27S";
		checkDuration(()->{
			List<String> prices = shops().stream().map(shop -> 
				String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
				).collect(Collectors.toList());
			out.println(prices);
		});
	}
	
	@Ignore
	@Test
	public void testFindPricessParallel(){
		final String product = "myPhone27S";
		checkDuration(()->{
			List<String> prices = shops().parallelStream().map(shop -> 	//<- Use parallel stream instead of plain stream
				String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
				).collect(Collectors.toList());
			out.println(prices);
		});
	}
	
	
	@Test
	public void testFindPricessWithCompletableFutureStream(){
		final String product = "myPhone27S";
		checkDuration(()->{
			List<CompletableFuture<String>> priceFutures = shops().stream().map(shop -> CompletableFuture.supplyAsync(() -> 
				String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
				)).collect(Collectors.toList());
			
			out.println(priceFutures.stream()
//					.map(CompletableFuture<String>::get)	//<- unhandled exception
					.map(CompletableFuture::join)
					.collect(Collectors.toList()));
		});
	}
	
	
	
	static void checkDuration(Runnable runnable){
		long start = System.nanoTime();
		
		runnable.run();
		
		long duration = (System.nanoTime() - start)/1_000_000;
		out.printf("Done in %s msecs\n", duration);
	}
	
	static List<Shop> shops(){
		return Arrays.asList(new Shop("BestPrice")
				, new Shop("LetsSaveBig")
				, new Shop("MyFavoriteShop")
				, new Shop("BuyitAll")
				);
	}
	
	//---------------------
	static class Shop{
		String name;
		Shop(String name){
			this.name = name;
		}
		
		public String getName(){
			return name;
		}
		
		//Blocking method;
		public double getPrice(String product){
			return calculatePrice(product);
		}
		
		public Future<Double> getPriceAsync(String product){
			CompletableFuture<Double> futurePrice = new CompletableFuture<>();
			new Thread(() -> {
				double price = getPrice(product);
				futurePrice.complete(price);		//<- Q: What if this Future never gets a change to complete?
													//A: Client that call Future.get() will never return;
			}).start();
			return futurePrice;
		}
		
		
		public Future<Double> getPriceAsync2(String product){
			CompletableFuture<Double> futurePrice = new CompletableFuture<>();
			ForkJoinPool.commonPool().execute(() -> {
				try{
					double price = getPrice(product);
					throw new Exception("No Such Product");
//					futurePrice.complete(price);
					
//					throw new Exception("No Such Product");	//<- This exception is thrown after the future has completed;
															//Thus it will be be caught by the CompletableFuture;
				}catch(Exception e){
					futurePrice.completeExceptionally(e);
				}
			});
			
			return futurePrice;
		}
		
		public Future<Double> getPriceAsync3(String product){
			return CompletableFuture.supplyAsync(() -> calculatePrice(product));	//<- provide the same error management as in getPriceAsync2();
				//Refer to CompletableFuture$AsyncSupply.run() for implementation details.
		}
		double calculatePrice(String product){
			product = Optional.<String>of(product).orElse("");
			delay();	//simulate time-consuming process;
			return rand.nextDouble() * product.charAt(0) + product.charAt(1);
		}
		
		static final Random rand = new Random();
		
		static void delay(){
			try{
				Thread.sleep(1000L);
			}catch(InterruptedException e){
				throw new RuntimeException();
			}
		}
	}
	
	
	
	private static final PrintStream out = System.out;
	
}

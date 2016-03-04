package com.mec.duke;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Function;
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
		checkDuration(()->{
			List<String> prices = shops2().stream().map(shop -> 
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
				).collect(Collectors.toList()); //<- costs 1028 msecs;
			out.println(prices);
		});
		checkDuration(()->{
			List<String> prices = shops2().parallelStream().map(shop -> 	//<- Use parallel stream instead of plain stream
			String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
					).collect(Collectors.toList()); //<- cost 2002 msecs, 100% time increase for only 1 shop, LOL;
			out.println(prices);
		});
	}
	
	
	@Ignore
	@Test
	public void testFindPricessWithCompletableFutureStream(){
		final String product = "myPhone27S";
		checkDuration(()->{
			List<CompletableFuture<String>> priceFutures = shops().stream().map(shop -> CompletableFuture.supplyAsync(() -> 
				String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
				)).collect(Collectors.toList()); //<- Collect these ComputableFuture into List first;
			
			out.println(priceFutures.stream()
//					.map(CompletableFuture<String>::get)	//<- unhandled exception
					.map(CompletableFuture::join)
					.collect(Collectors.toList()));
		});
		checkDuration(()->{
			List<CompletableFuture<String>> priceFutures = shops2().stream().map(shop -> CompletableFuture.supplyAsync(() -> 
			String.format("%s price is %.2f", shop.getName(), shop.getPrice(product))
					)).collect(Collectors.toList());
			
			out.println(priceFutures.stream()
//					.map(CompletableFuture<String>::get)	//<- unhandled exception
					.map(CompletableFuture::join)
					.collect(Collectors.toList()));
		});
	}
	
	@Test
	public void testCustomExecutor(){
//		final Executor executor = Executors.newFixedThreadPool(
//				Math.min(shops().size(), 100),
//				r -> {
//					Thread t = new Thread(r);
//					t.setDaemon(true);
//					return t;
//				});
		final Function<Integer, Executor> executorSupplier = size -> 
				Executors.newFixedThreadPool(
					Math.min(size, 100),	//or Integer.max()
					r -> {
						Thread t = new Thread(r);
						t.setDaemon(true);
						return t;
				});
				
		final Executor executor = executorSupplier.apply(shops().size());
		
		final String product = "myPhone27S";
		checkDuration(() -> {
			List<CompletableFuture<String>> futures = shops().stream()
				.map(shop -> CompletableFuture.supplyAsync(
					() -> String.format("%s price is %s", shop.getName(), shop.getPrice(product))
					, executor))
				.collect(Collectors.toList());
			
			out.println(futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
		});
		checkDuration(() -> {
			List<CompletableFuture<String>> futures = shops2().stream()
					.map(shop -> CompletableFuture.supplyAsync(
							() -> String.format("%s price is %s", shop.getName(), shop.getPrice(product))
							, executor))	//<-----------------executor with awkward thread pool size;
					.collect(Collectors.toList());
			
			out.println(futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
		});
		
		//--------------------------------------------------------
		final Executor executor2 = executorSupplier.apply(shops2().size());
		checkDuration(() -> {
			List<CompletableFuture<String>> futures = shops2().stream()
					.map(shop -> CompletableFuture.supplyAsync(
							() -> String.format("%s price is %s", shop.getName(), shop.getPrice(product))
							, executor2))
					.collect(Collectors.toList());
			
			out.println(futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
		});
	}
	
	
	
	static void checkDuration(Runnable runnable){
		long start = System.nanoTime();
		
		runnable.run();
		
		long duration = (System.nanoTime() - start)/1_000_000;
		out.printf("Done in %s msecs\n", duration);
	}
	
	static List<Shop> shops(){	//<- Note: number of shops coplies with number of CPU cores
		return Arrays.asList(new Shop("BestPrice")
				, new Shop("LetsSaveBig")
				, new Shop("MyFavoriteShop")
				, new Shop("BuyitAll")
				);
	}
	
	static List<Shop> shops2(){
		return Arrays.asList(new Shop("BestPrice")
				, new Shop("LetsSaveBig")
				, new Shop("MyFavoriteShop")
				, new Shop("BuyitAll")
				, new Shop("AmazonIsHere")
//				, new Shop("WhoIsCallingEBay")
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

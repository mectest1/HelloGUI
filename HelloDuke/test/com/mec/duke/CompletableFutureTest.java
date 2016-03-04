package com.mec.duke;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import com.mec.duke.CompletableFutureTest.Discount.Code;

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
	
	@Ignore
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
	
	//-------------------------------------------------------------------------------
	@Ignore
	@Test
	public void testGetPrices2(){
		final String product = "myPhone27S";
		checkDuration(() -> {
			out.println(
					shops2().stream().map(shop -> shop.getPrice2(product))
					.map(Quote::parse)
					.map(Discount::applyDiscount)
					.collect(Collectors.toList())
					);
		});
		
		
		checkDuration(() -> {
			out.println(
					shops2().parallelStream()
					.map(shop -> shop.getPrice2(product))
					.map(Quote::parse)
					.map(Discount::applyDiscount)
					.collect(Collectors.toList())
					);
		});
	}
	
	
	@Ignore
	@Test
	public void testCompositeFuture(){
		final String product = "myPhone27S";
		final Function<Integer, Executor> executorSupplier = size -> 
		Executors.newFixedThreadPool(
			Math.min(size, 100),	//or Integer.max()
			r -> {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
		});
		
		final Executor executor = executorSupplier.apply(shops2().size());
		
		checkDuration(()->{
			List<CompletableFuture<String>> priceFutures = 
					shops2().stream()
					.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice2(product), executor))
					.map(future -> future.thenApply(Quote::parse))
					.map(future -> future.thenCompose(quote -> 	//<- compose: kinda like Stream.flatMap
						CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
//					.map(future -> future.thenApply(quote -> 
//						CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
						.collect(Collectors.toList());
			out.println(priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
		});
					
	}
	
	@Ignore
	@Test
	public void testCombineFutures(){
		final String product = "myPhone27S";
		
		DoubleSupplier getRate = () -> {
			delay();
			return Math.random();
		};
		
		
		Shop shop = shops().get(0);
		
		checkDuration(() -> {	//The second CompletableFuture dependes on the first one
			CompletableFuture<Double> futurePrice = CompletableFuture.supplyAsync(() -> shop.getPrice(product))
					.thenCompose(price -> 
						CompletableFuture.supplyAsync(() -> 
							price * getRate.getAsDouble()
						)
					);
			out.println(futurePrice.join());
		});
		
		
		checkDuration(()->{	//The two CompletableFutures are independent here;
			CompletableFuture<Double> futurePrice = CompletableFuture.supplyAsync(() -> shop.getPrice(product))
					.thenCombine(CompletableFuture.supplyAsync(() -> getRate.getAsDouble()), 
							(price, rate) -> price * rate
						)
					;
//			out.println(futurePrice.get());	//<- unhandled exception
			out.println(futurePrice.join());
		});
		
	}
	
	
	@Ignore
	@Test
	public void testCombineFuturesInJDK7(){
		final String product = "myPhone27S";
		
		DoubleSupplier getRate = () -> {
			delay();
			return Math.random();
		};
		ExecutorService executor = Executors.newCachedThreadPool();
		
		Shop shop = shops().get(0);
		
		checkDuration(() -> {
			final Future<Double> futureRate = executor.submit(new Callable<Double>(){

				@Override
				public Double call() {
					return getRate.getAsDouble();
				}
				
			});
			
			final Future<Double> futurePriceInUSD = executor.submit(new Callable<Double>(){

				@Override
				public Double call() throws Exception{
					double price = shop.getPrice(product);
					return price * futureRate.get();
				}
				
			});
			
			try {
				out.println("Executed in Java 7 grammar");
				out.println(futurePriceInUSD.get());
			} catch (Exception e) {
				e.printStackTrace(out);
			}
		});
	}
	
	@Test
	public void testOnComplete(){
		final String product = "myPhone27S";
		final Function<Integer, Executor> executorSupplier = size -> 
		Executors.newFixedThreadPool(
			Math.min(size, 100),	//or Integer.max()
			r -> {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
		});
		
		final Executor executor = executorSupplier.apply(shops2().size());
		setDelayType(DelayType.RANDOM);
		checkDuration(() -> {
			
			Stream<CompletableFuture<String>> priceFutures = shops2().stream()
					.map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice2(product), executor))
					.map(future -> future.thenApply(Quote::parse))
					.map(future -> future.thenCompose(quote -> 
					CompletableFuture.supplyAsync(
							() -> Discount.applyDiscount(quote)
							, executor)
							))
					;
			
			//		CompletableFuture[] futures = priceFutures.toArray(size -> new CompletableFuture[size]);
			long start = System.nanoTime();
			@SuppressWarnings("unchecked")
			CompletableFuture<Void>[] futures = priceFutures
//			.map(f -> f.thenAccept(out::println))	//<----Invoked when the CompletableFuture finishes;
			.map(f -> f.thenAccept(s -> {
				out.printf("%s (done in %s msecs)\n\n", s, (System.nanoTime() - start ) /1_000_000);
			}))	//<----Invoked when the CompletableFuture finishes;
			.toArray(size -> new CompletableFuture[size]);
			CompletableFuture.allOf(futures).join();	//<- wait all tasks to complete
//			CompletableFuture.anyOf(futures).join();	//<- wait for the first one to complete
		});
		
		setDelayType(DelayType.NORMAL);
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
	static class Quote{
		final String shopName;
		final double price;
		final Discount.Code discountCode;
		public Quote(String shopName, double price, Code discountCode) {
			super();
			this.shopName = shopName;
			this.price = price;
			this.discountCode = discountCode;
		}
		static Quote parse(String s){
			Objects.requireNonNull(s);
			String[] split = s.split(":");
			String shopName = split[0];
			double price = Double.parseDouble(split[1]);
			Discount.Code discountCode = Discount.Code.valueOf(split[2]);
			return new Quote(shopName, price, discountCode);
		}
		public final String getShopName() {
			return shopName;
		}
		public final double getPrice() {
			return price;
		}
		public final Discount.Code getDiscountCode() {
			return discountCode;
		}
		
		//
	}
	static class Discount{
		enum Code{
			NONE(0)
			,SILVER(5)
			,GOLD(10)
			,PLATINUM(15)
			,DIAMOND(20)
			;
			private final int percentage;
			private Code(int percentage){
				this.percentage = percentage;
			}
		}
		public static String applyDiscount(Quote quote){
			return String.format("%s price is %s", quote.getShopName(), 
					apply(quote.getPrice(), quote.getDiscountCode())
					);
		}
		static double apply(double price, Code code){
			delay();	//Simulate a delay in the Discount service response;
			return price * (100 - code.percentage)/100;
		}
	}
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
		
		
		//---------------
		String getPrice2(String product){
			double price = calculatePrice(product);
			Discount.Code code = Discount.Code.values()[rand.nextInt(Discount.Code.values().length)];
			return String.format("%s:%.2f:%s", name, price, code);
		}
		
		
		
		static final Random rand = new Random();
		
		
	}
	static void delay(){
//		try{
//			Thread.sleep(1000L);
//		}catch(InterruptedException e){
//			throw new RuntimeException();
//		}
		delayType.delay();
	}
	
	
	void setDelayType(DelayType delayType){
		this.delayType = Optional.ofNullable(delayType).orElse(DelayType.NORMAL);
	}
	static DelayType delayType = DelayType.NORMAL;
	static enum DelayType{
		NORMAL(() -> {
			try{
				Thread.sleep(1000L);
			}catch(InterruptedException e){
				throw new RuntimeException();
			}
		})
		,RANDOM(() -> {
			try{
				Thread.sleep(500 + rand.nextInt(2000));
			}catch(Exception e){
				e.printStackTrace(out);
			}
		})
		;
		
		Runnable run;
		private DelayType(Runnable run){
			this.run = run;
		}
		
		void delay(){
			run.run();
		}
	}
	
	static final Random rand = new Random();
	private static final PrintStream out = System.out;
	
}

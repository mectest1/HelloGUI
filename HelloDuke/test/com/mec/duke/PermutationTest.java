package com.mec.duke;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

/**
 * ref: ref: http://www.programcreek.com/2013/02/leetcode-permutations-java/
 * @author MEC
 *
 */
public class PermutationTest {

	@Ignore
	@Test
	public void testOrdinal() {
		Arrays.asList(Color.values()).forEach(c -> {
			out.printf("ordinal: %s, color: %s\n", c.ordinal(), c.name());
		});
	}
	
	@Ignore
	@Test
	public void testPermutation(){
		List<Integer> indices = Arrays.asList(0, 1, 2);
		List<List<Integer>> permutationResult = Permutation.permutate(indices);
		permutationResult.forEach(out::println);
		out.printf("permutation result size: %s\n", permutationResult.size());
	}
	
	
	
	/**
	 * Try to solve the house problem by brutal force, without <strong>any</strong> optimization. 
	 */
//	@Ignore
	@Test
	public void trySolveHouseProblem(){
		List<List<Color>> colors = Permutation.permutate(Arrays.asList(Color.values()));
		List<List<Nation>> nations = Permutation.permutate(Arrays.asList(Nation.values()));
		List<List<Beverage>> beverages = Permutation.permutate(Arrays.asList(Beverage.values()));
		List<List<Cigar>> cigars = Permutation.permutate(Arrays.asList(Cigar.values()));
		List<List<Pet>> pets = Permutation.permutate(Arrays.asList(Pet.values()));
		
		Path p = Paths.get("data/PermutationTest_result.txt");
		out.printf("Started on %s\n", LocalDateTime.now());
		colors.parallelStream().forEach(color -> {
			nations.parallelStream().forEach(nation -> {
				beverages.parallelStream().forEach(beverage -> {
					cigars.parallelStream().forEach(cigar -> {
						pets.parallelStream().forEach(pet ->{
							List<House> houses = getHouseList(color, nation, beverage, cigar, pet);
								if(validateHouses(houses)){
									out.println(houses);
									try {
										Files.write(p, houses.stream().map(h -> h.toString()).collect(Collectors.toList())
												, StandardCharsets.UTF_8
												, StandardOpenOption.APPEND, StandardOpenOption.CREATE, StandardOpenOption.WRITE
												);
									} catch (Exception e) {
										e.printStackTrace(out);
									}
								}
							
						});
					});
				});
			});
		});
		out.printf("Finished on %s\n", LocalDateTime.now());
		//result:
//		Started on 2016-01-17T19:14:50.339
//		[House [index=0, color=YELLOW, nationality=NORWEGIAN, beverage=WATER, cigar=DUNHILL, pet=CAT], 
//		House [index=1, color=BLUE, nationality=DANE, beverage=TEA, cigar=BLENDS, pet=HORSE], 
//		House [index=2, color=RED, nationality=BRIT, beverage=MILK, cigar=PALL_MALL, pet=BIRD], 
//		House [index=3, color=GREEN, nationality=GERMAN, beverage=COFFEE, cigar=PRINCE, pet=FISH], 
//		House [index=4, color=WHITE, nationality=SWEDE, beverage=BEER, cigar=BLUEMASTER, pet=DOG]]
//		Finished on 2016-01-17T22:29:41.456
	}
	
	
//	@Test
	public static boolean validateHouses(List<House> house){
//		boolean retval = false;
		List<Boolean> conditions = new ArrayList<>();
//		conditions.addAll(Arrays.asList(true, true, false));
		//Rule 1:
		conditions.add(house.parallelStream().anyMatch(h -> Nation.BRIT == h.getNationality() && Color.RED == h.getColor()));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 2:
		conditions.add(house.parallelStream().anyMatch(h -> Nation.SWEDE == h.getNationality() && Pet.DOG == h.getPet()));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 3:
		conditions.add(house.parallelStream().anyMatch(h -> Nation.DANE == h.getNationality() && Beverage.TEA == h.getBeverage()));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 4:
		conditions.add(house.parallelStream().anyMatch(h -> h.getIndex() < House.HOUSE_NUMBER - 1
				&& Color.GREEN == h.getColor()
				&& Color.WHITE == house.get(h.getIndex() + 1).getColor()
				));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 5:
		conditions.add(house.parallelStream().anyMatch(h -> Color.GREEN == h.getColor() && Beverage.COFFEE == h.getBeverage()));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 6:
		conditions.add(house.parallelStream().anyMatch(h -> Cigar.PALL_MALL == h.getCigar() && Pet.BIRD == h.getPet()));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 7:
		conditions.add(house.parallelStream().anyMatch(h -> Cigar.DUNHILL == h.getCigar() && Color.YELLOW == h.getColor()));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 8:
		conditions.add(Beverage.MILK == house.get(House.CENTER_POS_INDEX).getBeverage());
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 9:
		conditions.add(Nation.NORWEGIAN == house.get(0).getNationality());
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 10:
		conditions.add(house.parallelStream().anyMatch(h -> 
				(
						h.getIndex() < House.HOUSE_NUMBER - 1
						&& Cigar.BLENDS == h.getCigar()
						&& Pet.CAT == house.get(h.getIndex() + 1).getPet()
				)
				||
				(
						0 < h.getIndex()
						&& Cigar.BLENDS == h.getCigar()
						&& Pet.CAT == house.get(h.getIndex() - 1).getPet()
						
						)
				
				
				));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 11:
		conditions.add(house.parallelStream().anyMatch(h -> 
				(
						h.getIndex() < House.HOUSE_NUMBER - 1
						&& Pet.HORSE == h.getPet()
						&& Cigar.DUNHILL == house.get(h.getIndex() + 1).getCigar()
				)
				||
				(
						0 < h.getIndex()
						&& Pet.HORSE == h.getPet()
						&& Cigar.DUNHILL == house.get(h.getIndex() - 1).getCigar()
						
						)
				
				
				));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 12:
		conditions.add(house.parallelStream().anyMatch(h -> Cigar.BLUEMASTER == h.getCigar() && Beverage.BEER == h.getBeverage()));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 13:
		conditions.add(house.parallelStream().anyMatch(h -> Nation.GERMAN == h.getNationality() && Cigar.PRINCE == h.getCigar()));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
		//Rule 14:
		conditions.add(house.parallelStream().anyMatch(h -> 
				(
						h.getIndex() < House.HOUSE_NUMBER - 1
						&& Nation.NORWEGIAN == h.getNationality()
						&& Color.BLUE == house.get(h.getIndex() + 1).getColor()
				)
				||
				(
					0 < h.getIndex()
					&& Nation.NORWEGIAN == h.getNationality()
					&& Color.BLUE == house.get(h.getIndex() -1).getColor()
				)
				));
		
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		//Rule 15:
		conditions.add(house.parallelStream().anyMatch(h -> 
				(
						h.getIndex() < House.HOUSE_NUMBER - 1
						&& Cigar.BLENDS == h.getCigar()
						&& Beverage.WATER == house.get(h.getIndex() + 1).getBeverage()
				)
				||
				(
					0 < h.getIndex()
					&& Cigar.BLENDS == h.getCigar()
					&& Beverage.WATER == house.get(h.getIndex() -1).getBeverage()
				)
				));
		if(!conditions.get(conditions.size() - 1)){
			return false;
		}
		
//		boolean retval = conditions.stream().reduce((l, r) -> l && r).get();
//		boolean retval = true;
//		out.printf("Conditions: %s, result: %s\n", conditions, retval);
//		return retval;
		return true;
	}
	
	
	
	
//	@Test
//	public void testFilterList(){
//		List<List<Color>> colors = Permutation.permutate(Arrays.asList(Color.values()));
//		List<List<Nation>> nations = Permutation.permutate(Arrays.asList(Nation.values()));
//		
//		Tuple<List<List<Color>>, List<List<Nation>>> result = filterList(colors, Color.RED, nations, Nation.BRIT);
//		List<List<Color>> colorsR = result.getObj1();
//		List<List<Nation>> nationsR = result.getObj2();
//		colorsR.forEach(out::println);
//		out.println();
//		nationsR.forEach(out::println);
//	}
	
	
//	/**
//	 * Given a two collections of list, with <strong>any</strong> two list in them have the <strong>same</strong> index 
//	 * for <code>matcher1</code>|<code>match2</code>, then put them in the returned list. 
//	 * @param listCollection1
//	 * @param match1
//	 * @param listCollection2
//	 * @param match2
//	 * @return
//	 */
//	<T1, T2> Tuple<List<List<T1>>, List<List<T2>>> filterList(List<List<T1>> listCollection1, T1 match1, List<List<T2>> listCollection2, T2 match2){
//
//		Set<List<T1>> retList1 = new HashSet<>();
//		Set<List<T2>> retList2 = new HashSet<>();
//		
//		for(int i = 0; i < listCollection1.size(); ++i){
//			for(int j = 0; j < listCollection2.size(); ++j){
//				List<T1> list1 = listCollection1.get(i);
//				List<T2> list2 = listCollection2.get(j);
//				
//				if(matchPosition(list1, match1, list2, match2)){
//					retList1.add(list1);
//					retList2.add(list2);
//				}
//			}
//		}
//		
//		return new Tuple<>(new ArrayList<>(retList1), new ArrayList<>(retList2));
//	}
//	
	@FunctionalInterface
	static interface PositionFilter{
		<T1, T2> boolean matchPosition(List<T1> list1, T1 match1, List<T2> list2, T2 match2);
	}
	/**
	 * Check of <code>match1</code> in <code>list1</code> bears the same position with <code>match2</code> in <code>list2</code>
	 * @param list1
	 * @param list2
	 * @param match1
	 * @param match2
	 */
	<T1, T2> boolean matchPosition(List<T1> list1, T1 match1, List<T2> list2, T2 match2){

		
		boolean retval = false;

		if(list1.indexOf(match1) == list2.indexOf(match2)){
			retval = true;
		}
		return retval;
	}
	
	/**
	 * Return the house list in specific order: from 0 to {@link House#HOUSE_NUMBER}. Characteristics for each house are given
	 * through the parameters listed here.
	 * @param colors colors for each house
	 * @param nations nationality of each host of each house
	 * @param beverages favourite beverage of each host   
	 * @param cigars favourite cigar of each host
	 * @param pets pets for each house
	 * @return
	 */
	List<House> getHouseList(List<Color> colors, List<Nation> nations, List<Beverage> beverages, List<Cigar> cigars, List<Pet> pets){
		List<House> retval = new ArrayList<>();
		
		for(int index = 0; index < House.HOUSE_NUMBER; ++index){
			retval.add(new House(index, colors.get(index), nations.get(index), beverages.get(index), cigars.get(index), pets.get(index)));
		}
		
		return retval;
	}
	
	
	static class Tuple<T1, T2>{
		T1 obj1;
		T2 obj2;
		public Tuple(T1 obj1, T2 obj2) {
			super();
			this.obj1 = obj1;
			this.obj2 = obj2;
		}
		public T1 getObj1() {
			return obj1;
		}
		public T2 getObj2() {
			return obj2;
		}
	}
	static class Permutation{
		/**
		 * Input the original item list, list all possible permutations.
		 * <dl>
		 * <dt>INPUT</dt>
		 * <dd>
		 * [0, 1, 2]
		 * </dd>
		 * <dt>OUTPUT</dt>
		 * <dd>[
		 * <ul>
		 * 	<li>[0, 1, 2]</li>
		 *	<li>[1, 0, 2]</li>
		 *	<li>[1, 2, 0]</li>
		 *	<li>[2, 1, 0]</li>
		 *	<li>[2, 0, 1]</li>
		 *	<li>[0, 2, 1]</li>
		 * </ul>
		 * ]</dd>
		 * <dl>
		 * @param items items list to impose permutation
		 * @return
		 */
		public static <T> List<List<T>> permutate(List<T> items){
			List<List<T>> retval = new ArrayList<>();
			
			permutate(items, 0, retval);
			
			return retval;
		}
		
		private static <T> void permutate(List<T> items, int startIndex, List<List<T>> resultCollector){
			if(startIndex < items.size()){
				resultCollector.add(new ArrayList<>(items));
			}
//			for(int i = startIndex + 1; i < items.size(); ++i){
//				swap(items, startIndex, i);
//				permutate(items, 1 + startIndex, resultCollector);
//				swap(items, startIndex, i);	//<--swap back
//			}
			for(int swapFrom = startIndex; swapFrom < items.size() - 1 ; ++swapFrom){
				for(int swapTo = swapFrom + 1; swapTo < items.size(); ++swapTo){
					swap(items, swapFrom, swapTo);
					permutate(items, 1 + swapFrom, resultCollector);
					swap(items, swapFrom, swapTo);
				}
			}
		}
		
		private static <T> void swap(List<T> items, int i, int j){
			T tmp = items.get(i);
			items.set(i, items.get(j));
			items.set(j, tmp);
		}

	}
	
	private static final PrintStream out = System.out;
	
	
	static class House{
		/**
		 * Total number of houses
		 */
		public static final int HOUSE_NUMBER = 5;
		public static final int CENTER_POS_INDEX = 2;
		
		int index;	//Starts from 0
		Color color;
		Nation nationality;
		Beverage beverage;
		Cigar cigar;
		Pet pet;
		public House(int index, Color color, Nation nationality, Beverage beverage, Cigar cigar, Pet pet) {
			this.index = index;
			this.color = color;
			this.nationality = nationality;
			this.beverage = beverage;
			this.cigar = cigar;
			this.pet = pet;
		}
		public int getIndex() {
			return index;
		}
		public Color getColor() {
			return color;
		}
		public Nation getNationality() {
			return nationality;
		}
		public Beverage getBeverage() {
			return beverage;
		}
		public Cigar getCigar() {
			return cigar;
		}
		public Pet getPet() {
			return pet;
		}
		@Override
		public String toString() {
			return "House [index=" + index + ", color=" + color + ", nationality=" + nationality + ", beverage="
					+ beverage + ", cigar=" + cigar + ", pet=" + pet + "]";
		}
	}
	
	enum Color{
		 RED
		,GREEN
		,WHITE
		,YELLOW
		,BLUE
	}
	
	enum Nation{
		 BRIT
		,SWEDE
		,DANE
		,GERMAN
		,NORWEGIAN
	}
	
	enum Beverage{
		 TEA
		,COFFEE
		,MILK
		,BEER
		,WATER
	}
	
	enum Cigar{
		 PALL_MALL
		,DUNHILL
		,BLENDS
		,BLUEMASTER
		,PRINCE
	}
	
	enum Pet{
		 DOG
		,BIRD
		,CAT
		,HORSE
		,FISH	//<-
	}
	
	
	
}

package com.mec.duke;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

//ref: http://www.programcreek.com/2013/02/leetcode-permutations-java/
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
		static final int NUMBER = 5;
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

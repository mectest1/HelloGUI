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
	
	@Test
	public void testPermutation(){
		List<Integer> indices = Arrays.asList(0, 1, 2, 3, 4);
		List<List<Integer>> permutationResult = permutate(indices);
		permutationResult.forEach(out::println);
		out.printf("permutation result size: %s\n", permutationResult.size());
	}
	
	
	
	public static <T> List<List<T>> permutate(List<T> items){
		List<List<T>> retval = new ArrayList<>();
		
		permutate(items, 0, retval);
		
		return retval;
	}
	
	private static <T> void permutate(List<T> items, int startIndex, List<List<T>> resultCollector){
		if(startIndex < items.size()){
			resultCollector.add(new ArrayList<>(items));
		}
//		for(int i = startIndex + 1; i < items.size(); ++i){
//			swap(items, startIndex, i);
//			permutate(items, 1 + startIndex, resultCollector);
//			swap(items, startIndex, i);	//<--swap back
//		}
		for(int swapFrom = startIndex; swapFrom < items.size() - 1 ; ++swapFrom){
			for(int swapTo = swapFrom + 1; swapTo < items.size(); ++ swapTo){
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

	private static final PrintStream out = System.out;
	
//	ArrayList<House> newPermutation(){
//		ArrayList<House> retval = new ArrayList<>();
//		
//	}
	Set<ArrayList<House>> permutateHouse(){
		Set<ArrayList<House>> retval = new HashSet<>();
		
//		ArrayList<House> permutation = new ArrayList<>();
//		for(Color c : Color.values()){
//			for(Nation n : Nation.values()){
//				for (Beverage b : Beverage.values()){
//					for (Cigar cg : Cigar.values()){
//						for (Pet p : Pet.values()){
							ArrayList<House> houseList = new ArrayList<>();
//							houseList.add(new House(0, c0, n0, b0, cg0, p0));
//							houseList.add(new House(2, c1, n1, b1, cg1, p1));
//							houseList.add(new House(3, c2, n2, b2, cg2, p2));
//							houseList.add(new House(4, c3, n3, b3, cg3, p3));
//							houseList.add(new House(5, c4, n4, b4, cg4, p4));
							retval.add(houseList);
//						}
//					}
//				}
//			}
//		}
		
		return retval;
	}
	
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

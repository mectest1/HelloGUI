package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableListTest {

	@Ignore
	@Test
	public void testObservable() {
//		fail("Not yet implemented");
		ObservableList<String> list = FXCollections.observableArrayList("one", "two");
		out.printf("After creating list: %s\n", list);
		
		//
		list.addAll("three", "four");
		out.printf("After adding elements: %s\n", list);
		
		list.remove(1, 3);
		out.printf("After removing elements: %s\n", list);
		
		list.retainAll("one");
		out.printf("After retaining \"one\": %s\n", list);
		
		//Create another ObservableList
		ObservableList<String> list2 = FXCollections.observableArrayList("1", "2", "3");
		
		//
		list.setAll(list2);
		out.printf("After setting list2 to list: %s\n", list);
		
		//Create another list
		ObservableList<String> list3 = FXCollections.observableArrayList("ten", "twenty", "thirty");
		
		ObservableList<String> list4 = FXCollections.concat(list2, list3);
		out.printf("list 2 is %s \n", list2);
		out.printf("list 3 is %s\n ", list3);
		out.printf("After concatenating list 2 and list 3: %s\n", list4);
		
	}

	@Test
	public void testListInvalidationTest(){
		ObservableList<String> list = FXCollections.observableArrayList("one", "two");
		
		//Add an invalidation listener to the list
		list.addListener((Observable e) -> out.println("List is invalid"));
		
		//
		out.println("Before adding three");
		list.add("three");
		out.println("After adding three");
		
		out.println("Before adding four and five");
		list.addAll("four", "five");
		out.println("After adding four and five");
		
		out.println("Before replacing one with one");
		list.set(0, "one");
		out.println("After replacing one with one");
		
	}
	
	private static final PrintStream out = System.out;
}

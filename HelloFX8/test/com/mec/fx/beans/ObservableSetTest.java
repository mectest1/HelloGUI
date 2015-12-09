package com.mec.fx.beans;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public class ObservableSetTest {

	@Ignore
	@Test
	public void testChangeListener() {
//		fail("Not yet implemented");
		ObservableSet<String> s1 = FXCollections.observableSet("one", "two", "three");
		
		//
		out.printf("s1: %s\n", s1);
		
		//Create a set, and not and observable set
		Set<String> s2 = new HashSet<String>();
		s2.add("one");
		s2.add("two");
		out.printf("s2: %s\n", s2);
		
		//
		ObservableSet<String> s3 = FXCollections.observableSet(s2);
		s3.add("three");
		out.printf("s3: %s\n", s3);
	}


	@Ignore
	@Test
	public void testInvalidationListener(){
		ObservableSet<String> set = FXCollections.observableSet("one", "two");
		
		//Add an invalidationListener to the set
		set.addListener((Observable o) -> out.println("Set is invalid"));
		
		//
		out.println("Before adding three");
		set.add("three");
		out.println("After adding three");
		
		out.println("Before adding four");
		set.add("four");
		out.println("After adding four");
		
		out.println("Before removing one");
		set.remove("one");
		out.println("After removing one");
		
		out.println("Before removing 123");
		set.remove("123");		//<- invalidation event is not fired;
		out.println("After  removing 123");
		
	}
	
	@Ignore
	@Test
	public void testSetChangeListener(){
		ObservableSet<String> set = FXCollections.observableSet("one", "two");
		SetChangeListener<? super String> listener = (SetChangeListener.Change<? extends String> c) -> ChangeListeners.newInstance().onChanged(c);
		set.addListener(listener);
		//
		set.add("three");
		set.add("one");	//<- No change event is fired;
		
		//Create a set
		Set<String> s = new HashSet<>();
		s.add("four");
		s.add("five");
		
		set.addAll(s);
		
		//
		set.remove("one");
		set.clear();	//<- Remove change event is fired four times;
	}
	
	@Test
	public void testMapChangeListener(){
		ObservableMap<String, Integer> map = FXCollections.observableHashMap();
		
		MapChangeListener<String, Integer> l = c -> ChangeListeners.newInstance().onChanged(c);
		map.addListener(l);
		
		out.println("Before adding one -> 1");
		map.put("one", 1);
		out.println("After adding one -> 1");
		
		out.println("Before adding two -> 2");
		map.put("two", 2);
		out.println("After adding two -> 2");
		
		
		out.println("Before put one -> 100");
		map.put("one", 100);	//<- Will trigger Remove event as well as Add event
		out.println("After put one -> 100");
		
		out.println("Before calling clear()");
		map.clear();	//<- Multiple remove events are fired;
		out.println("After calling clear()");
		//
		
	}

	private static PrintStream out = System.out;
}

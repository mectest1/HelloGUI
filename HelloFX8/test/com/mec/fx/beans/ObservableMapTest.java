package com.mec.fx.beans;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class ObservableMapTest {

	@Ignore
	@Test
	public void testObservable() {
//		fail("Not yet implemented");
		ObservableMap<String, Integer> map1 = FXCollections.observableHashMap();
		
		map1.put("one", 1);
		map1.put("two", 2);
		out.printf("Map 1: %s\n", map1);
		
		//
		Map<String, Integer> backingMap = new HashMap<>();
		backingMap.put("ten", 10);
		backingMap.put("twenty", 20);
		
		ObservableMap<String, Integer> map2 = FXCollections.observableMap(backingMap);
		out.printf("Map 2: %s\n", map2);
		
	}
	
	
	@Test
	public void testInvalidationListener(){
		ObservableMap<String, Integer> map = FXCollections.observableHashMap();
		
		//Add an InvalidationListener to the Map;
		InvalidationListener l = o -> out.println("Map is invalid");
		map.addListener(l);
		
		
		//
		out.println("Before adding one -> 1");
		map.put("one", 1);
		out.println("After adding one -> 2");
		
		
		out.println("Before adding two -> 2");
		map.put("two", 2);
		out.println("After adding two -> 2");
		
		out.println("Before adding one -> 1");
		map.put("one", 1);	//adding the same key->value does not trigger an invalidation event
		out.println("After adding one -> 1");
		
		out.println("Before adding one -> 100");
		map.put("one", 100);
		out.println("After adding one -> 100");
		
		out.println("Before calling clear");
		map.clear();	//<- multiple invalidation events are fired.
		out.println("After calling clear");
	}

	
	private static final PrintStream out = System.out;
}

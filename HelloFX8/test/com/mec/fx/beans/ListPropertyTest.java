package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Test;

import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ListPropertyTest {

	@Test
	public void testListPropertyChange() {
//		fail("Not yet implemented");
		ListProperty<String> lp = new SimpleListProperty<>(FXCollections.observableArrayList());
		
		//
		lp.addListener(ListPropertyTest::invalidate);
		lp.addListener(ListPropertyTest::changed);
		lp.addListener(ListPropertyTest::onChanged);
		
		out.println("Before addAll()");
		lp.addAll("one", "two", "three");
		out.println("After addAll()");
		
		out.println("Before set()");
		//Replace the wrapped list with a new one
		lp.set(FXCollections.observableArrayList("two", "three"));
		out.println("After set");
		
		out.println("Before remove()");
		lp.remove("two");
		out.println("After remove()");
	}

	public static void invalidate(Observable list){
		out.println("List property is invalid.");
	}
	
	public static void changed(ObservableValue<? extends ObservableList<String>> observable,
			ObservableList<String> oldList,
			ObservableList<String> newList){
		out.printf("List property has changed. Old list: %s, New list: %s\n", oldList, newList);
	}
	
	public static void onChanged(ListChangeListener.Change<? extends String> change){
		while(change.next()){
			String action = change.wasPermutated() ? "Permutated"
						: change.wasUpdated() ? "Updated"
						: change.wasRemoved() && change.wasAdded() ? "Replaced"
						: change.wasRemoved() ? "Removed" : "Added";
			out.printf("Action taken on the list: %s, Removed: %s, Added: %s\n", action, change.getRemoved(), change.getAddedSubList());
		}
	}
	
	private static final PrintStream out = System.out;
}

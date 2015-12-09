package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;

public class SimpleListChangeTest {

	@Test
	public void testChangeListener() {
//		fail("Not yet implemented");
		ObservableList<String> list = FXCollections.observableArrayList();
		
		list.addListener((Change<? extends String> c) -> out.println("List has changed."));
		
		//
		list.add("one");
		list.add("two");
		FXCollections.sort(list);
		list.clear();
	}

	
	private static final  PrintStream out = System.out;
}

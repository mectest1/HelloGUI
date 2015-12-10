package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class ListBindingTest {

	@Ignore
	@Test
	public void testListBindnig() {
//		fail("Not yet implemented");
		ListProperty<String> lp = new SimpleListProperty<>(FXCollections.observableArrayList());
		
		StringProperty initStr = new SimpleStringProperty("Size: ");
		StringProperty desc = new SimpleStringProperty();
		desc.bind(initStr.concat(lp.sizeProperty()).concat(", Empty: ").concat(lp.emptyProperty()).concat(", List:").concat(lp.asString()));
		
		out.printf("Before addAll(): %s\n", desc.get());
		lp.addAll("John", "Jacobs");
		out.printf("After addAll(): %s\n", desc.get());
	}

	@Ignore
	@Test
	public void testBindingListReference(){
		ListProperty<String> lp1 = new SimpleListProperty<>(FXCollections.observableArrayList());
		ListProperty<String> lp2 = new SimpleListProperty<>(FXCollections.observableArrayList());
		
		out.printf("Before bind(): lp1.get() == lp2.get(): %s\n", lp1.get() == lp2.get());
		//
		lp1.bind(lp2);
		out.printf("After bind(): lp1.get() == lp2.get(): %s\n", lp1.get() == lp2.get());
		
		out.printf("Before allAll(): lp1 = %s, lp2 = %s\n", lp1, lp2);
		lp1.addAll("one", "two");
		out.printf("After allAll(): lp1 = %s, lp2 = %s\n", lp1, lp2);
		
		//Change the reference of the ObservableList in lp2
		lp2.set(FXCollections.observableArrayList("1", "2"));
		out.printf("After lp2.set(): lp1 = %s, lp2 = %s\n", lp1, lp2);
		
		//Cannot do the following as lp1 is a bound property
//		lp1.set(FXCollections.observableArrayList("1", "2"));	//<-RuntimeException: A bound value cannot be set
		
		//Unbind lp1
		lp1.unbind();
		out.printf("After unbind(): lp1 = %s, lp2 = %s\n", lp1, lp2);
		
		//Bind lp1 and lp2 bidirectionally
		lp1.bindBidirectional(lp2);
		out.printf("After bindBidirectional(): lp1 = %s, lp2 = %s\n", lp1, lp2);
		
		//
		lp1.set(FXCollections.observableArrayList("X", "Y"));
		out.printf("After lp1.set(): lp1 = %s, lp2 = %s\n", lp1, lp2);
		
	}
	
	
	@Ignore
	@Test
	public void testBindingListContent(){
		ListProperty<String> lp1 = new SimpleListProperty<>(FXCollections.observableArrayList());
		ListProperty<String> lp2 = new SimpleListProperty<>(FXCollections.observableArrayList());
		
		//Bind the content of lp1 to teh content of lp2
		lp1.bindContent(lp2);
		
		/*
		 * At this point, you can changehte content of lp1. However, that 
		 * will defeat the purpose of content binding, because the content of lp1 is no longer
		 * in sync with teh content of lp2.
		 * DO NOT DO THIS:
		 * lp1.addAll("X", "Y");
		 */
		print("Before lp2.addAll():", lp1, lp2);
		lp2.addAll("1", "2");
		print("After lp2.addAll():", lp1, lp2);
		
		lp1.unbindContent(lp2);
		print("After lp1.unbindContent(lp2):", lp1, lp2);
		
		//Bind lp1 adn lp2 contents bidirectinally
		lp1.bindContentBidirectional(lp2);
		
		print("Before lp1.adllAll():", lp1, lp2);
		lp1.addAll("3", "4");
		print("After lp1.adllAll():", lp1, lp2);
		
		print("Before lp2.adllAll():", lp1, lp2);
		lp2.addAll("5", "6");
		print("After lp2.adllAll():", lp1, lp2);
		
	}
	
	@Test
	public void testBindnigToListElements(){
		ListProperty<String> lp = new SimpleListProperty<>(FXCollections.observableArrayList());
		
		//Create a biding to the last elemtn of the list;
		ObjectBinding<String> last = lp.valueAt(lp.sizeProperty().subtract(1));
		out.printf("List: %s, Last Value: %s\n", lp.get(), last.get());
		
		//
		lp.add("John");
		out.printf("List: %s, Last Value: %s\n", lp.get(), last.get());
		
		//
		lp.addAll("donna", "Gesha");
		out.printf("List: %s, Last Value: %s\n", lp.get(), last.get());
		
		lp.remove("Geshan");
		out.printf("List: %s, Last Value: %s\n", lp.get(), last.get());
		
		//
		lp.clear();
		out.printf("List: %s, Last Value: %s\n", lp.get(), last.get());
	}
	private static final <T> void print(T  msg, ListProperty<T> lp1, ListProperty<T> lp2){
		out.printf("%s lp1: %s, lp2: %s\n", msg, lp1.get(), lp2.get());
	}
	private static final PrintStream out = System.out;
}

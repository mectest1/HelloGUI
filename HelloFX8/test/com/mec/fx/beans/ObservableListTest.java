package com.mec.fx.beans;

import java.io.PrintStream;

import org.junit.Ignore;
import org.junit.Test;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;

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

	@Ignore
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
	
	@Ignore
	@Test
	public void testListElementUpdate(){	//<- watch for the update of list element
		Callback<IntegerProperty, Observable[]> extractor = (IntegerProperty p) ->{
			//Print a message to know when it is called.
			out.printf("The extractor is called for %s\n", p);
			
			//Wrap the parameter in an Observable[] and return it;
			return new Observable[]{p};
		};
		
		ObservableList<IntegerProperty> list1 = FXCollections.observableArrayList(extractor);
		ObservableList<IntegerProperty> list2 = FXCollections.observableArrayList();	//<- won't watch for element update
		
		out.println("Before adding two elements...");
		IntegerProperty p1 = new SimpleIntegerProperty(10);
		IntegerProperty p2 = new SimpleIntegerProperty(20);
		list1.addAll(p1, p2);
		list2.addAll(p1, p2);
		out.println("After adding two elements...");
		
		
		//Add a change listener to the list;
		list1.addListener(ChangeListeners::onElementChanged);
		list2.addListener(ChangeListeners::onElementChanged);
		
		
		//
		p1.set(100);	//<--Only list1 will be notified of the element update;
		
	}
	
	@Test
	public void testListUpdateTotal(){
		Callback<Person, Observable[]> cb = (Person p) ->
			new Observable[]{p.firstNameProperty(), p.lastNameProperty()};
			
		ObservableList<Person> list = FXCollections.observableArrayList(cb);
		
		//
		ListChangeListener<? super Person> listener = p -> ChangeListeners.newInstance().onChanged(p); 
		list.addListener(listener);
		
		//
		out.println("==========================================");
		Person p1 = new Person("Li", "Na");
		out.printf("Before adding %s : %s\n", p1, list);
		list.add(p1);
		out.printf("After adding %s : %s\n", p1, list);
		
		//
		out.println("==========================================");
		Person p2 = new Person("Vivi", "Gin");
		Person p3 = new Person("Li", "He");
		out.printf("Before adding %s and %s\n", p2, p3);
		list.addAll(p2, p3);
		out.printf("After adding %s and %s\n", p2, p3);
		
		//
		out.println("==========================================");
		out.printf("Before sorting the list: %s\n", list);
		FXCollections.sort(list);
		out.printf("After sorting the list: %s\n", list);
		
		
		out.println("==========================================");
		//
		out.printf("Before updating %s : %s\n", p1, list);
		p1.setLastName("Smith");
		out.printf("After updating %s : %s\n", p1, list);
		
		out.println("==========================================");
		//
		Person p = list.get(0);
		Person p4 = new Person("Simon", "Ng");
		out.printf("Before replacing %s with %s: %s\n", p, p4, list);
		list.set(0, p4);
		out.printf("After replacing %s with %s: %s\n", p, p4, list);
		
		out.println("==========================================");
		//
		out.printf("Before setAll(): %s\n", list);
		Person p5 = new Person("Lia", "Li");
		Person p6 = new Person("Liz", "Na");
		Person p7 = new Person("Li", "Ho");
		list.setAll(p5, p6, p7);
		out.printf("After setAll(): %s\n", list);
		
		out.println("==========================================");
		//
		out.printf("Before removeAll(): %s\n", list);
		list.removeAll(p5, p7);		//<-Note that remove operation has been invoked twice;
		out.printf("After removeAll(): %s\n", list);
		
		//
		out.println("==========================================");
		Person p8 = new Person("John", "Smith");
		out.printf("Before add %s: %s\n", p8, list);
		list.add(p8);
		out.printf("After add %s: %s\n", p8, list);
		out.printf("Before clear(): %s\n", list);
		list.clear();				//<- Note that remove change event has been fired only once;
		out.printf("After clear(): %s\n", list);
		
	}
	
	
	private static final PrintStream out = System.out;
}

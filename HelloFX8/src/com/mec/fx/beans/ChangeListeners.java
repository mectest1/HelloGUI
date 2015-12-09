package com.mec.fx.beans;

import java.io.PrintStream;
import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.SetChangeListener;

public class ChangeListeners {

	private ChangeListeners(){
		
	}
	
	public static ChangeListeners newInstance(){
		return new ChangeListeners();
	}
	
	public <Key, Value> void onChanged(MapChangeListener.Change<Key, Value> change){
		if(change.wasRemoved()){
			out.printf("Removed (%s, %s)\n", change.getKey(), change.getValueRemoved());
		}
		if(change.wasAdded()){
			out.printf("Added (%s, %s)\n", change.getKey(), change.getValueAdded());
		}
	}
	public <T> void onChanged(SetChangeListener.Change<? super T> change){
		if(change.wasAdded()){
			out.printf("Added: %s", change.getElementAdded());
		}else if(change.wasRemoved()){
			out.printf("Removed: %s", change.getElementRemoved());
		}
		out.printf(", Set after the change: %s\n", change.getSet());
	}
	
	//Template
	public <T> void onChanged(ListChangeListener.Change<? super T> change){
		while(change.next()){
			if(change.wasPermutated()){
				//Handle permutations;
				handlePermutated(change);
			}else if(change.wasUpdated()){
				//Handle updates;
				handleUpdated(change);
			}else if(change.wasReplaced()){
				//Handle replaces;
				handleReplaced(change);
			}else{
				if(change.wasRemoved()){
					//Handle removals;
					handleRemoved(change);
				}else if(change.wasAdded()){
					//handl additions;
					handleAdded(change);
				}
			}
		}
	}
	
	private <T> void handlePermutated(ListChangeListener.Change<? extends T> change){
		out.println("Change Type: Permutated");
		out.printf("Permutated Range: %s\n", getRangeText(change));
		int start = change.getFrom();
		int end = change.getTo();
		//
		for(int oldIndex = start; oldIndex < end; ++oldIndex){
			int newIndex=  change.getPermutation(oldIndex);
			out.printf("index[%s] moved to index[%s]\n", oldIndex, newIndex);
		}
	}
	private <T> void handleUpdated(ListChangeListener.Change<? extends T> change){
		out.println("Change Type: Updated");
		out.printf("Update range: %s\n", getRangeText(change));
		
		out.printf("Updated list are %s\n", change.getList().subList(change.getFrom(), change.getTo()));
	}
	private <T> void handleReplaced(ListChangeListener.Change<? extends T> change){
		out.println("Change Type: Replaced");
		
		//A replace is the same as a "remove" followed with an "add"
		handleRemoved(change);
		handleAdded(change);
		
	}
	private <T> void handleRemoved(ListChangeListener.Change<? extends T> change){
			out.println("Change Type: Removed");
			
			int removedSize = change.getRemovedSize();
			List<? extends T> subList = change.getRemoved();
			
			out.printf("Remove Size: %s\n", removedSize);
			out.printf("Removed Range: %s\n", getRangeText(change));
			out.printf("Removed List: %s\n", subList);
		
		
	}
	private <T> void handleAdded(ListChangeListener.Change<? extends T> change){
		out.println("Change Type: Added");
		int addedSize = change.getAddedSize();
		List<? extends T> subList = change.getAddedSubList();
		
		out.printf("Added Size: %s\n", addedSize);
		out.printf("Added Range: %s\n", getRangeText(change));
		out.printf("Added List: %s\n", subList);
	}
	private <T> String getRangeText(ListChangeListener.Change<? extends T> change){
		return String.format("[%s, %s]", change.getFrom(), change.getTo());
	}
	
	
	public static <T> void onElementChanged(ListChangeListener.Change<? extends T> change){
		
		out.printf("List is %s\n", change.getList());
		
		
		//Work on only updates to the list
		while(change.next()){
			if(change.wasUpdated()){
				//Print the details of the update
				out.println("An update is detected");
				
				//
				int start = change.getFrom();
				int end = change.getTo();
				
				//
				out.printf("Update range: [%s, %s]\n", start, end);
				
				//
				List<? extends T> updatedElementsList = change.getList().subList(start, end);
				out.printf("Update elements: %s\n", updatedElementsList);
			}
			
		}
	}
	
	
	private static final PrintStream out = System.out;
	
}

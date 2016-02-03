package com.mec.fx;

import java.io.PrintStream;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OneShotTaskApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

	}

	
	@Ignore
	@Test
	public void testPrime(){
		IntStream.rangeClosed(0, 100).filter(PrimeUtil::isPrime).forEach(out::println);
	}
	
	private static final PrintStream out = System.out;
	
}


class WorkerStateUI extends GridPane{
	final Label title = new Label("");
	final Label message = new Label("");
	final Label running = new Label("");
	final Label state = new Label("");
	final Label totalWork = new Label("");
	final Label workDone = new Label("");
	final TextArea valeu = new TextArea("");
	final TextArea exception = new TextArea("");
	final ProgressBar progressBar = new ProgressBar();
	
	
	public WorkerStateUI(){
		addUI();
	}
	
	public WorkerStateUI(Worker<ObservableList<Long>> worker){
		addUI();
		bindToWorker(worker);
	}
	
	void addUI(){
		
	}
	
	void bindToWorker(Worker<ObservableList<Long>> worker){
		
	}
}

class PrimeUtil{
	public static boolean isPrime(long num){
		if(2 == num){
			return true;
		}
		if(num <= 1 || 0 == num %2){
			return false;
		}
		
		int ceil = (int) Math.ceil(Math.sqrt(num));
		boolean retval = !IntStream.rangeClosed(3, ceil).filter(i -> 0 != i%2)
				.anyMatch(i -> 0 == num%i);
		return retval;
	}
}

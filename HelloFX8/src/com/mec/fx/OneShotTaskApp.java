package com.mec.fx;

import java.util.stream.IntStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.binding.When;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class OneShotTaskApp extends Application {

	Button startBtn = new Button(Msg.get(this, "button.start"));
	Button cancelBtn = new Button(Msg.get(this, "button.cancel"));
	Button exitBtn = new Button(Msg.get(this, "button.exit"));
	
	
	//Create the task
	PrimeFinderTask task = new PrimeFinderTask();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Add event handlers to the buttons
		startBtn.setOnAction(e -> startTask());
		cancelBtn.setOnAction(e -> task.cancel());
		exitBtn.setOnAction(e -> primaryStage.close());
		
		//Enable/Disable the Start and Cancel buttons
		startBtn.disableProperty().bind(task.stateProperty().isNotEqualTo(State.READY));
		cancelBtn.disableProperty().bind(task.stateProperty().isNotEqualTo(State.RUNNING));
		
		//
		GridPane pane = new WorkerStateUI(task);
		HBox buttonBox = new HBox(5, startBtn, cancelBtn);
		BorderPane root = new BorderPane(pane);
		root.setTop(buttonBox);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	void startTask(){
		//Schedule the task on the background thread
		Thread backgroundThread = new Thread(task);
		backgroundThread.setDaemon(true);
		backgroundThread.start();
	}
	
//	@Ignore
//	@Test
//	public void testPrime(){
//		IntStream.rangeClosed(0, 100).filter(PrimeUtil::isPrime).forEach(out::println);
//	}
	

	
	static class PrimeFinderTask extends Task<ObservableList<Long>>{
		long lowerLimit = 1;
		long upperLimit= 30;
		long sleepTimeInMillis = 500;
		
		public PrimeFinderTask(){
			
		}
		
		public PrimeFinderTask(long lowerLimit, long upperLimit){
			this.lowerLimit = lowerLimit;
			this.upperLimit = upperLimit;
		}
		
		public PrimeFinderTask(long lowerLimit, long upperLimit, long sleepTimeInMillis){
			this(lowerLimit, upperLimit);
			this.sleepTimeInMillis = sleepTimeInMillis;
		}
		
		@Override
		protected ObservableList<Long> call() throws Exception {
			//An observable list to represent the results
			final ObservableList<Long> results = FXCollections.observableArrayList();
			
			//Update the title
			updateTitle(Msg.get(this, "title"));
			
			long count = upperLimit - lowerLimit + 1;
			long counter = 0;
			
//			LongStream.rangeClosed(lowerLimit, upperLimit).forEachOrdered(i -> {
//				
//					//Check if the task is cancelled
//				if(isCancelled()){
//					return;
//				}
//			});
			
			for(long i = lowerLimit; i<= upperLimit; ++i){
				//Check if the task is cancelled
				if(isCancelled()){
					continue;
				}
				
				//Increment the counter
				++counter;
				
				//Update message;
				updateMessage(String.format(Msg.get(this, "message"), i));
				
				//Sleep fpr some time to simulate a long running task
				try{
					Thread.sleep(sleepTimeInMillis);
				}catch(InterruptedException e){
					//Check if the task is cancelled
					if(isCancelled()){
						break;
					}
				}
				
				//Check if the number ifs a prim number
				if(PrimeUtil.isPrime(i)){
					//Add to the result list
					results.add(i);
					
					//Publish the read-only list to give the GUI access to the partial results
					updateValue(FXCollections.unmodifiableObservableList(results));
				}
				
				//Update the progress
				updateProgress(counter, count);
				
			}
			
			
			
			return results;
		}

		@Override
		protected void succeeded() {
			super.succeeded();
			updateMessage(Msg.get(this, "message.succeed"));
		}

		@Override
		protected void cancelled() {
			super.cancelled();
			updateMessage(Msg.get(this, "message.cancelled"));
		}

		@Override
		protected void failed() {
			super.failed();
			updateMessage(Msg.get(this, "message.failed"));
		}

		
		
		
	}
	
//	private static final PrintStream out = System.out;
	
}


class WorkerStateUI extends GridPane{
	final Label title = new Label("");
	final Label message = new Label("");
	final Label running = new Label("");
	final Label state = new Label("");
	final Label totalWork = new Label("");
	final Label workDone = new Label("");
	final Label progress = new Label("");
	final TextArea value = new TextArea("");
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
		value.setPrefColumnCount(20);
		value.setPrefRowCount(3);
		exception.setPrefColumnCount(20);
		exception.setPrefRowCount(3);
		//
		setHgap(5);
		setVgap(5);
		
		addRow(0, new Label(Msg.get(this, "label.title")), title);
		addRow(1, new Label(Msg.get(this, "label.message")), message);
		addRow(2, new Label(Msg.get(this, "label.state")), state);
		addRow(3, new Label(Msg.get(this, "label.total")), totalWork);
		addRow(4, new Label(Msg.get(this, "label.done")), workDone);
		addRow(5, new Label(Msg.get(this, "label.progress")), new HBox(2, progress, progressBar));
		addRow(6, new Label(Msg.get(this, "label.value")), value);
		addRow(7, new Label(Msg.get(this, "label.exception")), exception);
	}
	
	void bindToWorker(Worker<ObservableList<Long>> worker){
		//Bind labels to the properties of the worker
		title.textProperty().bind(worker.titleProperty());
		message.textProperty().bind(worker.messageProperty());
		running.textProperty().bind(worker.runningProperty().asString());
		state.textProperty().bind(worker.runningProperty().asString());
		totalWork.textProperty().bind(new When(worker.totalWorkProperty().isEqualTo(-1))
				.then(Msg.get(this, "info.unknown"))
				.otherwise(worker.totalWorkProperty().asString())
				);
		workDone.textProperty().bind(new When(worker.workDoneProperty().isEqualTo(-1))
				.then(Msg.get(this, "info.unknown"))
				.otherwise(worker.workDoneProperty().asString())
				);
		progress.textProperty().bind(new When(worker.progressProperty().isEqualTo(-1))
				.then(Msg.get(this, "info.unknown"))
//				.otherwise(worker.progressProperty().asString())
				.otherwise(worker.progressProperty().multiply(100)
						.asString(Msg.get(this, "info.progress.format")))
				);
		progressBar.progressProperty().bind(worker.progressProperty());
		value.textProperty().bind(worker.valueProperty().asString());
		
		//Display the exception message when an exception occurs in the worker
		worker.exceptionProperty().addListener((prop, oldVal, newVal) -> {
			if(null != newVal){
				exception.setText(newVal.getMessage());
			}else{
				exception.setText("");
			}
		});
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

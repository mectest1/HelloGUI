package com.mec.fx;

import java.io.PrintStream;

import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class RunLaterTest {

	@Test
	public void testRunLater() {
//		fail("Not yet implemented");
		Application.launch(RunLaterApp.class);
	}
	
	public static class RunLaterApp extends Application{

		
		@Override
		public void init() throws Exception {
			out.printf("init(): %s\n", Thread.currentThread().getName());
			
			Runnable task = () -> out.printf("Running the task on the %s\n", Thread.currentThread().getName());
			
			Platform.runLater(task);
		}

		@Override
		public void start(Stage primaryStage) throws Exception {
			primaryStage.show();
			primaryStage.close();
		}
		
	}

	private static final PrintStream out = System.out;
	
}

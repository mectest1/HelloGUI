package com.mec.fx;


import java.io.PrintStream;

import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ScreenDetailsAppTest {

	@Test
	public void testApp() {
//		fail("Not yet implemented");
		Application.launch(ScreenDetailsApp.class);
	}

	public static class ScreenDetailsApp extends Application{

		

		@Override
		public void start(Stage primaryStage) throws Exception {
			ObservableList<Screen> screenList = Screen.getScreens();
			
			out.printf("Screens count: %s\n", screenList.size());
			
			//Print the details of all screens
//			Screens count: 1
//			DPI: 96.0
//			Screen Bounds: Rectangle2D [minX = 0.0, minY=0.0, maxX=1920.0, maxY=1080.0, width=1920.0, height=1080.0]
//			Screen Visual Bounds: Rectangle2D [minX = 0.0, minY=0.0, maxX=1920.0, maxY=1040.0, width=1920.0, height=1040.0]
//			--------------------
			for(Screen screen : screenList){
				print(screen);
			}
			
			Platform.exit();
		}
		
		private void print(Screen screen){
			out.printf("DPI: %s\n", screen.getDpi());
			out.printf("Screen Bounds: %s\n", screen.getBounds());
			
			out.printf("Screen Visual Bounds: %s\n", screen.getVisualBounds());
			out.println("--------------------");
		}
		
		private static final PrintStream out = System.out;
	}
}

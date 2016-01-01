package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class WindowEventApp extends Application {

	private CheckBox canCloseCheckBox = new CheckBox(Msg.get(this, "canClose"));
	@Override
	public void start(Stage primaryStage) throws Exception {
		Button closeBtn = new Button(Msg.get(this, "close"));
		closeBtn.setOnAction(e -> primaryStage.close());
		
		//
		Button hideBtn = new Button(Msg.get(this, "hide"));
		hideBtn.setOnAction(e -> {
			showDialog(primaryStage);
			primaryStage.hide();
		});
		
		HBox root = new HBox();
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		root.getChildren().addAll(canCloseCheckBox, closeBtn, hideBtn);
		
		//Add window event handlers to the stage
		primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWING, e -> handle(e));
		primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> handle(e));
		primaryStage.addEventHandler(WindowEvent.WINDOW_HIDING, e -> handle(e));
		primaryStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, e -> handle(e));
		primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> handle(e));
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	private void handle(WindowEvent e){
		//Consume the event if the CheckBox is not selected;
		//thus preventing the user from closing the window
		EventType<WindowEvent> type = e.getEventType();
		if(WindowEvent.WINDOW_CLOSE_REQUEST == type && !canCloseCheckBox.isSelected()){
			e.consume();
		}
		
		out.printf(Msg.get(this, "info"), type, e.isConsumed());
	}
	
	public void showDialog(Stage mainWindow){
		Stage popup = new Stage();
		Button closeBtn = new Button(Msg.get(this, "popup.close"));
		closeBtn.setOnAction(e -> {
			popup.close();
			mainWindow.show();
		});
		
		HBox root = new HBox(closeBtn);
		root.setPadding(new Insets(20));
		root.setSpacing(20);
		
		Scene scene = new Scene(root);
		popup.setScene(scene);
		popup.setTitle(Msg.get(this, "popup.title"));
		popup.show();
		
	}
	
	private static final PrintStream out = System.out;

}

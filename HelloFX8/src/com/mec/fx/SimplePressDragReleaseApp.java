package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class SimplePressDragReleaseApp extends Application {

	TextField sourceFld = new TextField(Msg.get(this, "text.source"));
	TextField targetFld = new TextField(Msg.get(this, "text.target"));
	TextArea info = new TextArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane root = getUI();
		
		//Add event handlers
		addEventHandlers();
		
		
		Scene scene = new Scene(root);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	GridPane getUI(){
		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(20);;
		pane.addRow(0, new Label(Msg.get(this, "label.source")), sourceFld);
		pane.addRow(1, new Label(Msg.get(this, "label.target")), targetFld);
		
		Button clearBtn = new Button(Msg.get(this, "button.clearLog"));
		pane.add(clearBtn, 1, 2, 1, 1);
		clearBtn.setOnAction(e -> info.clear());
		GridPane.setHalignment(clearBtn, HPos.RIGHT);
		
		pane.add(info, 0, 3, 2, 1);
		GridPane.setVgrow(info, Priority.SOMETIMES);
		return pane;
	}
	
	void addEventHandlers(){
		//Add mouse event handlers for the source
		sourceFld.setOnMousePressed(e -> print("info.pressed"));
		sourceFld.setOnMouseDragged(e -> print("info.dragged"));
		sourceFld.setOnDragDetected(e -> print("info.detected"));
		sourceFld.setOnMouseReleased(e -> print("info.released"));
		
		//Add mouse event handlers for the target
		targetFld.setOnMouseDragEntered(e -> print("info.entered"));
		targetFld.setOnMouseDragOver(e -> print("info.over"));
		targetFld.setOnMouseDragReleased(e -> print("info.over.target"));
		targetFld.setOnMouseDragExited(e -> print("info.existed"));
	}
	
	void print(String msgFld){
		info.appendText(Msg.get(this, msgFld));
		info.appendText(Msg.get(this, "info.newline"));
	}
}

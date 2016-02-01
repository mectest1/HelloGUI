package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DragAndDropApp extends Application {

	TextField sourceFld = new TextField(Msg.get(this, "text.fx"));
	TextField targetFld = new TextField(Msg.get(this, "text.dnd"));
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Build UI
		GridPane root = getUIs();
		
		//Add event handlers for the source and target
		addDnDEventHandlers();
		
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	GridPane getUIs(){
		//Set prompt text
		sourceFld.setPromptText(Msg.get(this, "text.fx.prompt"));
		targetFld.setPromptText(Msg.get(this, "text.dnd.prompt"));
		
		//
		GridPane pane = new GridPane();
		pane.setHgap(5);
		pane.setVgap(20);
		pane.add(new Label(Msg.get(this, "text.label")), 0, 0, 2, 1);
		pane.addRow(1, new Label(Msg.get(this, "text.fx.label")), sourceFld);
		pane.addRow(2, new Label(Msg.get(this, "text.dnd.label")), targetFld);
		return pane;
	}
	
	void addDnDEventHandlers(){
		sourceFld.setOnDragDetected(this::dragDetected);
		targetFld.setOnDragOver(this::dragOver);
		targetFld.setOnDragDropped(this::dragDropped);
		sourceFld.setOnDragDone(this::dragDone);
	}
	
	void dragDetected(MouseEvent e){	//<--Only here have we got the MouseEvent
										//	It shall be DragEvent everywhere else 
										//	after Node#startDragAndDrop() is called.
		//User can drag only when there is text in the source field.
		String sourceText = sourceFld.getText();
		if(null == sourceText || sourceText.trim().isEmpty()){
			e.consume();
			return;
		}
		
		//Initiate a drag-and-drop gesture
		Dragboard dragboard = sourceFld.startDragAndDrop(TransferMode.COPY_OR_MOVE);
		
		//Add the source text to the Dragboard
		ClipboardContent content = new ClipboardContent();
		content.putString(sourceText);
		dragboard.setContent(content);
		
		e.consume();
	}

	void dragOver(DragEvent e){
		//If drag board has a string, let the event know 
		//that the target accepts copy and move transfer mode
		Dragboard dragboard = e.getDragboard();
		
		//
		if(dragboard.hasString()){
			e.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		e.consume();
	}
	
	
	void dragDropped(DragEvent e){
		//Transfer the data to the target
		Dragboard dragboard = e.getDragboard();
		
		//
		if(dragboard.hasString()){
			String text = dragboard.getString();
			targetFld.setText(text);
			
			//Data transfer is successfully
			e.setDropCompleted(true);
		}else{
			//Data transfer is not successfully
			e.setDropCompleted(false);
		}
		e.consume();
	}
	
	
	void dragDone(DragEvent e){
		//Check how data was transfered to the target.
		//If it was moved, clear the text in the source.
		TransferMode modeUsed = e.getTransferMode();
		if(TransferMode.MOVE == modeUsed){
			sourceFld.clear();
		}
		e.consume();
	}
	
}

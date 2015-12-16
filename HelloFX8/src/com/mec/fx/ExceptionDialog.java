package com.mec.fx;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ExceptionDialog extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Alert alert = new Alert(AlertType.ERROR);
		
		alert.setTitle(Msg.get(this, "title"));
		alert.setHeaderText(Msg.get(this, "header"));
		alert.setContentText(Msg.get(this, "content"));
		
		//Create expandable exception
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Exception ex = new FileNotFoundException(Msg.get(this, "exception"));
		
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();
		
		Label label = new Label(Msg.get(this, "label"));
		
		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		
		//
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		
		//
		//Set expandable exception into teh dialog pane
		alert.getDialogPane().setExpandableContent(expContent);
		
		alert.showAndWait();
		
	}

}

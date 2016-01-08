package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TextFlowEmbeddingNodesApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Text txt1 = new Text(Msg.get(this, "text1"));
		
		RadioButton rb1 = new RadioButton(Msg.get(this, "radio1"));
		RadioButton rb2 = new RadioButton(Msg.get(this, "radio2"));
		
		ToggleGroup group = new ToggleGroup();
		rb1.setToggleGroup(group);
		rb2.setToggleGroup(group);
		
		TextField nameField = new TextField();
		nameField.setPromptText(Msg.get(this, "prompt"));
		
		Text txt2 = new Text(Msg.get(this, "text2"));
		
		Button submitFormBtn = new Button(Msg.get(this, "submit"));
		
		//Create a TextFlow object with all nodes
		TextFlow root = new TextFlow(txt1, rb1, rb2, nameField, txt2, submitFormBtn);
		
		//Set the preferrred width and line spacing
		root.setPrefWidth(350);
		root.setLineSpacing(5);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

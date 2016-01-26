package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TextAreaApp extends Application {

	TextArea info = new TextArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField title = new TextField(Msg.get(this, "label.title"));
		title.setPromptText(Msg.get(this, "label.title.prompt"));
		
		TextArea content = new TextArea();
		content.setPromptText(Msg.get(this, "label.content.prompt"));
		content.setPrefColumnCount(20);
		content.setPrefRowCount(10);
		
		content.textProperty().addListener((observable, oldVal, newVal) -> print(content));
		
		Button clearBtn = new Button(Msg.get(this, "button.clearLog"));
		clearBtn.setOnAction(e -> info.clear());
		
		VBox root = new VBox(10
				, new Label(Msg.get(this, "label.title")), title
				, new Label(Msg.get(this, "label.content")), content
				, clearBtn, info
				);
		VBox.setVgrow(info, Priority.SOMETIMES);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	void print(TextArea content){
		info.appendText(String.format(Msg.get(this, "info.length"), content.getLength()));
		info.appendText(String.format(Msg.get(this, "info.count"), content.getParagraphs().size()));
		
		int i = 1;
		for(CharSequence paragraph : content.getParagraphs()){
			info.appendText(String.format(Msg.get(this, "info.paragraph"), i++, paragraph.length(), paragraph));
		}
		info.appendText(Msg.get(this, "info.newline"));
	}

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class HtmlEditorApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		HTMLEditor editor = new HTMLEditor();
		editor.setPrefSize(600, 300);
		
		TextArea html= new TextArea();
		html.setPrefSize(600, 300);
		html.setStyle(Msg.get(this, "style"));
		
		//
		Button html2Text = new Button(Msg.get(this, "button.htmlToText"));
		Button text2Html = new Button(Msg.get(this, "button.textToHtml"));
		html2Text.setOnAction(e -> html.setText(editor.getHtmlText()));
		text2Html.setOnAction(e -> editor.setHtmlText(html.getText()));
		
		//
		HBox buttons = new HBox(10, html2Text, text2Html);
		
		VBox root = new VBox(10, editor, buttons, html);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}

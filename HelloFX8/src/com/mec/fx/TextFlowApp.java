package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TextFlowApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create three text nodes
		Text txt1 = new Text(Msg.get(this, "text1"));
		txt1.setFill(Color.RED);
		txt1.setFont(Font.font(Msg.get(this, "font"), FontWeight.BOLD, 12));
		
		
		//
		Text txt2 = new Text(Msg.get(this, "text2"));
		txt2.setFill(Color.BLUE);
		
		Text txt3 = new Text(Msg.get(this, "text3"));
		
		//Crate a TextFlow object with the three Text nodes
		TextFlow root = new TextFlow(txt1, txt2, txt3);
		root.setPrefWidth(300);
		root.setLineSpacing(5);
		
		//
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();

	}

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StackPaneOverlayApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane textOverRect = createStackPane(Msg.get(this, "hello"), 1.0, true);
		StackPane rectOverText = createStackPane(Msg.get(this, "hello"), 1.0, false);
		StackPane transparenRectOverText = createStackPane("Hello", 0.5, false);
		StackPane rectOverBigText = createStackPane(Msg.get(this, "biggerText"), 1.0, false);
		StackPane transparentRectOverBigText = createStackPane(Msg.get(this, "biggerText"), 0.5, false);
		
		//Add all StackPanes to an HBox
		HBox root = new HBox(textOverRect, rectOverText, transparenRectOverText, rectOverBigText, transparentRectOverBigText);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

	private StackPane createStackPane(String str, double rectOpacity, boolean rectFirst){
		Rectangle rect = new Rectangle(60, 50);
		rect.setStyle(String.format(Msg.get(this, "style.rect"), rectOpacity));
		
		Text text = new Text(str);
		//Create a StackPane
		StackPane spane = new StackPane();
		//Add the Rectangle before the Text if rectFirst is true
		//Otherwise add the Text first
		if(rectFirst){
			spane.getChildren().addAll(rect, text);
		}else{
			spane.getChildren().addAll(text, rect);
		}
		
		//
		spane.setStyle(Msg.get(FlowPaneApp.class, "style"));
		return spane;
	}
}

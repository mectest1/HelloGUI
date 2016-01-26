package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ColorPickerApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		ColorPicker bgColor = new ColorPicker(Msg.get(this, "color.initial", Color::valueOf, Color.BLANCHEDALMOND));
		
		//A Rectangle to show the selected color from teh color picker
		Rectangle rect = new Rectangle(0, 0, 100, 50);
		rect.setFill(bgColor.getValue());
		rect.setStyle(Msg.get(this, "rect.style"));
		
		//Add an ActionEvetn handler to the ColorPicker, so you change
		//the fill color for the rectangle when you pick a new color
		bgColor.setOnAction(e -> rect.setFill(bgColor.getValue()));
		
		//
		HBox root = new HBox(10, new Label(Msg.get(this, "label.color")), bgColor, rect);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
}

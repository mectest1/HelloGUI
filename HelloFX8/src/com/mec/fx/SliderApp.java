package com.mec.fx;

import java.util.stream.Stream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SliderApp extends Application {

	Rectangle rect = new Rectangle(0, 0, 200, 50);
	Slider redSlider = getSlider();
	Slider greenSlider = getSlider();
	Slider blueSlider = getSlider();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Add change listener to all sliders
//		Stream.<Slider>of(redSlider, greenSlider, blueSlider);	//<- this can also work
		Stream.of(redSlider, greenSlider, blueSlider).forEach(s -> s.valueProperty().addListener(this::changed));
		
		//
		GridPane root = new GridPane();
		root.setVgap(10);
		root.add(rect, 0, 0, 2, 1);
		root.add(new Label(Msg.get(this, "label.usage")), 0, 1, 2, 1);
		root.addRow(2, new Label(Msg.get(this, "label.red")), redSlider);
		root.addRow(3, new Label(Msg.get(this, "label.green")), greenSlider);
		root.addRow(4, new Label(Msg.get(this, "label.blue")), blueSlider);
		
		//
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	
	public Slider getSlider(){
		Slider slider = new Slider(0, 255, 125);
		
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(85);
		slider.setMinorTickCount(10);
		slider.setBlockIncrement(20);
		slider.setSnapToTicks(true);
		
		return slider;
	}
	
	
	public void changed(ObservableValue<? extends Number> prop, Number oldVal, Number newVal){
		changeColor();
	}
	
	void changeColor(){
		int r = (int) redSlider.getValue();
		int g = (int) greenSlider.getValue();
		int b = (int) blueSlider.getValue();
		rect.setFill(Color.rgb(r, g, b));
	}
}

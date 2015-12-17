package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class NodeBoundsApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Rectangle rect = new Rectangle(0, 0, 50, 20);
		rect.setFill(Color.AQUA);
		
		BorderPane root = new BorderPane(rect);
		DropShadow dropShadow = new DropShadow();
		
		//Label info:
		Label label = new Label(printLayout(rect));
		CheckBox showShadow = new CheckBox(Msg.get(this, "showShadow"));
		showShadow.setOnAction(e -> {
			if(showShadow.isSelected()){
				rect.setEffect(dropShadow);
			}else{
				rect.setEffect(null);
			}
		});
		VBox labelBar = new VBox(5);
//		topBar.setBorder(new Border());
//		topBar.getChildren().addAll(label, showShadow);
		labelBar.getChildren().addAll(label);
		root.setBottom(labelBar);
		
		
		//
		Slider translationSlider = new Slider(0, 200, 0);
//		translationSlider.setRotate(90);
//		translationSlider.setTranslateY(translationSlider.getLayoutBounds().getWidth());
		translationSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			rect.setTranslateX(newVal.doubleValue());
			rect.setTranslateY(newVal.doubleValue());
			label.setText(printLayout(rect));
		});
//		HBox leftBar = new HBox(5);
//		leftBar.getChildren().addAll(translationSlider);
//		root.setLeft(new Group(leftBar));
		
		
		//
		Slider rotationSlider = new Slider(0, 360, 0);
//		rotationSlider.setRotate(90);
//		rotationSlider.setTranslateY(rotationSlider.getLayoutBounds().getWidth());
		rotationSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
			rect.setRotate(newVal.doubleValue());
			label.setText(printLayout(rect));
		});
//		HBox rightBar = new HBox(5);
//		rightBar.getChildren().addAll(rotationSlider);
//		root.setRight(rightBar);
		//By default, a Group will "auto-size" its managed resizable children to their preferred sizes during the layout 
		//pass to ensure that Regions and Controls are sized properly as their state changes.
//		root.setRight(new Group(rightBar));	//
		
		HBox controlBar = new HBox(5);
		controlBar.getChildren().addAll(showShadow, translationSlider, rotationSlider);
		root.setTop(new Group(controlBar));
		
		//
		Scene scene = new Scene(root, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		
	}
	
	private String printLayout(Node node){
		return String.format(Msg.get(this, "output"), 
				node.getLayoutBounds(),
				node.getBoundsInLocal(),
				node.getBoundsInParent()
				);
	}
	
//	private static final PrintStream out = System.out;

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RadialGradientApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Stop[] stops = new Stop[]{
				new Stop(0, Color.WHITE),
				new Stop(0.40, Color.GRAY),
				new Stop(0.60, Color.TAN),
				new Stop(1, Color.BLACK)
		};
		RadialGradient rg = new RadialGradient(-30, 1, 0.5, 0.5, 0.25, true, CycleMethod.NO_CYCLE, stops);
		RadialGradient rg2 = new RadialGradient(-30, 1, 0.5, 0.5, 0.25, true, CycleMethod.REPEAT, stops);
		RadialGradient rg3 = new RadialGradient(-30, 1, 0.5, 0.5, 0.5, true, CycleMethod.REPEAT, stops);
		RadialGradient rg4 = new RadialGradient(-30, 0.9, 0.5, 0.5, 0.25, true, CycleMethod.REPEAT, stops);
//		RadialGradient rg5 = new RadialGradient(-30, 1, 0.5, 0.5, 0.3, true, CycleMethod.REPEAT, stops);
		
		String colorValue = "radial-gradient(focus-angle 45deg, focus-distance 50%, center 50% 50%, radius 50%, white 0%, black 100%)";
		RadialGradient rg6 = RadialGradient.valueOf(colorValue);
		
		
		int radius = Msg.get(this, "circleWidth", Integer::parseInt, 100);
		Circle c = new Circle(radius, radius, radius);
		c.setFill(rg);
		
		Circle c2 = new Circle(radius, radius, radius);
		c2.setFill(rg2);
		
		Circle c3 = new Circle(radius, radius, radius);
		c3.setFill(rg3);
		
		Circle c4 = new Circle(radius, radius, radius);
		c4.setFill(rg4);
		
//		Circle c5 = new Circle(radius, radius, radius);
//		c5.setFill(rg5);
		
		Circle c6 = new Circle(radius, radius, radius);
		c6.setFill(rg6);
		
//		Group root = new Group(c, c2, c3);
		HBox root = new HBox(c, c4, c2, c3, c6);
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
}

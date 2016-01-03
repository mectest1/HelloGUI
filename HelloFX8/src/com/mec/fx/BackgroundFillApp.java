package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BackgroundFillApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Pane p1 = getCSSStyledPane();
		Pane p2 = getObjectStyledPane();
		
		//
		p1.setLayoutX(10);
		p2.setLayoutX(10);
		
		//Place p2 20px right to p1
		p2.layoutXProperty().bind(p1.layoutXProperty().add(p1.widthProperty().add(20)));
		p2.layoutYProperty().bind(p1.layoutYProperty());
		
		Pane root = new Pane(p1, p2);
		root.setPrefSize(240, 70);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}
	
	private Pane getCSSStyledPane(){
		Pane p = new Pane();
		p.setPrefSize(100,  50);
		p.setStyle(Msg.getList(this, "style").stream().reduce((r, s) ->  r + s).get());
		return p;
	}
	
	
	public Pane getObjectStyledPane(){
		Pane p = new Pane();
		p.setPrefSize(100,  50);
		
		BackgroundFill lightGrayFill = new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(4), new Insets(0));
		BackgroundFill redFill = new BackgroundFill(Color.RED, new CornerRadii(2), new Insets(4));
		
		//Create a Background object with two BackgroundFill objects;
		Background bg = new Background(lightGrayFill, redFill);
		p.setBackground(bg);
		
		//
		return p;
	}
	
	
	
	
	
	
	
	
	

}

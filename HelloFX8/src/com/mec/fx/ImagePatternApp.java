package com.mec.fx;

import java.net.URL;import java.util.List;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ImagePatternApp extends Application {

	private Image img;
	private String imgUrl = Msg.get(this, "imagePath");
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(Msg.get(this, "title"));
		if(null == img){
			Label error = new Label(String.format(Msg.get(this, "imagePath.error"), 
					imgUrl
					));
			error.setWrapText(true);
			error.setTextFill(Color.RED);
			Scene scene = new Scene(new Group(error));
			scene.setFill(Color.LIGHTGOLDENRODYELLOW);
			primaryStage.setScene(scene);
			primaryStage.show();
			return;
		}
		
		//
		ImagePattern p1 = new ImagePattern(img, 0, 0, 0.25, 0.25, true);
		Rectangle r1 = new Rectangle(100, 50);
		r1.setFill(p1);
		
		//
		ImagePattern p2 = new ImagePattern(img, 0, 0, 0.5, 0.5, true);
		Rectangle r2 = new Rectangle(100, 50);
		r2.setFill(p2);
		
		//
		ImagePattern p3 = new ImagePattern(img, 40, 10, 20, 20, false);
		Rectangle r3 = new Rectangle(100, 50);
		r3.setFill(p3);;
		
		//Fill a circle
		ImagePattern p4 = new ImagePattern(img, 0, 0, 0.5, 0.5, true);
		Circle c = new Circle(50, 50, 25);
		c.setFill(p4);
		
		HBox root = new HBox(r1, r2, r3, c);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}

	@Override
	public void init() throws Exception {
		URL url = getClass().getResource(imgUrl);
		if(null == url){
			//;;
			return;
		}
		img = new Image(url.toExternalForm());
	}

	
	
	
}

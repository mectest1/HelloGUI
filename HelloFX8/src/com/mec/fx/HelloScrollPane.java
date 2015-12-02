package com.mec.fx;

import com.mec.resources.Msg;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloScrollPane extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		//Reference to the Txt
		String message = HelloEarthRise.getMessage();
		Text textRef = new Text(message);
		textRef.setLayoutY(100D);
		textRef.setTextOrigin(VPos.TOP);
		textRef.setTextAlignment(TextAlignment.JUSTIFY);
		textRef.setWrappingWidth(400);
		textRef.setFill(Color.rgb(187, 195, 107));
		textRef.setFont(Font.font("SansSerif", FontWeight.BOLD, 24));
		
		//Provides the animated scrolling behaviour for the text
		TranslateTransition transTransition  = new TranslateTransition(new Duration(75000), textRef);
		transTransition.setToY(-820);
		transTransition.setInterpolator(Interpolator.LINEAR);
		transTransition.setCycleCount(Timeline.INDEFINITE);
		
		//Create an ImageView containing he Image
		Image image = new Image(HelloEarthRise.getImageUrl());
		ImageView imageView = new ImageView(image);
		
		//Create a group containing the text
//		Group textGroup = new Group(textRef);
//		textGroup.setLayoutX(50);
//		textGroup.setLayoutY(180);
//		textGroup.setClip(new Rectangle(430, 85));
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setLayoutX(50);
		scrollPane.setLayoutY(180);
		scrollPane.setPrefWidth(400);
		scrollPane.setPrefHeight(85);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setPannable(true);
		scrollPane.setContent(textRef);
		scrollPane.setStyle("-fx-background-color: transparent;");
		
		//Combine ImageView and group
//		Group root = new Group(imageView, textGroup);
		Group root = new Group(imageView, scrollPane);
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(HelloEarthRise.getTitle());
		primaryStage.show();
		
		//Start the text animation
		transTransition.play();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloEarthRise extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		//Reference to the Txt
		String message = Msg.get(this, "messages");
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
		Image image = new Image(Msg.get(this, "image.url"));
		ImageView imageView = new ImageView(image);
		
		//Create a group containing the text
		Group textGroup = new Group(textRef);
		textGroup.setLayoutX(50);
		textGroup.setLayoutY(180);
		textGroup.setClip(new Rectangle(430, 85));
		
		//Combine ImageView and group
		Group root = new Group(imageView, textGroup);
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		//Start the text animation
		transTransition.play();
		
	}

}

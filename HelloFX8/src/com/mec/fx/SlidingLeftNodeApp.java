package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.binding.When;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SlidingLeftNodeApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button b1 = new Button(Msg.get(this, "b1"));
		Button b2 = new Button(Msg.get(this, "b2"));
		Button b3 = new Button(Msg.get(this, "b3"));
		Button visibleBtn = new Button(Msg.get(this, "makeInvisible"));
		//
		visibleBtn.setOnAction(e -> b2.setVisible(!b2.isVisible()));
		
		//
		visibleBtn.textProperty().bind(new When(b2.visibleProperty())
				.then(Msg.get(this, "makeInvisible"))
				.otherwise(Msg.get(this, "makeVisible"))
				);
		
		//Bind the managed property of b2 to its visible property
		b2.managedProperty().bind(b2.visibleProperty());
		
		//
		HBox root = new HBox();
		root.getChildren().addAll(visibleBtn, b1, b2, b3);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

	
}

package com.mec.fx;

import com.mec.resources.Msg;
import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InlineStylesApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button yesBtn = new Button(Msg.get(this, "yes"));
		Button noButton = new Button(Msg.get(this, "no"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//Add an inline style to the Yes button
		yesBtn.setStyle(Msg.get(this, "style.button"));
		
		Button openBtn = new Button(Msg.get(this, "open"));
		Button saveBtn = new Button(Msg.get(this, "save"));
		Button closeBtn = new Button(Msg.get(this, "close"));
		
		//
		VBox vb1 = new VBox();
		vb1.setPadding(new Insets(10, 10, 10, 10));
		vb1.getChildren().addAll(yesBtn, noButton, cancelBtn);
		
		VBox vb2 = new VBox();
		vb2.setPadding(new Insets(10, 10, 10, 10));
		vb2.getChildren().addAll(openBtn, saveBtn, closeBtn);
		
		//Add a border to VBoxes using an inline style
		vb1.setStyle(Msg.get(this, "style.vbox.1"));
		vb2.setStyle(Msg.get(this, "style.vbox.2"));
		
		//
		HBox root = new HBox();
		root.setSpacing(20);
		root.setPadding(new Insets(10, 10, 10, 10));
		root.getChildren().addAll(vb1, vb2);
		
		//
		root.setStyle(Msg.get(this, "style.hbox"));
		
		//
		Scene scene = new Scene(root);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.setScene(scene);

		primaryStage.show();
		
	}

}

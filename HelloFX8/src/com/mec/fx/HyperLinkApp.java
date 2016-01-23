package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class HyperLinkApp extends Application {

	private WebView webview;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Must create a WebView object from the JavaFX Application Thread
		webview = new WebView();
		
		//Create soem hyperlinks
		Hyperlink link1 = new Hyperlink(Msg.get(this, "jdojo"));
		link1.setOnAction(e -> loadPage(Msg.get(this, "jdojo.link")));
		Hyperlink link2 = new Hyperlink(Msg.get(this, "yahoo"));
		link2.setOnAction(e -> loadPage(Msg.get(this, "yahoo.link")));
		Hyperlink link3 = new Hyperlink(Msg.get(this, "google"));
		link3.setOnAction(e -> loadPage(Msg.get(this, "google.link")));
		
		//
		HBox linkBox = new HBox(10, link1, link2, link3);
		linkBox.setAlignment(Pos.TOP_RIGHT);
		
//		BorderPane root = new BorderPane(webview, linkBox, null, null, null);
		BorderPane root = new BorderPane(webview);
		root.setTop(linkBox);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();

	}

	
	private void loadPage(String url){
		webview.getEngine().load(url);
	}
}

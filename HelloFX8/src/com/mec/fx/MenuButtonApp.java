package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MenuButtonApp extends Application{

	private WebView webview;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		webview = new WebView();
		
		//
		MenuItem link1 = new MenuItem(Msg.get(HyperLinkApp.class, "jdojo"));
		link1.setOnAction(e -> loadPage(Msg.get(HyperLinkApp.class, "jdojo.link")));
		MenuItem link2 = new MenuItem(Msg.get(HyperLinkApp.class, "yahoo"));
		link2.setOnAction(e -> loadPage(Msg.get(HyperLinkApp.class, "yahoo.link")));
		MenuItem link3 = new MenuItem(Msg.get(HyperLinkApp.class, "google"));
		link3.setOnAction(e -> loadPage(Msg.get(HyperLinkApp.class, "google.link")));
		
		MenuButton visitBtn = new MenuButton(Msg.get(this, "visit"));
		visitBtn.getItems().addAll(link1, link2, link3);
		
		
		HBox menuBtnBar = new HBox(10, visitBtn);
		menuBtnBar.setAlignment(Pos.CENTER_RIGHT);
		
		BorderPane root = new BorderPane(webview);
		root.setTop(menuBtnBar);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	private void loadPage(String url){
		webview.getEngine().load(url);
	}
	
	

}

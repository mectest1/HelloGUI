package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class SplitMenuButtonApp extends Application {
	WebView webview;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Must create a WebView from the JavaFX Application Thread
		webview = new WebView();
		
		//Create a SplitMenuButton
		SplitMenuButton splitBtn = new SplitMenuButton();
//		int count = 0;
		Msg.getList(this, "label").stream().forEachOrdered(l -> splitBtn.getItems().add(new MenuItem(l)));
//		Msg.getList(this, "label").stream().forEachOrdered(splitBtn.getItems().add(MenuItem::new));
		int count= 0;
		for(MenuItem item : splitBtn.getItems()){
			final int index = count;
//			if(0 == index){
//				splitBtn.setText(item.getText());
//				splitBtn.setText(item.getText());
//				splitBtn.setOnAction(e -> webview.getEngine().load(Msg.getList(this, "link").get(index)));
//			}
			item.setOnAction(e -> webview.getEngine().load(Msg.getList(this, "link").get(index)));
			++count;
		}
		
		splitBtn.setText(Msg.get(this, "home"));
		splitBtn.setOnAction(splitBtn.getItems().get(0).getOnAction());
		
		//
		BorderPane root = new BorderPane(webview);
		BorderPane.setAlignment(splitBtn, Pos.CENTER_RIGHT);
		root.setTop(splitBtn);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

}

package com.mec.fx;

import java.util.stream.Stream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		MenuBar menuBar = new MenuBar();
		Stream.of(Msg.get(this, "menus").split(Msg.get(this, "menu.delimiter")))
			.forEach(m -> menuBar.getMenus().add(new Menu(m.trim())));
		
		BorderPane root = new BorderPane();
		root.setTop(menuBar);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

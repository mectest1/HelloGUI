package com.mec.fx;

import java.util.List;
import java.util.Map;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class FXParamApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parameters p = this.getParameters();
		Map<String, String> namedParams = p.getNamed();
		List<String> unnamedParams = p.getUnnamed();
		List<String> rawParams = p.getRaw();
		
//		String paramStr = "Named Parameters: %s\nUnnamed Parameters: %s\nRaw Parameters: %s";
		String paramStr = Msg.get(this, "paramStr");
		
		TextArea ta = new TextArea(String.format(paramStr, namedParams, unnamedParams, rawParams));
		Group root = new Group(ta);
		primaryStage.setScene(new Scene(root));
//		primaryStage.setTitle("Application Parameters");
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	public static void main(String[] args) {
//		launch(args);
		launch(new String[]{"Anna", "Lola"});
//		FXParamApp.launch(new String[]{"Anna", "Lola", "width=200", "height=100"});	//failed named parameters: named parameter should prefix with --
//		FXParamApp.launch(new String[]{"Anna", "Lola", "--width=200", "--height=100"});
	}
}

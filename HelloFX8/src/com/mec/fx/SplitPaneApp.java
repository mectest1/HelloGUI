package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SplitPaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextArea desc1 = new TextArea();
		desc1.setPrefColumnCount(10);
		desc1.setPrefRowCount(4);
		
		TextArea desc2 = new TextArea();
		desc2.setPrefColumnCount(10);
		desc2.setPrefRowCount(4);
		
		VBox vb1 = new VBox(new Label(Msg.get(this, "label.desc1")), desc1);
		VBox vb2 = new VBox(new Label(Msg.get(this, "label.desc2")), desc2);
		
		//
		SplitPane sp = new SplitPane();
		sp.getItems().addAll(vb1, vb2);
		
		HBox root = new HBox(10, sp);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		HBox.setHgrow(sp, Priority.ALWAYS);
		root.setMaxWidth(Double.MAX_VALUE);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

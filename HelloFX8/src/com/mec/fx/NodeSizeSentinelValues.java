package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NodeSizeSentinelValues extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button okBtn = new Button(Msg.get(this, "ok"));
		Button cancelBtn = new Button(Msg.get(this, "cancel"));
		
		//OVerride the instrinsic width of the casncel button
		cancelBtn.setPrefWidth(100);
		
		VBox root = new VBox();
		root.getChildren().addAll(okBtn, cancelBtn);
		
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		String output = String.format(Msg.get(this, "output"), 
				okBtn.getPrefWidth(),
				okBtn.getMinWidth(),
				okBtn.getMaxWidth(),
				
				cancelBtn.getPrefWidth(),
				cancelBtn.getMinWidth(),
				cancelBtn.getMaxWidth()
				);
		
		Label info = new Label(output);
		root.getChildren().add(info);
		primaryStage.sizeToScene();
		
		
	}

	
	private static final PrintStream out = System.out;
}

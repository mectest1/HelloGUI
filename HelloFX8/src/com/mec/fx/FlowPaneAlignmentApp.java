package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FlowPaneAlignmentApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FlowPane fp1 = createFlowPane(Pos.BOTTOM_RIGHT);
		FlowPane fp2 = createFlowPane(Pos.BOTTOM_LEFT);
		FlowPane fp3 = createFlowPane(Pos.CENTER);
		
		HBox root = new HBox(fp1, fp2, fp3);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	private FlowPane createFlowPane(Pos alignment){
		FlowPane fp = new FlowPane(5, 5);
		fp.setPrefSize(200, 100);
		fp.setPrefSize(200, 100);
		fp.setAlignment(alignment);
		fp.getChildren().addAll(new Text(alignment.toString())
				, new Button(Msg.get(this, "b1"))
				, new Button(Msg.get(this, "b2"))
				, new Button(Msg.get(this, "b3"))
				);
		fp.setStyle(Msg.get(this, "style"));
		return fp;
	}
}

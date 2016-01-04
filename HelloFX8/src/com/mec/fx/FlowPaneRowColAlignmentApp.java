package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FlowPaneRowColAlignmentApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FlowPane fp1 = createFlowPane(Orientation.HORIZONTAL, VPos.TOP, HPos.LEFT);
		FlowPane fp2 = createFlowPane(Orientation.HORIZONTAL, VPos.CENTER, HPos.LEFT);
		FlowPane fp3 = createFlowPane(Orientation.VERTICAL, VPos.CENTER, HPos.RIGHT);
		
		HBox root = new HBox(fp1, fp2, fp3);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	private FlowPane createFlowPane(Orientation orientation, VPos rowAlign, HPos colAlign){
		//Show the row or column aligntment value in a text
		Text t = new Text();
		if(Orientation.HORIZONTAL == orientation){
			t.setText(rowAlign.toString());
		}else{
			t.setText(colAlign.toString());
		}
		
		//Show the orientation of the FlowPane in a TextPane
		TextArea ta = new TextArea(orientation.toString());
		ta.setPrefColumnCount(5);
		ta.setPrefRowCount(3);
		//
		FlowPane fp = new FlowPane(orientation, 5, 5);
		fp.setRowValignment(rowAlign);
		fp.setColumnHalignment(colAlign);
		fp.setPrefSize(175, 130);
		fp.getChildren().addAll(t, ta);
		fp.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		return fp;
	}

}

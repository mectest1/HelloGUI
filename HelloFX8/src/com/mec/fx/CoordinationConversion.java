package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CoordinationConversion extends Application {

	private Circle marker;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField fName = new TextField();
		TextField lName = new TextField();
		TextField salary = new TextField();
		//
		
		//The Circle node is unmanaged
		marker = new Circle(5);
		marker.setManaged(false);
		marker.setFill(Color.RED);
		marker.setMouseTransparent(true);
		
		//
		HBox hb1 = new HBox();
		HBox hb2 = new HBox();
		HBox hb3 = new HBox();
		hb1.getChildren().addAll(new Label(Msg.get(this, "firstName")), fName);
		hb2.getChildren().addAll(new Label(Msg.get(this, "lastName")), lName);
		hb3.getChildren().addAll(new Label(Msg.get(this, "salary")), salary);
		
		
		Label info = new Label();
		
		VBox root = new VBox();
		root.getChildren().addAll(hb1, hb2, hb3, marker, info);
		
		Scene scene = new Scene(root);
		scene.focusOwnerProperty().addListener((observable, oldVal, newVal) -> {
			String placedInfo = placeMarker(newVal);
			info.setText(placedInfo);
			primaryStage.sizeToScene();
		}); 
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	private String placeMarker(Node newNode){	//<-------Need further test;
		double nodeMinX = newNode.getLayoutBounds().getMinX();
		double nodeMinY = newNode.getLayoutBounds().getMinY();
		double markerWidth = marker.getLayoutBounds().getWidth();
		double markerHeight = marker.getLayoutBounds().getHeight();
		
		Point2D nodeInScene = newNode.localToScene(nodeMinX, nodeMinY);
		Point2D nodeInMarkerLocal = marker.sceneToLocal(nodeInScene);
		Point2D nodeInMarkerParent = marker.parentToLocal(nodeInMarkerLocal);
		
		//
		//Position the circle appropriately
		marker.relocate(
//				nodeInMarkerParent.getX() + marker.getLayoutBounds().getMinX(), 
//				nodeInMarkerParent.getY() + marker.getLayoutBounds().getMinY()
//				nodeInMarkerParent.getX(),
//				nodeInMarkerParent.getY()
				nodeInScene.getX() - markerWidth/2,
				nodeInScene.getY() - markerHeight/2
				);
		
		return String.format(Msg.get(this, "output"), 
				nodeMinX, nodeMinY,
				nodeInScene, 
				nodeInMarkerLocal,
				nodeInMarkerParent
				);
	}

}

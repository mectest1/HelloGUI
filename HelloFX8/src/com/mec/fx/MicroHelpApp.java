package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MicroHelpApp extends Application {

	private Text helpText = new Text();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField fName = new TextField();
		TextField lName = new TextField();
		TextField salary = new TextField();
		
		Button closeBtn = new Button(Msg.get(this, "button"));
		closeBtn.setOnAction(e -> Platform.exit());
		
		//
		String helpAttr = Msg.get(this, "attr.name.help");
		fName.getProperties().put(helpAttr, Msg.get(this, "attr.value.firstName"));
		lName.getProperties().put(helpAttr, Msg.get(this, "attr.value.lastName"));
		salary.getProperties().put(helpAttr, Msg.get(this, "attr.value.salary"));
		
		//The help node is unmanaged
		helpText.setManaged(false);
		helpText.setTextOrigin(VPos.TOP);
		helpText.setFill(Color.RED);
		helpText.setFont(Font.font(null, 9));
		helpText.setMouseTransparent(true);
		helpText.setVisible(false);
		
		//Add all nodes to a GridPane
		GridPane root = new GridPane();
		
		root.add(new Label(Msg.get(this, "label.firstName")), 1, 1);
		root.add(fName, 2, 1);
		
		root.add(new Label(Msg.get(this, "label.lastName")), 1, 2);
		root.add(lName, 2, 2);
		
		root.add(new Label(Msg.get(this, "label.salary")), 1, 3);
		root.add(salary, 2, 3);
		
		root.add(closeBtn, 3, 3);
		root.add(helpText, 4, 3);
		
		//
		Scene scene = new Scene(root, 300, 100);
		
		//Add a change listener to the scene, so you know when the focus owner
		//changes and display the micro help
		scene.focusOwnerProperty().addListener((observable, oldVal, newVal) -> {
			focusChanged(observable, oldVal, newVal);
		});
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	private void focusChanged(ObservableValue<? extends Node> observable, 
			Node oldNode, Node newNode
			){
		String microHelpText = (String) newNode.getProperties().get(Msg.get(this, "attr.name.help"));
		
		//
		if(null == microHelpText || microHelpText.isEmpty()){
			helpText.setVisible(false);
			return;
		}
		
		//
		helpText.setText(microHelpText);
		helpText.setVisible(true);
		
		//Position the help text nod
		double x = newNode.getLayoutX() + newNode.getLayoutBounds().getMinX() -
				helpText.getLayoutBounds().getMinX();
		double y = newNode.getLayoutY() + newNode.getLayoutBounds().getMinY() +
					newNode.getLayoutBounds().getHeight() - 
					helpText.getLayoutBounds().getMinY();
		
		helpText.setLayoutX(x);
		helpText.setLayoutY(y);
		
		//
		helpText.setWrappingWidth(newNode.getLayoutBounds().getWidth());
				
	}
}

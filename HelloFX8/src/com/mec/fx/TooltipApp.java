package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TooltipApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label nameLbl = new Label(Msg.get(this, "label.name"));
		TextField nameFld = new TextField();
		Button saveBtn = new Button(Msg.get(this, "button.save"));
		Button closeBtn = new Button(Msg.get(this, "button.close"));
		
		//Set an ActionEvent handler
		closeBtn.setOnAction(e -> primaryStage.close());
		
		//Add tooptips for Name field and save button
		Tooltip.install(nameLbl, new Tooltip(Msg.get(this, "tip.nameLbl")));
		nameFld.setTooltip(new Tooltip(Msg.get(this, "tip.name")));			//<- Convenient method to set tooltip for Controls
		Tooltip.install(saveBtn, new Tooltip(Msg.get(this, "tip.save")));	//<- Canonical way to set tooptip for any node
		
		//Add and configure tootip for Name close button
		Tooltip closeBtnTip = new Tooltip(Msg.get(this, "tip.close"));
		closeBtnTip.setStyle(Msg.get(this, "tip.close.style"));
		
		//Displ;ay the icon above text
		closeBtnTip.setContentDisplay(ContentDisplay.TOP);
		
		//
		Label closeTipIcon = new Label(Msg.get(this, "tip.close.icon"));
		closeTipIcon.setStyle(Msg.get(this, "tip.close.icon.style"));
		closeBtnTip.setGraphic(closeTipIcon);
		
		//
		closeBtn.setTooltip(closeBtnTip);
		
		//
		HBox root = new HBox(10, nameLbl, nameFld, saveBtn, closeBtn);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

}

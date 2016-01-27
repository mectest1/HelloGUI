package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PaginationApp extends Application{

	private static final int PAGE_COUNT = 5 ;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Pagination pagination = new Pagination(PAGE_COUNT);
		
		//Set the page factory
		pagination.setPageFactory(this::getPage);
		pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);	//<- Change page indicators to bullet buttons
		
		//
		VBox root = new VBox(10, pagination);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
	
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	private Node getPage(int pageIndex){
		Label retval = new Label(String.format(Msg.get(this, "content.pattern"), 1 + pageIndex));
		retval.setStyle(Msg.get(FlowPaneApp.class, "style"));
		retval.setPrefHeight(200);
		retval.setPrefWidth(150);
		return retval;
	}
	
	

}

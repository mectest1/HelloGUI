package com.mec.fx;

import java.io.PrintStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TreeViewEditingDataApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TreeView<String> treeView = TreeViewUtil.getTreeView();
		
		//Make the TreeView editable
		treeView.setEditable(true);
		
		//Set a cell factory to use TextFieldTreeCell
		treeView.setCellFactory(TextFieldTreeCell.forTreeView());
		
		//Set the editing related event handlers
		treeView.setOnEditStart(this::editStart);
		treeView.setOnEditCommit(this::editCommit);
		treeView.setOnEditCancel(this::editCancel);
		
		HBox root = new HBox(treeView);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	private void editStart(TreeView.EditEvent<String> e){
		out.printf(Msg.get(this, "msg.startEdit"), e.getTreeItem());
	}
	
	private void editCommit(TreeView.EditEvent<String> e){
		out.printf(Msg.get(this, "msg.commitEdit"), e.getTreeItem(), 
				e.getOldValue(), e.getNewValue()
				);
	}
	
	private void editCancel(TreeView.EditEvent<String> e){
		out.printf(Msg.get(this, "msg.cancelEdit"), e.getTreeItem());
	}
	
	
	private static final PrintStream out = System.out;
}


























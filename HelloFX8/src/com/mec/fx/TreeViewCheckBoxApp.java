package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TreeViewCheckBoxApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TreeView<String> treeView = TreeViewUtil.getCheckBoxTreeView();
		
		TreeItem<String> deptItem = treeView.getRoot().getChildren().get(1);
		((CheckBoxTreeItem<String>) deptItem).setIndependent(true);
		
		//Set the cell factory to draw t CheckBox in cells
		treeView.setCellFactory(CheckBoxTreeCell.forTreeView());
		
		HBox root = new HBox(10, treeView);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

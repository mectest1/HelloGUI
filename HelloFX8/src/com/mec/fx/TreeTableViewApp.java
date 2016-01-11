package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TreeTableViewApp extends Application {

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {
		TreeItem<Person> rootNode = TreeTableUtil.getModel();
		rootNode.setExpanded(true);
		
		//Create a TreeTableView with model
		TreeTableView<Person> treeTable = new TreeTableView<>(rootNode);
		treeTable.setPrefWidth(400);
		
		
		//Add age column
		
		
		//Add columns:
		treeTable.getColumns().addAll(TreeTableUtil.getfirstNameColumn()
				, TreeTableUtil.getLastNameColumn()
				, TreeTableUtil.getBirthDate()
				, TreeTableUtil.getAgeCategoryColumn()
				, TreeTableUtil.getAgeDescColumn()
				);
		
		
		HBox root = new HBox(treeTable);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

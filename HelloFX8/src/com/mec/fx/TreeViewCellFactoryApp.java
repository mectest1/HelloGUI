package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TreeViewCellFactoryApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TreeView<String> treeView = TreeViewUtil.getTreeView();
		
		//Set a cell factory to prepend the row index to the TreeItme value
		treeView.setCellFactory(tv -> {
			TreeCell<String> cell = new TreeCell<String>(){

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if(empty){
						this.setText(null);
						this.setGraphic(null);
					}else{
						String value = this.getTreeItem().getValue();
						this.setText(String.format(Msg.get(TreeViewCellFactoryApp.class, "text.pattern"), this.getIndex(), value));
					}
				}
				
			};
			
			return cell;
		});
		
		
		HBox root = new HBox(20, treeView);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

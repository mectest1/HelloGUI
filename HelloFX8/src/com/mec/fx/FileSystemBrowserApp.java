package com.mec.fx;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FileSystemBrowserApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create a rootnode using the current directory
		PathTreeItem rootNode = new PathTreeItem(Paths.get(Msg.get(this, "currentDir")));
		TreeView<Path> treeView = new TreeView<>(rootNode);
		treeView.setShowRoot(false);	//<- hide the root node. aka the "currentDir";
		
		//Set a cell factory to display only file name
		treeView.setCellFactory(tv -> {
			TreeCell<Path> cell = new TreeCell<Path>(){

				@Override
				protected void updateItem(Path item, boolean empty) {
					super.updateItem(item, empty);
					if(null != item && !empty){
						Path fileName = item.getFileName();
						if(null == fileName){
							this.setText(item.toString());
						}else{
							this.setText(fileName.toString());
						}
						this.setGraphic(this.getTreeItem().getGraphic());
					}else{
						this.setText(null);
						this.setGraphic(null);
					}
				}
				
			};
			return cell;
		});
		
		
		
		HBox root = new HBox(treeView);
		
		
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		treeView.prefWidthProperty().bind(treeView.getScene().widthProperty());
	}

}























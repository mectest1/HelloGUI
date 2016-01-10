package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TreeItemAddDeleteApp extends Application {

	private TreeView<String> treeView = new TreeView<>();
	private TextArea msgLogFld = new TextArea();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		treeView.getSelectionModel().selectFirst();
		
		//Create the root node and adds event handler to it
		TreeItem<String> depts = new TreeItem<>(Msg.get(this, "item.root"));
		depts.addEventHandler(TreeItem.branchExpandedEvent(), this::branchExpanded);
		depts.addEventHandler(TreeItem.branchCollapsedEvent(), this::branchCollapsed);
		depts.addEventHandler(TreeItem.childrenModificationEvent(), this::childrenModification);
		
		//Set the root node for the window
		treeView.setRoot(depts);
		
		VBox rightPane = getRightPane();
		
		HBox root = new HBox(10, treeView, rightPane);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	private VBox getRightPane(){
		TextField itemFld = new TextField();
		
		Button addItemBtn = new Button(Msg.get(this, "add"));
		addItemBtn.setOnAction(e -> this.addItem(itemFld.getText()));
		
		//
		Button removeItemBtn = new Button(Msg.get(this, "remove"));
		removeItemBtn.setOnAction(e -> this.removeItem());
		
		//
		msgLogFld.setPrefRowCount(15);
		msgLogFld.setPrefColumnCount(25);
		VBox box = new VBox(5 
				, new Label(Msg.get(this, "label.select"))
				, new HBox(5
						, new Label(Msg.get(this, "label.item"))
						, itemFld
						, addItemBtn
						)
				, removeItemBtn
				, new Label(Msg.get(this, "label.msg"))
				, msgLogFld
				);
		return box;
	}
	
	private void addItem(String value){
		if(null == value || value.trim().isEmpty()){
			logMsg(Msg.get(this, "msg.add.empty"));
			return;
		}
		
		TreeItem<String> parent = treeView.getSelectionModel().getSelectedItem();
		if(null == parent){
			logMsg(Msg.get(this, "msg.add.notSelected"));
			return;
		}
		
		//Check for duplicate
		for(TreeItem<String> child : parent.getChildren()){
			if(value.equals(child.getValue())){
				logMsg(String.format(Msg.get(this, "msg.add.duplicate"), value, parent.getValue()));
				return;
			}
		}
		
		TreeItem<String> newItem = new TreeItem<String>(value);
		parent.getChildren().add(newItem);
		if(!parent.isExpanded()){
			parent.setExpanded(true);
		}
		treeView.getSelectionModel().select(newItem);
	}
	private void removeItem(){
		TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
		if(null == item){
			logMsg(Msg.get(this, "msg.remove.null"));
			return;
		}
		
		TreeItem<String> parent = item.getParent();
		if(null == parent){
			logMsg(Msg.get(this, "msg.remove.root"));
		}else{
			parent.getChildren().remove(item);
		}
	}

	private void branchExpanded(TreeItem.TreeModificationEvent<String> e){
		String nodeValue = e.getSource().getValue();
		logMsg(String.format(Msg.get(this, "msg.expanded"), nodeValue));
	}
	
	private void branchCollapsed(TreeItem.TreeModificationEvent<String> e){
		String nodeValue = e.getSource().getValue();
		logMsg(String.format(Msg.get(this, "msg.collapsed"), nodeValue));
	}
	
	private void childrenModification(TreeItem.TreeModificationEvent<String> e){
		if(e.wasAdded()){
			e.getAddedChildren().stream().forEach(item -> logMsg(String.format(Msg.get(this, "msg.added"), item.getValue())));
		}
	}
	
	private void logMsg(String msg){
		msgLogFld.appendText(msg + Msg.get(this, "newline"));
	}
}

















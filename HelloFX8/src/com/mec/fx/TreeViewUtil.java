package com.mec.fx;

import com.mec.resources.Msg;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class TreeViewUtil {

		public static TreeView<String> getTreeView(){
			TreeItem<String> depts = new TreeItem<>(Msg.get(TreeViewUtil.class, "dept.name"));
			
			//
			Msg.getList(TreeViewUtil.class, "dept").stream().forEach( e -> 
					depts.getChildren().add(new TreeItem<>(e)));
			
			Msg.getList(TreeViewUtil.class, "dept.is").stream().forEach(e -> depts.getChildren().get(0).getChildren().add(new TreeItem<>(e)));
			Msg.getList(TreeViewUtil.class, "dept.claim").stream().forEach(e -> depts.getChildren().get(1).getChildren().add(new TreeItem<>(e)));
			Msg.getList(TreeViewUtil.class, "dept.underwriting").stream().forEach(e -> depts.getChildren().get(2).getChildren().add(new TreeItem<>(e)));
			
			return new TreeView<>(depts);
		}
		public static TreeView<String> getCheckBoxTreeView(){
			CheckBoxTreeItem<String> depts = new CheckBoxTreeItem<>(Msg.get(TreeViewUtil.class, "dept.name"));
			
			//
			Msg.getList(TreeViewUtil.class, "dept").stream().forEach( e -> 
			depts.getChildren().add(new CheckBoxTreeItem<>(e)));
			
			Msg.getList(TreeViewUtil.class, "dept.is").stream().forEach(e -> depts.getChildren().get(0).getChildren().add(new CheckBoxTreeItem<>(e)));
			Msg.getList(TreeViewUtil.class, "dept.claim").stream().forEach(e -> depts.getChildren().get(1).getChildren().add(new CheckBoxTreeItem<>(e)));
			Msg.getList(TreeViewUtil.class, "dept.underwriting").stream().forEach(e -> depts.getChildren().get(2).getChildren().add(new CheckBoxTreeItem<>(e)));
			
			return new TreeView<>(depts);
		}
}

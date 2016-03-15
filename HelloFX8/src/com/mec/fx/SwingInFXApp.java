package com.mec.fx;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SwingInFXApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		SwingNode swingNode = new SwingNode();
		JButton helloBtn = new JButton(Msg.get(this, "hello"));
		helloBtn.addActionListener(e -> {
			JOptionPane.showConfirmDialog(helloBtn, Msg.get(this, "msg"));
		});
		swingNode.setContent(helloBtn);
		
		StackPane root = new StackPane(swingNode);
		root.setStyle(Msg.get(this, "root.style"));
		Scene scene = new Scene(root, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

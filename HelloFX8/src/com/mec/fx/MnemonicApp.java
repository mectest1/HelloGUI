package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MnemonicApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox();
		root.setSpacing(10);;
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		//
		Scene scene = new Scene(root);
		Label msg = new Label(Msg.get(this, "msg"));
		Label lbl = new Label(Msg.get(this, "press"));
		
		//Use Alt+1 as the mnemonic for Button 1
		Button btn1 = new Button(Msg.get(this, "b1"));
		btn1.setOnAction(e -> lbl.setText(Msg.get(this, "b1.clicked")));
		
		
		//Use Alt+2 as the mnemonic key for Button 2
		Button btn2 = new Button(Msg.get(this, "b2"));
		btn2.setOnAction(e -> lbl.setText(Msg.get(this, "b2.clicked")));
		KeyCombination kc = new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.ALT_DOWN);
		Mnemonic mnemonic = new Mnemonic(btn2, kc);
		scene.addMnemonic(mnemonic);
		
		//Add a accelarator key to the scene
		KeyCombination kc4 = new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN);
		Runnable task = () -> scene.getWindow().hide();
		scene.getAccelerators().put(kc4, task);
		
		//Add all children to the VBox
		root.getChildren().addAll(msg, lbl, btn1, btn2);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

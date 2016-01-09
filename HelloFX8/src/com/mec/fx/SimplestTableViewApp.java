package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimplestTableViewApp extends Application {

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {
		TableView<Person> table = new TableView<>(PersonTableUtil.getPersonList());
		
		//Add columns to the TableView
		table.getColumns().addAll(PersonTableUtil.getIdColumn(), 
				PersonTableUtil.getFirstNameColumn(),
				PersonTableUtil.getLastNameColumn(),
				PersonTableUtil.getBirthDateColumn()
				);
		
		//
		VBox root = new VBox(table);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NestedTableViewApp extends Application {

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage primaryStage) throws Exception {
		TableView<Person> table = new TableView<>(PersonTableUtil.getPersonList());
		TableColumn<Person, String> nameCol = new TableColumn<>(Msg.get(this, "name"));
		nameCol.getColumns().addAll(PersonTableUtil.getFirstNameColumn(),
				PersonTableUtil.getLastNameColumn());
		//Add columns to the TableView
		table.getColumns().addAll(PersonTableUtil.getIdColumn(), 
				nameCol,
				PersonTableUtil.getBirthDateColumn()
				, PersonTableUtil.getSelectColumn()
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

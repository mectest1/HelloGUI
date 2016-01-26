package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TitledPaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextField firstNameFld = new TextField();
		firstNameFld.setPrefColumnCount(8);
		
		TextField lastNameFld = new TextField();
		lastNameFld.setPrefColumnCount(8);
		
		//
		DatePicker dob = new DatePicker();
		dob.setPrefWidth(150);
		
		//
		GridPane grid = new GridPane();
		grid.addRow(0, new Label(Msg.get(this, "label.firstName")), firstNameFld);
		grid.addRow(1, new Label(Msg.get(this, "label.lastName")), lastNameFld);
		grid.addRow(2, new Label(Msg.get(this, "label.DOB")), dob);
		
		TitledPane infoPane = new TitledPane();
		infoPane.setText(Msg.get(this, "title.text"));
		infoPane.setContent(grid);
		
		Image img = new Image(getClass().getResource(Msg.get(this, "title.graphic")).toExternalForm());
		ImageView imgView = new ImageView(img);
		infoPane.setGraphic(imgView);
		
		HBox root = new HBox(10, infoPane);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}

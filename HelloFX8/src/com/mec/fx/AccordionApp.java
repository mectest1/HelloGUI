package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AccordionApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		TitledPane generalPane = getGeneralPane();
		TitledPane addressPane = getAddressPane();
		TitledPane phonePane = getPhonePane();
		
		//
//		Accordion root = new Accordion(generalPane, addressPane, phonePane);
		Accordion root = new Accordion();
		root.getPanes().addAll(generalPane, addressPane, phonePane);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root, 300, 300);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

	
	TitledPane getGeneralPane(){
		GridPane grid = new GridPane();
		String[] fields = Msg.get(this, "fields.person").split(Msg.get(this, "field.delimiter"));
		for(int i = 0; i < fields.length; ++i){
			grid.addRow(i, new Label(fields[i].trim()), new TextField());
		}
		
		TitledPane phonePane = new TitledPane(Msg.get(this, "person.title"), grid);
		return phonePane;
	}
	
	TitledPane getAddressPane(){
		GridPane grid = new GridPane();
		String[] fields = Msg.get(this, "fields.address").split(Msg.get(this, "field.delimiter"));
		for(int i = 0; i < fields.length; ++i){
			grid.addRow(i, new Label(fields[i].trim()), new TextField());
		}
		
		TitledPane phonePane = new TitledPane(Msg.get(this, "address.title"), grid);
		return phonePane;
	}
	
	TitledPane getPhonePane(){
		GridPane grid = new GridPane();
		String[] fields = Msg.get(this, "fields.phone").split(Msg.get(this, "field.delimiter"));
		for(int i = 0; i < fields.length; ++i){
			grid.addRow(i, new Label(fields[i].trim()), new TextField());
		}
		
		TitledPane phonePane = new TitledPane(Msg.get(this, "phone.title"), grid);
		return phonePane;
	}
	
}

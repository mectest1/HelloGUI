package com.mec.fx;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TableViewMapDataApp extends Application {

	private final String idColumnKey = Msg.get(TableViewMapDataApp.class, "id");
	private final String firstNameColumnKey = Msg.get(TableViewMapDataApp.class, "firstNameColumn");
	private final String lastNameColumnKey = Msg.get(TableViewMapDataApp.class, "lastNameColumn");
	private final String birthDateColumnKey = Msg.get(TableViewMapDataApp.class, "birthDate");
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		TableView<Map> table = new TableView<>();
		ObservableList<Map<String, Object>> items = this.getMapData();
		table.getItems().addAll(items);
		table.setTableMenuButtonVisible(true);
		this.addColumns(table);
		
		
		HBox root = new HBox(table);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	public ObservableList<Map<String, Object>> getMapData(){
		ObservableList<Map<String, Object>> items = FXCollections.observableArrayList();
		ObservableList<Person> persons = PersonTableUtil.getPersonList();
	
		//Extract the person data, add the data to a Map, and add the Map
		//to the items list
		for(Person p : persons){
			Map<String, Object> map = new HashMap<>();
			map.put(idColumnKey, p.getPersonId());
			map.put(firstNameColumnKey, p.getFirstName());
			map.put(lastNameColumnKey, p.getLastName());
			map.put(birthDateColumnKey, p.getBirthDate());
			items.add(map);
		}
		
		return items;
	}
	
	
	@SuppressWarnings("unchecked")
	public void addColumns(TableView<Map> table){
		TableColumn<Map, Integer> idCol = new TableColumn<>(Msg.get(this, "header.id"));
		idCol.setCellValueFactory(new MapValueFactory<>(idColumnKey));
		
		TableColumn<Map, String> firstNameCol = new TableColumn<>(Msg.get(this, "header.firstName"));
		firstNameCol.setCellValueFactory(new MapValueFactory(firstNameColumnKey));
		
		TableColumn<Map, String> lastNameCol = new TableColumn<>(Msg.get(this, "header.lastName"));
		lastNameCol.setCellValueFactory(new MapValueFactory<>(lastNameColumnKey));
		
		TableColumn<Map, LocalDate> birthDateCol = new TableColumn<>(Msg.get(this, "header.birthDate"));
		birthDateCol.setCellValueFactory(new MapValueFactory<>(birthDateColumnKey));
		
		table.getColumns().addAll(idCol, firstNameCol, lastNameCol, birthDateCol);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

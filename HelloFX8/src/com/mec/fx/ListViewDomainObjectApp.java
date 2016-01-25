package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ListViewDomainObjectApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		ListView<Person> persons = new ListView<>();
		persons.setPrefSize(150, 120);
		
		//
		Msg.getList(this, "person").forEach(s -> {
			String[] fields = s.split(Msg.get(this, "person.delimiter"));
			persons.getItems().add(new Person(fields));
		});
		
		//Add a custom cell factory to dislay formtted names of persons
		persons.setCellFactory(l -> {
			return new ListCell<Person>(){

				@Override
				protected void updateItem(Person item, boolean empty) {
					super.updateItem(item, empty);
					
					int index = this.getIndex();
					
					String name = null;
					
					//Format name
					if(null == item || empty){
						
					}else{
						name = String.format(Msg.get(ListViewDomainObjectApp.class, "cell.format"), index + 1, item.getLastName(), item.getFirstName());
					}
					
					setText(name);
					setGraphic(null);
				}
				
			};
		});
		
		HBox root = new HBox(10, new Label(Msg.get(this, "label.persons")), persons);
		HBox.setHgrow(persons, Priority.ALWAYS);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

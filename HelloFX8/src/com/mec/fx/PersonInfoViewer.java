package com.mec.fx;

import java.io.IOException;

import com.mec.fx.beans.Person;
import com.mec.fx.views.PersonEditDialogController;
import com.mec.fx.views.PersonOverviewController;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PersonInfoViewer extends Application {

	public PersonInfoViewer(){
		populateData();
	}
	
	private void populateData(){
		personData.add(new Person("Hans", "Muster"));
		personData.add(new Person("Ruth", "Mueller"));
		personData.add(new Person("Heinz", "Kurz"));
		personData.add(new Person("Cornelia", "Meier"));
		personData.add(new Person("Werner", "Meyer"));
		personData.add(new Person("Lydia", "Kunz"));
		personData.add(new Person("Anna", "Best"));
		personData.add(new Person("Stefan", "Meier"));
		personData.add(new Person("Martin", "Mueller"));
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(Msg.get(this, "title"));
		
		initRootLayout();
		
		showPersonOverview();
	}
	
	private void initRootLayout() throws Exception{
		//Laod root layout from fxml file;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/mec/fx/views/RootLayout.fxml"));
		rootLayout = (BorderPane) loader.load();
		
		//show the scene containing the root layout
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void showPersonOverview() throws Exception{
		//Load person overview
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/mec/fx/views/PersonOverview.fxml"));
//		PersonOverviewController c = new PersonOverviewController();
//		loader.setController(c);
		
		AnchorPane personOverview = (AnchorPane) loader.load();
		
		//Set person overview into the center of root layout;
		rootLayout.setCenter(personOverview);
		
		//Get the controller access to the main app
		PersonOverviewController controller = loader.getController();
		controller.setPersonInfoViewer(this);
	}
	
	
	public ObservableList<Person> getPersonData(){
		return personData;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public boolean showPersonEditDialog(Person person){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/mec/fx/views/PersonEditDialog.fxml"));
			
			AnchorPane page = (AnchorPane) loader.load();
			
			//Create the dialog stage
			Stage dialogStage = new Stage();
			dialogStage = new Stage();
			dialogStage.setTitle(Msg.get(this, "editPerson.title"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);
			
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}



	private ObservableList<Person> personData = FXCollections.observableArrayList();
	private Stage primaryStage;
	private BorderPane rootLayout;

}

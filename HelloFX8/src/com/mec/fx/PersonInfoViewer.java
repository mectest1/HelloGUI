package com.mec.fx;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import com.mec.fx.beans.BeanMarshal;
import com.mec.fx.beans.Person;
import com.mec.fx.beans.PersonListWrapper;
import com.mec.fx.views.PersonEditDialogController;
import com.mec.fx.views.PersonOverviewController;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
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
		
		//Set the application icon
//		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(Msg.get(this, "icon"))));
		primaryStage.getIcons().add(new Image(Msg.get(this, "icon.url")));
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

	public boolean showPersonEditDialog(Person person, EditType editType){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/mec/fx/views/PersonEditDialog.fxml"));
			
			AnchorPane page = (AnchorPane) loader.load();
			
			//Create the dialog stage
			Stage dialogStage = new Stage();
			dialogStage = new Stage();
			String title = "";
			if(EditType.EDIT == editType){
				title = Msg.get(this, "editPerson.title");
			}else{	//EditType.ADD
				title = Msg.get(this, "addPerson.title");
			}
			dialogStage.setTitle(title);
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

	
	
	public File getPersonFilePath(){
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		String filePath = prefs.get(Msg.get(this, "prefs.filePath"), null);
		if(null != filePath){
			return new File(filePath);
		}else{
			return null;
		}
	}
	
	public void setPersonFilePath(File file){
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		if(null != file){
			prefs.put(Msg.get(this, "prefs.filePath"), file.getPath());
			
			//Update the stage title
			primaryStage.setTitle(String.format(
					Msg.get(this, "title.withSuffix.formatter"), 
					Msg.get(this, "title"),
					file.getName()
					));
		}else{
			prefs.remove(Msg.get(this, "prefs.filePath"));
			
			//Update the stage title
			primaryStage.setTitle(Msg.get(this, "title"));
		}
	}
	
	public void loadPersonDataFromFile(File file){
		try {
			PersonListWrapper wrapper = BeanMarshal.loadFromXML(PersonListWrapper.class, file);
			personData.clear();
			personData.addAll(wrapper.getPersons());
			//Save the file path to the registry
			setPersonFilePath(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Msg.get(this, "loadPersonData.alert.title"));
			alert.setHeaderText(Msg.get(this, "loadPersonData.alert.header"));
			alert.setContentText(String.format(Msg.get(this, "loadPersonData.alert.content"), 
					file.getPath(),e.getMessage()
					));

			alert.showAndWait();
		}
	}
	
	public void savePersonDataToFile(File file){
		try{
			PersonListWrapper wrapper = new PersonListWrapper();
			wrapper.setPersons(personData);
			
			BeanMarshal.saveToXML(wrapper, file);
		}catch(Exception e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle(Msg.get(this, "savePersonData.alert.title"));
			alert.setHeaderText(Msg.get(this, "savePersonData.alert.header"));
			alert.setContentText(String.format(Msg.get(this, "savePersonData.alert.content"), 
					file.getPath(), e.getMessage()
					));
		}
	}

	public static enum EditType{
		 ADD
		,EDIT
		;
	}

//	private Preferences prefs = Preferences.userNodeForPackage(getClass());
	private ObservableList<Person> personData = FXCollections.observableArrayList();
	private Stage primaryStage;
	private BorderPane rootLayout;

}

package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PersonApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Person model = new Person();
		String dateFormat = Msg.get(this, "dateFormat");
		PersonView view = new PersonView(model, dateFormat);
		
		//Must setthe scene before creating the presenter that uses
		//the scene to listen for the focus change
		Scene scene = new Scene(view);
		
		PersonPresenter presenter = new PersonPresenter(model, view);
		view.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		//
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		
		primaryStage.show();
	}

}

package com.mec.fx.views;

import com.mec.fx.TetrisPaneApp;

import javafx.fxml.FXML;

public class TetrisPaneController {
	
	@FXML
	private void initialize(){
		
	}
	
	
	@FXML
	private void sayHello(){
		tetrisApp.sayHello();
	}
	
	public void setTetrisApp(TetrisPaneApp tetrisApp) {
		this.tetrisApp = tetrisApp;
	}



	private TetrisPaneApp tetrisApp;

}

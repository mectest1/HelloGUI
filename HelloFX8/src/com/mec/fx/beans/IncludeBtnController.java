package com.mec.fx.beans;

import java.io.PrintStream;

import javafx.fxml.FXML;

public class IncludeBtnController {

	
	@FXML
	private CloseBtnController includedCloseBtnController;
	
	
	@FXML
	private void initialize(){
		out.println("InicludeBtnController initialized");
		out.println(includedCloseBtnController);
	}
	
	private static final PrintStream out = System.out;
}

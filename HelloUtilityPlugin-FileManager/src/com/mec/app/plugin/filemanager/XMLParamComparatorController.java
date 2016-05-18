package com.mec.app.plugin.filemanager;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;

public class XMLParamComparatorController {
	
	
	
	@FXML
	private void initialize(){
		Path currentPath = Paths.get(".");
		out.println(currentPath);
	}

	static final PrintStream out = System.out;
	
}

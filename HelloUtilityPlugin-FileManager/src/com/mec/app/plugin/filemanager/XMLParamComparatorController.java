package com.mec.app.plugin.filemanager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.mec.app.plugin.filemanager.resources.MsgLogger;

import javafx.fxml.FXML;

public class XMLParamComparatorController {
	
	
	
	@FXML
	private void initialize(){
		Path currentPath = Paths.get(".");
		try {
			//Result: E:\Git\github.com\mectest1\HelloGUI\HelloUtility

			logger.log(currentPath.toRealPath().toString());
		} catch (IOException e) {
			logger.log(e);
		}
	}

//	static final PrintStream out = System.out;
	MsgLogger logger = MsgLogger.defaultLogger();
}

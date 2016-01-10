package com.mec.application;

import com.mec.resources.DialogFactory;
import com.mec.resources.MsgLogger;
import com.mec.resources.JarTool;
import com.mec.resources.Msg;
import com.mec.resources.ViewFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
public class RootPaneController implements MsgLogger{
	
	@FXML
	private TextArea logMsg;
	
	@FXML
	private void onExit(){
		Platform.exit();
	}
	
	@FXML
	private void onAbout(){
//		Alert about = new Alert(AlertType.INFORMATION);
//		about.setTitle(Msg.get(this, "about.title"));
//		about.setHeaderText(Msg.get(this, "about.header"));
//		about.setContentText(Msg.get(this, "about.content"));
		Alert about = DialogFactory.newAlert(AlertType.INFORMATION, 
				Msg.get(this, "about.title"), 
				Msg.get(this, "about.header"), 
				Msg.get(this, "about.content")
		);
		
		about.getButtonTypes().clear();
		
		ButtonType okButton = new ButtonType(Msg.get(this, "about.ok"), ButtonData.OK_DONE);
		about.getButtonTypes().add(okButton);
		about.showAndWait();
	}
	
	@FXML
	private void initialize(){
//		prepareNewLog();
		ViewFactory.setLogOutput(this);
		logMsg.getStyleClass().clear(); //clear default style classes for log msg
		logMsg.getStyleClass().add(Msg.get(this, "style.logMsg"));
//		logMsg.setStyle("-fx-background-color: white;");
	}
	
	@FXML
	private void onBase64Decoder(){
		
//		com.sun.org.apache.xml.internal.security.Init.init();
		
//		String viewUrl = Msg.get(this, "menu.view.base64decoder.url");
//		Pane viewPane = ViewFactory.loadView(viewUrl);
//		
////		Stage stage = new Stage();
////		stage.setScene(new Scene(viewPane));
//		Stage stage = ViewFactory.newStage(viewPane, Msg.get(this, "menu.view.base64decoder.title"));
//		
//		stage.show();
		ViewFactory.showNewStage(Msg.get(this, "menu.view.base64decoder.url"), Msg.get(this, "menu.view.base64decoder.title"));
	}
	
	
	@FXML
	private void onPatchRelease(){
		ViewFactory.showNewStage(Msg.get(this, "menu.view.patchRelease.url"), Msg.get(this, "menu.view.patchRelease.title"));
	}
	
	@FXML
	private void onClearLog(){
		logMsg.clear();
//		prepareNewLog();
	}
	
//	private void prepareNewLog(){
//		try {
//			if (null != sw) {
//				sw.close();
//			}
//			sw = new StringWriter();
//			PrintWriter pw = new PrintWriter(sw);
//			ViewFactory.setLogOutput(pw);
//		} catch (Exception e) {
//			appendLog(JarTool.exceptionToStr(e));
//		}
//	}
//	private void appendLog(String msg){
////		logMsg.setText(new StringBuilder(logMsg.getText()).append(msg).toString());
//		logMsg.appendText(msg);
////		logMsg.setScrollTop(Double.MAX_VALUE);	//scroll to bottom
//	}
	
	

	@Override
	public void log(String msg) {
//		appendLog(msg);
		logMsg.appendText(msg);
	}



//	private StringWriter sw;
	
}

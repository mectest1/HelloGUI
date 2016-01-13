package com.mec.resources;

import java.awt.TextField;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ViewFactory {

	public static void setLogOutput(MsgLogger logger){
//		ViewFactory.out = out;
		ViewFactory.logger = logger;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T loadView(String viewURL){
		T retval = null;
		try {
			retval = (T) FXMLLoader.load(ViewFactory.class.getResource(viewURL));
		} catch (Exception e) {
//			e.printStackTrace();
//			retval =  null;
//			e.printStackTrace(out);
			log(e);
		}
		return retval;
	}
	
	private static Stage newStage(Parent sceneRoot, String stageTitle){
		Scene scene = new Scene(sceneRoot);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.sizeToScene();
		stage.setTitle(stageTitle);
		return stage;
	}
	
	public static Optional<Stage> showNewStage(String viewUrl, String stageTitle){
		Pane viewPane = ViewFactory.loadView(viewUrl);
		if(null == viewPane){
			log(new IllegalArgumentException(String.format(Msg.get(ViewFactory.class, "exception.loadViewError"), viewUrl)));
			return Optional.empty();
		}
		Stage stage = ViewFactory.newStage(viewPane, stageTitle);
		stage.show();
		return Optional.of(stage);
	}
	
	public static Alert newAlert(AlertType alertType, String title, String header){
		Alert retval = new Alert(alertType);
		retval.setTitle(title);
		retval.setHeaderText(header);
		return retval;
	}
	
	public static TextFlow newTextFlow(String text, Optional<Integer> maxLength){
		if(maxLength.isPresent()){
//			int maxSize = Msg.get(ViewFactory.class, "textFlow.maxSize", Integer::parseInt, 1000);
			int ml = maxLength.get();
			if(ml < text.length()){
				text = new StringBuilder(text.substring(0, ml)).append(Msg.get(ViewFactory.class, "textFlow.ellipsis")).toString();
			}
		}
		TextFlow tf = new TextFlow(new Text(text));
		return tf;
	}
	
	private static void log(Exception e){
//		if(null != out){
//			e.printStackTrace(out);	//<- it's not working
//			out.flush();
//		}
		if(null != logger){
//			log(e);
			logger.log(e);
		}
	}
//	private static PrintWriter out;
	private static MsgLogger logger;
}

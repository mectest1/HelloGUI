package com.mec.resources;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
	
	public static void showNewStage(String viewUrl, String stageTitle){
		Pane viewPane = ViewFactory.loadView(viewUrl);
		if(null == viewPane){
			log(new IllegalArgumentException(String.format(Msg.get(ViewFactory.class, "exception.loadViewError"), viewUrl)));
			return;
		}
		Stage stage = ViewFactory.newStage(viewPane, stageTitle);
		stage.show();
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

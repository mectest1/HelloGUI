package com.mec.resources;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ViewFactory {

	public static void setLogOutput(MsgLogger logger){
//		ViewFactory.out = out;
		ViewFactory.logger = logger;
	}
	
	public static <T> T loadView(String viewURL){
		LoadViewResult<T, ?> loadResult = loadViewDeluxe(viewURL);
		return (T) loadResult.getViewRoot();
	}
	
	@SuppressWarnings("unchecked")
	public static <V, C> LoadViewResult<V, C> loadViewDeluxe(String viewURL){
		V retval = null;
		C controller = null;
		try {
			URL view = ViewFactory.class.getResource(viewURL);
			Objects.requireNonNull(view);
//			retval = (T) FXMLLoader.load(ViewFactory.class.getResource(viewURL));
//			ResourceBundle viewBundle = ResourceBundle.getBundle(Msg.get(ViewFactory.class, "viewMessages"));
//			Objects.requireNonNull(viewBundle);
			String bundleBase = JarTool.trimLeadingSlash(JarTool.trimTrailing(viewURL, FXML_SUFFIX));
			ResourceBundle viewBundle;
			try {
				viewBundle = ResourceBundle.getBundle(bundleBase);
				FXMLLoader loader = new FXMLLoader(view, viewBundle);
				retval = loader.load();
				controller = loader.getController();
			} catch (MissingResourceException e) {
				retval = (V) FXMLLoader.load(view);
			}
		} catch (Exception e) {
//			e.printStackTrace();
//			retval =  null;
//			e.printStackTrace(out);
			log(e);
		}
//		return retval;
		return new LoadViewResult<>(retval, controller);
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
	
	public static Optional<String> inputText(String defaultValue, String title, String header){
		TextInputDialog textInput = new TextInputDialog(defaultValue);
		
		textInput.setTitle(title);
		textInput.setHeaderText(header);
		
		Optional<String> retval = textInput.showAndWait();
		return retval;
	}
	
	public static Optional<File> inputOpenFile(Window ownerWindow){
		FileChooser fs = new FileChooser();
		File file = fs.showOpenDialog(ownerWindow);
		return Optional.ofNullable(file);
	}
	
	public static Optional<File> inputSaveFile(Window ownerWindow){
		FileChooser fs = new FileChooser();
		File file = fs.showSaveDialog(ownerWindow);
		return Optional.ofNullable(file);
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
	
	public static void onClickedViewMenuItem(ActionEvent event){
		MenuItem menuItem = (MenuItem) event.getTarget();
		@SuppressWarnings("unchecked")
		HashMap<String, String> userData = (HashMap<String, String>) menuItem.getUserData();
		String view = userData.get(FXML_ATTR_KEY_VIEW);
		String title = userData.get(FXML_ATTR_KEY_TITLE);
		ViewFactory.showNewStage(view, title);
	}
	
	
	
	public static class LoadViewResult<ViewRoot, Controller>{
		ViewRoot viewRoot;
		Controller controller;
		public LoadViewResult(ViewRoot viewRoot, Controller controller) {
			super();
			this.viewRoot = viewRoot;
			this.controller = controller;
		}
		public ViewRoot getViewRoot() {
			return viewRoot;
		}
		public void setViewRoot(ViewRoot viewRoot) {
			this.viewRoot = viewRoot;
		}
		public Controller getController() {
			return controller;
		}
		public void setController(Controller controller) {
			this.controller = controller;
		}
	}
//	private static PrintWriter out;
	private static MsgLogger logger;
	private static final String FXML_SUFFIX = Msg.get(ViewFactory.class, "fxml.suffix");
	public static final String FXML_ATTR_KEY_VIEW = Msg.get(ViewFactory.class, "fxml.attrKey.view");
	public static final String FXML_ATTR_KEY_TITLE = Msg.get(ViewFactory.class, "fxml.attrKey.title");
}

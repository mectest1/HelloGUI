package com.mec.application.views;

//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mec.application.beans.PatchReleaseConfigBean;
import com.mec.application.beans.PatchReleaseConfigBean.PatchReleaseConfigBeanFavoriteCell;
import com.mec.application.beans.PatchReleaseConfigBean.PatchReleaseConfigBeanHistoryCell;
import com.mec.application.beans.PatchReleaseConfigBean.PatchReleaseConfigBeans;
import com.mec.resources.Config;
import com.mec.resources.FileParser;
import com.mec.resources.JarTool;
import com.mec.resources.Msg;
import com.mec.resources.MsgLogger;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

public class PatchReleaseController implements MsgLogger{

	@FXML
	private TextField configName;
	
	@FXML
	private TextField workSpaceDirectory;
	
	@FXML
	private TextField patchReleaseDirectory;
	
	@FXML
	private TextArea modifyList;
	
	
	
	@FXML
	private TextArea logMsg;
	
	
	@FXML
	private Button startPatch;
	@FXML
	private Button clearLogBtn;
	
	@FXML
	private ListView<PatchReleaseConfigBean> favList;
	@FXML
	private ListView<PatchReleaseConfigBean> historyList;
	
	//------------
	
	@FXML
	private void initialize(){
//		logMsg.textProperty().bind(logs);
		fieldEmpty.bind(
				Bindings.or(workSpaceDirectory.textProperty().isEmpty(), patchReleaseDirectory.textProperty().isEmpty())
				.or(modifyList.textProperty().isEmpty())
				);
		startPatch.disableProperty().bind(fieldEmpty);
		clearLogBtn.disableProperty().bind(logMsg.textProperty().isEmpty());
		
//		workSpaceDirectory.setPromptText(Msg.get(this, "prompt.workspaceDir"));
//		modifyList.setPromptText(Msg.get(this, "prompt.modifyList"));
//		patchReleaseDirectory.setPromptText(Msg.get(this, "prompt.patchReleaseDir"));
		
		//
		Config.config(this).setLogger(this);
//		Callback<ListView<PatchReleaseConfigBean>, ListCell<PatchReleaseConfigBean>> favCellFactory= l -> 
//			new ListCell<PatchReleaseConfigBean>(){
//				@Override
//				protected void updateItem(PatchReleaseConfigBean item, boolean empty) {
//					super.updateItem(item, empty);
//					if(empty){
//						setText("");
//					}else{
//						setText(item.getName());
//					}
//				}
//			};
//		favList.setCellFactory(favCellFactory);
		favList.setCellFactory(l -> new PatchReleaseConfigBeanFavoriteCell());
		PatchReleaseConfigBeans favConfigList = Config.config(this).load(Msg.get(this, "config.favorites"), PatchReleaseConfigBeans.class)
				.orElseGet(PatchReleaseConfigBeans::new);
		favList.getItems().addAll(favConfigList.getConfigs());
		
		historyList.setCellFactory(l -> new PatchReleaseConfigBeanHistoryCell());
		PatchReleaseConfigBeans historyConfigList = Config.config(this).load(Msg.get(this, "config.history"), PatchReleaseConfigBeans.class)
				.orElseGet(PatchReleaseConfigBeans::new);
		historyList.getItems().addAll(historyConfigList.getConfigs());
		favList.getSelectionModel().selectedItemProperty().addListener(onListItemSelected(favList));
//		Stream.of(favList, historyList).forEach(list -> list.getSelectionModel().selectedIndexProperty().addListener(onListItemSelected(list)));;
		historyList.getSelectionModel().selectedItemProperty().addListener(onListItemSelected(historyList));
		
//		favList.setCellFactory(new PropertyValueFactory<>("name"));
//		logMsg.getScene().getWindow().setOnCloseRequest(this::onWindowClosed);	//<-the windows is still uninitialized;
		onClearLog();	//clear log on application start
		Platform.runLater(() -> logMsg.getScene().getWindow().setOnCloseRequest(this::onWindowClosed));
	}
	
	
	@FXML
	private void onStartPatch(){
		try{
			
			String workspaceDirStr = JarTool.normalizePath(workSpaceDirectory.getText());
			Path workspaceDir = Paths.get(workspaceDirStr);
			JarTool.validateDirectory(workspaceDir, String.format(Msg.getExpMsg(this, "invalid.workspace"), workspaceDirStr));
			
			String patchReleaseDirStr = JarTool.normalizePath(patchReleaseDirectory.getText());
			Path patchReleaseDir = Paths.get(patchReleaseDirStr);
			if(!(Files.exists(patchReleaseDir))){
				log(String.format(Msg.get(this, "info.patchReleaseDir.create"), patchReleaseDir));
			}
			
			String modifyListContent = modifyList.getText();
			List<String> modifyList = FileParser.normalizeModifyList(modifyListContent);
			Map<String, Set<String>> modifyListMap = FileParser.parseModifyList(modifyList);
			
			for(String projectName : modifyListMap.keySet()){
				Set<String> sourceFileList = modifyListMap.get(projectName);
				jarTool.writeFilesToJar(workspaceDir, projectName, sourceFileList, patchReleaseDir);
				log(FileParser.NEWLINE);
			}
			
			relocateJars(patchReleaseDir);
			writeReadMe(patchReleaseDir, modifyList);
			
			//
			saveHistoryList();
		}catch(Exception e){
			log(e);
		}
	}
	
	
	private void relocateJars(Path patchReleaseDir) throws IOException{
		Path eeLibDir = null;
		for(Path jarFile : Files.list(patchReleaseDir).collect(Collectors.toList())){
			String jarFileName = jarFile.getFileName().toString();
			if(JarTool.WEB_CONTENT_JAR.matcher(jarFileName).matches()){
				//
			}else if(JarTool.EE_LIB_JAR.matcher(jarFileName).matches()){
				if(null == eeLibDir){
					eeLibDir = patchReleaseDir.resolve(Msg.get(this, "path.EE_LIB"));
					if(Files.exists(eeLibDir)){
						Files.move(eeLibDir, patchReleaseDir.resolve(String.format(Msg.get(this, "path.EE_LIB.bak"), System.currentTimeMillis())));
						eeLibDir =patchReleaseDir.resolve(Msg.get(this, "path.EE_LIB"));
					}
					if(Files.notExists(eeLibDir)){
						Files.createDirectory(eeLibDir);
						JarTool.validateDirectory(eeLibDir, String.format(Msg.get(this, "path.EE_LIB.error"), eeLibDir.toAbsolutePath()));
					}
				}
				log(String.format(Msg.get(this, "info.moveJar"), jarFileName, eeLibDir.toAbsolutePath()));
				Files.move(jarFile,eeLibDir.resolve(jarFileName));
			}else{
				log(String.format(Msg.get(this, "info.dontMove"), jarFileName, patchReleaseDir));
			}
		}
	}
	
	private void writeReadMe(Path patchReleaseDir, List<String> contentLines) throws IOException{
		Path readMe = JarTool.createNewFile(patchReleaseDir, 
				Msg.get(this, "path.README"), 
				String.format(Msg.get(this, "path.README.bak"), Msg.get(this, "path.README"), System.currentTimeMillis()));
		try(PrintWriter writer = new PrintWriter((Files.newBufferedWriter(readMe)))){
			for(String line : contentLines){
				if(null == line){
					continue;
				}
				writer.println(line);
			}
		}
		
	}
	
	@FXML
	private void onClearLog(){
		logMsg.clear();
	}
	
	
	
	@FXML
	private void onFavoriteSaveItem(ActionEvent evt){
		PatchReleaseConfigBean config = favList.getSelectionModel().getSelectedItem();
		if(null == config){
			return;
		}
		config.setName(configName.getText());
		config.setWorkspaceDirectory(Paths.get(workSpaceDirectory.getText()));
		config.setPatchReleaseDirectory(Paths.get(patchReleaseDirectory.getText()));
		favList.refresh();
		saveFavoriteListToFile();
	}
	@FXML
	private void onFavoriteDeleteItem(ActionEvent evt){
		PatchReleaseConfigBean config = favList.getSelectionModel().getSelectedItem();
		if(null == config){
			return;
		}
		favList.getItems().remove(config);
	}
	@FXML
	private void onFavoriteNewItem(ActionEvent evt){
//		String name = ViewFactory.inputText(DateUtil.getPathNameForNow(), "", "");
		String name = configName.getText();
		String modifyListStr = modifyList.getText();
		Path workspaceDirectory = Paths.get(workSpaceDirectory.getText());
		Path pathReleaseDirectory = Paths.get(patchReleaseDirectory.getText());
		PatchReleaseConfigBean config = new PatchReleaseConfigBean(name, workspaceDirectory, pathReleaseDirectory);
		if(!favList.getItems().contains(config)){
			favList.getItems().add(0, config);
		}
		favList.getSelectionModel().select(config);
		saveFavoriteListToFile();
//		if(null != modifyListStr){
		modifyList.setText(modifyListStr);
//		}
	}
	
	private void onWindowClosed(WindowEvent evt){
		Config.config(this).setLogger(MsgLogger.defaultLogger());
		saveFavoriteListToFile();
		saveHistoryListToFile();
	}
	
	private void saveFavoriteListToFile(){
		PatchReleaseConfigBeans configs = new PatchReleaseConfigBeans(favList.getItems());
		Config.config(this).save(Msg.get(this, "config.favorites"), configs);
	}
	private void saveHistoryList(){
		String name = configName.getText();
		String modifyListStr = modifyList.getText();
		Path workspaceDirectory = Paths.get(workSpaceDirectory.getText());
		Path pathReleaseDirectory = Paths.get(patchReleaseDirectory.getText());
		PatchReleaseConfigBean config = new PatchReleaseConfigBean(name, workspaceDirectory, pathReleaseDirectory);
		config.setModifyList(modifyListStr);
		historyList.getItems().add(0, config);
	}
	
	private void saveHistoryListToFile(){
		List<PatchReleaseConfigBean> historyRecords = historyList.getItems();
		if(HISTORY_MAX < historyRecords.size()){
			historyRecords = historyRecords.subList(0, HISTORY_MAX);
		}
		PatchReleaseConfigBeans configs = new PatchReleaseConfigBeans(historyRecords);
		Config.config(this).save(Msg.get(this, "config.history"), configs);
	}

//	private ChangeListener<? super Number> onListItemSelected(ListView<PatchReleaseConfigBean> listView){
//		return (ob, oldVal, newVal) -> {
////			ListView<PatchReleaseConfigBean> listView = (ListView<PatchReleaseConfigBean>) evt.getTarget();
//			PatchReleaseConfigBean config = favList.getSelectionModel().getSelectedItem();
//			if(null == config){
//				return;
//			}
//			configName.setText(config.getName());
//			workSpaceDirectory.setText(config.getWorkspaceDirectory().toString());
//			patchReleaseDirectory.setText(config.getPatchReleaseDirectory().toString());
//			if(historyList == listView){
//				modifyList.setText(config.getModifyList());
//			}
//		};
//	}
	private ChangeListener<? super PatchReleaseConfigBean> onListItemSelected(ListView<PatchReleaseConfigBean> listView){
		return (ob, oldVal, newVal) -> {
//			ListView<PatchReleaseConfigBean> listView = (ListView<PatchReleaseConfigBean>) evt.getTarget();
			PatchReleaseConfigBean config = favList.getSelectionModel().getSelectedItem();
			if(null == config){
				return;
			}
			configName.setText(config.getName());
			workSpaceDirectory.setText(config.getWorkspaceDirectory().toString());
			patchReleaseDirectory.setText(config.getPatchReleaseDirectory().toString());
			if(historyList == listView){
				modifyList.setText(config.getModifyList());
			}
		};
	}
	
	@Override
	public void log(String msg) {
		logMsg.appendText(msg);
	}
	
//	private PatchReleaseConfigBeans favConfigList;
	private JarTool jarTool = JarTool.newInstance(this);
	private BooleanProperty fieldEmpty = new SimpleBooleanProperty();
	private static final int HISTORY_MAX = Msg.get(PatchReleaseController.class, "history.maxItem", Integer::parseInt, 20);
}

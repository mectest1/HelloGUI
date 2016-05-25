package com.mec.app.plugin.filemanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mec.app.plugin.filemanager.resources.Msg;
import com.mec.app.plugin.filemanager.resources.MsgLogger;
import com.mec.app.plugin.filemanager.resources.XMLStructComparator;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class XMLParamComparatorController implements MsgLogger{
	
	
	
	@FXML
	private TextArea logMsg;
	@FXML
	private TextField paramRootDirField;
	@FXML
//	private ListView<Path> characteristicPathsView;
	private ListView<RelativePathItem> characteristicPathsView;
	@FXML
	private TableView<Map.Entry<Path, List<Set<Path>>>> xmlParamFilesGroupedBySetView;
	@FXML
	private TableColumn<Map.Entry<Path, List<Set<Path>>>, Path> xmlParamFileCol;
	@FXML
	private TableColumn<Map.Entry<Path, List<Set<Path>>>, List<Set<Path>>> groupedNumDirSetCol;
	@FXML
//	private TreeView<Map.Entry<Path, List<Set<Path>>>> numDirsGroupView;
	private TreeView<List<Set<Path>>> numDirsGroupView;
	
	@FXML
	private MenuItem menuItemSaveDiffReport;
	@FXML
	private Pane mainPane;
	
//	@FXML
//	private HBox topBannerGroup;
//	@FXML
//	private HBox operationInProgressGroup;
//	@FXML
//	private HBox rootDirGroup;
//	@FXML
//	private BorderPane mainPane; 
	
	
	private FileChooser fileChooser = new FileChooser();
	private DirectoryChooser dirChooser = new DirectoryChooser();
	
	
	
	ParamDirectoryExtractor pe;
//	ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(this);
//	OperationInProgress oip;
//	XMLStructComparator diff = XMLStructComparator.inst();
	/**
	 * A map from each characteristic path to its grouped NumDirs;
	 * <ul>
	 * 	<li>cpsWithNumDirsParams: Characteristic Path => Grouped NumDirs by XML Struct</li>
	 * 	<li>Grouped NumDirs by XML Struct: Param File Name => List of Grouped NumDirs</li>
	 * 	<li>List of Grouped NumDirs: Grouped NumDirs *</li>
	 * 	<li>Grouped NumDirs: Set of NuMDirs</li>
	 * </ul>
	 * <h4>For example:</h4>
	 * <pre>
	 * {Func_Group_1 => {Param_File_1.xml => [{num1, num2}, {num3, num4}]
	 * 			,Param_File_2.xml => [{num1, num3}, {num2, num4}]}
	 * ,Func_Group_2 => {Param_File_3.xml => [{num1, num2, num5}, {num3, num4}]
	 * 			,Param_File_4.xml => [{num1, num3}, {num2, num4, num5}]}
	 * }
	 * </pre>
	 */
	Map<Path, Map<Path, List<Set<Path>>>> cpsWithNumDirParams;
	
	/**
	 * Functions same as {@link #cpsWithNumDirParams}. This one is used for the GUI appilcation only.
	 */
	Map<Path, Map<Path, List<Set<Path>>>> cpsWithNumDirParamsTmp;
	Path paramRootDir;
	
	@FXML
	private void initialize(){
//		Path currentPath = Paths.get(".");
//		try {
			//Result: E:\Git\github.com\mectest1\HelloGUI\HelloUtility

//			logger.log(currentPath.toRealPath().toString());
//			log(currentPath.toRealPath().toString());
//		} catch (IOException e) {
//			logger.log(e);
//		}
//		pe = ParamDirectoryExtractor.newInst(this);
//		mainPane.setDisable(true);
		pe = ParamDirectoryExtractor.newInst(MsgLogger.defaultLogger());
		
		fileChooser.getExtensionFilters().add(new ExtensionFilter(
				//Note: *.<extension>
				Msg.get(this, "report.file.desc"), Msg.getList(this, "report.file.ext")));
		fileChooser.setTitle(Msg.get(this, "report.file.chooser.title"));
		fileChooser.setInitialDirectory(Paths.get(System.getProperty(Msg.get(this, "report.file.initial.dir.prop"))).toFile());
		fileChooser.setInitialFileName(Msg.get(this, "report.file.initial.fileName"));
		
		
//		String initialDir = paramRootDirField.getText().trim();
//		if(initialDir.isEmpty()){
		String initialDir = Msg.get(this, "dir.param.root.initial");
//		}
//		dirChooser.setInitialDirectory(Paths.get(Msg.get(this, initialDir)).toFile());
//		dirChooser.setInitialDirectory(new File(Paths.get(Msg.get(this, initialDir)).toString()));
		dirChooser.setTitle(Msg.get(this, "dir.chooser.title"));
		
//		Platform.runLater(()->{
		paramRootDirField.textProperty().addListener(this::onParamRootDirFieldChanged);
//		});
		
		characteristicPathsView.setCellFactory(lv -> new RelativePathListCell(paramRootDir));
		characteristicPathsView.getSelectionModel().selectedItemProperty().addListener(this::onCharacteristicPathViewSelectedItemChanged);
		
		xmlParamFilesGroupedBySetView.getSelectionModel().selectedItemProperty().addListener(this::onXmlParamFilesGroupedBySetViewSelectedItemChanged);
		xmlParamFileCol.setCellValueFactory(cdf -> new SimpleObjectProperty<Path>(cdf.getValue().getKey()));
		
		groupedNumDirSetCol.setCellValueFactory(cdf -> new SimpleObjectProperty<List<Set<Path>>>(cdf.getValue().getValue()));
		groupedNumDirSetCol.setCellFactory(cdf -> new ListOfSetsColumnCell());
		
		numDirsGroupView.setCellFactory(tv -> new NumDirGroupsTreeCell());
		numDirsGroupView.getSelectionModel().selectedItemProperty().addListener(this::onNumDirsGroupViewSelectedItemChanged);
		
		
//		numDirsGroupView.setShowRoot(true);
//		oip = new OperationInProgress();
//		topBannerGroup.getChildren().remove(operationInProgressGroup);
//		showRootDirGroup();
//		showOperationInProgressGroup();
	}
	
	
	
	@FXML
	private void onChooseParamRootDir(){
//		File
		File paramRootDir = dirChooser.showDialog(getOwnerWindow());
		if(null != paramRootDir){
			paramRootDirField.setText(paramRootDir.toPath().toAbsolutePath().toString());
		}
	}
	
	private void onParamRootDirFieldChanged(Observable ob, String oldVal, String newVal){
		String rootDirStr = paramRootDirField.getText().replaceAll(BACK_SLASH, SLASH).trim();
		Path newPath = Paths.get(rootDirStr);
		if(!(Files.isDirectory(newPath) && Files.exists(newPath))){
			log(Msg.get(this, "error.paramRotoDir.invalid"), newVal);
			return;
		}
		
		cpsWithNumDirParamsTmp = new HashMap<>();
		this.paramRootDir = newPath;
		populateCharacteristicPathsList();
	}
	
	private void populateCharacteristicPathsList(){
		List<Path> charPaths = pe.getCharacteristicPath(paramRootDir);
		
		characteristicPathsView.getItems().clear();
//		characteristicPathsView.getItems().addAll(charPaths);
		charPaths.forEach(p -> characteristicPathsView.getItems().add(new RelativePathItem(p)));
		
//		characteristicPathsView.get
	}
	
//	@FXML
//	private void onCharacteristicPathViewSelectedItemChanged(Observable ob, Path oldVal, Path newVal){
	private void onCharacteristicPathViewSelectedItemChanged(Observable ob, RelativePathItem oldVal, RelativePathItem newVal){
//		Path characteristicPath = newVal;
		Path characteristicPath = newVal.getCharacteristicPath();
		Map<Path, List<Set<Path>>> groupedNumDirsByXmlStruct = 
				cpsWithNumDirParamsTmp.computeIfAbsent(characteristicPath, pe::groupNumDirContents);
//				cpsWithNumDirParamsTmp.computeIfAbsent(characteristicPath, p -> {
//					this.disableMainPane();
//					Map<Path, List<Set<Path>>> retval = pe.groupNumDirContents(p);
//					this.enableMainPane();
//					return retval;
//				});
//		Map<Path, List<Set<Path>>> groupedNumDirsByXmlStruct = 
//				cpsWithNumDirParamsTmp.computeIfAbsent(characteristicPath, p -> {
////					D
////					oip.show();
////					showOperationInProgressGroup();
//					
////					mainPane.setDisable(true);
////					rootDirGroup.setDisable(true);
////					try {
////						Thread.currentThread().sleep(1000);
////					} catch (Exception e) {
////						log(e);
////					}
//					Map<Path, List<Set<Path>>>  retval = pe.groupNumDirContents(p);
////					mainPane.setDisable(false);
//					
////					rootDirGroup.setDisable(false);
////					oip.hide();
////					showRootDirGroup();
//					return retval;
//				});
		
		
		Map<Path, List<Set<Path>>> filteredGroups = groupedNumDirsByXmlStruct.entrySet()
				.stream().filter(entry -> pe.isStartsWithBankGroundAndCountryCode(entry.getKey()))
				//ref: http://www.mkyong.com/java8/java-8-filter-a-map-examples/
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		
		xmlParamFilesGroupedBySetView.getItems().clear();
		xmlParamFilesGroupedBySetView.getItems().addAll(filteredGroups.entrySet());
		
		clearLog();
		
		//To update the ListCell style
		newVal.setPopulated(true);
		//NoSuchMethod in 1.8.0_20, got implemented in 1.8.0_65
		characteristicPathsView.refresh();
		
//		Map<Path, List<Set<Path>>> groupedNumDirsByXmlStruct = cpsWithNumDirParamsTmp.get(characteristicPath);
//		if(null != groupedNumDirsByXmlStruct){
//			Map<Path, List<Set<Path>>> filteredGroups = groupedNumDirsByXmlStruct.entrySet()
//			.stream().filter(entry -> pe.isStartsWithBankGroundAndCountryCode(entry.getKey()))
//			//ref: http://www.mkyong.com/java8/java-8-filter-a-map-examples/
//			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//	
//			xmlParamFilesGroupedBySetView.getItems().clear();
//			xmlParamFilesGroupedBySetView.getItems().addAll(filteredGroups.entrySet());
//			
//			clearLog();
//		}else{
//			GroupNumDirsTask task = new GroupNumDirsTask(characteristicPath);
//			task.onFinished(g -> {
//				this.cpsWithNumDirParamsTmp.put(characteristicPath, g);
//				Map<Path, List<Set<Path>>> filteredGroups = g.entrySet()
//					.stream().filter(entry -> pe.isStartsWithBankGroundAndCountryCode(entry.getKey()))
//					//ref: http://www.mkyong.com/java8/java-8-filter-a-map-examples/
//					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//		
//				xmlParamFilesGroupedBySetView.getItems().clear();
//				xmlParamFilesGroupedBySetView.getItems().addAll(filteredGroups.entrySet());
//				
//				clearLog();
//				
//			});
//			task.start();
//		}
		
	}
	
	
	private void onXmlParamFilesGroupedBySetViewSelectedItemChanged(Observable ob, 
			Map.Entry<Path, List<Set<Path>>> oldVal, Map.Entry<Path, List<Set<Path>>> newVal){
		if(null == newVal){
			return;
		}
		List<Set<Path>> groupedNumDirs = newVal.getValue();
		
//		NumDirGroupsTreeItem numDirGroupsRootNode = new NumDirGroupsTreeItem(this.paramRootDir, newVal);
		NumDirGroupsTreeItem numDirGroupsRootNode = new NumDirGroupsTreeItem(this.paramRootDir, newVal.getKey(), newVal.getValue());
		numDirGroupsRootNode.populateChildNodes();
		numDirGroupsRootNode.setExpanded(true);
		
		numDirsGroupView.setRoot(numDirGroupsRootNode);
		//NoSuchMethodException in 1.8.20, got implemented in 1.8.65.
		numDirsGroupView.refresh();
		
		Path paramFile = newVal.getKey();
		String groupedNumDirsStr = ParamDirectoryExtractor.listOfSetsToStr(groupedNumDirs);
		clearLog();
		log(Msg.get(this, "info.paramFileToNumDirsGroup"), paramFile.toString(), groupedNumDirsStr);
	}
	
	private void onNumDirsGroupViewSelectedItemChanged(Observable ob, 
			TreeItem<List<Set<Path>>> oldVal, TreeItem<List<Set<Path>>> newVal){
		if(null == newVal){
			return;
		}
		
		NumDirGroupsTreeItem binaryGroups = (NumDirGroupsTreeItem) newVal;
		
//		StringPrefixLogger sl = new StringPrefixLogger(Msg.get(this, "info.xmlStructDiffPattern"));
		StringLogger sl = new StringLogger();
		Path paramFile = binaryGroups.getXmlParmFileRelativePath();
		Path paramRootDir = binaryGroups.getRootDir();
		List<Set<Path>> groupedNumDirs = binaryGroups.getGroupedDirs();
		String diffReport = pe.getDiffReport(paramRootDir, groupedNumDirs, paramFile, sl);
		
		clearLog();
		log(diffReport);
	}
	
	
	@FXML
	private void onSaveDiffReport(){
		File outputFile = fileChooser.showSaveDialog(getOwnerWindow());
		
		if(null == outputFile){
			return;
		}
//		if(null == cpsWithNumDirParams){
//			parseParamXmlStruct(this.paramRootDir);
//		}
//		Path outputPath = outputFile.toPath();
//		clearLog();
//		ln(Msg.get(this, "info.save.diffReport.start"), outputPath.toString());
//		
//		outputDifferenceLog(outputPath);
//		
//		ln(Msg.get(this, "info.save.diffReport.end"), outputPath.toString());
//		CompletableFuture<Void> saveDiffReportTask = 
		CompletableFuture.runAsync(() -> {
//			mainPane.setDisable(true);
//			menuItemSaveDiffReport.setDisable(true);
			Platform.runLater(this::disableMainPane);
			
			if(null == cpsWithNumDirParams){
				parseParamXmlStruct(this.paramRootDir);
			}
			Path outputPath = outputFile.toPath();
//			clearLog();
//			ln(Msg.get(this, "info.save.diffReport.start"), outputPath.toString());
//			
//			outputDifferenceLog(outputPath);
//			
//			ln(Msg.get(this, "info.save.diffReport.end"), outputPath.toString());
			
			
			//May result into exception in FXApplication Thread
//			menuItemSaveDiffReport.setDisable(false);
//			mainPane.setDisable(false);
			Platform.runLater(() -> {
				clearLog();
				ln(Msg.get(this, "info.save.diffReport.start"), outputPath.toString());
				
				outputDifferenceLog(outputPath);
				
				ln(Msg.get(this, "info.save.diffReport.end"), outputPath.toString());
				this.enableMainPane();
			});
		}).whenComplete((v, t) -> {
			MsgLogger.defaultLogger().log((Exception)t);
//			menuItemSaveDiffReport.setDisable(false);
//			mainPane.setDisable(false);
			Platform.runLater(this::enableMainPane);
		});
//		try {
//			saveDiffReportTask.join();
//		} catch (CancellationException | CompletionException e) {
//			log(e);
//			mainPane.setDisable(false);
//		}
	}
	
	
	
	
	
	
	private Stage getOwnerWindow(){
		return (Stage) logMsg.getScene().getWindow();
	}
//	private void showRootDirGroup(){
//		ObservableList<Node> groups = topBannerGroup.getChildren();
//		if(groups.contains(operationInProgressGroup)){
//			groups.remove(operationInProgressGroup);
//		}
//		if(!groups.contains(rootDirGroup)){
//			groups.add(rootDirGroup);
//		}
////		topBannerGroup.getChildren().remove(operationInProgressGroup);
////		topBannerGroup.getChildren().add(rootDirGroup);
//	}
//	private void showOperationInProgressGroup(){
//		ObservableList<Node> groups = topBannerGroup.getChildren();
//		if(!groups.contains(operationInProgressGroup)){
//			groups.add(operationInProgressGroup);
//		}
//		if(groups.contains(rootDirGroup)){
//			groups.remove(rootDirGroup);
//		}
////		topBannerGroup.getChildren().remove(rootDirGroup);
//		
////		topBannerGroup.getChildren().add(operationInProgressGroup);
////		
//	}
	
	
	
	
	
	
	
	
	/**
	 * Parse XML Param Files stored in descendant NumDirs of this <code>paraRootDir</code>, 
	 * and store the parsing result in {@link #cpsWithNumDirParams}.
	 * 
	 * <p>
	 * <strong>Note:</strong> this method should be invoked before 
	 * </p>
	 * @param paramRootDir
	 * @return 
	 */
	void parseParamXmlStruct(Path paramRootDir) {
//	Map<Path, Map<Path, List<Set<Path>>>>  parseParamXmlStruct(Path paramRootDir) {
		Stream.of(paramRootDir).forEach(Objects::requireNonNull);
		
		this.paramRootDir = paramRootDir;
//		cpsWithNumDirParams = new HashMap<>();
		Map<Path, Map<Path, List<Set<Path>>>>  retval = new HashMap<>();
//		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(this);
//			BufferedWriter outputWriter = Files.newBufferedWriter(outputLogFile, StandardOpenOption.CREATE,
//					StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
			List<Path> cps = pe.getCharacteristicPath(paramRootDir);
			for(Path cp : cps){
//				logAndWrite(outputWriter, Msg.get(this, "info.funcGroup"), paraRootDir.relativize(cp).toString());
				Map<Path, List<Set<Path>>> groupedNumDirsByXmlStruct = pe.groupNumDirContents(cp);
//				cpsWithNumDirParams.put(cp, groupedNumDirsByXmlStruct);
				retval.put(cp, groupedNumDirsByXmlStruct);
				//Reserver only paramFiles from HSBC/US when multiple bankGroups and countryCodes are involved 
//				Set<Path> filteredParamFiles = groupedNumDirsByXmlStruct.keySet().stream().filter(paramFile -> {
//					if(PATH_NAME_COUNT > paramFile.getNameCount()){
//						return true;
//					}else{
//						String bankGroup = paramFile.getName(0).getFileName().toString();
//						String countryCode = paramFile.getName(1).getFileName().toString();
////						if(DEFAULT_BANK_GROUP.equals(bankGroup)
////							&& DEFAULT_COUNTRY_CODE.equals(countryCode)
////							){
////							return true;
////						}else{
////							return false;
////						}
//						if(DEFAULT_BANK_GROUP.equals(bankGroup)
//								&& !DEFAULT_COUNTRY_CODE.equals(countryCode)
//								){
//							return false;
//						}else{
//							return true;
//						}
//					}
//				}).collect(Collectors.toSet());
				
			}
			
		cpsWithNumDirParams = retval;
		cpsWithNumDirParamsTmp = cpsWithNumDirParams;
//		return retval;
	}
	
	/**
	 * Record the XMLParamFiles difference into log file. 
	 * <p>
	 * <strong>Note: </strong> invoke {@link #parseParamXmlStruct(Path)} before invoking this method
	 * </p>
	 * @param outputLogFile
	 */
	void outputDifferenceLog(Path outputLogFile){
		Objects.requireNonNull(outputLogFile);
		
		try(BufferedWriter outputWriter = Files.newBufferedWriter(outputLogFile, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)){
			
//			Set<Path> cps = cpsWithNumDirParams.keySet();
			for(Path cp : cpsWithNumDirParams.keySet()){
				logAndWrite(outputWriter, Msg.get(this, "info.funcGroup"), this.paramRootDir.relativize(cp).toString());
				Map<Path, List<Set<Path>>> groupedNumDirsByXmlStruct = cpsWithNumDirParams.get(cp);
				
				//Get only paramFiles that start with "HSBC/US", or not start with "HSBC" at all
				List<Path> filteredParamFiles = groupedNumDirsByXmlStruct.keySet().stream()
						.filter(pe::isStartsWithBankGroundAndCountryCode).collect(Collectors.toList());
				
				for(Path paramFile : filteredParamFiles){
					List<Set<Path>> groupedNumDirs = groupedNumDirsByXmlStruct.get(paramFile);
					groupedNumDirs = ParamDirectoryExtractor.getFirstElementOfEachSet(groupedNumDirs);
					
					String groupedNumDirsStr = ParamDirectoryExtractor.listOfSetsToStr(groupedNumDirs);
					logAndWrite(outputWriter, Msg.get(this, "info.paramFileToNumDirsGroup")
							, paramFile.toString(), groupedNumDirsStr);
					
					
					if(1 < groupedNumDirs.size()){
						StringPrefixLogger sl = new StringPrefixLogger(Msg.get(this, "info.xmlStructDiffPattern"));
						String diffReport = pe.getDiffReport(paramRootDir, groupedNumDirs, paramFile, sl);
						if(!diffReport.isEmpty()){
							logAndWrite(outputWriter, NEW_LINE);
							logAndWrite(outputWriter, diffReport);
						}
					}
					
				}
			}
		} catch (IOException e) {
			log(e);
		}
		
	}
	
	
	void disableMainPane(){
		mainPane.setDisable(true);
		menuItemSaveDiffReport.setDisable(true);
		getOwnerWindow().setTitle(Msg.get(XMLParamComparator.class, "title.operationInProgress"));
	}
	void enableMainPane(){
		mainPane.setDisable(false);
		menuItemSaveDiffReport.setDisable(false);
		getOwnerWindow().setTitle(Msg.get(XMLParamComparator.class, "title"));
	}
	
	void logAndWrite(Writer writer, String format, Object ... args) throws IOException{
		String content = String.format(format, args);
//		logger.log(content);
		log(content);
		writer.append(content);
	}
	
	void logAndWrite(Writer writer, String content) throws IOException{
//		logger.log(content);
		log(content);
		writer.append(content);
	}
	
	private void clearLog(){
		logMsg.clear();
//		log(msg);
	}
	@Override
	public void log(String msg) {
//		logger.log(msg);
		logMsg.appendText(msg);
//		logMsg.
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//========================================================================================
//	static 
//	static class GroupNumDirsTask extends Task<Map<Path, List<Set<Path>>>>{
//		ParamDirectoryExtractor pe = ParamDirectoryExtractor.newInst(MsgLogger.defaultLogger());
//		
//		Path characteristicPath;
//		public GroupNumDirsTask(Path characteristicPath){
//			//
//			this.characteristicPath = characteristicPath;
//		}
//
//		@Override
//		protected Map<Path, List<Set<Path>>> call() throws Exception {
//			Map<Path, List<Set<Path>>> retval = pe.groupNumDirContents(characteristicPath);
//			return retval;
//		}
//
////		@Override
////		protected void updateProgress(long workDone, long max) {
////			// TODO Auto-generated method stub
////			super.updateProgress(workDone, max);
////		}
////
////		@Override
////		protected void updateMessage(String message) {
////			// TODO Auto-generated method stub
////			super.updateMessage(message);
////		}
////
////		@Override
////		protected void updateValue(Map<Path, List<Set<Path>>> value) {
////			// TODO Auto-generated method stub
////			super.updateValue(value);
////		}
//		
//		public void onFinished(Consumer<Map<Path, List<Set<Path>>>> taskFinished){
//			this.stateProperty().addListener((ob, oldVal, newVal) -> {
//				if(State.SCHEDULED != newVal){
//					return;
//				}
//				
//				taskFinished.accept(this.getValue());
//			});
//		}
//		public void start(){
////			this.stateProperty().addListener((ob, oldVal, newVal) -> {ob.});
//			Thread t = new Thread();
//			t.setDaemon(true);
//			t.start();
//		}
//		
//		
//	}
	
	
	
	//========================================================================================
	static class OperationInProgress{
		private ProgressIndicator pi = new ProgressIndicator();
		private Stage stage;
		public OperationInProgress(){
			stage = new Stage(StageStyle.UTILITY);
			
			Scene scene = new Scene(pi, 300, 400);
			stage.setScene(scene);
		}
		public void show(){
			stage.showAndWait();
		}
		public void hide(){
			stage.hide();
		}
	}
	//========================================================================================
//	static class NumDirGroupsTreeItem extends TreeItem<Map.Entry<Path, List<Set<Path>>>>{
	static class NumDirGroupsTreeItem extends TreeItem<List<Set<Path>>>{
		Path rootDir;
//		Map.Entry<Path, List<Set<Path>>> groupedDirs;
		Path xmlParmFileRelativePath;
		List<Set<Path>> numDirGroups;
		public NumDirGroupsTreeItem(Path rootDir, Path xmlParmFileRelativePath, List<Set<Path>> numDirGroups){
			this.rootDir = rootDir;
//			this.groupedDirs = groupedDirs;
			this.xmlParmFileRelativePath = xmlParmFileRelativePath;
			this.numDirGroups = numDirGroups;
		}
		public Path getRootDir() {
			return rootDir;
		}
		public List<Set<Path>> getGroupedDirs() {
			return numDirGroups;
		}
		public Path getXmlParmFileRelativePath() {
			return xmlParmFileRelativePath;
		}
		public void populateChildNodes(){
			List<Set<Path>> representativeNumDirGroups = ParamDirectoryExtractor.getFirstElementOfEachSet(numDirGroups);
			for(int i = 0; i < representativeNumDirGroups.size(); ++i){
				for(int j = i + 1; j < representativeNumDirGroups.size(); ++j){
					List<Set<Path>> subBinaryGroups = Stream.of(representativeNumDirGroups.get(i), representativeNumDirGroups.get(j))
							.collect(Collectors.toList());
					getChildren().add(new NumDirGroupsTreeItem(rootDir, xmlParmFileRelativePath, subBinaryGroups));
				}
			}
		}
		
	}
	static class NumDirGroupsTreeCell extends TreeCell<List<Set<Path>>>{

		@Override
		protected void updateItem(List<Set<Path>> item, boolean empty) {
			super.updateItem(item, empty);
//			if(empty || null == item){
//				this.setText("");
//				this.setGraphic(null);
//			}else{
////				NumDirGroupsTreeItem treeItem = ;
//				String binaryGroupsStr = ParamDirectoryExtractor.listOfSetsToStr(item);
//				this.setText(binaryGroupsStr);
//			}
			if(empty){
				this.setText("");
				this.setGraphic(null);
			}else{
//				NumDirGroupsTreeItem treeItem = ;
				NumDirGroupsTreeItem treeItem = (NumDirGroupsTreeItem)getTreeItem();
				if(null != treeItem){
					item = treeItem.getGroupedDirs();
					String binaryGroupsStr = ParamDirectoryExtractor.listOfSetsToStr(item);
					this.setText(binaryGroupsStr);
				}
			}
		}
	}
	//========================================================================================
	static class ListOfSetsColumnCell extends TableCell<Map.Entry<Path, List<Set<Path>>>, List<Set<Path>>>{

		@Override
		protected void updateItem(List<Set<Path>> item, boolean empty) {
			super.updateItem(item, empty);
			if(empty || null == item){
				setText("");
				setGraphic(null);
			}else{
//				String groupedSetsStr = ParamDirectoryExtractor.listOfSetsToStr(getItem());
				String groupedSetsStr = ParamDirectoryExtractor.listOfSetsToStrWithOnlyRepresentativeElement(getItem());
				setText(groupedSetsStr);
			}
		}
		
	}
	//========================================================================================
	static class RelativePathItem{
		private Path characteristicPath;
		private boolean populated = false;
		public RelativePathItem(Path characteristicPath) {
			super();
			this.characteristicPath = characteristicPath;
		}
		public boolean isPopulated() {
			return populated;
		}
		public void setPopulated(boolean populated) {
			this.populated = populated;
		}
		public Path getCharacteristicPath() {
			return characteristicPath;
		}
	}
	//----------------------------------------------------------------------------------------
	
	
//	static class RelativePathListCell extends ListCell<Path>{
	static class RelativePathListCell extends ListCell<RelativePathItem>{
		private Path rootDir;
		public RelativePathListCell(Path rootDir){
			this.rootDir = rootDir;
//			this.getStylesheets().add(STYLE_CLASS_UMPOPULATED);
//			setUnpopulated();
		}
//		@Override
//		protected void updateItem(Path item, boolean empty) {
//			super.updateItem(item, empty);
//			if(empty || null == item){
//				this.setText("");
//				this.setGraphic(null);
//			}else{
//				this.setText(getRelativePath(item).toString());
//			}
//		}
		@Override
		protected void updateItem(RelativePathItem item, boolean empty) {
			super.updateItem(item, empty);
			if(empty || null == item){
				this.setText("");
				this.setGraphic(null);
			}else{
				Path p = item.getCharacteristicPath();
				this.setText(getRelativePath(p).toString());
				
				if(!item.isPopulated()){
					if(!this.getStyleClass().contains(STYLE_CLASS_UMPOPULATED)){
						this.getStyleClass().add(STYLE_CLASS_UMPOPULATED);
					}
				}else{
					if(this.getStyleClass().contains(STYLE_CLASS_UMPOPULATED)){
						this.getStyleClass().remove(STYLE_CLASS_UMPOPULATED);
					}
				}
			}
		}
		private Path getRelativePath(Path p){
			if(null == rootDir){
				return p;
			}else{
				return rootDir.relativize(p);
			}
		}
//		public void setUnpopulated(){
//			if(!this.getStyleClass().contains(STYLE_CLASS_UMPOPULATED)){
//				this.getStyleClass().add(STYLE_CLASS_UMPOPULATED);
//			}
//		}
//		public void clearUnpopulated(){
//			if(this.getStyleClass().contains(STYLE_CLASS_UMPOPULATED)){
//				this.getStyleClass().remove(STYLE_CLASS_UMPOPULATED);
//			}
//		}
		
		static final String STYLE_CLASS_UMPOPULATED = Msg.get(XMLParamComparatorController.class, "style.class.unpopulated");
	}
	//========================================================================================

	/**
	 * <h3>Consider the structure in XML Param parent directory</h3>
	 * <p>PARAM_ROOT_DIRECTORY/</p>
	 * <ul>
	 * <li>
	 * 	<h4>Function_Group_Name/</h4>
	 * 	<ul>
	 * 		<li>
	 * 
	 * 		</li>
	 * 		<li>{Module|System}/{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{Module|System}/{<strong>number</strong>}/{ConfigType}/{FuncType}/*.xml</li>
	 * 		<li>{ConfigType}/{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{Module|System}/{Multi|Single}/{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{ConfigType}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{BankGroup}/{CountryCode}/*.js	//<--------</li>
	 * 		<li>{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{ConfigType}/{FuncType}/*.{xml|.xsl}</li>
	 * 		<li>[Module|System]/{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{FuncType}/*.xml</li>
	 * 		<li>{<strong>number</strong>}/{BankGroup}/{CountryCode}/{AP|WEB}/{ConfigType}/{FuncType}/[{SubFuncType}]/*.{xml|.xsl}</li>
	 * 		<li>*.vbs</li>
	 * 		<li></li>
	 * 	</ul>
	 * </li>
	 * <li>
	 * 	<h4>*.xlsx</h4>
	 * </li>
	 * </ul>
	 * 
	 * <h3>Notes:</h3>
	 * <ul>
	 * 	<li>{<strong>number</strong>} may not start from 1</li>
	 * 	<li>Sub-directories in <strong>Function_Group_Name</strong>/{*}/{<strong>number</strong>} are supposed to have the <em>same</em> structure, 
	 * it's under these <em>characteristic directories</em> that we we are supposed to check the structure of directors & XML files.</li>
	 * 	<li>The comparison process should be proceeded for each parent directory of 
	 * <strong>Function_Group_Name</strong>/{*}/{<strong>number</strong>}</li>
	 * 	<li></li>
	 * </ul>
	 * @author MEC
	 *
	 */
	static class ParamDirectoryExtractor{
//		private static final ParamDirectoryExtractor instance = new ParamDirectoryExtractor();
		private Path rootDir;
		private ParamDirectoryExtractor(MsgLogger logger){
			this.logger = logger;
//			xsc.setLogger(logger);
		}
		private ParamDirectoryExtractor(Path root){
			Objects.requireNonNull(root);
			if(!Files.exists(root)){
				logger.log(new FileNotFoundException(Msg.get(XMLParamComparatorController.class, "error.pathNotExist")));
			}
			this.rootDir = root;
		}
		
		public static ParamDirectoryExtractor newInst(MsgLogger logger){
//			ParamDirectoryExtractor retval =  new ParamDirectoryExtractor(paramRootDirectory);
//			retval.setLogger(logger);
			return new ParamDirectoryExtractor(logger);
		}
//		public static ParamDirectoryExtractor newInst(MsgLogger logger, Path paramRootDirectory){
//			ParamDirectoryExtractor retval =  new ParamDirectoryExtractor(paramRootDirectory);
//			retval.setLogger(logger);
//			return retval;
//		}
		
		//
		/**
		 * Function groups directories are those direct children of {@link #rootDir}
		 * @return
		 */
		public List<Path> getFunctionGroups(){
			List<Path> retval = new ArrayList<>();
			try {
				List<Path> subDirs = Files.list(rootDir).filter(Files::isDirectory).collect(Collectors.toList());
				retval.addAll(subDirs);
			} catch (IOException e) {
				logger.log(e);
			}
			return retval;
		}
		
		
		/**
		 * <p><strong>Characteristic path</strong> are those directories 
		 * have <strong><em>direct child directory</em></strong> with name pattern <code>\d+</code>
		 * </p>
		 * <p>
		 * This method will check all the descendant directories of <code>fromDir</code>,
		 * in case any directory matches principles listed above, it will be includedi in
		 * the returned list.
		 * </p>
		 * @param fromDir
		 * @return
		 */
		public List<Path> getCharacteristicPath(Path fromDir){
			List<Path> retval = new ArrayList<>();
			if(!Files.isDirectory(fromDir)){
				return retval;
			}
			
			try {
				
//				Optional<Path> numDir = Files.list(fromDir).filter(p -> {
//					Matcher m = NUM_SUB_DIR_PATTERN.matcher(p.getFileName().toString());
//					
//					return m.matches();
////					return false;
//					
//				}).findAny();
//				
//				if(numDir.isPresent()){
//					retval.add(fromDir);
//				}
				
				List<Path> subDirs = Files.list(fromDir).collect(Collectors.toList());
				for(Path subDir : subDirs){
					if(!Files.isDirectory(subDir)){
						continue;
					}
					
//					Matcher m = NUM_SUB_DIR_PATTERN.matcher(subDir.getFileName().toString());
					
//					if(m.matches()){	//a {number} folder is found
					if(isNumDir(subDir)){
						if(!retval.contains(fromDir)){
							retval.add(fromDir);
						}
					}else{	//check its sub directories
						List<Path> subCharacteristicPaths = getCharacteristicPath(subDir);
						retval.addAll(subCharacteristicPaths);
					}
					
				}
			} catch (IOException e) {
				logger.log(e);
			}
			//
			return retval;
		}
		
		

		/**
		 * If <code>paramFile</code> name starts with <em>HSBC</em>, then filter only those <em>HSBC/US</em>, 
		 * otherwise simply return this paramFile; 
		 * @param paramFile
		 * @return
		 */
		boolean isStartsWithBankGroundAndCountryCode(Path paramFile){
			if(PATH_NAME_COUNT > paramFile.getNameCount()){
				return true;
			}else{
				String bankGroup = paramFile.getName(0).getFileName().toString();
				String countryCode = paramFile.getName(1).getFileName().toString();
//				if(DEFAULT_BANK_GROUP.equals(bankGroup)
//					&& DEFAULT_COUNTRY_CODE.equals(countryCode)
//					){
//					return true;
//				}else{
//					return false;
//				}
//				if(DEFAULT_BANK_GROUP.equals(bankGroup)
//						&& !DEFAULT_COUNTRY_CODE.equals(countryCode)
//						){
//					return false;
//				}else{
//					return true;
//				}
				if(DEFAULT_BANK_GROUP.equals(bankGroup)){
					if(DEFAULT_COUNTRY_CODE.equals(countryCode)){
						return true;
					}else{
						return false;
					}
				}else{	//!DEFAULT_BANK_GROUP.equals(bankGroup)
					if(bankGroup.startsWith(DEFAULT_BANK_GROUP)	//directories like HSBCD, HSBCN, etc.
						|| bankGroup.startsWith(IGNORE_BANK_GROUP)
						){	
						return false;
					}else{
						return true;
					}
				}
			}
		}
		
		/**
		 * Get the normalized string representation of this list of grouped sets. e.g.:
		 * <quote>
		 * [{1,2,3},{4,5,6},{7,8,9},{10}}]
		 * </quote>
		 * @param groupedNumDirs
		 * @return
		 */
//		public String groupedNumDirsToStr(List<Set<Path>> groupedNumDirs){
		public static String listOfSetsToStr(List<Set<Path>> groupedNumDirs){
			String groupedNumDirsStr = groupedNumDirs.stream()
					.map(sp -> sp.stream()
							.sorted(Comparator.comparingInt(p -> Integer.parseInt(p.getFileName().toString())))
							.map(p -> p.getFileName().toString())
//							.collect(Collectors.joining(",", "{", "}"))
							.collect(Collectors.joining(CHAR_ITEM_DELIMITER, CHAR_SET_START, CHAR_SET_END))
//					).collect(Collectors.joining(",", "[", "]"));
					).collect(Collectors.joining(CHAR_ITEM_DELIMITER, CHAR_LIST_START, CHAR_LIST_END));
//			logger.log(groupedNumDirsStr);
			return groupedNumDirsStr;
		}
		
		/**
		 * Grouped NumDirs are displayed by only it representative element. e.g.:
		 * The original
		 * <quote>
		 * 	[{1,2,3},{4,5,6},{7,8,9},{10}}]
		 * </quote>
		 * would be displayed as:
		 * <quote>
		 * [{1},{4},{7},{10}}]
		 * </quote>
		 * @param groupedNumDirs
		 * @return
		 */
		public static String listOfSetsToStrWithOnlyRepresentativeElement(List<Set<Path>> groupedNumDirs){
			
//		public String groupedNumDirsToStrWithOnlyRepresentativeElement(List<Set<Path>> groupedNumDirs){
//			String groupedNumDirsStr = groupedNumDirs.stream()
//					.map(sp -> 
////						{
////						Set<Path> elementsSortedSet = 
//						sp.stream()
//							.sorted(Comparator.comparingInt(p -> Integer.parseInt(p.getFileName().toString())))
////							.collect(Collectors.toSet())
////							;
////							.findFirst().map
////							.map(this::getFirst)
////						Set<Path> firstElemSet = getFirst(elementsSortedSet);
////						return firstElemSet.stream()
//							.map(p -> p.getFileName().toString())
//////							.map
//////							.collect(Collectors.joining(",", "{", "}"))
//							.collect(Collectors.joining(CHAR_ITEM_DELIMITER, CHAR_SET_START, CHAR_SET_END))
////							;
////						}
////					).collect(Collectors.joining(",", "[", "]"));
//					).collect(Collectors.joining(CHAR_ITEM_DELIMITER, CHAR_LIST_START, CHAR_LIST_END));
////			logger.log(groupedNumDirsStr);
//			return groupedNumDirsStr;
//		}
//			List<Set<Path>> listOfSetsWithOnlyRepresentativeElement = groupedNumDirs.stream()
//					.map(s -> getFirst(s)).collect(Collectors.toList());
			List<Set<Path>> listOfSetsWithOnlyRepresentativeElement = getFirstElementOfEachSet(groupedNumDirs);
			return listOfSetsToStr(listOfSetsWithOnlyRepresentativeElement);
		}
		
		/**
		 * Get a list of sets with only the first element of the original set, e.g.:
		 * <dl>
		 * 	<dt>INPUT</dt>
		 * 	<dd>[{1, 2, 3}, {4, 5}, {6}]</dd>
		 * 	<dt>OUTPUT</dt>
		 * 	<dd>[{1}, {2}, {3}]</dd>
		 * </dl>
		 * @param listOfElements
		 * @return
		 */
		static <T> List<Set<T>> getFirstElementOfEachSet(List<Set<T>> listOfElements){
			return listOfElements.stream().map(s -> getFirst(s)).collect(Collectors.toList());
		}
		/**
		 * Return the first element as the representative element of this set, e.g.:
		 * <dl>
		 * 	<dt>INPUT</dt>
		 * 	<dd>{1, 2, 3}</dd>
		 * 	<dt>OUTPUT</dt>
		 * 	<dd>{1}</dd>
		 * </dl>
		 * @param elements
		 * @return
		 */
		static <T> Set<T> getFirst(Set<T> elements){
//		static <T> Stream<T> getFirst(Stream<T> elements){
			if(elements.isEmpty() || 1 == elements.size()){
//			if(elements.is || 1 == elements.size()){
				return elements;
			}
			Set<T> retval = new HashSet<>(1);
			retval.add(elements.stream().findFirst().get());
//			retval.add(elements.findFirst().get());
			return retval;
		}
		
		
		/**
		 * Compare XMlParamFiles from different NumDir groups in the <code>groupedNumDirs</code>,
		 * and generate the difference report. 
		 * 
		 * <p>
		 * Report result will be collected by {@link StringLogger} <code>sl</code>
		 * </p>
		 * @param rootDir
		 * @param groupedNumDirs
		 * @param xmlFileRelativePath
		 * @param sl
		 * @return
		 */
		String getDiffReport(Path rootDir, List<Set<Path>> groupedNumDirs, Path xmlFileRelativePath,
				StringLogger sl){
			Stream.of(rootDir, groupedNumDirs, xmlFileRelativePath).forEach(Objects::requireNonNull);

//			StringPrefixLogger sl = new StringPrefixLogger(Msg.get(XMLParamComparatorController.class, "info.xmlStructDiffPattern"));
			groupedNumDirs = getFirstElementOfEachSet(groupedNumDirs);
			XMLStructComparator diff = XMLStructComparator.newInst(sl);
			
			//[{1,2},{3,4},{5,6}] -> [1, 3, 5]
			List<Path> representativeNumDirList = groupedNumDirs.stream()
					.map(s -> s.stream().findFirst().get())
					.collect(Collectors.toList());
			
//			for(Path numDir : representativeNumDirList){
//				representativeNumDirList.stream().filter(r -> !numDir.equals(r))
//					.forEach(r -> {
//						Path xml1 = rootDir.resolve(numDir).resolve(xmlFileRelativePath);
//						Path xml2 = rootDir.resolve(r).resolve(xmlFileRelativePath);
//						diff.isXMLFilesEqual(xml1, xml2);
//					});
//			}
			Path numDir1 = null;
			Path numDir2 = null;
			for(int i = 0; i < representativeNumDirList.size(); ++i){
				for(int j = i + 1; j < representativeNumDirList.size(); ++j){
					numDir1 = representativeNumDirList.get(i);
					numDir2 = representativeNumDirList.get(j);
//					Path xml1 = rootDir.resolve(numDir1).resolve(xmlFileRelativePath);
//					Path xml2 = rootDir.resolve(numDir2).resolve(xmlFileRelativePath);
					Path xml1 = numDir1.resolve(xmlFileRelativePath);
					Path xml2 = numDir2.resolve(xmlFileRelativePath);
					sl.log(NEW_LINE);
					sl.log(Msg.get(XMLParamComparatorController.class, "info.compareFiles"), 
//							rootDir.relativize(numDir1).toString(), 
//							rootDir.relativize(numDir2).toString());
							numDir1.getFileName().toString(), 
							numDir2.getFileName().toString());
					diff.isXMLFilesEqual(xml1, xml2);
				}
			}
			
			return sl.toString();
		}
		
		
		
		//----------------------------------------------------------------------
		
		/**
		 * Group each descendant XML file in this <code>characteristicPath</code>
		 * according to their structures. Final result would look like this:
		 * <dl>
		 * <dt>Result:</dt>
		 * <dd>{<strong>key</strong> -> <strong>sets</strong>};</dd>
		 * <dt>key:</dt>
		 * <dd>path of the XML file, <code>relativized</code> by the <code>numDir</code></dd>
		 * <dt>sets:</dt>
		 * <dd>Looks like {{1}, {2, 3}, {4}}, where numDirs are grouped 
		 * according to XML equality behavior as defined in {@link XMLStructComparator#isXMLFilesEqual(Path, Path)}</dd>
		 * </dl>
		 * 
		 *  
		 * @param characteristicPath
		 * @return List of sets of <code>numDirs</code> grouped by struct of
		 * 		<code>xmlParamFiles</code> in each <code>numDirs</code> 
		 */
//		Map<Path, Set<Set<Path>>> groupNumDirContents(Path characteristicPath){
		Map<Path, List<Set<Path>>> groupNumDirContents(Path characteristicPath){
//			return groupNumDirContents(characteristicPath, null);
//		}
//		
//		Map<Path, List<Set<Path>>> groupNumDirContents(Path characteristicPath, Predicate<Path> xmlParamFileFilter){
//			Map<Path, Set<Set<Path>>> retval = new HashMap<>();
			Map<Path, List<Set<Path>>> retval = new HashMap<>();
			Objects.requireNonNull(characteristicPath);
			if(!Files.exists(characteristicPath)
				|| !Files.isDirectory(characteristicPath)){
				logger.log(Msg.get(XMLParamComparatorController.class, "error.invalidCharacteristicPath")
						, characteristicPath.toString());
				return retval;
//				return;
			}
			
//			try {
//				List<Path> numDirs = Files.list(characteristicPath)
//						.filter(this::isNumDir)
//						.collect(Collectors.toList());
				List<Path> numDirs = listChildNumDirs(characteristicPath);
//				numDirs.stream().collect(Collectors.groupingBy(classifier))
				for(Path numDir : numDirs){
//					walkXmlFilesInNumDir(numDir, numDirs);
					List<Path> xmlParamFiles = listXmlParamFiles(numDir);
					
//					if(null != xmlParamFileFilter){
//						xmlParamFiles = xmlParamFiles.stream().filter(xmlParamFileFilter).collect(Collectors.toList());
//					}
					
					for(Path xmlPathRelativeToNumDir : xmlParamFiles){
//						List<Set<Path>> xmlStructSets = groupXmlFiles(xmlPathRelativeToNumDir, numDirs);
//						retval.computeIfAbsent(xmlPathRelativeToNumDir, key -> xmlStructSets);
						retval.computeIfAbsent(xmlPathRelativeToNumDir, key -> 
							groupXmlFiles(key, numDirs)
						);
					}
				}
				
//			} catch (IOException e) {
//				logger.log(e);
//			}
			
			return retval;
		}
		
		
//		private Map<Path, Set<Set<Path>>> walkXmlFilesInNumDir(Path currentNumDir, List<Path> siblingNumDirs){
//		private void walkXmlFilesInNumDir(Path currentNumDir, List<Path> siblingNumDirs){
//			Map<Path, Set<Set<Path>>> retval = new HashMap<>();
			
			
//			return retval;
			
//		}
		
		/**
		 * List all direct child <code>numDirs</code> in this <code>characteristicPath</code> 
		 * @param characteristicPath
		 * @return
		 */
		List<Path> listChildNumDirs(Path characteristicPath){
			List<Path> numDirs = null;
			if(Files.isDirectory(characteristicPath)){
				try {
					numDirs = Files.list(characteristicPath)
							.filter(this::isNumDir)
							.collect(Collectors.toList());
				} catch (IOException e) {
					logger.log(e);
					
				}
			}
			if(null == numDirs){
				numDirs = new ArrayList<>();
			}
			return numDirs;
		}
		
		/**
		 * List all the descendant parameter XML file paths <strong>relative</strong> to <code>numDir</code>
		 * will be returned.
		 * @param numDir
		 * @return
		 */
		List<Path> listXmlParamFiles(Path numDir){
			List<Path> retval = new ArrayList<>();
			Stream<Path> descendantPaths;
			try {
				descendantPaths = Files.walk(numDir);
				descendantPaths.filter(p -> !Files.isDirectory(p))
						.filter(this::isXmlFile).forEach(xmlFile -> {
							retval.add(numDir.relativize(xmlFile));
						});
				descendantPaths.close();
			} catch (IOException e) {
				logger.log(e);
			}
			
			return retval;
		}
		
		
		/**
		 * Grouping <code>curAndSiblingNumDirs</code> according to different structures of the xml file as 
		 * specified by <code>xmlPathRelativeToNumDir</code>
		 * @param xmlPathRelativeToNumDir
		 * @param curAndSiblingNumDirs
		 */
//		private void groupXmlFiless(Path xmlPathRelativeToNumDir, Path currentNumDir, List<Path> curAndSiblingNumDirs){
		private List<Set<Path>> groupXmlFiles(Path xmlPathRelativeToNumDir, List<Path> curAndSiblingNumDirs){
//			Map<>
//			Set<Path> numDirSet = new HashSet<>();
			List<Set<Path>> numDirSetGroup = new ArrayList<>();
//			Set<XMLParamFile> xmlParamFileSet = new HashSet<>();
//			Path xmlFile = null;
			for(Path numDir : curAndSiblingNumDirs){
//				xmlFile = numDir.resolve(xmlPathRelativeToNumDir);
//				XMLParamFile paramFile = new XMLParamFile(xmlPathRelativeToNumDir, numDir);
//				if
				Optional<Set<Path>> numDirWithSameStruct = numDirSetGroup.stream()
						.filter(numDirSet -> isWithSameXmlStruct(xmlPathRelativeToNumDir, numDir, numDirSet))
						.findAny();
				if(numDirWithSameStruct.isPresent()){
					numDirWithSameStruct.get().add(numDir);
				}else{
					Set<Path> newXmlStructSet = new HashSet<>();
					newXmlStructSet.add(numDir);
					numDirSetGroup.add(newXmlStructSet);
				}
				
			}
			return numDirSetGroup;
//			curAndSiblingNumDirs.stream().collect(Collectors.);
		}
		
		private boolean isWithSameXmlStruct(Path xmlPathRelativeToNumDir, Path numDirNew, Set<Path> numDirWithSameStruct){
			if(null == numDirWithSameStruct){
				return false;
			}
			boolean retval = false;
			retval = numDirWithSameStruct.stream().anyMatch(numDirOld -> {
				Path xml1 = numDirNew.resolve(xmlPathRelativeToNumDir);
				Path xml2 = numDirOld.resolve(xmlPathRelativeToNumDir);
//				return XMLStructComparator.inst().isXMLFilesEqual(xml1, xml2);
				return xsc.isXMLFilesEqual(xml1, xml2);
			});
			return retval;
		}
		
		private boolean isNumDir(Path subDir){
			Matcher m = NUM_SUB_DIR_PATTERN.matcher(subDir.getFileName().toString());
			return m.matches();
		}
		
//		public void setLogger(MsgLogger logger) {
//			this.logger = logger;
//		}
		
		private boolean isXmlFile(Path xmlFile){
			return xmlFile.getFileName().toString().toLowerCase().endsWith(XML_SUFFIX);
		}
		

		private MsgLogger logger = MsgLogger.defaultLogger();
		private XMLStructComparator xsc = XMLStructComparator.newInst(logger); 
		static final Pattern NUM_SUB_DIR_PATTERN = Pattern.compile(Msg.get(XMLParamComparatorController.class, "pattern.numSubDir"));
		static final String XML_SUFFIX = Msg.get(XMLParamComparatorController.class, "suffix.xml");
		static final String CHAR_SET_START= Msg.get(XMLParamComparatorController.class, "char.set.start");
		static final String CHAR_SET_END= Msg.get(XMLParamComparatorController.class, "char.set.end");
		static final String CHAR_LIST_START= Msg.get(XMLParamComparatorController.class, "char.list.start");
		static final String CHAR_LIST_END= Msg.get(XMLParamComparatorController.class, "char.list.end");
		static final String CHAR_ITEM_DELIMITER= Msg.get(XMLParamComparatorController.class, "char.item.delimiter");
	}
	
//	static class XMLParamFile{
//		Path xmlPathRelativeToNumDir;
//		Path numDir;
//		Document domStruct;
//		Path xmlFileFullPath;
//		public XMLParamFile(Path xmlPathRelativeToNumDir, Path numDir) {
//			super();
//			this.xmlPathRelativeToNumDir = xmlPathRelativeToNumDir;
//			this.numDir = numDir;
//			this.xmlFileFullPath = numDir.relativize(xmlPathRelativeToNumDir);
//		}
//		public Path getXmlPathRelativeToNumDir() {
//			return xmlPathRelativeToNumDir;
//		}
//		public void setXmlPathRelativeToNumDir(Path xmlPathRelativeToNumDir) {
//			this.xmlPathRelativeToNumDir = xmlPathRelativeToNumDir;
//		}
//		public Path getNumDir() {
//			return numDir;
//		}
//		public void setNumDir(Path numDir) {
//			this.numDir = numDir;
//		}
//		public Document getDomStruct() {
//			return domStruct;
//		}
//		public void setDomStruct(Document domStruct) {
//			this.domStruct = domStruct;
//		}
//		public Path getXmlFileFullPath() {
//			return xmlFileFullPath;
//		}
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + ((domStruct == null) ? 0 : domStruct.hashCode());
//			result = prime * result + ((xmlPathRelativeToNumDir == null) ? 0 : xmlPathRelativeToNumDir.hashCode());
//			return result;
//		}
//		@Override
//		public boolean equals(Object obj) {
//			if(!(obj instanceof XMLParamFile)){
//				return false;
//			}
//			
//			Path xml1 = getXmlFileFullPath();
//			Path xml2 = ((XMLParamFile)obj).getXmlFileFullPath();
//			return XMLStructComparator.inst().isXMLFilesEqual(xml1, xml2);
//		}
//		
//	}

//	static final PrintStream out = System.out;
	static final String IGNORE_BANK_GROUP = Msg.get(XMLParamComparatorController.class, "path.filter.bankGroup.ignore");
	static final String DEFAULT_BANK_GROUP = Msg.get(XMLParamComparatorController.class, "path.filter.bankGroup");
	static final String DEFAULT_COUNTRY_CODE = Msg.get(XMLParamComparatorController.class, "path.filter.countryCode");
	static final int PATH_NAME_COUNT = 2;
	static final String SLASH = Msg.get(XMLParamComparatorController.class, "path.slash");
	static final String BACK_SLASH = Msg.get(XMLParamComparatorController.class, "path.backSlash");
//	MsgLogger logger = MsgLogger.defaultLogger();
}

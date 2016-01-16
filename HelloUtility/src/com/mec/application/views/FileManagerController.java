package com.mec.application.views;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import com.mec.resources.FileParser;
import com.mec.resources.JarTool;
import com.mec.resources.Msg;
import com.mec.resources.MsgLogger;
import com.mec.resources.ViewFactory;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextFlow;

/**
 * A file manager is needed;
 * @author MEC
 */
public class FileManagerController implements MsgLogger{

	@FXML
	private TreeView<Path> fileBrowser;
	

	
	
	
	@FXML
	private TextArea statusInfo;
	
	@FXML
	private Button searchFileBtn;
	@FXML
	private Button searchWorkspaceBtn;
	
	
	
	@FXML
	private void initialize(){
		initFileBrowser();
//		initAccelerators();
	}
	
	
	
	@Override
	public void log(String msg) {
		statusInfo.appendText(msg);
	}


	@Override
	public void log(Exception e) {
		Alert alert = ViewFactory.newAlert(AlertType.ERROR
				, Msg.get(this, "error.title")
				, String.format(Msg.get(this, "error.header"), e.getClass().getName(), e.getMessage())
				);
		TextFlow errorStack = ViewFactory.newTextFlow(JarTool.exceptionToStr(e)
				, Optional.of(Msg.get(this, "error.details.maxLength", Integer::parseInt, 1000))
				);
//		errorStack.setPadding(new Insets(Msg.get(this, "error.dialog.padding", Integer::parseInt, 5)));
		alert.getDialogPane().setExpandableContent(errorStack);
		alert.showAndWait();
	}

	
	@FXML
	private void onAbout(){
		Alert about = ViewFactory.newAlert(AlertType.INFORMATION
				, Msg.get(this, "about.title")
				, Msg.get(this, "about.header"));
		about.setContentText(String.format(Msg.get(this, "about.content"), 
				Paths.get(Msg.get(this, "path.current")).toAbsolutePath().normalize()
				));
		about.showAndWait();
	}
	
	
	
	
	


	private void initFileBrowser(){
//		TreeItem<Path> myComputer = new PathTreeItem(Paths.get(MY_COMUTER));
		TreeItem<Path> myComputer = new PathTreeItem(Paths.get(Msg.get(this, "rootPath")), this);
//		TreeItem<Path> myComputer = new PathTreeItem(Paths.get(Msg.get(this, "rootPath")));
		myComputer.setExpanded(true);
//		testBtn.setGraphic(FileParser.getImage(Msg.get(this, "img.file")));
		
		fileBrowser.setRoot(myComputer);
		fileBrowser.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		fileBrowser.setShowRoot(false);
		fileBrowser.setCellFactory(tv -> new PathTreeCell());
		
		
		fileBrowser.getSelectionModel().getSelectedIndices().addListener(this::onFileBrowserListSelectoinChanged);
		fileBrowser.addEventHandler(KeyEvent.KEY_PRESSED, this::onFileBrowserKeyPressed);
//		fileBrowser.setOnKeyPressed(value);
	}
	
	@SuppressWarnings("unchecked")
	private void onFileBrowserKeyPressed(KeyEvent event){
		TreeView<Path> fileBrowser = (TreeView<Path>) event.getTarget();
		final KeyCode keyCode = event.getCode();
		if(!isTraversableKey(keyCode)){
			return;
		}
			
		String name = getKeyPathStr(keyCode);
		int selected = fileBrowser.getSelectionModel().getSelectedIndex();
		int index = selected + 1;
		for(	; 	//Search: starts from the next item following current selected item
				index < fileBrowser.getExpandedItemCount(); 
				++index){
			TreeItem<Path> item = fileBrowser.getTreeItem(index);
			Path fn = item.getValue().getFileName();
			if(null == fn){
				fn = item.getValue().getRoot();
			}
			String fileName = fn.toString().toUpperCase();
			if(fileName.startsWith(name)){
				break;
			}
		}
		if(index < fileBrowser.getExpandedItemCount()){
			fileBrowser.getSelectionModel().clearAndSelect(index);;
			fileBrowser.scrollTo(index);
		}else{		//Not found? Then search from the beginning of the TreeView;
			for( index = 0; index < selected; ++index){
				TreeItem<Path> item = fileBrowser.getTreeItem(index);
				Path fn = item.getValue().getFileName();
				if(null == fn){
					fn = item.getValue().getRoot();
				}
				String fileName = fn.toString().toUpperCase();
				if(fileName.startsWith(name)){
					break;
				}
			}
			if(index < selected){
				fileBrowser.getSelectionModel().clearAndSelect(index);
				fileBrowser.scrollTo(index);
			}
		}
	}
	
	/**
	 * Can the pressed key be used to traverse items in the file browser?
	 * @param keyCode
	 * @return
	 */
	private static final boolean isTraversableKey(KeyCode keyCode){
		return (keyCode.isLetterKey() 
				|| keyCode.isDigitKey()
				|| KeyCode.PERIOD == keyCode
//				|| KeyCode.UNDERSCORE == keyCode	//Should combine with SHIFT
				);
	}
	
	/**
	 * Get the path characters used to traverse the file browser tree
	 * @param keyCode
	 * @return
	 */
	private static final String getKeyPathStr(KeyCode keyCode){
		if(KeyCode.PERIOD == keyCode){
			return CHAR_PERIOD;
		}
//		if(KeyCode.UNDERSCORE == keyCode){
//			return CHAR_UNDERSTORE;
//		}
		return keyCode.getName();
	}
	
	private void onFileBrowserListSelectoinChanged(ListChangeListener.Change<? extends Integer> change){
//		fileBrowser.getSelectionModel()
//		.getSelectedItems().stream()
//		.map(p ->p.getValue().toAbsolutePath().toString())
//		.reduce((r, s) -> r + FileParser.NEWLINE + s)
//		.ifPresent( val -> statusInfo.setText(val));
		ObservableList<TreeItem<Path>> selectedItems = fileBrowser.getSelectionModel().getSelectedItems();
		String fileName = "";
		for(TreeItem<Path> item : selectedItems){
			if(null == item){
				continue;
			}
			Path p = item.getValue();
			if(null == p || null == p.toAbsolutePath()){
				continue;
			}
			fileName += p.toAbsolutePath().toString() + FileParser.NEWLINE;
//			log(fileName + FileParser.NEWLINE);
		}
		statusInfo.setText(fileName);
		
	}
	
	public static final String CHAR_PERIOD = ".";
//	public static final String CHAR_UNDERSTORE = "_";
	
	
	private static final class PathTreeCell extends TreeCell<Path>{
		
		@Override
		protected void updateItem(Path item, boolean empty) {
			super.updateItem(item, empty);
			if(null == item || empty){
				this.setText(null);
				this.setGraphic(null);
			}else{
				Path fn = item.getFileName();
				if(null != fn){
					this.setText(fn.toString());
				}else{
					this.setText(item.toString());
				}
				this.setGraphic(this.getTreeItem().getGraphic());
			}
		}
		
	}
	
	private static final class PathTreeItem extends TreeItem<Path>{
		private Optional<Boolean> isLeaf = Optional.empty();
		private boolean isChildrenLoaded = false;
		private MsgLogger logger;
//		private static final ExecutorService populateChildrenService = Executors.newCachedThreadPool();
		private static final ExecutorService populateChildrenService = new ForkJoinPool();
//		private static final ExecutorService populateChildrenService = ForkJoinPool.commonPool();
		
		public PathTreeItem(Path p, MsgLogger logger){
			super(p);
			this.logger = logger;
			ImageView icon = null;
			if(Files.isDirectory(p)){
				icon = FileParser.getImage(Msg.get(FileManagerController.class, "img.folder"));
			}else{
				icon = FileParser.getImage(Msg.get(FileManagerController.class, "img.file"));
			}
			this.setGraphic(icon);
		}
		@Override
		public boolean isLeaf() {
			if(!isLeaf.isPresent()){
				isLeaf = Optional.of(!Files.isDirectory(this.getValue()));
			}
			return isLeaf.get();
		}

		@Override
		public ObservableList<TreeItem<Path>> getChildren() {
			if(!isChildrenLoaded){
				isChildrenLoaded = true;
				populateChildren(this);
//				Platform.runLater(() -> populateChildren(this));
				
			}
			return super.getChildren();
		}
	

		private void populateChildren(TreeItem<Path> item){
			item.getChildren().clear();
			
			Path currentPath = item.getValue();
//			if(MY_COMUTER.equals(currentPath.getFileName().toString()) && null == item.getParent()){
			if(null == item.getParent()){
				FileSystems.getDefault().getRootDirectories().forEach(rootDirectory -> item.getChildren().add(new PathTreeItem(rootDirectory, logger)));
				return;
			}else if(!Files.isDirectory(currentPath)){
				return;
			}
			
//			populateChildrenService.submit(this::populateChildrenTask);
			populateChildrenService.submit(() -> populateChildrenTask(item));
		}
		
		
//		private void populateChildrenTask(){
		private void populateChildrenTask(TreeItem<Path> treeItem){
			Path currentPath = treeItem.getValue();
			List<PathTreeItem> retval = new ArrayList<>();
			try {
//				Files.list(currentPath).forEach(pathItem -> item.getChildren().add(new PathTreeItem(pathItem, this.logger)));
				List<Path> files = Files.list(currentPath).sorted((left, right) -> {
					if(Files.isDirectory(left) && ! Files.isDirectory(right)){	//<- Directory should be listed before ordinary files
						return -1;
					}else if(!Files.isDirectory(left) && Files.isDirectory(right)){
						return 1;
					}else{
						return left.compareTo(right);
					}
				}).collect(Collectors.toList());
				for(Path p : files){
					if(!Files.exists(p)){	//Omit files without access permission
						continue;
					}
					DosFileAttributes attr = Files.readAttributes(p, DosFileAttributes.class);
					if(attr.isHidden() || attr.isSystem()){
						continue;
					}
					retval.add(new PathTreeItem(p, logger));
				}
				
				treeItem.getChildren().addAll(retval);
			} catch (IOException e) {
				logger.log(e);
			}
		}
		
		
	}
	
//	private static final String MY_COMUTER = Msg.get(FileManagerController.class, "myComputer");
}

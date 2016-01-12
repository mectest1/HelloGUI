package com.mec.application.views;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mec.resources.FileParser;
import com.mec.resources.Msg;
import com.mec.resources.MsgLogger;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

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
	private void initialize(){
		initFileBrowser();
//		initAccelerators();
	}
	
	
	
	@Override
	public void log(String msg) {
//		statusInfo.clear();
		statusInfo.appendText(msg);
	}



	private void initFileBrowser(){
//		TreeItem<Path> myComputer = new PathTreeItem(Paths.get(MY_COMUTER));
		TreeItem<Path> myComputer = new PathTreeItem(Paths.get(Msg.get(this, "rootPath")), this);
//		TreeItem<Path> myComputer = new PathTreeItem(Paths.get(Msg.get(this, "rootPath")));
		myComputer.setExpanded(true);
		
		fileBrowser.setRoot(myComputer);
		fileBrowser.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		fileBrowser.setShowRoot(false);
		fileBrowser.setCellFactory(tv -> new PathTreeCell());
		
		
		fileBrowser.getSelectionModel().getSelectedIndices().addListener(this::onFileBrowserListSelectoinChanged);
		fileBrowser.addEventHandler(KeyEvent.KEY_PRESSED, this::onFileBrowserKeyPressed);
//		fileBrowser.setOnKeyPressed(value);
	}
	
	private void onFileBrowserKeyPressed(KeyEvent event){
		KeyCode keyCode = event.getCode();
		if(!(keyCode.isLetterKey() || keyCode.isDigitKey())){
			return;
		}
			
		String name = keyCode.getName();
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
			
			//Files.isDirectory(currentPath):
			try {
//				Files.list(currentPath).forEach(pathItem -> item.getChildren().add(new PathTreeItem(pathItem, this.logger)));
				List<Path> files = Files.list(currentPath).sorted((l, r) -> {
					if(Files.isDirectory(l) && ! Files.isDirectory(r)){	//<- Directory should be listed before ordinary files
						return -1;
					}else if(!Files.isDirectory(l) && Files.isDirectory(r)){
						return 1;
					}else{
						return l.compareTo(r);
					}
				}).collect(Collectors.toList());
				for(Path p : files){
					DosFileAttributes attr = Files.readAttributes(p, DosFileAttributes.class);
					if(attr.isHidden() || attr.isSystem()){
						continue;
					}
					item.getChildren().add(new PathTreeItem(p, logger));
				}
			} catch (IOException e) {
//				logger.log(e);
				throw new IllegalArgumentException(e);
			}
		}
		
		
	}
	
//	private static final String MY_COMUTER = Msg.get(FileManagerController.class, "myComputer");
}

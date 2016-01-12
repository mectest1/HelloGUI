package com.mec.application.views;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.mec.resources.FileParser;
import com.mec.resources.Msg;
import com.mec.resources.MsgLogger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

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
	}
	
	
	
	@Override
	public void log(String msg) {
		statusInfo.appendText(msg);
	}



	private void initFileBrowser(){
//		TreeItem<Path> myComputer = new PathTreeItem(Paths.get(MY_COMUTER));
		TreeItem<Path> myComputer = new PathTreeItem(Paths.get(Msg.get(this, "rootPath")), this);
		myComputer.setExpanded(true);
		
		fileBrowser.setRoot(myComputer);
		fileBrowser.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		fileBrowser.setShowRoot(false);
		fileBrowser.setCellFactory(tv -> new PathTreeCell());
		
		ListChangeListener<Integer> listener = change -> {
			Optional<String> selectedFiles = fileBrowser.getSelectionModel()
					.getSelectedItems().stream()
					.map(p ->{
						try {
							return p.getValue().toAbsolutePath().toString();
						} catch (Exception e) {
							log(String.format(Msg.get(this, "exception.map"), p));
							log(e);
						}
						return null;
					})
					.reduce((r, s) -> r + FileParser.NEWLINE + s);
			selectedFiles.ifPresent( val -> statusInfo.setText(val));
		};
		fileBrowser.getSelectionModel().getSelectedIndices().addListener(listener);
	}
	
	private static final class PathTreeCell extends TreeCell<Path>{

		@Override
		protected void updateItem(Path item, boolean empty) {
			super.updateItem(item, empty);
			if(null == item || empty){
				this.setText(null);
				this.setGraphic(null);
			}else{
				String cellText = item.toString();
				if(null != item.getFileName()){
					cellText = item.getFileName().toString();
				}
				this.setText(cellText.toString());
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
				FileSystems.getDefault().getRootDirectories().forEach(rootDirectory -> item.getChildren().add(new PathTreeItem(rootDirectory, this.logger)));
			}else if(Files.isDirectory(currentPath)){
				try {
					Files.list(currentPath).forEach(pathItem -> item.getChildren().add(new PathTreeItem(pathItem, this.logger)));
				} catch (IOException e) {
					logger.log(e);
//					throw new IllegalArgumentException(e);
				}
			}
		}
		
		
	}
	
//	private static final String MY_COMUTER = Msg.get(FileManagerController.class, "myComputer");
}

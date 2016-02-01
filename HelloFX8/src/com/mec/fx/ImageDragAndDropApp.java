package com.mec.fx;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.List;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImageDragAndDropApp extends Application {
	ImageView imageView = new ImageView();
	Button clearBtn = new Button(Msg.get(this, "button.clear"));
	Scene scene;
	TextArea info = new TextArea();

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = getUIs();
		scene = new Scene(root);
		primaryStage.setScene(scene);
		
		//
		addDnDEventHandlers();
		
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
			
	}
	
	VBox getUIs(){
		Label msgLbl = new Label(Msg.get(this, "label.dnd"));
		
		//Set the size for the image view
		imageView.setFitWidth(300);
		imageView.setFitHeight(300);
		imageView.setSmooth(true);
		imageView.setPreserveRatio(true);
		
		//
		clearBtn.setOnAction(e -> imageView.setImage(null));
		
		VBox box = new VBox(10, msgLbl, imageView, clearBtn, info);
		return box;
	}
	
	void addDnDEventHandlers(){
		scene.setOnDragOver(this::dragOver);
		scene.setOnDragDropped(this::dragDropped);
	}
	
	void dragOver(DragEvent e){
		//You can drag an image, a URL or a file
		Dragboard dragboard = e.getDragboard();
		
		//
		if(dragboard.hasImage() || dragboard.hasFiles() || dragboard.hasUrl()){
			e.acceptTransferModes(TransferMode.ANY);
		}
		e.consume();
	}

	void dragDropped(DragEvent e){
		boolean isCompleted = false;
		
		//Transfer the data to the target
		Dragboard dragboard = e.getDragboard();
		
		//
		if(dragboard.hasImage()){
			this.transferImage(dragboard.getImage());
			isCompleted = true;
		}else if(dragboard.hasFiles()){
			isCompleted = transferImageFile(dragboard.getFiles());
		}else if(dragboard.hasUrl()){
			isCompleted = transferImageUrl(dragboard.getUrl());
		}else{
			info.appendText(Msg.get(this, "info.image"));
		}
		
		//Data transfer is successfull, or not
		e.setDropCompleted(isCompleted);
		e.consume();
	}
	
	boolean transferImageUrl(String url){
		try{
			imageView.setImage(new Image(url));
			return true;
		}catch(Exception e){
			info.appendText(expToStr(e));
		}
		return false;
	}
	
	
	
	
	void transferImage(Image image){
		imageView.setImage(image);
	}
	
	boolean transferImageFile(List<File> files){
		//look at the mime typeof all file
		//use the first file having the mime type as "image/xxx"
		try{
			for(File file : files){
				String mimeType = Files.probeContentType(file.toPath());
				if(null != mimeType && mimeType.startsWith(Msg.get(this, "mime.image"))){
					transferImageUrl(file.toURI().toURL().toExternalForm());
					return true;
				}
			}
		}catch(Exception e){
			info.appendText(expToStr(e));
		}
		
		return false;
	}
	
	public static String expToStr(Throwable e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		sw.append(Msg.get(SimplePressDragReleaseApp.class, "info.newline"));
		return sw.toString();
	}
}

package com.mec.fx;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FileChooserApp extends Application {
	private Stage primaryStage;
	private HTMLEditor resumeEditor;
	private final FileChooser fileDialog = new FileChooser();
	TextArea info = new TextArea();
	Path initialDirectory;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		resumeEditor = new HTMLEditor();
		resumeEditor.setPrefSize(600, 300);
		initialDirectory = Paths.get(Msg.get(this, "directory.initial"));
		if(Files.notExists(initialDirectory) || !Files.isDirectory(initialDirectory)){
			Files.createDirectory(initialDirectory);
		}
		
		//Filter only HTML editors
		fileDialog.getExtensionFilters().add(
				new ExtensionFilter(Msg.get(this, "extension.html.desc"), 
						Msg.get(this, "extension.html").split(Msg.get(this, "extension.delimiter"))
				));
		
		Button openBtn = new Button(Msg.get(this, "button.open"));
		openBtn.setOnAction(e -> openFile());
		
		//
		Button saveBtn = new Button(Msg.get(this, "button.save"));
		saveBtn.setOnAction(e -> saveFile());
		
		//
		Button closeBtn = new Button(Msg.get(this, "button.close"));
		closeBtn.setOnAction(e -> primaryStage.close());
		
		
		HBox buttons = new HBox(20, openBtn, saveBtn, closeBtn);
		buttons.setAlignment(Pos.CENTER_RIGHT);
		
		VBox root = new VBox(20, resumeEditor, buttons, info);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		//Mnemonic openMnemonic = new Mnemonic(openBtn, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		//<- Mnemonics cannot be registered with CONTROL_DOWN?
//		Mnemonic openMnemonic = new Mnemonic(openBtn, new KeyCodeCombination(KeyCode.O, KeyCombination.ALT_DOWN));
//		scene.addMnemonic(openMnemonic);
		
//		Mnemonic saveMnemonic = new Mnemonic(saveBtn, new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN));
//		scene.addMnemonic(saveMnemonic);
		
		//resumeEditor.requestFocus();	//<- NullPointerException will be thrown if it is called now
//		Platform.runLater(() -> resumeEditor.requestFocus());
		scene.getAccelerators().put(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN), ()-> openBtn.fire() );
		scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), ()-> saveBtn.fire() );
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

	void openFile(){
		fileDialog.setTitle(Msg.get(this, "open.title"));
		fileDialog.setInitialDirectory(initialDirectory.toFile());
		File file = fileDialog.showOpenDialog(primaryStage);
//		File file = fileDialog.showOpenDialog(null);	//<- do not block the primary stage
		if(null == file){
			return;
		}
		try {
			//Read the file and populate the HTML editor
			byte[] bytes = Files.readAllBytes(file.toPath());
//			resumeEditor.setHtmlText(Charset.defaultCharset().decode(ByteBuffer.wrap(bytes)).toString());
			resumeEditor.setHtmlText(new String(bytes));
		} catch (IOException e) {
			info.appendText(toString(e));
			info.appendText(Msg.get(this, "newline"));
		}
		
	}
	
	void saveFile(){
		fileDialog.setTitle(Msg.get(this, "save.title"));
		fileDialog.setInitialDirectory(initialDirectory.toFile());
		fileDialog.setInitialFileName(Msg.get(this, "save.initial"));
		File file = fileDialog.showSaveDialog(primaryStage);
		if(null == file){
			return;
		}else{
			//Write the HTML contents to the file. Overwrite the existing file
			String html = resumeEditor.getHtmlText();
			try {
				Files.write(file.toPath(), html.getBytes(), StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				info.appendText(toString(e));
				info.appendText(Msg.get(this, "newline"));
			}
		}
	}
	
	static String toString(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
}

































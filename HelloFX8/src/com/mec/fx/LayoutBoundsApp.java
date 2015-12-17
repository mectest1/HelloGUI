package com.mec.fx;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LayoutBoundsApp extends Application {

	private boolean grouped = false;
	@Override
	public void init() throws Exception {
		Map<String,String> params = getParameters().getNamed();
		String groupedStr = params.get(Msg.get(this, "param.grouped"));
		if(Boolean.parseBoolean(groupedStr)){
			grouped = true;
		}
				
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String buttonLabel = Msg.get(this, "button.label");
		
		Button b1 = new Button(buttonLabel);
		b1.setEffect(new DropShadow());
		
		Button b2 = new Button(buttonLabel);
		
		Button b3 = new Button(buttonLabel);
		b3.setEffect(new DropShadow());
		b3.setRotate(Msg.get(this, "button.rotate", Integer::parseInt, 0));
		
		Button b4 = new Button(buttonLabel);
		
		VBox root = new VBox();
		if(grouped){
			root.getChildren().addAll(new Group(b1), b2, new Group(b3), b4);
		}else{
			root.getChildren().addAll(b1, b2, b3, b4);
		}
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		int counter = 0;
//		Arrays.asList(b1, b2, b3, b4).stream().forEach(b -> {out.printf(Msg.get(LayoutBoundsApp.this, "output"), ++counter, b.getLayoutBounds()));
		for(Button button : new Button[]{b1, b2, b3, b4}){
			out.printf(Msg.get(this, "output"), ++counter, button.getLayoutBounds(), button.getBoundsInLocal());
		}
	}

//	private static 
//	private static int counter = 0;
	private static final PrintStream out = System.out;
}

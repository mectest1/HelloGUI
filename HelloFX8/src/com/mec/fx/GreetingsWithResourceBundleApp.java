package com.mec.fx;

import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GreetingsWithResourceBundleApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		URL view = getClass().getResource(Msg.get(this, "view"));
		Objects.requireNonNull(view);
		
		String rbBase = Msg.get(this, "bundle.base");
		FXMLLoader loader = new FXMLLoader(view, ResourceBundle.getBundle(rbBase));
		
		Label greetings = loader.load();
		
		ResourceBundle rbHi = ResourceBundle.getBundle(rbBase, new Locale("hi", "in"));
//		loader.setResources(rbHi);
//		Label greetings2 = loader.load();
		Label greetings2 = FXMLLoader.load(view, rbHi);
		
		ResourceBundle rbCN = ResourceBundle.getBundle(rbBase, Locale.SIMPLIFIED_CHINESE);
		Label greetings3 = FXMLLoader.load(view, rbCN);
		
		VBox root = new VBox(5, greetings, greetings2, greetings3);
		root.setPadding(new Insets(10));
		root.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
		Scene scene = new Scene(root);
		
		
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

}

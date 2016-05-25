package com.mec.application;
	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mec.application.beans.PluginService.LoadViewResult;
import com.mec.resources.Msg;
import com.mec.resources.Plugins;
import com.mec.resources.ViewFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	static Arguments args;

	@Override
	public void start(Stage primaryStage) {
		try {
			Optional<String> plugin = args.getPlugin();
			if(plugin.isPresent()){
				String pluginName = plugin.get();
				if(Plugins.listPlugins().contains(pluginName)){
					Plugins.load(pluginName);
					return;
				}
			}
			
			LoadViewResult<BorderPane, RootPaneController> loadResult = ViewFactory.loadViewDeluxe(Msg.get(this, "rootPane"));
			BorderPane root = loadResult.getViewRoot();
			RootPaneController rootController = loadResult.getController();
			Scene scene = new Scene(root,400,400);
			
			primaryStage.setScene(scene);
			primaryStage.setTitle(Msg.get(this, "title"));
			try {
				primaryStage.show();
				root.requestFocus();	//Clear focus for log msg panel;
			} catch (Exception e) {
				rootController.log(e);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Main.args = new Arguments(args); 
		launch(args);
	}
	
	
	
	static class Arguments{
		List<String> args;
		public Arguments(String[] args){
			if(null == args){
				this.args = new ArrayList<>();
			}else{
				this.args = Arrays.stream(args).collect(Collectors.toList());
			}
		}
		
		public Optional<String> getPlugin(){
			String argPlugin = Msg.get(Main.class, "arg.plugin");
			for(int i = 0; i < args.size(); ++i){
				if(argPlugin.equals(args.get(i))){
					if(i + 1 < args.size()){
						return Optional.of(args.get(i + 1));
					}
				}
			}
			return Optional.empty();
		}
	}
}

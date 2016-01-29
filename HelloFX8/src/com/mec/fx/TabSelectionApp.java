package com.mec.fx;

import java.util.stream.Stream;

import com.mec.fx.TabPaneApp.AddressTab;
import com.mec.fx.TabPaneApp.GeneralTab;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TabSelectionApp extends Application {
	TextArea info = new TextArea();

	@Override
	public void start(Stage primaryStage) throws Exception {
		TabPane tabPane = generateTabPane();
//		ImageView privacyIcon = TabPaneApp.getImage(Msg.get(TabPaneApp.class, "img.person"));
//		Tab generalTab = new GeneralTab(Msg.get(TabPaneApp.class, "tab.general"), privacyIcon);
//		
//		ImageView addressIcon = TabPaneApp.getImage(Msg.get(TabPaneApp.class, "img.address"));
//		Tab addressTab = new AddressTab(Msg.get(TabPaneApp.class, "tab.address"), addressIcon);
		
		
		//Add selection a change listener to Tabs
//		Stream.of(generalTab, addressTab).forEach(t -> t.setOnSelectionChanged(this::tabSelectedChagned));
		tabPane.getTabs().forEach(t -> t.setOnSelectionChanged(this::tabSelectedChagned));
		
		
//		TabPane tabPane = new TabPane();
		
		//Add a ChagneListener to the selection model
		tabPane.getSelectionModel().selectedItemProperty().addListener(this::selectionChanged);
		
		//
		VBox root = new VBox(10, tabPane, info);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}

	
	void selectionChanged(ObservableValue<? extends Tab> prop, Tab oldTab, Tab newTab){
		String oldTabText = null == oldTab ? Msg.get(this, "info.noSelection") : oldTab.getText();
		String newTabText = null == newTab ? Msg.get(this, "info.noSelection") : newTab.getText();
		info.appendText(String.format(Msg.get(this, "info.selectionChanged"), oldTabText, newTabText));
	}
	
	
	void tabSelectedChagned(Event e){
		Tab tab = (Tab) e.getSource();
		info.appendText(String.format(Msg.get(this, "info.tabSelected"), tab.getText(), tab.isSelected()));
	}

	static TabPane generateTabPane(){
		ImageView privacyIcon = TabPaneApp.getImage(Msg.get(TabPaneApp.class, "img.person"));
		Tab generalTab = new GeneralTab(Msg.get(TabPaneApp.class, "tab.general"), privacyIcon);
		
		ImageView addressIcon = TabPaneApp.getImage(Msg.get(TabPaneApp.class, "img.address"));
		Tab addressTab = new AddressTab(Msg.get(TabPaneApp.class, "tab.address"), addressIcon);
		
		TabPane tabPane = new TabPane();
		tabPane.getTabs().addAll(generalTab, addressTab);
		return tabPane;
	}
}

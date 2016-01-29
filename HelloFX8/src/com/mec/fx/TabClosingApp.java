package com.mec.fx;

import java.util.Stack;
import java.util.stream.Stream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TabClosingApp extends Application {
	TabPane tabPane = TabSelectionApp.generateTabPane();
	Stack<Tab> closedTabs = new Stack<>();
	//
	CheckBox allowClosingTabsFlag = new CheckBox(Msg.get(this, "check.closable"));
	Button restoreTabsBtn = new Button(Msg.get(this, "button.restore"));
	ChoiceBox<TabPane.TabClosingPolicy> tabClosingPolicyChoices = new ChoiceBox<>();
	
	//
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		tabPane.getTabs().forEach(t -> {
			t.setOnCloseRequest(this::tabClosingRequest);
			t.setOnClosed(this::tabClosed);
		});
		
		restoreTabsBtn.setOnAction(e -> restoreTabs());
		Stream.of(TabPane.TabClosingPolicy.values()).forEach(p -> tabClosingPolicyChoices.getItems().add(p));
		//Set the default value for the tab closing policy
		tabClosingPolicyChoices.setValue(tabPane.getTabClosingPolicy());
		tabPane.tabClosingPolicyProperty().bind(tabClosingPolicyChoices.valueProperty());
		
		//
		BorderPane root = new BorderPane();
		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		grid.addRow(0, allowClosingTabsFlag, restoreTabsBtn);
		grid.addRow(1, new Label(Msg.get(this, "label.closingPolicy")), tabClosingPolicyChoices);
		
		root.setTop(grid);
		root.setCenter(tabPane);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		//
	}
	
	void tabClosingRequest(Event e){
		if(!allowClosingTabsFlag.isSelected()){
			e.consume();
		}
	}
	
	void tabClosed(Event e){
		Tab tab = (Tab) e.getSource();
		closedTabs.push(tab);
	}
	
	void restoreTabs(){
		if(closedTabs.isEmpty()){
			return;
		}
		
		Tab closedTab = closedTabs.pop();
		tabPane.getTabs().add(closedTab);
	}
	
//	void closingPolicyChanged(ObservableValue<? extends TabPane.TabClosingPolicy> prop, 
//			TabPane.TabClosingPolicy oldVal,
//			TabPane.TabClosingPolicy newVal
//			){
//		tabPane.setTabClosingPolicy(newVal);
//	}

}

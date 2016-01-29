package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TabPaneApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		ImageView privacyIcon = getImage(Msg.get(this, "img.person"));
		Tab generalTab = new GeneralTab(Msg.get(this, "tab.general"), privacyIcon);
		
		ImageView addressIcon = getImage(Msg.get(this, "img.address"));
		Tab addressTab = new AddressTab(Msg.get(this, "tab.address"), addressIcon);
		
		//
		TabPane tabPane = new TabPane();
		tabPane.getTabs().addAll(generalTab, addressTab);
		
		ToggleButton floatBtn = new ToggleButton(Msg.get(this, "toggle.float"));
		floatBtn.setOnAction(e -> {
			if(floatBtn.isSelected()){
				tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);	//set TabPane to floating mode
			}else{
				tabPane.getStyleClass().remove(TabPane.STYLE_CLASS_FLOATING); //set TabPane in recessed mode
			}
		});
		HBox toggleBar= new HBox(floatBtn);
		toggleBar.setAlignment(Pos.CENTER_RIGHT);
		
		BorderPane root = new BorderPane(tabPane);
		root.setBottom(toggleBar);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	static class GeneralTab extends Tab{
		TextField firstNameFld = new TextField();
		TextField lastNameFld = new TextField();
		DatePicker dob = new DatePicker();
		
		public GeneralTab(String text, Node graphic){
			this.setText(text);
			this.setGraphic(graphic);
			init();
		}
		
		void init(){
			GridPane grid = new GridPane();
			grid.addRow(0, new Label(Msg.get(TabPaneApp.class, "label.firstName")), firstNameFld);
			grid.addRow(1, new Label(Msg.get(TabPaneApp.class, "label.lastName")), lastNameFld);
			grid.addRow(2, new Label(Msg.get(TabPaneApp.class, "label.dob")), dob);
			setContent(grid);
		}
	}
	
	
	
	static class AddressTab extends Tab{
		TextField streetFld = new TextField();
		TextField cityFld = new TextField();
		TextField stateFld = new TextField();
		TextField zipFld = new TextField();
		
		public AddressTab(String text, Node graphics){
			setText(text);
			setGraphic(graphics);
			init();
		}
		
		void init(){
			GridPane root = new GridPane();
			root.addRow(0, new Label(Msg.get(TabPaneApp.class, "label.street")), streetFld);
			root.addRow(1, new Label(Msg.get(TabPaneApp.class, "label.city")), cityFld);
			root.addRow(2, new Label(Msg.get(TabPaneApp.class, "label.state")), stateFld);
			root.addRow(3, new Label(Msg.get(TabPaneApp.class, "label.zip")), zipFld);
			setContent(root);
			
		}
	}
	
	static ImageView getImage(String imgUrl){
		return new ImageView(new Image(imgUrl));
	}

}

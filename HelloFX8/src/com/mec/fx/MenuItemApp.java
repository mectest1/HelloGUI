package com.mec.fx;

import java.util.function.BiConsumer;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MenuItemApp extends Application {
	ToggleGroup shapeGroup;
	
	static final int CANVAS_WIDTH = 200;
	static final int CANVAS_HEIGHT = 200;

	static final int CIRCLE_RADIUS = 180;
	static final int CENTER_OFFSET = 10;
	static final int ELLIPSE_RADIUS_A = 180;
	static final int ELLIPSE_RADIUS_B = 150;
	
	//A canvas to draw shapes
	Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
	Slider strokeWidthSlider;
	BooleanProperty paintStroke = new SimpleBooleanProperty(false);
	
	//Create three RadioMenuItems for shapes
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Menu fileMenu = getFileMenu();
		Menu optionsMenu = getOptionsMenu();
		
		//
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, optionsMenu);
		
		//Draw the default shape, which is a Rectangle;
		shapeGroup.selectToggle(shapeGroup.getToggles().get(0));
		this.draw();
		
		
		//
		BorderPane root = new BorderPane(canvas);
		root.setTop(menuBar);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	void draw(){
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		
		//Set drawing parameters;
		gc.setFill(Color.TAN);
		gc.setStroke(Color.RED);
		gc.setLineWidth(strokeWidthSlider.getValue());
		
		//
		ShapeType shapeType = getSelectedShape(shapeGroup);
		if(ShapeType.NONE == shapeType){
			clear();
		}else{
			shapeType.paint(gc, paintStroke.getValue());
		}
	}
	
	void clear(){
		canvas.getGraphicsContext2D().clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		shapeGroup.getToggles().forEach(item -> ((RadioMenuItem)item).setSelected(false));
	}
	Menu getFileMenu(){
		
		shapeGroup = new ToggleGroup();
		Menu fileMenu = new Menu(Msg.get(this, "menu.file"));
		
		
		Msg.getList(this, "item.shape").stream().forEach(shape -> {
			RadioMenuItem shapeItem = new RadioMenuItem(shape);
			int keyStartIndex = shape.indexOf(Msg.get(this, "item.shape.keyStart"));
			String shapeType = shape.substring(keyStartIndex + 1);
			shapeItem.setAccelerator(new KeyCharacterCombination(
					String.valueOf(shape.charAt(keyStartIndex + 1))
					, KeyCombination.ALT_DOWN));;
			shapeItem.setOnAction(e -> draw());
			shapeItem.setUserData(ShapeType.valueOf(shapeType.toUpperCase()));
			fileMenu.getItems().add(shapeItem);
			shapeGroup.getToggles().add(shapeItem);
		});
		
		
		MenuItem clearItem = new MenuItem(Msg.get(this, "item.clear"));
		clearItem.setOnAction(e -> clear());
		
		MenuItem exitItem = new MenuItem(Msg.get(this, "item.exit"));
		exitItem.setOnAction(e -> Platform.exit());
		
		//Add menu items to the File menu
		fileMenu.getItems().addAll(new SeparatorMenuItem(), 
				clearItem, new SeparatorMenuItem(),
				exitItem
				);
		return fileMenu;
	}
	
	Menu getOptionsMenu(){
		//Draw stroke by default
		RadioMenuItem strokeItem = new RadioMenuItem(Msg.get(this, "item.drawStroke"));
		paintStroke.bind(strokeItem.selectedProperty());
		strokeItem.setSelected(true);
		
		//Redraw the shape whendraw stroke option toggles
		strokeItem.setOnAction(e -> syncStroke());
		
		//Configure the slider
		strokeWidthSlider = new Slider(1, 10, 1);
		strokeWidthSlider.setShowTickLabels(true);
		strokeWidthSlider.setShowTickMarks(true);
		strokeWidthSlider.setMajorTickUnit(2);
		strokeWidthSlider.setSnapToPixel(true);
		strokeWidthSlider.valueProperty().addListener(this::strokeWidthChanged);

		CustomMenuItem strokeWidthItem = new CustomMenuItem(strokeWidthSlider);
		strokeWidthItem.disableProperty().bind(strokeItem.selectedProperty().not());
		strokeWidthItem.setHideOnClick(false);
		
		Menu options = new Menu(Msg.get(this, "menu.options"));
		options.getItems().addAll(strokeItem, strokeWidthItem);
		return options;
		
	}
	
	void strokeWidthChanged(ObservableValue<? extends Number> prop, Number oldVal, Number newVal){
		draw();
	}
	
	
	ShapeType getSelectedShape(ToggleGroup shapes){
		RadioMenuItem shapeItem = (RadioMenuItem) shapes.getSelectedToggle();
		if(null == shapeItem){
			return ShapeType.NONE;
		}
		return (ShapeType) shapeItem.getUserData();
	}

	void syncStroke(){
		//Enable/disable the slider
		
		//
		draw();
	}
	
	
	static enum ShapeType{
		 RECTANGLE((gc, stroke) -> {
			 gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
			 if(stroke){
				 gc.strokeRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
			 }
		 })
		,CIRCLE((gc, stroke) -> {
			gc.fillOval(CENTER_OFFSET, CENTER_OFFSET, CIRCLE_RADIUS, CIRCLE_RADIUS);
			if(stroke){
				gc.strokeOval(CENTER_OFFSET, CENTER_OFFSET, CIRCLE_RADIUS, CIRCLE_RADIUS);
			}
		})
		,ELLIPSE((gc, stroke) -> {
			gc.fillOval(CENTER_OFFSET, CENTER_OFFSET, ELLIPSE_RADIUS_A, ELLIPSE_RADIUS_B);
			if(stroke){
				gc.strokeOval(CENTER_OFFSET, CENTER_OFFSET, ELLIPSE_RADIUS_A, ELLIPSE_RADIUS_B);
			}
		})
		,NONE
		;
		
		ShapeType(BiConsumer<GraphicsContext, Boolean> paintAlgorithm){
			 this.paint = paintAlgorithm;
		}
		
		ShapeType(){
			
		}
		
		public void paint(GraphicsContext gc, Boolean stroke){
			paint.accept(gc, stroke);
		}
		private BiConsumer<GraphicsContext, Boolean> paint = (gc, stroke) -> {};
	}
}























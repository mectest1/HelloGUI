package com.mec.fx;

import java.util.function.Consumer;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ContextMenuApp extends Application {

	//A canvas to draw shapes
	Canvas canvas = new Canvas(CIRCLE_RADIUS, CIRCLE_RADIUS);
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Add mouse click event handler to the canvas to show the context menu
		canvas.setOnMouseClicked(this::showContextMenu);
		
		//
		BorderPane root = new BorderPane(canvas);
		root.setTop(new Label(Msg.get(this, "label.info")));
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	void showContextMenu(MouseEvent e){
		if(MouseButton.SECONDARY != e.getButton()){
			return;
		}
		getContextMenu().show(canvas, e.getScreenX(), e.getScreenY());
	}
	
	
	
	ContextMenu getContextMenu(){
		if(null != ctxMenu){
			return ctxMenu;
		}
		
		ctxMenu = new ContextMenu();
		Msg.getList(this, "item.shape").forEach(item -> {
			MenuItem menuItem = new MenuItem(item.trim());
			menuItem.setOnAction(e -> {
				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.clearRect(0, 0, CIRCLE_RADIUS, CIRCLE_RADIUS);
				gc.setFill(Color.TAN);
				ShapeType.valueOf(menuItem.getText().toUpperCase()).drawWith(gc);
			});
			ctxMenu.getItems().add(menuItem);
		});
		return ctxMenu;
	}
	
	ContextMenu ctxMenu;

	static final int CIRCLE_RADIUS = 200;
	static final Point2D ELLIPSE_CENTER = new Point2D(10, 40);
	static final Dimension2D ELLIPSE_RADIUS = new Dimension2D(180, 120);
	
	static enum ShapeType{
		 RECTANGLE(gc -> gc.fillRect(0, 0, CIRCLE_RADIUS, CIRCLE_RADIUS))
		,CIRCLE(gc -> gc.fillOval(0, 0, CIRCLE_RADIUS, CIRCLE_RADIUS))
		,ELLIPSE(gc -> gc.fillOval(ELLIPSE_CENTER.getX(), ELLIPSE_CENTER.getY(), ELLIPSE_RADIUS.getWidth(), ELLIPSE_RADIUS.getHeight()))
		;
		
		
		ShapeType(Consumer<GraphicsContext> drawAlgorithm){
			this.drawAlgorithm = drawAlgorithm;
		}
		
		public void drawWith(GraphicsContext gc){
			drawAlgorithm.accept(gc);
		}
		public void clearAndDrawWith(GraphicsContext gc){
			gc.clearRect(0, 0, CIRCLE_RADIUS, CIRCLE_RADIUS);
			gc.setFill(Color.TAN);
			drawAlgorithm.accept(gc);
		}
		
		private Consumer<GraphicsContext> drawAlgorithm;
		
	}
}

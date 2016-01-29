package com.mec.fx;

import java.util.stream.IntStream;

import com.mec.fx.ContextMenuApp.ShapeType;
import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ToolBarApp extends Application {

	//A canvas to draw shapes
	Canvas canvas = new Canvas(200, 200);
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		//Create TOolBar items
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Button rectBtn = new Button("", new Rectangle(0, 0, 16, 16));
		rectBtn.setOnAction(e -> ShapeType.RECTANGLE.clearAndDrawWith(gc));
		
		Button circleBtn = new Button("", new Circle(0, 0, 8));
		circleBtn.setOnAction(e -> ShapeType.CIRCLE.clearAndDrawWith(gc));
		
		Button ellipseBtn = new Button("", new Ellipse(8, 8, 8, 6));
//		ellipseBtn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		ellipseBtn.setOnAction(e -> ShapeType.ELLIPSE.clearAndDrawWith(gc));
		
		Button exitBtn = new Button(Msg.get(this, "button.exit"));
		exitBtn.setOnAction(e -> Platform.exit());
		
		ToolBar toolbar = new ToolBar();
		toolbar.getItems().addAll(rectBtn, circleBtn, ellipseBtn, exitBtn);
		//Set tooltips
		IntStream.range(0, toolbar.getItems().size()).forEach(index -> {
			((Button)toolbar.getItems().get(index)).setTooltip(new Tooltip(Msg.getList(this, "tooltip").get(index)));
		});;

//		toolbar.getItems().forEach(item -> {
//			Button button = (Button)item;
//		});
		
		BorderPane root = new BorderPane(canvas);
		root.setTop(toolbar);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
	}

}

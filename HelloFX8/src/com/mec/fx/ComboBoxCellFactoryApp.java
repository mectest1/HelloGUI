package com.mec.fx;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class ComboBoxCellFactoryApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Label shapeLbl = new Label(Msg.get(this, "label"));
		ComboBox<String> shapes = new ComboBox<>();

		Msg.getList(this, "shape").forEach(s -> shapes.getItems().add(s));
		
		//Set the cellFactory property
		shapes.setCellFactory(l -> new StringShapeCell()); 
		
		//Set the buttonCell property
		shapes.setButtonCell(new StringShapeCell());
		
		HBox root = new HBox(10, shapeLbl, shapes);
		root.setStyle(Msg.get(FlowPaneApp.class, "style"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
	}
	
	
	class StringShapeCell extends ListCell<String>{

		@Override
		protected void updateItem(String item, boolean empty) {
			//Net to call the super first
			super.updateItem(item, empty);
			
			
			//Set the text and graphic for the cell
			if(empty){
				setText(null);
				setGraphic(null);
			}else{
				setText(item);
				Shape shape = getShape(item);
				setGraphic(shape);
			}
		}
		
		Shape getShape(String shape){
			ShapeType type = ShapeType.valueOf(shape);
			switch(type){
				case Line:
					return new Line(0, 10, 20, 10);
				case Rectangle:
					return new Rectangle(0, 0, 20, 20);
				case Circle:
					return new Circle(20, 20, 10);
				default:
					return null;
			}
		}
		
	}
	static enum ShapeType{
		Line
		,Rectangle
		,Circle
	}
}

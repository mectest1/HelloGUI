package com.mec.fx;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

import com.mec.resources.Msg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CanvasApp extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		Canvas canvas = new Canvas(400, 100);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		//Set the line width and fill color
		gc.setLineWidth(2.0);
		gc.setFill(Color.RED);
		
		//Draw a rounded rectangle
		gc.strokeRoundRect(10, 10, 50, 50, 10, 10);
		
		//Fill and oval
		gc.fillOval(70, 10, 50, 20);
		
		//Draw text
		gc.strokeText(Msg.get(this, "canvas.text"), 10, 85);
		
		//Draw an Image
		Image image = new Image(Msg.get(this, "canvas.image"));
		gc.drawImage(image, 130, 10, 60, 80);
		
		//Write custom pixels to create a pattern
		writePixels(gc);
		
		//
		Pane root = new Pane();
		root.getChildren().add(canvas);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle(Msg.get(this, "title"));
		primaryStage.show();
		
		
		
	}

	
	void writePixels(GraphicsContext gc){
		byte[] pixels = getPixelsData();
		PixelWriter pw = gc.getPixelWriter();
		
		//Out data is in the BYET_RGB format
		PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();
		
		int spacing = 5;
		int imageWidth = 200;
		int imageHeight = 100;
		
		
		//Roughly compute the number of rows and column
		int rows = imageHeight / (REGION_HEIGHT + spacing);
		int columns =imageWidth / (REGION_WIDTH + spacing);
		
		
		//Write the pixels to the canvas
		IntStream.range(0, rows).forEach(y -> {
			IntStream.range(0, columns).forEach(x -> {
				int xPos = 200 + x * (REGION_WIDTH + spacing);
				int yPos = y * (REGION_HEIGHT + spacing);
				
				pw.setPixels(xPos, yPos, REGION_WIDTH, REGION_HEIGHT, pixelFormat, pixels, 0, REGION_WIDTH *3);
			});
		});;
	}
	
	
	byte[] getPixelsData(){
		//Each pixels in the w X h region will take 3 bytes
		byte[] pixels = new byte[REGION_WIDTH * REGION_HEIGHT * 3];
		
		//Height to width ratio
		double ratio = 1.0 * REGION_HEIGHT/REGION_WIDTH;
		
		//Generate pixel data
		IntStream.range(0, REGION_HEIGHT).forEach(y -> {
			IntStream.range(0, REGION_WIDTH).forEach(x -> {
				int i = y * REGION_WIDTH * 3 + 3 * x;
				
					if(x <= y/ratio){
						pixels[i] = -1;		//red -1 means 255 (-1 & 0xff = 255)
						pixels[i+1] = 0;	//green = 0
						pixels[i+2] = 0;	//blue = 0
					}else{
						pixels[i] = 0; 		//red = 0;
						pixels[i+1] = -1;	//green: 255
						pixels[i+2] = 0;	//blue = 0
					}
				
			});
		});
		
		return pixels;
	}
	
	
	
	private static final int REGION_WIDTH=20;
	private static final int REGION_HEIGHT=20;
	
}

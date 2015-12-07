package com.mec.duke.tut;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import com.mec.duke.tut.view.ClientController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Client extends Application{
	
//	private Client(String host, int port){
//		
//	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/mec/duke/tut/view/ClientView.fxml"));
		
		AnchorPane pane = (AnchorPane) loader.load();
		Scene scene = new Scene(pane);
		primaryStage.setScene(scene);
		
		
		controller = loader.getController();
		controller.init();
		controller.setClient(this);
		
		primaryStage.show();
	}


	@Override
	public void init() throws Exception{
		try{
			String host = "localhost";
			int port = 10019;
			socket = new Socket(host,  port);
			
			outs = new DataOutputStream(socket.getOutputStream());
			ins = new DataInputStream(socket.getInputStream());
			
			receiveThread = new ClientThread(this);
			new Thread(receiveThread).start();
//			init();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public static void startNew(String host, int port){
		try {
//			Client client = new Client(host, port);
			
		} catch (Exception e) {
			out.println("Create client failed");
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
//		client.
		receiveThread.stop();
		socket.close();
	}
	public void receiveMessage(){
		try {
			String msgText = ins.readUTF();
//			return msg;
//			String msgText = client.receiveMessage();
			if(null == msgText || msgText.isEmpty()){
				return;
			}
//			messageHistory.setText(String.format("%s\n%s", messageHistory.getText(), msgText));
			controller.appendMessage(msgText);
		} catch (Exception e) {
//			e.printStackTrace();
			out.println("receive message failed");
			receiveThread.stop();
		}
	}
	
	public void sendMessage(String msgText){
		if(null == msgText || msgText.isEmpty()){
			return;
		}
		try {
			outs.writeUTF(msgText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	private Client client;
	private ClientController controller;
	private ClientThread receiveThread;
	private DataOutputStream outs;
	private DataInputStream ins;
	private Socket socket;
	private static final PrintStream out = System.out;
	
}

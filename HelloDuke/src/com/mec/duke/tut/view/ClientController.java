package com.mec.duke.tut.view;

import com.mec.duke.tut.Client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientController {

	@FXML
	private TextArea messageHistory;
	
	@FXML
	private TextField message;
	
	
	
	public ClientController() {
//		message.requestFocus();
	}


	//	@FXML
	private void sendMessage(){
		String msgText = message.getText();
		if(null != msgText && !msgText.isEmpty()){
			client.sendMessage(msgText);
		}
		message.clear();
	}
	
	
//	@FXML
//	public void receiveMessage(){
//		String msgText = client.receiveMessage();
//		if(null == msgText || msgText.isEmpty()){
//			return;
//		}
//		messageHistory.setText(String.format("%s\n%s", messageHistory.getText(), msgText));
//	}
	
	public void appendMessage(String msgText){
		String oldContent = messageHistory.getText();
//		if(null == oldContent || oldContent.isEmpty()){
//			return;
//		}
		String newContent = String.format("%s\n%s", oldContent, msgText);
		messageHistory.setText(newContent);
//		System.out.printf("Scroll Left = %s, Scroll Top = %s\n", messageHistory.getScrollLeft(), messageHistory.getScrollTop());
		messageHistory.positionCaret(newContent.length());
	}
	
	public void setClient(Client client){
		this.client = client;
	}
	
	@FXML
	private void onSend(){
		sendMessage();
	}
	
	public void init(){
		message.requestFocus();
	}
	
	private Client client;
	
}

package com.mec.application.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import com.mec.resources.Msg;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class BaseCodeDecoderController {

	@FXML
	private TextArea encodedText;
	
	@FXML
	private TextArea decodedText;
	
	
	@FXML
	private void onDecodeBase64Gzip(){
		String encodedStr = encodedText.getText();
		String decodedStr = decodeStr(encodedStr, true).get();
		decodedText.setText(decodedStr);
//		decodedText.positionCaret(2);
	}
	
	@FXML
	private void onDecodeBase64(){
		String encodedStr = encodedText.getText();
		
		String decodedStr = decodeStr(encodedStr, false).get();
		decodedText.setText(decodedStr);
//		decodedText.positionCaret(0);
	}
	
	
	public static Optional<String> decodeStr(String encodedStr, boolean isGZipped){
		if(null == encodedStr || encodedStr.isEmpty()){
			return Optional.empty();
		}
		String decodedStr = null;
		try{
			byte[] decodedBytes = Base64.decode(encodedStr.getBytes());
//			decodedStr = new String(decodedBytes, "UTF-8");
			InputStream is = new ByteArrayInputStream(decodedBytes);
			
			if(isGZipped){
				is = new GZIPInputStream(is);
			}
			ObjectInputStream objIn = new ObjectInputStream(is);
			Object obj = objIn.readObject();
			decodedStr = (String) obj;
			
			is.close();
			objIn.close();
		}catch(Base64DecodingException | UnsupportedEncodingException | ClassNotFoundException e){
			decodedStr = String.format(ERROR_OUTPUT, e.getClass().getName(), e.getMessage());	//<- Should be generalized;
		}catch(IOException e){
			decodedStr = String.format(ERROR_OUTPUT, e.getClass().getName(), e.getMessage());	//<- Should be generalized;
		}//Base64DecodingException
		return Optional.of(decodedStr);
	}
	
//	static{
//		com.sun.org.apache.xml.internal.security.Init.init();	//?
//	}
	private static String ERROR_OUTPUT = Msg.get(BaseCodeDecoderController.class, "decode.error.message");
}

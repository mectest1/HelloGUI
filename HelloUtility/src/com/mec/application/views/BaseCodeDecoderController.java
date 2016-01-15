package com.mec.application.views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.mec.resources.Msg;
import com.mec.resources.ViewFactory;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class BaseCodeDecoderController {

	@FXML
	private TextArea encodedText;
	
	@FXML
	private TextArea decodedText;
	
	@FXML
	private Button decodeGzippedButton;
	
	@FXML
	private Button decodeBase64Button;
	
	@FXML
	private Button encodeGzippedButton;
	
	@FXML
	private Button encodeBase64Button;
	
	@FXML
	private Button switchButton;
	
	
	
//	@FXML
//	private Button clearButton;
	
	@FXML
	private void onDecodeBase64Gzip(){
		decodeBase64(true);
	}
	
	@FXML
	private void onDecodeBase64(){
		decodeBase64(false);
	}
	
	@FXML
	private void onEncodeBase64Gzip(){
		encodeBase64(true);
	}
	
	@FXML
	private void onEncodBase64(){
		encodeBase64(false);
	}
	
	@FXML
	private void onExtraBase64ToStringUTF8(){
		decodeBase64ToStringDirectly(Msg.get(this, "charset.default"));
	}
	
	@FXML
	private void onExtraBase64ToString(){
		Optional<String> charSetStr = ViewFactory.inputText(Msg.get(this, "charset.default"), Msg.get(this, "charset.new.title"), Msg.get(this, "charset.new.header")); 
		decodeBase64ToStringDirectly(charSetStr.get());
	}
	
	@FXML
	private void initialize(){
		encodedText.setPromptText(Msg.get(this, "encodedText.prompt"));
		decodedText.setPromptText(Msg.get(this, "decodedText.prompt"));
		decodeGzippedButton.setText(Msg.get(this, "decodeGzippedButton.label"));
		decodeBase64Button.setText(Msg.get(this, "decodeBase64Button.label"));
		encodeGzippedButton.setText(Msg.get(this, "encodeGzippedButton.label"));
		encodeBase64Button.setText(Msg.get(this, "encodeBase64Button.label"));
		switchButton.setText(Msg.get(this, "switchButton.label"));
//		clearButton.setText(Msg.get(this, "clearButton.label"));
		
		decodeGzippedButton.disableProperty().bind(encodedText.textProperty().isEmpty());
		decodeBase64Button.disableProperty().bind(encodedText.textProperty().isEmpty());
		
		encodeGzippedButton.disableProperty().bind(decodedText.textProperty().isEmpty());
		encodeBase64Button.disableProperty().bind(decodedText.textProperty().isEmpty());
		
		switchButton.disableProperty().bind(Bindings.and(decodedText.textProperty().isEmpty(), encodedText.textProperty().isEmpty()));
//		clearButton.disableProperty().bind(Bindings.and(decodedText.textProperty().isEmpty(), encodedText.textProperty().isEmpty()));
	}
	
	@FXML
	private void onSwitch(){
		onPrepare(null);
		String decodedStr = decodedText.getText();
		decodedText.setText(encodedText.getText());
		encodedText.setText(decodedStr);
	}
	
//	@FXML
//	private void onClear(){
//		boolean dFocused = false;
//		if(decodedText.isFocused()){
//			dFocused = true;
//		}
//		decodedText.clear();
//		encodedText.clear();
//		
//		if(dFocused){
//			decodedText.requestFocus();
//		}else{
//			encodedText.requestFocus();
//		}
//	}
	private void decodeBase64(boolean isGZipped){
		String encodedStr = encodedText.getText();
		
		
		onPrepare(decodedText);
		Optional<String> decodedStr;
		try {
			decodedStr = decodeBase64(encodedStr, isGZipped);
			decodedStr.ifPresent(str -> decodedText.setText(str));
		} catch (Exception e) {
			String errorInfo = String.format(ERROR_OUTPUT, e.getClass().getName(), e.getMessage());	//<- Should be generalized;
//			decodedText.setText(errorInfo);
			onError(decodedText, errorInfo);
		}
	}
	
	private void decodeBase64ToStringDirectly(String charsetName){
		String encodedStr = encodedText.getText();
		
		
		onPrepare(decodedText);
		Optional<String> decodedStr;
		try {
			Charset charset = Charset.forName(charsetName);
			decodedStr = decodeBase64ToStringDirectly(encodedStr, charset);
			decodedStr.ifPresent(str -> decodedText.setText(str));
		} catch (Exception e) {
			String errorInfo = String.format(ERROR_OUTPUT, e.getClass().getName(), e.getMessage());	//<- Should be generalized;
//			decodedText.setText(errorInfo);
			onError(decodedText, errorInfo);
		}
		
	}
	
	private void encodeBase64(boolean isGZipped){
		String decodedStr = decodedText.getText();
		
//		encodedText.clear();
		onPrepare(encodedText);
		Optional<String> encodedStr;
		try{
			encodedStr = encodeBase64(decodedStr, isGZipped);
			encodedStr.ifPresent(str -> encodedText.setText(str));
		}catch(Exception e){
			String errorInfo = String.format(ERROR_OUTPUT, e.getClass().getName(), e.getMessage());
//			encodedText.setText(errorInfo);
			onError(encodedText, errorInfo);
		}
	}
	
	private void onPrepare(TextArea textArea){
		decodedText.getStyleClass().remove(ERROR_STYLE);
		encodedText.getStyleClass().remove(ERROR_STYLE);
		if(null != textArea){
			textArea.clear();
		}
	}
	private void onError(TextArea textArea, String errorInfo){
		textArea.getStyleClass().add(ERROR_STYLE);
		textArea.setText(errorInfo);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Optional<T> decodeBase64(String encodedStr, boolean isGZipped) throws IllegalArgumentException, ClassNotFoundException, IOException, ClassCastException{
		if(null == encodedStr || encodedStr.isEmpty()){
			return Optional.empty();
		}
		T decodedResult = null;
		byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedStr.getBytes());	//Base64.getMimeDecoder()
		InputStream is = new ByteArrayInputStream(decodedBytes);
		
		if(isGZipped){
			is = new GZIPInputStream(is);
		}
		ObjectInputStream objIn = new ObjectInputStream(is);
		Object obj = objIn.readObject();
		decodedResult = (T) obj;
		
		is.close();
		objIn.close();
		return Optional.of(decodedResult);
	}
	
	private static Optional<String> decodeBase64ToStringDirectly(String encodedStr, Charset charSet){
		if(null == encodedStr){
			return Optional.empty();
		}
		byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedStr.getBytes());
		String retval = new String(decodedBytes, charSet);
		return Optional.of(retval);
	}
	
	public static Optional<String> encodeBase64(Object decodedObj, boolean isGZipped) throws IOException{
		if(null == decodedObj){
			return Optional.empty();
		}
		String encodedStr = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStream os = bos;
		if(isGZipped){
			os = new GZIPOutputStream(os); 	//<----Q: Bytes are not written into GZIPOutputStream, but why?
											//A: Because the ObjectOutputStream is not closed ASAP. (flush() would not work)
		}
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(decodedObj);
		oos.close();						//<- This will fix the "GZIPOutputStream not updated" problem; 
		
		encodedStr = Base64.getMimeEncoder().encodeToString(bos.toByteArray());
		
		return Optional.of(encodedStr);
	}
	
	private static final String ERROR_OUTPUT = Msg.get(BaseCodeDecoderController.class, "decode.error.message");
	private static final String ERROR_STYLE = Msg.get(BaseCodeDecoderController.class, "errorStyle"); 
}

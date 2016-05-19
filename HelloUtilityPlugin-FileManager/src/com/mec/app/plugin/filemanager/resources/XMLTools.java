package com.mec.app.plugin.filemanager.resources;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLTools {

	
	private static final XMLTools instance = new XMLTools();
	private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private XMLTools(){
		dbf = DocumentBuilderFactory.newInstance();
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.log(e);
		}
	}
	
	
	public static XMLTools inst(){
		return instance;
	}
	
	
	public String nodeToStr(Node doc){
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			t.setOutputProperty(OutputKeys.STANDALONE, "yes");
			
			StringWriter sw = new StringWriter();
			Source xmlSource = new DOMSource(doc);
			Result outputTarget = new StreamResult(sw);
			t.transform(xmlSource, outputTarget);
			
			return sw.toString();
		} catch (TransformerException e) {
			logger.log(e);
			return e.getMessage();
		}
	}
	
	public Document fileToDom(Path xmlFile){
		Document retval = null;
		try {
			retval = db.parse(xmlFile.toFile());
		} catch (SAXException | IOException e) {
			logger.log(e);
		}
		return retval;
	}
	
	public void setLogger(MsgLogger logger) {
		this.logger = logger;
	}



	private MsgLogger logger = MsgLogger.defaultLogger(); 
}

package com.mec.duke;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLAppTest {

	@Test
	public void testDOMParse() throws Exception{
		final String catalogInfo = "resources/xml/catalog.xml";
		Path catalog = Paths.get(catalogInfo);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setErrorHandler(new ErrorHandler() {
			@Override
			public void warning(SAXParseException exception) throws SAXException {
				exception.printStackTrace(out);
			}
			
			@Override
			public void fatalError(SAXParseException exception) throws SAXException {
				exception.printStackTrace(out);
			}
			
			@Override
			public void error(SAXParseException exception) throws SAXException {
				exception.printStackTrace(out);
			}
		});
		
		
		Document doc = db.parse(catalog.toFile());
		
		out.println(doc);
	}

	
	
	private static final PrintStream out = System.out;
}

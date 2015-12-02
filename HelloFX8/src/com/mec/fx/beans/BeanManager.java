package com.mec.fx.beans;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class BeanManager {

	
	
	public static <T> void saveToXML(T obj, File xmlFile ) throws Exception{
		JAXBContext context = JAXBContext.newInstance(obj.getClass());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		m.marshal(obj, xmlFile);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T loadFromXML(Class<? extends T> clazz, File xmlFile) throws Exception{
		JAXBContext context = JAXBContext.newInstance(clazz);
		Unmarshaller um = context.createUnmarshaller();
		
		return (T) um.unmarshal(xmlFile);
	}
}

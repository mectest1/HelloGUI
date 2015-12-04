package com.mec.fx.beans;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate>{

	@Override
	public LocalDate unmarshal(String dateStr) throws Exception {
		return DateUtil.parse(dateStr);
	}

	@Override
	public String marshal(LocalDate date) throws Exception {
		return DateUtil.format(date);
	}

	
}

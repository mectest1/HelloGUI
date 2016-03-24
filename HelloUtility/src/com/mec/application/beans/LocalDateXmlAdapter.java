package com.mec.application.beans;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.mec.resources.DateUtil;

public class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate>{

	@Override
	public LocalDate unmarshal(String dateStr) throws Exception {
		return DateUtil.parse(dateStr).get();
	}

	@Override
	public String marshal(LocalDate date) throws Exception {
		return DateUtil.format(date).get();
	}

	
}

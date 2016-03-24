package com.mec.application.beans;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.mec.resources.DateUtil;

public class LocalDateTimeXmlAdapter extends XmlAdapter<String, LocalDateTime>{

	@Override
	public LocalDateTime unmarshal(String dateStr) throws Exception {
		return DateUtil.parseLocalDateTime(dateStr).get();
	}

	@Override
	public String marshal(LocalDateTime date) throws Exception {
		return DateUtil.formatLocalDateTime(date).get();
	}

	
}

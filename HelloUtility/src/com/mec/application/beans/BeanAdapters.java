package com.mec.application.beans;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.mec.resources.DateUtil;

public class BeanAdapters {

	
	public static class PathXmlAdapter extends XmlAdapter<String, Path> {

		@Override
		public Path unmarshal(String v) throws Exception {
			return Paths.get(v);
		}

		@Override
		public String marshal(Path v) throws Exception {
			return v.toString();
		}

		
		
	}
	
	

	
	
	
	
	public static class LocalDateTimeXmlAdapter extends XmlAdapter<String, LocalDateTime>{

		@Override
		public LocalDateTime unmarshal(String dateStr) throws Exception {
			return DateUtil.parseLocalDateTime(dateStr).get();
		}

		@Override
		public String marshal(LocalDateTime date) throws Exception {
			return DateUtil.formatLocalDateTime(date).get();
		}

		
	}
	
	public static class LocalDateXmlAdapter extends XmlAdapter<String, LocalDate>{

		@Override
		public LocalDate unmarshal(String dateStr) throws Exception {
			return DateUtil.parseLocalDate(dateStr).get();
		}

		@Override
		public String marshal(LocalDate date) throws Exception {
			return DateUtil.formatLocalDate(date).get();
		}

		
	}

}

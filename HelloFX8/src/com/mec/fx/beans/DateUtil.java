package com.mec.fx.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

	public static final String DATE_PATTERN = "dd.MM.yyyy";
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	
	public static String format(LocalDate date){
		if(null == date){
			return null;
		}
		return DATE_FORMATTER.format(date);
	}
	
	public static LocalDate parse(String dateString){
		try{
			return DATE_FORMATTER.parse(dateString, LocalDate::from);
		}catch(DateTimeParseException e){
			return null;
		}
	}
	
	public static boolean validDate(String dateString){
		return null != parse(dateString);
	}
}

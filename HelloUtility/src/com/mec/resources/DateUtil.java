package com.mec.resources;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateUtil {

//	public static final String DATE_PATTERN = Msg.get(DateUtil.class, "date.pattern");
//	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	static{
		Optional<String> datePattern = Msg.getOptional(DateUtil.class, "date.pattern");
		datePattern.ifPresent(p -> DATE_FORMATTER = DateTimeFormatter.ofPattern(p));
	}
	
	public static Optional<String> format(LocalDate date){
		if(null == date){
			return Optional.empty();
		}
		return Optional.of(DATE_FORMATTER.format(date));
	}
	
	public static Optional<LocalDate> parse(String dateString){
		try{
			return Optional.of(DATE_FORMATTER.parse(dateString, LocalDate::from));
		}catch(DateTimeParseException e){
			return Optional.empty();
		}
	}
	
	public static boolean validDate(String dateString){
//		return null != parse(dateString);
		return parse(dateString).isPresent();
	}
}

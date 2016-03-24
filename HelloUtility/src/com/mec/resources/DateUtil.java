package com.mec.resources;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Optional;

public class DateUtil {

//	public static final String DATE_PATTERN = Msg.get(DateUtil.class, "date.pattern");
//	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
	private static DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	public static DateTimeFormatter FILENAME_DAET_FORMATTER;
	
	
	static{
		Optional<String> datePattern = Msg.getOptional(DateUtil.class, "date.pattern.default");
		datePattern.ifPresent(p -> DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern(p));
		
		FILENAME_DAET_FORMATTER = new DateTimeFormatterBuilder()
				.parseCaseInsensitive()
				.append(DateTimeFormatter.ISO_LOCAL_DATE)
				.appendLiteral('T')
				.appendValue(ChronoField.HOUR_OF_DAY, 2)
				.appendValue(ChronoField.MINUTE_OF_HOUR, 2)
				.appendValue(ChronoField.SECOND_OF_MINUTE, 2)
//				.optionalStart()
//				.appendValue(ChronoField.NANO_OF_SECOND, 4)
//				.optionalEnd()
				.toFormatter();
	}
	
	public static Optional<String> formatLocalDate(LocalDate date){
		if(null == date){
			return Optional.empty();
		}
		return Optional.of(DEFAULT_DATE_FORMATTER.format(date));
	}
	
	public static Optional<LocalDate> parseLocalDate(String dateString){
		try{
			return Optional.of(DEFAULT_DATE_FORMATTER.parse(dateString, LocalDate::from));
		}catch(DateTimeParseException e){
			return Optional.empty();
		}
	}
	public static Optional<String> formatLocalDateTime(LocalDateTime date){
		if(null == date){
			return Optional.empty();
		}
		return Optional.of(DEFAULT_DATE_FORMATTER.format(date));
	}
	
	public static Optional<LocalDateTime> parseLocalDateTime(String dateString){
		try{
			return Optional.of(DEFAULT_DATE_FORMATTER.parse(dateString, LocalDateTime::from));
		}catch(DateTimeParseException e){
			return Optional.empty();
		}
	}
	
	public static boolean validDate(String dateString){
//		return null != parse(dateString);
		return parseLocalDate(dateString).isPresent();
	}
	
	
	//-----------------------------------
	public static String getPathNameForNow(){
		return FILENAME_DAET_FORMATTER.format(LocalDateTime.now());
	}
	
	//-----------------------------------
}

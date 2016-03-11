package com.mec.duke;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

public class TimeAPITest {

	@Ignore
	@Test
	public void testTimeAPI() {
//		LocalTime now = LocalTime.now();
		LocalDateTime now = LocalDateTime.now();
		getNativeSupportedFormatter().forEach(f -> {
			try{
				out.printf("%s\n", f.format(now));
			}catch(Exception e){
				out.printf("%s - %s\n", e.getClass().getName(), e.getMessage());
			}
		});
	}
	@Ignore
	@Test
	public void testTimeAPIWithFormatterName() {
//		LocalTime now = LocalTime.now();
		LocalDateTime now = LocalDateTime.now();
		Arrays.stream(NativeDateTimeFormatter.values()).forEach(f -> {
			try{
				out.printf("%s - %s\n", f.name(), f.getFormatter().format(now));
			}catch(Exception e){
				out.printf("%s - %s: %s\n", f.name(), e.getClass().getSimpleName(), e.getMessage());
			}
		});
	}
	
	@Test
	public void testOldCalendar(){
		Calendar now = Calendar.getInstance();
		
		now.set(Calendar.MONTH, Calendar.FEBRUARY);	//29
		out.println(now.getActualMaximum(Calendar.DAY_OF_MONTH));
		now.set(Calendar.MONTH, Calendar.APRIL);	//30
		out.println(now.getActualMaximum(Calendar.DAY_OF_MONTH));
		now.set(Calendar.MONTH, Calendar.MARCH);	//31
		out.println(now.getActualMaximum(Calendar.DAY_OF_MONTH));
	}
	
	
	
	static enum NativeDateTimeFormatter{
		BASIC_ISO_DATE(DateTimeFormatter.BASIC_ISO_DATE)
		 ,ISO_DATE(DateTimeFormatter.ISO_DATE)
		 ,ISO_DATE_TIME(DateTimeFormatter.ISO_DATE_TIME)
		 ,ISO_INSTANT(DateTimeFormatter.ISO_INSTANT)
		 ,ISO_LOCAL_DATE(DateTimeFormatter.ISO_LOCAL_DATE)
		 ,ISO_LOCAL_DATE_TIME(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
		 ,ISO_LOCAL_TIME(DateTimeFormatter.ISO_LOCAL_TIME)
		 ,ISO_OFFSET_DATE(DateTimeFormatter.ISO_OFFSET_DATE)
		 ,ISO_OFFSET_DATE_TIME(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
		 ,ISO_OFFSET_TIME(DateTimeFormatter.ISO_OFFSET_TIME)
		 ,ISO_ORDINAL_DATE(DateTimeFormatter.ISO_ORDINAL_DATE)
		 ,ISO_TIME(DateTimeFormatter.ISO_TIME)
		 ,ISO_WEEK_DATE(DateTimeFormatter.ISO_WEEK_DATE)
		 ,ISO_ZONED_DATE_TIME(DateTimeFormatter.ISO_ZONED_DATE_TIME)
		 ,RFC_1123_DATE_TIME(DateTimeFormatter.RFC_1123_DATE_TIME)
		 ;
		
		NativeDateTimeFormatter(DateTimeFormatter formatter){
			this.formatter = formatter;
		}
		
		DateTimeFormatter getFormatter(){
			return formatter;
		}
		
		DateTimeFormatter formatter;
	}
	
	static Stream<DateTimeFormatter> getNativeSupportedFormatter(){
		return Stream.of(
				 DateTimeFormatter.BASIC_ISO_DATE
				,DateTimeFormatter.ISO_DATE
				,DateTimeFormatter.ISO_DATE_TIME
				,DateTimeFormatter.ISO_INSTANT
				,DateTimeFormatter.ISO_LOCAL_DATE
				,DateTimeFormatter.ISO_LOCAL_DATE_TIME
				,DateTimeFormatter.ISO_LOCAL_TIME
				,DateTimeFormatter.ISO_OFFSET_DATE
				,DateTimeFormatter.ISO_OFFSET_DATE_TIME
				,DateTimeFormatter.ISO_OFFSET_TIME
				,DateTimeFormatter.ISO_ORDINAL_DATE
				,DateTimeFormatter.ISO_TIME
				,DateTimeFormatter.ISO_WEEK_DATE
				,DateTimeFormatter.ISO_ZONED_DATE_TIME
				,DateTimeFormatter.RFC_1123_DATE_TIME
				);
	}
	static final PrintStream out = System.out;

}

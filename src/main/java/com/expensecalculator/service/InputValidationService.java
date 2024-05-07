package com.expensecalculator.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidationService {
	private static final String DIGIT_REGEX = "^[0-9]{1,}$";
	private static final Pattern digitPattern = Pattern.compile(DIGIT_REGEX);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	public static boolean isValidDigit(String input) {
        Matcher matcher = digitPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        } 
        return false;
	}
	
	public static Timestamp getTimestamp(String dateTimeString) {
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		return Timestamp.valueOf(dateTime);
	}
	
	public static long getNextMonthTimestamp(String startDateStr, String inputDateStr) throws ParseException {
        Date inputDate = dateFormat.parse(inputDateStr);
        Date startDate = dateFormat.parse(startDateStr);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH); 
        
        calendar.setTime(inputDate);
        calendar.add(Calendar.MONTH, 1);
        int date = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)<startDay? calendar.getActualMaximum(Calendar.DAY_OF_MONTH):startDay;
        calendar.set(Calendar.DAY_OF_MONTH, date);
        

        return calendar.getTimeInMillis();
    }
}

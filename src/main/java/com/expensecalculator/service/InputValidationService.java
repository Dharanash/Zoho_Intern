package com.expensecalculator.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidationService {
	private static final String DIGIT_REGEX = "^[0-9]{1,}$";
	private static final Pattern digitPattern = Pattern.compile(DIGIT_REGEX);
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static boolean isValidDigit(String input) {
        Matcher matcher = digitPattern.matcher(input);
        if (matcher.matches()) {
            return true;
        } 
        return false;
	}
	
	public static Timestamp getTimestamp(String date, String time) {
		return Timestamp.valueOf(date+" "+time+":00");
	}
}

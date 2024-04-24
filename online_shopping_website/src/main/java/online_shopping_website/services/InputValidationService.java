package online_shopping_website.services;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidationService {
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_%+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}$";
	private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{4,}$";
	private static final Pattern passwordPattern = Pattern.compile(PASSWORD_REGEX);
	
	public static boolean isValidEmail(String email) {
            Matcher matcher = emailPattern.matcher(email);
            if (matcher.matches()) {
                return true;
            } 
            return false;
    }
	
	public static boolean isValidPassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        if (matcher.matches()) {
            return true;
        } 
        return false;
}
}

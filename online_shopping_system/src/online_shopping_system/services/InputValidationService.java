package online_shopping_system.services;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidationService {

	private static final Scanner sc = new Scanner(System.in);
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
	
	public static String getStringInput(String message) {
		System.out.print(message);
		String input = sc.nextLine();
		return input;
	}

	public static int getIntegerInput(String message) {
		 while (true) {
	            try {
	                System.out.print(message);
	                int input = Integer.parseInt(sc.nextLine());
	                if (input < 0) {
	                    System.out.println("Input can't be negative.");
	                    continue;
	                }
	                return input;
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid input. Please enter an integer.\n");
	            }
	        }
	}

	public static double getDoubleInput(String message) {
		while (true) {
            try {
                System.out.print(message);
                double input = Double.parseDouble(sc.nextLine());
                if (input < 0) {
                    System.out.println("Input can't be negative.");
                    continue;
                }
                return input;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid double.");
            }
        }
	}

	public static boolean getYesOrNoInput(String message, String warning) {
		System.out.print(message);
		String input = sc.nextLine();
		while (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
			System.out.println(warning);
			System.out.print(message);
			input = sc.nextLine();
		}

		if (input.equalsIgnoreCase("yes")) {
			return true;
		}
		return false;
	}
	
	public static String getValidEmail(String message) {
        while (true) {
            System.out.print(message);
            String email = sc.nextLine();
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return email;
            } else {
                System.out.println("Invalid email address. Please try again.");
            }
        }
    }
}

package online_shopping_system.services;

import java.util.Scanner;

public class InputValidationService {

	private final static Scanner sc = new Scanner(System.in);

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
}

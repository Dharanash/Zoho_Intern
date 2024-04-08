import java.util.Scanner;

public class InputValidationService {
    private final static Scanner sc=new Scanner(System.in);
    public static String getStringInput(String message){
        System.out.print(message);
        String input=sc.nextLine();
        return input;
    }

    public static int getIntegerInput(String message){
        System.out.print(message);
        int input = sc.nextInt();
        while(input<0){
            System.out.println("input can't be negative");
            System.out.print(message);
            input=sc.nextInt();

        }
        sc.nextLine();
        return input;
    }

    public static boolean getYesOrNoInput(String message, String warning){
        System.out.print(message);
        String input=sc.nextLine();
        while(!input.equalsIgnoreCase("yes")&&!input.equalsIgnoreCase("no")){
            System.out.println(warning);
            System.out.print(message);
            input = sc.nextLine();
        }
        
        if(input.equalsIgnoreCase("yes")){
            return true;
        }
        return false;
    }
}


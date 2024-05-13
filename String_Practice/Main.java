package String_Practice;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        
        // code for getting character input without knowing initial size
        
        // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // System.out.println("Enter characters and press Enter:");

        // int inputChar;
        // char[] st = new char[100]; 
        // int i = 0;

        // while ((inputChar = reader.read()) != '\n') {
        //     st[i++] = (char) inputChar;
        // }

        // char[] ch = new char[i];
        // for(int j=0; j<i; j++){
        //     ch[j]=st[j];
        // }
        
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a string : ");
        char[] inputChars = sc.nextLine().toCharArray();
        CustomString cs = new CustomString(inputChars);
        char c='e';
        System.out.println(cs.indexOf(c));
        System.out.println(cs.indexOf(c, 5));
        System.out.println(cs.substring( 6));
        System.out.println(cs.substring(4,7));
        System.out.println(cs.trim());
        for(CustomString str: cs.split(c)){
            System.out.print(str+"-");
        }

        sc.close();

    }
}
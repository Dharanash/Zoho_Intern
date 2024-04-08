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
        String s = sc.nextLine();
        char c='e';
        CustomString cs= new CustomString();
        System.out.println(cs.contains(s, c));
        System.out.println(cs.indexOf(s, c));
        System.out.println(cs.indexOf(s, c, 5));
        System.out.println(cs.substring(s, 6));
        System.out.println(cs.substring(s, 4,7));
        System.out.println(cs.trim(s));
        for(String str: cs.split(s, c)){
            System.out.print(str+"-");
        }

        sc.close();

    }
}
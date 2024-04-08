import java.util.HashMap;
import java.util.Map;

public class Utiliy {
    public static String hashPassword(String password){
        String hpassword="";
        for(int i=0; i<password.length(); i++){
            char c = password.charAt(i);
            if(c=='z'){
                c='a';
            }
            else if(c=='Z'){
                c='Z';
            }
            else if(c=='9'){
                c='0';
            }
            else{
                c++;
            }
            
            hpassword+=c;
        }

        return hpassword;
    }

    public static boolean isPasswordCrt(String upassword, String hpassword){
        String password = Utiliy.hashPassword(upassword);
        if(password.equals(hpassword)){
            return true;
        }

        return false;
    }

    public static int getTotalAmount(HashMap<String, Product> products){
        int total=0;
        for(Map.Entry<String, Product> m : products.entrySet()){
            total += m.getValue().price*m.getValue().quantity;
        }

        return total;
    }
}

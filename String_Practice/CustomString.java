package String_Practice;

public class CustomString {
    public char[] toCharArray(String st){
        char[] ch = new char[st.length()];
        for(int i = 0; i < st.length(); i++){
            ch[i] = st.charAt(i);
        }
        return ch;
    }

    public String toString(char[] ch){
        String st="";
        for(char c: ch){
            st+=c;
        }

        return st;
    }
    
    public String substring(String st, int beginIndex){
        char[] ostring = toCharArray(st);
        char[] nstring = new char[ostring.length-beginIndex];
        int j=beginIndex;
        for(int i=0; i<nstring.length; i++){
            nstring[i]=ostring[j++];
        }
        return toString(nstring);
    }

    public String substring(String st, int beginIndex, int endIndex){
        char[] ostring = toCharArray(st);
        char[] nstring = new char[endIndex-beginIndex];
        int j=beginIndex;
        for(int i=0; i<nstring.length; i++){
            nstring[i]=ostring[j++];
        }
        return new String(nstring);
    }

    public int indexOf(String st, char c){
        char[] ostring = toCharArray(st);
        for(int i=0; i<ostring.length; i++){
            if(ostring[i]==c){
                return i;
            }
        }

        return -1;
    }

    public int indexOf(String st, char c, int fromIndex){
        char[] ostring = toCharArray(st);
        for(int i=fromIndex; i<ostring.length; i++){
            if(ostring[i]==c){
                return i;
            }
        }

        return -1;
    }

    public String replace(String st, char oldChar, char newChar){
        char[] ostring = toCharArray(st);
        for(int i=0; i<ostring.length; i++){
            if(ostring[i]==oldChar){
                ostring[i]=newChar;
            }
        }

        return new String(ostring);
    }

    public String[] split(String st, char delimiter){
        char[] ch = toCharArray(st);
        int count=1;
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == delimiter) {
                count++;
            }
        }

        String[] nstring=new String[count];
        String substring="";
        int j=0;
        for(int i=0; i<ch.length; i++){
            if (ch[i] == delimiter) {
                nstring[j++]=substring;
                substring="";
            }
            else{
                substring+=ch[i];
            }
            
        }

        if(substring!=""&&j<nstring.length){
            nstring[j]=substring;
        }

        return nstring;
    }

    public String trim(String st){
        String nstring ="";
        char[] ostring = toCharArray(st);
        int start=0, end=ostring.length-1;

        while(start<end){
            if(ostring[start]==' '){
                start++;
            }
            else if(ostring[end]==' '){
                end--;
            }
            else{
                break;
            }
        }

        for(int i=start; i<=end; i++){
            nstring+=ostring[i];
        }

        return nstring;
    }

    public String toUpperCase(String st) {
        char[] ch = toCharArray(st);
        for (int i = 0; i < ch.length; i++) {
            ch[i] = toUpperCase(st.charAt(i));
        }
        return new String(ch);
    }

    public char toUpperCase(char c) {
        if (c >= 'a' && c <= 'z') {
            return (char) (c - 'a' + 'A');
        }
        return c;
    }

    public String toLowerCase(String st) {
        char[] ch = toCharArray(st);
        for (int i = 0; i < ch.length; i++) {
            ch[i] = toUpperCase(st.charAt(i));
        }
        return new String(ch);
    }

    public char toLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c - 'A' + 'a');
        }
        return c;
    }

    public boolean contains(String str, char c){
        char[] ch = toCharArray(str);
        for(char x: ch){
            if(x==c){
                return true;
            }
        }

        return false;
    }


}

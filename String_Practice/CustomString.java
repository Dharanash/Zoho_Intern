package String_Practice;

import java.util.ArrayList;
import java.util.List;

public final class CustomString {
    private final char[] characters;

    public CustomString(char[] chars) {
        characters = chars;
    }

    public int length() {
        return characters.length;
    }

    public char charAt(int index) {
        if (index < 0 || index >= characters.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return characters[index];
    }

    public char[] toCharArray(){
        char[] ch = new char[characters.length];
        for(int i = 0; i < ch.length; i++){
            ch[i] = characters[i];
        }
        return ch;
    }

    public CustomString toCustomString(char[] ch){
        CustomString cs=new CustomString(ch);
        return cs;
    }
    
    public CustomString substring(int beginIndex){
        char[] ostring = characters;
        char[] nstring = new char[ostring.length-beginIndex];
        int j=beginIndex;
        for(int i=0; i<nstring.length; i++){
            nstring[i]=ostring[j++];
        }
        return toCustomString(nstring);
    }

    public CustomString substring(int beginIndex, int endIndex){
        char[] ostring = characters;
        char[] nstring = new char[endIndex-beginIndex];
        int j=beginIndex;
        for(int i=0; i<nstring.length; i++){
            nstring[i]=ostring[j++];
        }
        return new CustomString(nstring);
    }

    public int indexOf(char c){
        char[] ostring = characters;
        for(int i=0; i<ostring.length; i++){
            if(ostring[i]==c){
                return i;
            }
        }

        return -1;
    }

    public int indexOf( char c, int fromIndex){
        char[] ostring = characters;
        for(int i=fromIndex; i<ostring.length; i++){
            if(ostring[i]==c){
                return i;
            }
        }

        return -1;
    }

    @Override
    public String toString() {
        return new String(characters);
    }
    
    public CustomString replace(char oldChar, char newChar){
        char[] nstring = new char[characters.length];
        for(int i=0; i<nstring.length; i++){
            if(characters[i]==oldChar){
                nstring[i]=newChar;
            }
            else{
                nstring[i]=characters[i];
            }
        }

        return new CustomString(nstring);
    }

    public CustomString[] split(char delimiter){
        List<CustomString> parts = new ArrayList<>();
        int startIndex = 0;
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == delimiter) {
                parts.add(substring(startIndex, i));
                startIndex = i + 1;
            }
        }
        if (startIndex < characters.length) {
            parts.add(substring(startIndex, characters.length));
        }
        return parts.toArray(new CustomString[parts.size()]);
    }

    public CustomString trim(){
        int start=0, end=characters.length-1;

        while(start<end){
            if(characters[start]==' '){
                start++;
            }
            else if(characters[end]==' '){
                end--;
            }
            else{
                break;
            }
        }

        char[] ch=new char[end-start+1];
        for(int i=0; i<ch.length; i++){
            ch[i]=characters[start++];
        }

        return new CustomString(ch);
    }

    public CustomString toUpperCase() {
        char[] ch = new char[characters.length];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = toUpperCase(characters[i]);
        }
        return new CustomString(ch);
    }

    public char toUpperCase(char c) {
        if (c >= 'a' && c <= 'z') {
            return (char) (c - 'a' + 'A');
        }
        return c;
    }

    public CustomString toLowerCase() {
        char[] ch = new char[characters.length];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = toUpperCase(characters[i]);
        }
        return new CustomString(ch);
    }

    public char toLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char) (c - 'A' + 'a');
        }
        return c;
    }

    public boolean contains(char c){

        for(char x: characters){
            if(x==c){
                return true;
            }
        }

        return false;
    }

    public CustomString concat(CustomString str) {
        int thisLength = length();
        int otherLength = str.length();
        char[] result = new char[thisLength+otherLength];
        int i=0;
        for(i=0; i<thisLength;i++){
            result[i]=characters[i];
        }
        for(int j=0;j<otherLength;j++){
            result[i]=str.charAt(j);
        }

        return new CustomString(result);
    }


}

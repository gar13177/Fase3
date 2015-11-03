
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class ScannerC {
    
    private int length = 0;
    private int charPos = 0;
    private ArrayList<Character> charArray;
    private int column = 1;
    private int line = 1;
    private int pointer = 0;
    private int lineCount = 0;
    private ArrayList<Integer> lineLength;
    
    public ScannerC(String namePath) throws IOException{
        String theString;

        File file = new File(namePath);
        Scanner scanner = new Scanner(file);
        theString = scanner.nextLine();
        lineCount = 1;//primera columnta
        
        lineLength = new ArrayList();//vector con tamanos
        lineLength.add(theString.length());//se agrega primer tamano
        while (scanner.hasNextLine()) {
            String temp = scanner.nextLine();
            lineLength.add(temp.length()+1);//se agrega el Intro
            theString = theString + "\n" + temp;
            lineCount += 1;
        }
        scanner.close();
        
        charArray = new ArrayList();//trabajar todo con ArrayList
        for (char c : theString.toCharArray())
            charArray.add(c);
        
        length = charArray.size();
        
    }
    
    
    
    public String getString(int ini, int end){
        if (ini > length || end > length) return "";
        
        String temp = "";
        for (int i = ini; i < end; i++){
            temp += charArray.get(i);
        }
                
        return temp;
    }
    
    public int getLength(){
        return length;
    }
    
    public int getCharPos(){
        return charPos;
    }
    
    public void setCharPos(int charPos){
        this.charPos = charPos;
        int i = 0;
        while (charPos >= lineLength.get(i)){
            charPos += -lineLength.get(i);
            i += 1;
        }
        line = i+1;
        column = charPos;
    }
    
    public void setPosBy(int line, int column){
        charPos = 0;
        for (int i = 0; i < line-1; i++){
            charPos += (int)lineLength.get(i);
        }
        charPos += column;
    }
    
    
    public int Peek(){
        if (charPos >= length) return -1;
        
        return (int)charArray.get(charPos);
    }
    
    public int NextCh(){
        if (charPos >= length) return -1;
        char temp = charArray.get(charPos);
        
        if (temp == '\n') {// si es un Intro se mueve posicion
            line += 1;
            column = 1;
        }else{
            column += 1;
        }
        charPos += 1;
        System.out.println(temp);
        return (int)temp;
    }
    
    public void setPointer(int pointer){
        this.pointer = pointer;
    }
    
    public void setPointer(){
        pointer = charPos;
    }
    
    public int getPointer(){
        return pointer;
    }
    
    public int getLine(){
        return line;
    }
    
    public int getColumn(){
        return column;
    }
    
    public int getLineCount(){
        return lineCount;
    }
    
    public ArrayList getLineLength(){
        return lineLength;
    }
 
}

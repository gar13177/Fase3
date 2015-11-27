
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class CodeBuilder {
    
    private HashMap<String,String> tokens;
    private HashMap<String,String> keywords;
    private ArrayList<String> whiteSpace;
    
    public CodeBuilder(HashMap tokens, HashMap keywords, ArrayList whiteSpace, String table){
        this.tokens = tokens;
        this.keywords = keywords;
        this.whiteSpace = whiteSpace;
        //System.out.println("ejecutando");
        change("MyCompiler");
        change2("Table",table);
        //System.out.println("ejecutado");
        //copy("main");
    }
    
    
    public String addInit(){
        String text;
        text = "public void addInit(){\n";
        
        Iterator it = tokens.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            text+="tokens.put(\""+key+"\",\""+tokens.get(key)+"\");\n";
        }
        
        it = keywords.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            text+="keywords.put(\""+key+"\",\""+keywords.get(key)+"\");\n";
        }
        
        for (String st: whiteSpace){
            String val = "";
            for (int i = 0; i < st.length(); i++){//para cada caracter
                //como no sabemos si puede incluir un entero, escribimos todo como char
                val += "(char)"+(int)st.charAt(i);//agregamos el char
                if (i < st.length()-1)//si aun no hemos llegado al ultimo
                    val += "+";
                
                
            }
            text += "whiteSpace.add(\"\"+"+val+");\n";
        }
        
       
        text += "}";
        return text;
    }
    
    public void change(String namePath){
        PrintWriter writer = null;
        try{
            String theString;

            File file = new File("src\\"+namePath+".java");
            
            Scanner scanner = new Scanner(file);
            
            theString = scanner.nextLine();

            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                theString = theString + "\n" + temp;
            }
            
            scanner.close();
            
            theString = theString.replace("//-->ADDINIT", addInit());
            
            
            theString = theString.replace("\n",System.lineSeparator());
            
            
        
            writer = new PrintWriter("compilador\\"+namePath+".java","UTF-8");
            writer.write(theString);
        } catch (IOException ex) {
          // report
            //System.out.println("no se pudo");
        } finally {
           try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
 
    }
    
    public void change2(String namePath,String table){
        PrintWriter writer = null;
        try{
            String theString;

            File file = new File("src\\"+namePath+".java");
            
            Scanner scanner = new Scanner(file);
            
            theString = scanner.nextLine();

            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                theString = theString + "\n" + temp;
            }
            
            scanner.close();
            
            theString = theString.replace("//-->ADDINIT", table);
            
            
            theString = theString.replace("\n",System.lineSeparator());
            
            
        
            writer = new PrintWriter("compilador\\"+namePath+".java","UTF-8");
            writer.write(theString);
        } catch (IOException ex) {
          // report
            //System.out.println("no se pudo");
        } finally {
           try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
 
    }
    
    public void copy(String namePath){
        PrintWriter writer = null;
        try{
            String theString;

            File file = new File("src\\"+namePath+".txt");
            
            Scanner scanner = new Scanner(file);
            
            theString = scanner.nextLine();

            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                theString = theString + "\n" + temp;
            }
            
            scanner.close();            
            
            theString = theString.replace("\n",System.lineSeparator());
            
            
        
            writer = new PrintWriter("compilador\\"+namePath+".java","UTF-8");
            writer.write(theString);
        } catch (IOException ex) {
          // report
            //System.out.println("no se pudo");
        } finally {
           try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
    }
    
}

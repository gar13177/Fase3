

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
public class Printer {
    
    public Printer(String txt, String name){
        PrintWriter writer = null;
        txt = txt.replace("\n",System.lineSeparator());
        if (!name.endsWith(".txt")) name+=".txt";

        try {
            writer = new PrintWriter(name,"UTF-8");
            writer.write(txt);
        } catch (IOException ex) {
          // report
        } finally {
           try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
    }
    
}

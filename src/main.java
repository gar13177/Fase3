
import java.io.IOException;
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
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ScannerC scanner;
        Scanner input = new Scanner(System.in);
        Parser parser;
        
        try{
            System.out.println("Ingrese nombre de archivo de especificacion lexica");
            String name = input.nextLine();
            scanner = new ScannerC(name);
            parser = new Parser(scanner);
            boolean result = parser.Cocol();
            System.out.println("Fue aceptado: "+result);
            if (result){//compilado con exito
                Operations operations = new Operations(parser.getProductions(),parser.getTokens());
                
                if (operations.isValid()){
                    System.out.println("Parser bien definido");
                    operations.BuildAutomata();
                    operations.BuildReduce();
                    operations.BuildTable();
                    

                    CodeBuilder code = new CodeBuilder(parser.getTokens(),parser.getKeywords(),parser.getWhite(),operations.getTable().autoString());
                }else{
                    System.out.println("Parser mal defindo");
                }
                //operations.FirstTry();
                //operations.FollowAll();
                               
            }          
            
            
        } catch (IOException e){
            
        }
    }
    
}


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
        try{
            System.out.println("Ingrese nombre de archivo a lexear");
            String name = input.nextLine();
            scanner = new ScannerC(name);
            MyCompiler mc = new MyCompiler(scanner);
            if (mc.read()){
                System.out.println("Lectura Correcta");
                mc.parse();
            }
            System.out.println(mc.getResult());            
            
        } catch (IOException e){
            
        }
    }
    
}

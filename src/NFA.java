
/**
 * Kevin Estuardo Garcia Guerra
 * Carne No. 13177
 * Construccion de NFA a partir del algoritmo http://algs4.cs.princeton.edu/54regexp/NFA.java.html
 */

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author Kevin
 * Kevin Estuardo Garcia Guerra
 * Carne 13177
 */
public class NFA {
    private String nstatesc;
    private Digraph nstates;
    private int accept;
    private String alphabet;
    private long time;
    
    public NFA (String regexp){
        time = System.currentTimeMillis();
        //validacion de la cadena
        InfixToPostfix inf = new InfixToPostfix();
        String postfix = inf.convert(regexp);
        Digraph dg = new Digraph(postfix.length());
        //arbol lexico
        dg = inf.toTreeR(postfix, dg, postfix.length()-1);
        //System.out.println(postfix);
        //System.out.println(dg);
        postfix = inf.toString(postfix,dg,postfix.length()-1);
        //expresion regular valida
        nstatesc = (char)741+postfix+(char)568;
        alphabet = buildtAlphabet(nstatesc);
        accept = nstatesc.length();
        //Digrafo de longintud +1 para estado final
        nstates = new Digraph(accept+1);
        //pila
        Stack<Integer> lfor = new Stack();
        
        //se recorre cada caracter en la cadena ingresada
        for (int i = 0; i < accept; i++){
            //inicialmente lp apunta a la misma posicion actual
            int left = i;
            
            //se guarda la posicion de parentesis o or
            if (nstatesc.charAt(i) == (char)741 || nstatesc.charAt(i) == (char)248) 
                lfor.push(i);
            
            //si se encuentra el cierre de un parentesis
            //se espera encontrar el inicio del mismo o un or
            else if (nstatesc.charAt(i) == (char)568){
                //or guarda la posicion del stack superior
                int or = lfor.pop();
                
                if (nstatesc.charAt(or) == (char)248){
                    //si es un or,  el inicio del parentesis se encuentra
                    //en una posicion antes
                    left = lfor.pop();
                    
                    //se conecta el inicio del parenteis con la siguiente
                    //posicion despues de |
                    nstates.addEdge(left, or+1);
                    
                    //se conecta el | con el final del parentesis
                    nstates.addEdge(or, i);
                }
                //de lo contrario lp guarda la posicion de (
                else left = or;
            }
            
            //si la siguiente posicion es * entonces se enlaza los parentesis
            if (i < accept-1 && nstatesc.charAt(i+1) == (char)569){
                //se guarda el parentesis ( con la estrella *
                nstates.addEdge(left, i+1);
                //se guarda la estrella * con el parentesis (
                nstates.addEdge(i+1, left);
            }
            
            //se genera conexion epsilon con la siguiente posicion
            if ((""+(char)741+(char)569+(char)568+(char)200).contains(""+nstatesc.charAt(i)))
                nstates.addEdge(i,i+1);
        }
        
        //System.out.println(nstatesc);
        //System.out.println(nstates);
        
        //System.out.println(alphabet);
        time = System.currentTimeMillis()-time;
    }
    
    public boolean recognizes(String txt){
        for (int i = 0; i<txt.length(); i++){
            if (!alphabet.contains(""+txt.charAt(i)))
                return false;
        }     
        
        Set<Integer> states = new LinkedHashSet();
        states.add(0);
        states = eClosure(states);
        
        for (int i = 0; i <txt.length(); i++){
            states = move(states,txt.charAt(i));
            states = eClosure(states);
        }
        
        return states.contains(accept);
    }
    
   
    public Digraph getDigraph(){
        return nstates;
    }
    
    public  String buildtAlphabet(String txt){
        //se eliminan los caracteres especiales
        txt = txt.replace(""+(char)741, "");
        txt = txt.replace(""+(char)568, "");
        txt = txt.replace(""+(char)399, "");
        txt = txt.replace(""+(char)200, "");
        txt = txt.replace(""+(char)248, "");
        txt = txt.replace(""+(char)569, "");
        
        int len = txt.length();
        char cha;
        String txt2 = "";
        while (0 < len){
            cha = txt.charAt(0);
            txt = txt.replace(""+cha, "");
            txt2 += cha;
            len = txt.length();
        }        
        return txt2;
    }
    
    public Set move(Set<Integer> arraytemp, char ch){
        Set<Integer> array = new LinkedHashSet();
        
        for (int x: arraytemp){
            if (x<nstatesc.length())
                if (nstatesc.charAt(x)==ch )
                    array.add(x+1);
        }
        
        return array;
    }
    
    public Set eClosure(Set<Integer> arraytemp){
              
        boolean ctemp;
        Set<Integer> array;
        do{
            array = new LinkedHashSet(arraytemp);
            for (int x : arraytemp){
                for (int w: nstates.adj(x)){
                    //System.out.println(w);
                    array.add(w); 
                }
            }
            ctemp = array.equals(arraytemp);
            arraytemp = array;
        }while (!ctemp);
        
        return arraytemp;
    }
    
    public String getAlphabet(){
        return alphabet;
    }  
    
    public int getFinalState(){
        return accept;
    }
    
    @Override
    public String toString(){
        String txt = "LECTURA = "+nstatesc+"\n";
        txt += "ESTADOS = {";
        for (int i = 0; i <= accept; i++){
            txt += " "+i;
            if (i != accept) txt +=",";
        }
        txt += "}\n";
        
        txt += "SIMBOLOS ={";
        for (int i = 0; i< getAlphabet().length(); i++){
            txt += " "+getAlphabet().charAt(i);
            if (i != getAlphabet().length()-1) txt += ",";
        }
        txt += "}\nINICIO = {0}\n";
        
        txt += "ACEPTACION = {"+(accept)+"}\n";
        
        txt += "TRANSICION = "+ nstates.rawString();
        
        for (int i = 0; i < nstatesc.length(); i++){
            if (getAlphabet().contains(""+nstatesc.charAt(i))){
                txt += "("+i+", "+nstatesc.charAt(i)+", "+(i+1)+")";
                if (i < nstatesc.length()-2) txt += "-";
            }
            
        }
        txt += "\nTIEMPO = "+time+" milisegundos";
        return txt;
    }
    
    public String getTransitions(){
        return nstatesc;
    }
    
    public String toStringDraw(){
        String txt = "digraph finite_state_machine {\n";
        txt += "rankdir=LR;\n";
        //txt += "size=\"8.5\";\n";
        
        txt += "node [shape = doublecircle style = filled];\n";
        
        txt += accept+";\n";
        
        txt += "node [shape = circle];\n";
        
        
        for (int i = 0; i<nstates.V(); i++){
            //para cada conexion
            for (int j: nstates.adj(i)){
                txt += i+" -> "+j+" [ label = \"~\" ];\n";
            }
        }
        
        for (int i = 0; i<nstatesc.length(); i++){
            if (alphabet.contains(""+nstatesc.charAt(i))){
                txt += i+" -> "+(i+1)+" [ label = \""+nstatesc.charAt(i)+"\" ];\n";
            }
        }
        
        txt += 0+" [fillcolor = \"red\"]\n";
               
        txt += "}";
        //System.out.println(txt);
        
        return txt;
        
    }
    
}

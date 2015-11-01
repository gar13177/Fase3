/**
 * Kevin Estuardo Garcia Guerra
 * Carne No. 13177
 * Conversion infix to postfix
 * Referencia: https://gist.github.com/DmitrySoshnikov/1239804
 * Transcripcion de javaScript a java
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


/**
 *
 * @author Kevin
 */
public class InfixToPostfix {
    
    public InfixToPostfix(){
        
    }
    
    public String convert(String reStr){
        //System.out.println(reStr);
        reStr = valTxt(reStr);
        //System.out.println(reStr);
        //this.reStr = reStr;
        Stack<Character> output = new Stack<Character>();
        Stack<Character> stack = new Stack<Character>();
        
        for (int k = 0; k < reStr.length();k++){
            char c = reStr.charAt(k);
            
            if (c == '(')
                stack.push(c);
            
            else if (c == ')'){
                while (stack.peek() != '('){
                    output.push(stack.pop());
                }
                stack.pop();
            }
            
            else{
                while(!stack.isEmpty()){
                    char peekedChar = stack.peek();
                    int peekedCharPrecedence = precedenceOf(peekedChar);
                    int currentCharPrecedence = precedenceOf(c);
                    
                    if(peekedCharPrecedence >= currentCharPrecedence){
                        output.push(stack.pop());
                    }else{
                        break;
                    }
                }
                stack.push(c);
            }
        }
        
        while (!stack.isEmpty())
            output.push(stack.pop());
        //this.output = output;
        String txt = output.toString();
        txt = txt.replace("[", "");
        txt = txt.replace("]", "");
        txt = txt.replace(",", "");
        txt = txt.replace(" ", "");
        //System.out.println(txt);
        return txt;
    }
    
    private int precedenceOf(char txt){
        if (txt== '(') return 1;
        else if (txt == '|') return 2;
        else if (txt == '.') return 3;
        
        else if (txt == '?') return 4;
        else if (txt == '*') return 4;
        else if (txt == '+') return 4;
        
        else if (txt == '^') return 5;
        
        return 6;
    }
        
    public Digraph toTreeR(String regex, Digraph dg, int position){
        //posicion final de la cadena
        
        int rg = position - 1;//rama derecha es el más cercano
        int lf = position - 2;//rama izquierda se supone el segundo
        
        if (regex.charAt(position)=='.' || regex.charAt(position)=='|'){
            //el siguiente mas cercano siempre tiene conexion
            dg.addEdge(position, rg);
            //si la posicion de la derecha es una operacion
            if (regex.charAt(rg)=='*'||regex.charAt(rg)=='|'||regex.charAt(rg)=='.' ||regex.charAt(rg)=='+'||regex.charAt(rg)=='?'){
                //si la derecha es una operacion, el nuevo grafo contiene dicha operacion
                dg = toTreeR(regex,dg,rg);
                
                //dado que es una operacion, ahora la posicion izquierda cambia
                lf = getMinimum(dg,rg)-1;
                
            }
            dg.addEdge(position, lf);
            return toTreeR(regex,dg,lf);
            
        }else if (regex.charAt(position)=='*' || regex.charAt(position) == '+' || regex.charAt(position) == '?'){
            dg.addEdge(position, rg);
            //si es * se calcula lo que queda debajo
            return toTreeR(regex,dg,rg);
        }
        
        return dg;
    }
    
    public int getMinimum(Digraph dg,int position){
        
        List<Integer> tempst = new ArrayList();
        for (int j: dg.adj(position)){
            tempst.add(j);
        }
        if (tempst.isEmpty())
            return position;
        
        return getMinimum(dg, Collections.min(tempst));
        
    }
    
    public String valTxt(String txt){
        txt = txt.replace(" ","");
        txt = txt.replace(".","");
        int i = txt.length();
        if (i != 0){
            String txttemp = "";
            for (int j = 0; j < i-1; j++){
                txttemp += txt.charAt(j);
                
                if (txt.charAt(j+1)=='|' || txt.charAt(j+1) == ')' || txt.charAt(j+1) == '*' || txt.charAt(j+1) == '?'|| txt.charAt(j+1) == '+'|| txt.charAt(j)=='(' || txt.charAt(j) == '|'){
                    
                }else{
                    txttemp += '.';
                }
                
            }
            txttemp += txt.charAt(i-1);
            txt = txttemp;
        }
        
        return txt;
    }
    
    public String toString(String regex, Digraph dg, int position){
        
        List<Integer> temp = new ArrayList();
        
        for (int i:dg.adj(position))
            temp.add(i);
       
        //nodo derecho 
        //nodo izquierdo
        int lf = position;
        int rg = position;
        
        if (!temp.isEmpty()){
            rg = Collections.max(temp);
            lf = Collections.min(temp);
        }
        //System.out.println(lf+" "+position+" "+rg);
        //System.out.println(position+" "+lf+" "+rg);
        if (regex.charAt(position)=='.'){ 
            
            return "("+toString(regex,dg,lf)+")("+toString(regex,dg,rg)+")";
        }else if(regex.charAt(position)=='|'){
            return "("+toString(regex,dg,lf)+")|("+toString(regex,dg,rg)+")";
            
        }else if (regex.charAt(position)=='*'){
            
            return "("+toString(regex,dg,rg)+")*";
        }else if (regex.charAt(position)=='+'){
            String tempst = toString(regex,dg,rg);
            return "("+tempst+")("+tempst+")*";
        }else if (regex.charAt(position)=='?'){
            
            return "("+toString(regex,dg,rg)+")|(~)";
        }

        return ""+regex.charAt(position);
    }
    
}

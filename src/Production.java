
import java.util.ArrayList;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Production {
    
    private ArrayList<Token> tokens = new ArrayList();
    private int size = 0;
    
    public Production(){
        
    }
    
    public Production (Production pd){
        tokens = pd.getProduction();
        size = tokens.size();
    }
    
    public Production(Token tk){
        tokens.add(tk);
        size = tokens.size();
    }
    
    public ArrayList getProduction (){
        return this.tokens;
    }
    
    public Token get(int i){
        return tokens.get(i);
    }
    
    public void addToken(Token tk){
        //if (!tokens.contains(tk)){
            tokens.add(tk);
            size = tokens.size();
        //}
    }
    
    public void replace(int i, Token tk){
        tokens.set(i, tk);
    }
    
    public void addTokenInit(Token tk){
        tokens.add(0,tk);//agregar elemento al inicio
        size = tokens.size();
    }
    
    @Override
    public boolean equals (Object o){
        
        Production pd = (Production) o;
        
        
        boolean val;
        val = pd.getSize() == getSize();
        if (val) {
            for (int i = 0; i < getSize(); i++){
                val = val && pd.getProduction().get(i).equals(tokens.get(i));
            }
        }
        
        return val;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.tokens);
        hash = 97 * hash + this.size;
        return hash;
    }
    
    public int getSize(){
        size = tokens.size();
        return size;
    }
    
    public void remove(int i){
        tokens.remove(i);//quita token
        size = tokens.size();
    }
    
    @Override
    public String toString(){
        String st = "";        
        for (Token tk: tokens){
            st += "["+tk.toString()+"]";
        }
        return st;
    }
    
    public boolean contains(Token tk){
        for (Token tkk:tokens){
            if (tkk.equals(tk)) return true;
        }
        return false;
    }
    
}

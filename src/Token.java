/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Token {
    
    private String value = "";
    private int type = 0;
    /**
     * tipo:
     * 1 = es ident pero no sabe de que tipo
     * 2 = es string
     * 3 = es char
     * 4 = es token declarado (Token de los del compilador)
     * 5 = es produccion
     * 6 = es epsilon
     */
    
    
    public Token(String value){
        this.value = value;
        type = 1;
    }
    
    public Token (String value, int tipo){
        this.value = value;
        this.type = tipo;
    }
    
    
    public String getValue(){
        return this.value;
    }
    
    public int getType(){
        return this.type;
    }
    
    public void setValue(String st){
        value = st;
    }
    
    public void setType (int tp){
        type = tp;
    }
    
    public boolean equals (Object  o){
        boolean val;
        Token tk = (Token)o;
        val = tk.getType() == this.getType();//son del mismo tipo
        val = val && tk.getValue().equals(this.getValue());// el valor es identico
        return val;
    }
    
    @Override
    public String toString(){
        String st = "";
        st += "Value: "+value+", Type: "+type;        
        return st;
    }
    
}

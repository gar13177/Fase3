
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
     * 0 = es token final
     */
    
    
    public Token(String value){
        this.value = value;
        type = 1;
    }
    
    public Token (String value, int tipo){
        this.value = value;
        this.type = tipo;
    }
    
    public Token (Token tk){
        this.value = tk.getValue();
        this.type = tk.getType();
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
    
    @Override
    public boolean equals (Object  o){
        boolean val;
        Token tk = (Token)o;
        val = tk.getType() == this.getType();//son del mismo tipo
        val = val && tk.getValue().equals(this.getValue());// el valor es identico
        return val;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.value);
        hash = 47 * hash + this.type;
        return hash;
    }
    
    @Override
    public String toString(){
        String st = "";
        st += "<Value: "+value+", Type: "+type+">";        
        return st;
    }
    
    /**
     * Metodo que retorna una forma mas amigable de ver el token
     * @return 
     */
    public String geString(){
        String val = value;
        val = val.replace('"'+"", "''");
        String st = "";
        if (type == 2)
            st += "''"+val+"''";
        else if (type == 3)
            st += "'"+val+"'";
        else
            st+= val;
        return st;
    }
    
}

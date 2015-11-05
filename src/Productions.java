
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Productions {
    
    private ArrayList<Production> productions = new ArrayList();
    private int size = 0;
    
    public Productions(){
        
    }
    
    public Productions(Productions pd){
        productions = pd.getProductions();
        size = productions.size();
    }
    
    public void addProduction(Production pd){
        
        
        if (!productions.contains(pd)){//si no existe una produccion igual
            productions.add(pd);
            size = productions.size();
        }
        
        //return !val;//si existe alguna, retornara false
    }
    
    public Production get(int i){
        return productions.get(i);
    }
    
    public void replace(int i, Production pd){
        productions.set(i, pd);
    }
    
    public int getSize(){
        return size;
    }
    
    public ArrayList getProductions(){
        return productions;
    
    }
    
    public boolean equals(Object o){
        Productions pd = (Productions) o;
        
        System.out.println("comparacion de productions");
        boolean val;
        val = pd.getSize() == getSize();
        if (val) {
            for (int i = 0; i < getSize(); i++){
                val = val && pd.getProductions().get(i).equals(productions.get(i));
            }
        }
        
        return val;
    }
    
    @Override
    public String toString(){
        String st = "";
        for (Production pd:productions){
            st += pd.toString()+"\n";
        }
        return st;
    }
    
    /**
     * Agrega un token al final de cada produccion
     * Util cuando se realiza un kleene
     * @param tk 
     */
    public void addAllEnd(Token tk){
        for (Production pd: productions){
            pd.addToken(tk);
        }
    }
    
    public void addAll(Productions pd){
        productions.addAll(pd.getProductions());
    }
    
}


import java.util.List;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Tuple {
    private Set<Set> D;
    private Set<Integer> s;
    
    public Tuple(Set<Set> D, Set<Integer> s){
        this.D = D;
        this.s = s;
    }
    
    public Set<Set> getD(){
        return D;
    }
    
    public Set<Integer> gets(){
        return s;
    }
    
    public void add(Set<Integer> ss){
        D.add(ss);
    }
    
    public boolean equals(Tuple o){
        if (o.gets().equals(s)){
            return true;
        }
        return false;
    }
    
    public String toString(){
        String txt = s.toString()+" - ";
        for (Set<Integer> st: D){
            txt+= "{"+st.toString()+"}";
        }
        return txt;
    }
}

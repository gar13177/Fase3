
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
public class Relation {
    private int state;
    private Token token;
    private String head;
    private Production production;
    
    public Relation(){}
    
    public Relation(int state, Token token, String head, Production production){
        this.state = state;
        this.token = token;
        this.head = head;
        this.production = production;                
    }
    
    public Relation(int state, Token token){
        this.state = state;
        this.token = token;               
    }
    
    @Override
    public boolean equals(Object o){
        boolean val = true;
        Relation rl = (Relation)o;
        val = val && rl.getState()==state;
        val = val && rl.getToken().equals(token);
        val = val && rl.getHead().equals(head);
        val = val && rl.getProduction().equals(production);
        return val;
    }
    
    public String toString(){
        String val = "";
        val += "("+state+", "+token.getString()+", "+head+" -> "+production+")";
        return val;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + this.state;
        hash = 83 * hash + Objects.hashCode(this.token);
        hash = 83 * hash + Objects.hashCode(this.head);
        hash = 83 * hash + Objects.hashCode(this.production);
        return hash;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @return the token
     */
    public Token getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(Token token) {
        this.token = token;
    }

    /**
     * @return the head
     */
    public String getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(String head) {
        this.head = head;
    }

    /**
     * @return the production
     */
    public Production getProduction() {
        return production;
    }

    /**
     * @param production the production to set
     */
    public void setProduction(Production production) {
        this.production = production;
    }
    
}

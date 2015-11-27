
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
public class AutoLR0 {
    private ArrayList<Items> states = new ArrayList();
    private ArrayList<Transition> transitions = new ArrayList();
    
    AutoLR0(){}
    
    public void addState(Items it){
        if (!states.contains(it))
            getStates().add(it);
    }
    
    public int addStateReturnIndex(Items it){
        if (getStates().contains(it))
            return getStates().indexOf(it);
        
        getStates().add(it);
        return getStates().size()-1;//indice nuevo
    }
    
    public int indexOf(Items it){
        return getStates().indexOf(it);
    }
    
    public void addTransition(int state0, int state1, Token tk){
        Transition tr = new Transition(state0,state1,tk);
        if (!transitions.contains(tr))
            getTransitions().add(tr);
    }
    
    public int getSizeStates(){
        return getStates().size();
    }
    
    public int getSizeTransitions(){
        return getTransitions().size();
    }
    
    public Items getState(int i){
        return getStates().get(i);
    }
    
    @Override
    public String toString(){
        String val = "";
        val+= "Estados:\n";
        for (int i = 0; i < getStates().size(); i++){//para cada estado
            val += "----"+i+"----\n";
            val += getStates().get(i);
        }
        
        val += "Transiciones\n";
        for (int i = 0; i < getTransitions().size(); i++){//para cada transicion
            val += getTransitions().get(i)+"\n";
        }
        return val;
    }
    
    public String toStringDraw(){
        String txt = "digraph finite_state_machine {\n";
        txt += "rankdir=UD;\n";
        //txt += "size=\"8.5\";\n";
        
        txt += "node [shape = box, style = rounded];\n";
        for (int i = 0; i < getStates().size(); i++){
            txt += i+" ";
        }
        txt += ";\n";
        
        
        for (int i = 0; i < getTransitions().size(); i++){//para cada transición
            txt += getTransitions().get(i).getState0()+" -> "+getTransitions().get(i).getState1()+" [ label = \""+getTransitions().get(i).getTransition().getString()+"\" ];\n"; 
        }
        
        for (int i = 0; i < getStates().size(); i++){//para cada estado
            txt += i+"[label = \"I"+i+"\n"+getStates().get(i).getString()+"\"]";
        }
        
        txt += "}";
        
        
        return txt;
    }

    /**
     * @return the states
     */
    public ArrayList<Items> getStates() {
        return states;
    }

    /**
     * @param states the states to set
     */
    public void setStates(ArrayList<Items> states) {
        this.states = states;
    }

    /**
     * @return the transitions
     */
    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    /**
     * @param transitions the transitions to set
     */
    public void setTransitions(ArrayList<Transition> transitions) {
        this.transitions = transitions;
    }
    
}

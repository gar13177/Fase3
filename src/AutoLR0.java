
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
            states.add(it);
    }
    
    public int addStateReturnIndex(Items it){
        if (states.contains(it))
            return states.indexOf(it);
        
        states.add(it);
        return states.size()-1;//indice nuevo
    }
    
    public int indexOf(Items it){
        return states.indexOf(it);
    }
    
    public void addTransition(int state0, int state1, Token tk){
        Transition tr = new Transition(state0,state1,tk);
        if (!transitions.contains(tr))
            transitions.add(tr);
    }
    
    public int getSizeStates(){
        return states.size();
    }
    
    public int getSizeTransitions(){
        return transitions.size();
    }
    
    public Items getState(int i){
        return states.get(i);
    }
    
    @Override
    public String toString(){
        String val = "";
        val+= "Estados:\n";
        for (int i = 0; i < states.size(); i++){//para cada estado
            val += "----"+i+"----\n";
            val += states.get(i);
        }
        
        val += "Transiciones\n";
        for (int i = 0; i < transitions.size(); i++){//para cada transicion
            val += transitions.get(i)+"\n";
        }
        return val;
    }
    
    public String toStringDraw(){
        String txt = "digraph finite_state_machine {\n";
        txt += "rankdir=UD;\n";
        //txt += "size=\"8.5\";\n";
        
        txt += "node [shape = box, style = rounded];\n";
        for (int i = 0; i < states.size(); i++){
            txt += i+" ";
        }
        txt += ";\n";
        
        
        for (int i = 0; i < transitions.size(); i++){//para cada transición
            txt += transitions.get(i).getState0()+" -> "+transitions.get(i).getState1()+" [ label = \""+transitions.get(i).getTransition().geString()+"\" ];\n"; 
        }
        
        for (int i = 0; i < states.size(); i++){//para cada estado
            txt += i+"[label = \""+states.get(i).getString()+"\"]";
        }
        
        txt += "}";
        
        
        return txt;
    }
    
    
    
    /**
     * Clase Transition, contiene una transicion hacia el siguente estado
     * state0 --> token --> state1
     * state0,1 enteros indicando la posicion en arraylist states
     */
    private class Transition{
        private int state0;
        private int state1;
        private Token transition;
                
        Transition(){}
        
        Transition(int state0, int state1,  Token transition){
            this.state0 = state0;
            this.state1 = state1;
            this.transition = transition;
        }

        /**
         * @return the state0
         */
        public int getState0() {
            return state0;
        }

        /**
         * @param state0 the state0 to set
         */
        public void setState0(int state0) {
            this.state0 = state0;
        }

        /**
         * @return the state1
         */
        public int getState1() {
            return state1;
        }

        /**
         * @param state1 the state1 to set
         */
        public void setState1(int state1) {
            this.state1 = state1;
        }

        /**
         * @return the transition
         */
        public Token getTransition() {
            return transition;
        }

        /**
         * @param transition the transition to set
         */
        public void setTransition(Token transition) {
            this.transition = transition;
        }
        
        @Override
        public boolean equals(Object o){
            boolean val = true;
            Transition tr = (Transition)o;
            val = val && state0 == tr.getState0();
            val = val && state1 == tr.getState1();
            val = val && transition.equals(tr.getTransition());
            return val;            
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 41 * hash + this.state0;
            hash = 41 * hash + this.state1;
            hash = 41 * hash + Objects.hashCode(this.transition);
            return hash;
        }
        
        public String toString(){
            String st="";
            st+= state0 +" --"+transition+"--> "+state1;
            return st;
        }
        
    }
    
}

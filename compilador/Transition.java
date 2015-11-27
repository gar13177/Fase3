
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
* Clase Transition, contiene una transicion hacia el siguente estado
* state0 --> token --> state1
* state0,1 enteros indicando la posicion en arraylist states
*/
public class Transition{
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

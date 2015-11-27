/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Action {

    /**
     * action guarda el tipo de accion a realizar 0 es shift 1 es goto 2 es
     * reduce 3 es accept null de lo contrario
     */
    private int action;

    /**
     * number guarda lo que corresponda a la accion shift: nuevo estado goto:
     * nuevo estado reduce: cantidad de elementos en la produccion null de lo
     * contrario
     */
    private int number;

    /**
     * head solo esta activo para reduce guarda la cabeza de produccion null de
     * lo contrario
     */
    private String head;

    public Action() {

    }

    public Action(int action, int number, String head) {
        this.action = action;
        this.number = number;
        this.head = head;
    }

    public Action(int action, int number) {
        this.action = action;
        this.number = number;
    }
    
    public String toString(){
        String val = "";
        if (action == 0){
            val += "S"+number;
        }else if (action == 1){
            val += "G"+number;
        }else if (action == 2){
            val += "R"+number;
        }else if (action == 3){
            val+="acc";
        }
        return val;
    }

    /**
     * @return the action
     */
    public int getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
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
}

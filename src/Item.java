
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Solamente es una produccion tipo item, si se guardan varios items
 *
 * @author Kevin
 */
public class Item {

    private String head = "";//cabeza de dicha produccion
    private Production production = new Production();//produccion en si
    private int index = 0;//donde esta el punto
    private boolean accept = false;

    public Item(Item it) {
        this.head = it.getHead();
        this.production = it.getProduction();
        this.index = it.getIndex();
        if (index == production.getSize() - 1) {//si esta apuntando al ultimo
            if (production.get(index).getType() == 0)//si es $
            {
                accept = true;
            }
        }
    }

    public Item() {

    }

    @Override
    public boolean equals(Object o) {
        boolean val;
        Item it = (Item) o;
        val = it.getProduction().equals(this.production);//tienen la misma produccion
        val = val && it.getHead().equals(this.head);//tienen la misma cabeza 
        val = val && it.getIndex() == this.index;//tienen el mismo index
        return val;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.head);
        hash = 19 * hash + Objects.hashCode(this.production);
        hash = 19 * hash + this.index;
        hash = 19 * hash + (this.isAccept() ? 1 : 0);
        return hash;
    }

    public Item(String head, Production pd, int index) {
        this.head = head;
        this.production = pd;
        this.index = index;
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

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        String st = "";
        st += head + " -> ";
        for (int i = 0; i < production.getSize(); i++) {
            if (index == i) {
                st += "°";
            }
            st += production.get(i);
        }
        if (index == production.getSize()) {
            st += "°";
        }

        return st;
    }

    /**
     * @return the accept
     */
    public boolean isAccept() {
        return accept;
    }

    /**
     * @param accept the accept to set
     */
    public void setAccept(boolean accept) {
        this.accept = accept;
    }
    
    /**
     * Metodo para presentar un item de forma mas amigable
     * @return 
     */
    public String getString(){
        String st = "";
        st += head + " -> ";
        for (int i = 0; i < production.getSize(); i++) {
            if (index == i) {
                st += " ●";
            }
            st += " "+production.get(i).getString();
        }
        if (index == production.getSize()) {
            st += " ●";
        }

        return st;
    }
}

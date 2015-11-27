
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
public class Items {
    private ArrayList<Item> items = new ArrayList();
    private int size = 0;
    
    public Items(Items it){
        this.items = it.getItems();
        this.size = items.size();
    }
    
    public Items(Item it){
        items.add(it);
        size = items.size();
    }
    
    public Items(){}
    
    public void add(Item it){
        if (!items.contains(it)){//si no esta contenido se agrega
            items.add(it);
            size = items.size();
        }
    }
    
    
    public void addAll(Items its){
        for (int i = 0; i< its.getSize(); i++){
            add(its.getItems().get(i));
        }
    }
    
    public void remove(int i){
        items.remove(i);
        size = items.size();
    }
    
    public Item get(int i){
        return items.get(i);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.items);
        hash = 79 * hash + this.size;
        return hash;
    }
    
    @Override
    public boolean equals(Object o){
        Items it = (Items)o;
        boolean val = true;
        if (it.getSize()==this.size){
            for (Item i: it.getItems()){//para cada item
                val = val && items.contains(i);//revisa que contiene cada item
            }
        }else return false;
        
        return val;
    }

    /**
     * @return the items
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(ArrayList<Item> items) {
        this.items = items;
        this.size = items.size();
    }

    /**
     * @return the size
     */
    public int getSize() {
        this.size = items.size();
        return size;
    }

    public String toString(){
        String st = "";       
        for (Item it: items){
            st+=it+"\n";
        }
        return st;
    }
    
    /**
     * Metodo para presentar los items de forma mas amigable
     * @return 
     */
    public String getString(){
        String st = "";
        for (Item it: items){
            st+= it.getString()+"\n";
        }
        return st;
    }
}

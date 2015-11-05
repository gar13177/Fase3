
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class Operations {
    
    private HashMap<String,Productions> productions;
    private HashMap<String,String> tokens;
    private HashMap<String,LinkedHashSet<Token>> first;
    private HashMap<String,LinkedHashSet<Token>> follow;
    
    
    public Operations(){
        
    }
    
    public Operations(HashMap pd, HashMap tk){
        this.productions = pd;
        this.tokens = tk;
        
    }
    
    /**
     * Produccion pd que se debe analizar
     * Arraylist hd de cabezas de produccion que se han visitado
     * Arraylist tks de tokens que son first
     * 
     * @param pd
     * @param hd 
     */
    public LinkedHashSet<Token> First(Production pd, ArrayList<String> hd, LinkedHashSet<Token> tks){
        
        if (pd.getSize()>0){
            if (pd.get(0).getType() != 5){//si no es una produccion
                tks.add(pd.get(0));

            }else{//si es una produccion

                String key = pd.get(0).getValue();//obtengo la cabeza de produccion
                if (!hd.contains(key)){//si no se ha calculado first de esa cabeza...
                    hd.add(key);// la nueva cabeza
                    Productions pds = productions.get(key);//obtenemos las producciones de esa cabeza
                    

                    LinkedHashSet<Token> temptks = new LinkedHashSet();

                    for (int i = 0; i < pds.getSize(); i++){//para cada produccion
                         temptks.addAll(First(pds.get(i),hd,temptks));//capturo los nuevos first
                    }
                    
                    Token tk = new Token("~",6);//token de epsilon
                    

                    if (temptks.contains(tk)){//si existe un epsilon
                        if (pd.getSize()>1){//si la produccion es mas grande que 1...
                            Production temppd = new Production(pd);
                            temppd.remove(0);//quito el token ya evaluado
                            //System.out.println("Existe epsilon y hay siguiente:"+temppd);
                            if (temppd.get(0).getType() == 5){//si el siguiente es produccion
                                //System.out.println("Sigue produccion");
                                //System.out.println(hd);
                                //System.out.println("Key:"+key);
                                hd.remove(hd.size()-1);
                                
                                //System.out.println(hd);
                            }
                            //System.out.println("Nueva produccion:"+temppd);
                            //System.out.println("Cabezas:"+hd);
                            LinkedHashSet<Token> temptks2 = First(temppd,hd,new LinkedHashSet<Token>());
                            //System.out.println("Token"+tk);
                            //System.out.println("Tokens:"+temptks2);
                            if (!temptks2.contains(tk)){//si no tiene un epsilon la siguiente produccion
                                temptks.remove(tk);//quito epsilon del actual
                                //System.out.println("No hay Epsilon");
                            }

                            temptks.addAll(temptks2);
                        }
                    }

                    tks.addAll(temptks);

                }

            }
        
        }
        
        //System.out.println(hd);
        //System.out.println("Tokens retornados:"+tks);
        
        return tks;
    }
    
    public void FirstAll(){
        first = new HashMap();
        
        Iterator it = productions.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            Productions pd = productions.get(key);//obtenemos las producciones
            //System.out.println("Cabeza: "+key);
            ArrayList<String> temphd = new ArrayList();
            LinkedHashSet<Token> temptk = new LinkedHashSet();
            temphd.add(key);
            for (int i = 0; i < pd.getSize(); i++){//para cada produccion
                temptk.addAll(First(pd.get(i),temphd,new LinkedHashSet<Token>()));
            }
            //System.out.println(temptk);
            first.put(key, temptk);
        }
        
        System.out.println("First-----------------");
        it = first.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            System.out.println("Cabeza: "+key+" Cuerpo: "+first.get(key));
        }
    }
    
    public LinkedHashSet<Token> Follow(int index, Production pd, ArrayList<String> hd, LinkedHashSet<Token> tks){
        
        if(follow.containsKey(pd.get(index).getValue())){//si el follow ya se calculo
            LinkedHashSet<Token> nt = follow.get(pd.get(index).getValue());
            tks.addAll(nt);
            return tks;
        }
        if (!hd.contains(pd.get(index).getValue())){//si no se ha explorado esa cabeza
            if (pd.get(index).getType()==5){//se realiza  solo si es una produccion
                if (pd.getSize()-1<index){//si la produccion a followear no es la ultima
                    if (pd.get(index+1).getType() == 5){//si el que sigue es produccion
                        LinkedHashSet temptks = first.get(pd.get(index+1).getValue());//se toma first del siguiente
                        Token temptk = new Token("~",6);//token vacio
                        if (temptks.contains(temptk)){//si contiene el vacio...
                            temptks.remove(temptk);//quitamos epsilon
                            hd.add(pd.get(index).getValue());//agrego la cabeza ya visitada

                            Iterator it = productions.keySet().iterator();
                            String keyy = "";
                            while (it.hasNext()){
                                String key = (String) it.next();
                                if (productions.get(key).contains(pd)){//si la produccion esta contenida
                                    keyy = key;//encontramos la cabeza de produccion que contiene dicha produccion
                                }
                            }
                            
                            //computamos siguiente follow

                            Token tkk = new Token(keyy,5);//creamos un token de produccion tipo keyy
                            LinkedHashSet<Token> temptksn = new LinkedHashSet();//nuevo conjunto follow
                            it = productions.keySet().iterator();
                            while (it.hasNext()){
                                String key = (String) it.next();
                                for (int i = 0; i < productions.get(key).getSize(); i++){//para cada produccion
                                    
                                    for ( int j = 0; j<productions.get(key).get(i).getSize(); j++){//para cada token
                                        if (tkk.equals(productions.get(key).get(i).get(j))){//si el token es igual
                                            temptksn.addAll(Follow(j,productions.get(key).get(i),hd, new LinkedHashSet<Token>()));//calculamos follow de ese token
                                        }
                                    }
                                }
                            }//termina calculo de follow
                            
                            follow.put(keyy, temptksn);//nuevo follow agregado
                            tks.addAll(temptksn);//agregamos follow del siguiente


                        }
                        tks.addAll(temptks);//agregamos los del conjunto first
                    }else if(pd.get(index+1).getType() != 6){
                        tks.add(pd.get(index+1));
                    }
                    /**
                     * falta corregir si se agrega ejemplo: T -> A B '~', que pasa con epsilon?
                     */

                }else{//si la expresion es la ultima...
                    hd.add(pd.get(index).getValue());//agrego la cabeza
                    Iterator it = productions.keySet().iterator();
                    String keyy = "";
                    while (it.hasNext()){
                        String key = (String) it.next();
                        if (productions.get(key).contains(pd)){//si la produccion esta contenida
                            keyy = key;
                        }
                    }
                    
                    //computamos siguiente follow

                    Token tkk = new Token(keyy,5);//creamos un token de produccion tipo keyy
                    LinkedHashSet<Token> temptksn = new LinkedHashSet();//nuevo conjunto follow
                    it = productions.keySet().iterator();
                    while (it.hasNext()){
                        String key = (String) it.next();
                        for (int i = 0; i < productions.get(key).getSize(); i++){//para cada produccion
                            //System.out.println("tamano:"+productions.get(key).get(i).getSize());
                            //System.out.println("produccion:"+productions.get(key).get(i));

                            for ( int j = 0; j<productions.get(key).get(i).getSize(); j++){//para cada token
                                if (tkk.equals(productions.get(key).get(i).get(j))){//si el token es igual
                                    temptksn.addAll(Follow(j,productions.get(key).get(i),hd, new LinkedHashSet<Token>()));//calculamos follow de ese token
                                }
                            }
                        }
                    }//termina calculo de follow
                    
                    
                   
                    follow.put(keyy, temptksn);//nuevo follow agregado
                    tks.addAll(temptksn);
                }

            }
        }
        
        return tks;        
    }
    
    public void FollowAll(){
        follow = new HashMap();
        
        Token tk = new Token("$",3);//token que se agrega al final de primera produccion
        
        Iterator it = productions.keySet().iterator();
        while (it.hasNext()){
            String key = (String) it.next();
            if (productions.get(key).getHead()){//si es la cabeza
                //System.out.println("si hay cabeza");
                LinkedHashSet<Token> temp = new LinkedHashSet();
                temp.add(tk);
                follow.put(key, temp);//agregamos el dolar a follow del primero
            }
        }
        System.out.println("Follow------------");
        System.out.println("Paso base:"+follow);
        
        it = productions.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            Productions pd = productions.get(key);//obtenemos las producciones
            for (int i = 0; i < pd.getSize(); i++){//para cada produccion
                for (int j = 0; j<pd.get(i).getSize(); j++){//para cada token
                    if (pd.get(i).get(j).getType()==5){//si es produccion
                        follow.put(key, Follow(j,pd.get(i),new ArrayList<String>(),new LinkedHashSet<Token>()));
                    }
                }
            }
        }
        
        
        it = follow.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            LinkedHashSet fl = follow.get(key);//obtenemos las producciones
            System.out.println("Cabeza: "+key);
            System.out.println(fl);
        }
        
        
    }
    
         
    public boolean isValid(){
        
        Iterator tt1 = productions.keySet().iterator();
        while (tt1.hasNext()){
            String key = (String)tt1.next();
            System.out.println(key+": "+productions.get(key));
        }
        
        HashMap<String, Productions> tempproductionsM = new HashMap();
        
        Iterator it = productions.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            Productions pd = productions.get(key);//obtenemos las producciones
            
            Productions tempproductions = new Productions();
            
            for (int i = 0; i < pd.getSize(); i++){//para cada produccion en productions
                
                Production tempproduction = new Production();
                
                //System.out.println("Tamano:"+pd.get(i).getSize());
                for (int j = 0; j < pd.get(i).getSize(); j++){//para cada token de cada produccion
                    int t = Complete(pd.get(i).get(j));
                    if (t==1){//si no es algo
                        return false;//con el primer token no valido, no es valido
                    }else{
                        
                        Token tk = new Token(pd.get(i).get(j));//tomamos el token 
                        
                        tk.setType(t);//actualizamos su tipo
                        
                        tempproduction.addToken(tk);//construimos nuevsas producciones
                    }
                }
                //System.out.println("Producciones: "+tempproduction);
                tempproductions.addProduction(tempproduction);
                tempproductions.setHead(pd.getHead());
            
            }
            
            tempproductionsM.put(key, tempproductions);
            
            
        }
        
        productions = new HashMap();
        productions = tempproductionsM;
        
        /*System.out.println("-------------------------");
        Iterator tt = productions.keySet().iterator();
        while (tt.hasNext()){
            String key = (String)tt.next();
            System.out.println(key+": "+productions.get(key));
        }*/
        
        return true;
    }
    
    public int Complete(Token tk){
        
        if (tk.getType()==1){//si aun no tiene tipo
        
            if (tokens.containsKey(tk.getValue())){//si existe un token con el mismo valor
                //tk.setType(4);
                return 4;
            }
            
            if (productions.containsKey(tk.getValue())){//si existe una produccion con el mismo valor
                //tk.setType(5);
                return 5;
            }
            
            if (tk.getValue().equals("~")){//si es epsilon declarado
                //tk.setType(6);
                return 6;
            }
            
        }else{
            if (tk.getValue().equals("~")) return 6;
            return tk.getType();//ya tiene tipo definido
        }

        return 1;//no es nada
    }
    
    
}
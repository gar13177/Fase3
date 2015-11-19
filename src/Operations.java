
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
    private HashMap<String,LinkedHashSet<Token>> follow;
    private AutoLR0 automata;
    
    
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
    public LinkedHashSet<Token> First(Production pd){
        LinkedHashSet<Token> first = new LinkedHashSet();
        if (pd.get(0).getType()!=5){//si no es produccion
            first.add(pd.get(0));//agregamos el token a first
        }else{//si es produccion
            Productions pds = productions.get(pd.get(0).getValue());//obtenemos todas las producciones de esa cabeza
            LinkedHashSet<Token> fst = new LinkedHashSet();//first temp
            for (int i = 0; i < pds.getSize(); i++){//para cada produccion calculamos First
                /**
                 * aqui es donde se debe modificar para evaluar exclusividad
                 */
                fst.addAll(First(pds.get(i)));//agregamos el first de todas sus producciones
            }
            Token tk = new Token("~",6);//creamos token epsilon
            if (fst.contains(tk)&& pd.getSize()>1){//si fst contiene epsilon, y es de tamano mayor que 1
                pd.remove(0);//quito el token evaluado
                LinkedHashSet<Token> fsn = First(pd);//vuelvo a computar first
                if (!fsn.contains(tk)){//si el first de lo que sigue no contiene epsilon
                    fst.remove(tk);//quitamos epsilon de lo que ya teniamos
                }
                
                first.addAll(fsn);
            }
            first.addAll(fst);
        }
        return first;
    }
    
    public void FirstAll(){
        Iterator it = productions.keySet().iterator();
        while (it.hasNext()){
            String key = (String) it.next();
            Productions pds = productions.get(key);//obtenemos todas las producciones
            for (int i = 0; i < pds.getSize(); i++){//si es la cabeza
                System.out.println("---------------");
                System.out.println("Produccion:"+productions.get(key).get(i));
                System.out.println("First:"+First(productions.get(key).get(i)));
            }
        }
    }
    
    public void Follow(Token tk){
        if (!follow.containsKey(tk.getValue())){//calculamos follow solo si no existe
            /**
             * para calcular follow tenemos que identificar en donde esta ubicado cada uno
             */
            Iterator it = productions.keySet().iterator();
            while (it.hasNext()){
                String key = (String) it.next();//para todas las producciones
                
                Productions pds = productions.get(key);//obtengo todas las producciones
                for (int i = 0; i<pds.getSize(); i++){//recorro cada una de las producciones
                    Production pd = pds.get(i);//tomo una produccion a la vez
                    if (pd.contains(tk)){//si la produccion contiene al token
                        ComputeFollow(tk,pd,key);//calculamos follow para esa produccion
                    }
                    
                }
                
            }
            
        }         
    }
    
    
    /**
     * 
     * @param tk token a buscar para follow
     * @param pd produccion en la que estamos evaluando
     * @param key cabeza de produccion si es necesario
     */
    public void ComputeFollow(Token tk, Production pd, String key){
        if (!follow.containsKey(tk.getValue())){
            follow.put(tk.getValue(), new LinkedHashSet<Token>());//si no existe follow de eso,lo creamos
        }
        for (int i = 0; i < pd.getSize(); i++){//para cada token encontrado
            if (pd.get(i).equals(tk)){//si es igual, computamos follow
                
                if (i != pd.getSize()-1){//si no es el ultimo
                    
                    if (pd.get(i+1).getType()!=5){//si lo que sigue no es produccion
                        
                        LinkedHashSet<Token> fl = follow.get(tk.getValue());//tomamos el follow de hasta ahora
                        fl.add(pd.get(i+1));//agregamos el nuevo token
                        follow.put(tk.getValue(),fl);//actualizamos follow
                    }else{//si lo que sigue es produccion
                        Production pdt = new Production();
                        for (int j = i+1; j<pd.getSize(); j++){//tomamos la produccion desde lo siguiente
                            pdt.addToken(pd.get(j));//agregamos cada token desde i+1
                        }
                        //System.out.println("---------------------\nDonde me encuentro");
                        //System.out.println("Token evaluado: "+tk+", Produccion: "+pd+", Cabeza: "+key);
                        //System.out.println("Nueva produccion: "+pdt);
                        LinkedHashSet<Token> fr = First(pdt);//calculamos first del siguiente
                        Token tktemp = new Token("~",6);//creamos un token epsilon
                        if (fr.contains(tktemp)){//si el first contiene epsilon
                            fr.remove(tktemp);//quitamos epsilon
                            Token tkhead = new Token(key,5);//creamos un token de tipo produccion
                            Follow(tkhead);//calculamos follow de tkhead
                            LinkedHashSet<Token> fl = follow.get(tk.getValue());//tomamos el follow de hasta ahora
                            LinkedHashSet<Token> flh = follow.get(key);//tomamos el follow de la cabeza
                            fl.addAll(flh);//agregamos el follow de la cabeza
                            fl.addAll(fr);//agregamos first
                            follow.put(tk.getValue(),fl);//actualizamos follow
                        }else{//si no contiene epsilon
                            LinkedHashSet<Token> fl = follow.get(tk.getValue());
                            fl.addAll(fr);//agregamos todo el first
                            follow.put(tk.getValue(), fl);//actualizamos follow
                        }
                    }
                }else{
                    Token tkhead = new Token(key,5);//creamos un token de tipo produccion
                    Follow(tkhead);//calculamos follow de tkhead
                    LinkedHashSet<Token> fl = follow.get(tk.getValue());//tomamos el follow de hasta ahora
                    LinkedHashSet<Token> flh = follow.get(key);//tomamos el follow de la cabeza
                    //System.out.println("cabeza:"+key);
                    //System.out.println("follow encontrado:"+flh);
                    //if (flh!= null)
                        fl.addAll(flh);//agregamos el follow de la cabeza
                    follow.put(tk.getValue(),fl);//actualizamos follow
                }
            }
        }
    }
    
    public void FollowAll(){
        follow = new HashMap();
        
        Token tk = new Token("$",0);//token que se agrega al final de primera produccion
        
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
        //System.out.println("Follow------------");
        //System.out.println("Paso base:"+follow);
        
        it = productions.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            tk = new Token(key,5);//creamos el token tipo produccion
            Follow(tk);//calculamos Follow de ese token
            
        }
        
        /*
        it = follow.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            LinkedHashSet fl = follow.get(key);//obtenemos las producciones
            System.out.println("Cabeza: "+key);
            System.out.println(fl);
        }
        */
        
    }
    
         
    public boolean isValid(){
        
        Iterator tt1 = productions.keySet().iterator();
        while (tt1.hasNext()){
            String key = (String)tt1.next();
            //System.out.println(key+": "+productions.get(key));
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
    
    
    public void BuildAutomata(){
        automata = new AutoLR0();
        Iterator it = productions.keySet().iterator();
        
        Items init = new Items();//primer estado
        
        //agrego $ al final de las producciones de la cabeza
        boolean var = true;//variable de salida cuando encuentro la cabeza
        while (it.hasNext() && var){
            String key = (String) it.next();
            if (productions.get(key).getHead()){//si es la cabeza
                Token tk = new Token(key,5);//creo una nueva produccion
                Token tkend = new Token("$",0);//token final
                
                //creo una nueva produccion que me lleve a la cabeza y tenga final
                Production pd = new Production();
                pd.addToken(tk);
                pd.addToken(tkend);
                
                //creo una nueva serie de producciones
                Productions pds = new Productions();
                pds.addProduction(pd);
                
                productions.put(key+key+"00", pds);//creo una nueva cabeza
                //si se la produccion era:
                // s -> a b c.
                //la nueva sera
                // ss -> s $
                
                init = generateItems(key+key+"00");//genero el primer estado
                var = false;
            }
        }
        automata.addState(Closure(init));//agrego primer estado
        
        int states = 0;//cantidad de estados
        int tempstates = 0;//cantidad anterior de estados
        int trans = 0;//cantidad de transiciones
        int temptrans = 0;//cantidad de transiciones anteriores
        
        do{
            tempstates = automata.getSizeStates();
            temptrans = automata.getSizeTransitions();
            for (int i = 0; i < tempstates; i++){//para la cantidad de estados actual
                Items actualstate = automata.getState(i);//estado actual
                for (int j = 0; j < actualstate.getSize(); j++){//para todos los items del estado
                    Item actualitem = actualstate.get(j);//tomo el item actual
                    int index = actualitem.getIndex();//index al que apunta
                    Production pd = actualitem.getProduction();//tomo la produccion actual
                    if (pd.getSize()!=index){//si no esta en el ultimo
                        Token tk = pd.get(index);
                        if (tk.getType() != 0){//si no es $
                            Items nitems = Goto(actualstate,tk);//realizo un goto
                            int state1 = automata.addStateReturnIndex(nitems);//agrego el nuevo estado y tomo su posicion en el array
                            automata.addTransition(i, state1, tk);//agrego la transicion
                        }
                    }
                }    
            }
            states = automata.getSizeStates();//nuevo tamano
            trans = automata.getSizeTransitions();//nuevas transicions

        }while (states != tempstates || trans != temptrans);//mientras alguna sea distinta
        
        new Printer(automata.toStringDraw(),"LR0");
        new Draw("LR0");
       
        System.out.println(automata);
    }
    
    public Items Closure(Items its){
        int size = 0;//valor por defecto para iniciar ciclo
        int nsize = 1;//valor por defecto para iniciar ciclo
        
        while (size!=nsize){
            size = its.getSize();
            Items itst = new Items();//nuevos items temporales
            for (int i = 0; i < its.getSize(); i++){//para cada item de its
                Item actual = its.get(i);//tomamos el item actual
                Production pd = actual.getProduction();//tomo la produccion
                int index = actual.getIndex();//tomo el index al que apunta
                if (index != pd.getSize()){//si no esta de ultimo
                    Token tk = pd.get(index);//tomamos el token al que apunta
                    if (tk.getType()==5){//si es produccion
                        itst.addAll(generateItems(tk.getValue()));
                    }
                }   
            }
            its.addAll(itst);
            nsize = its.getSize();
        }
        
        
        return its;
    }
    
    public Items generateItems(String key){
        Productions pds = productions.get(key);//tomo todas las producciones
        Items its = new Items();//contruyo nuevo conjunto de items
        for (int i = 0; i <pds.getSize(); i++){//para cada produccion
            its.add(new Item(key,pds.get(i),0));
        }
        return its;
    }
    
    public Items Goto(Items its,Token tk){
        Items itt = new Items();
        for (int i = 0; i<its.getSize(); i++){//para cada item
            int index = its.get(i).getIndex();//tomo el index
            Production pd = its.get(i).getProduction();//tomo la produccion
            if (index != pd.getSize()){//si no esta de ultimo
                if (pd.get(index).equals(tk)){//si ambos tokens son iguales
                    Item it = new Item(its.get(i));//construyo un nuevo item igual
                    it.setIndex(index+1);//muevo el index en 1
                    itt.add(it);//lo agrego al nuevo conjunto
                }
            }
            
        }
        return Closure(itt);
    }
    
}

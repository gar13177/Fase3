
import java.util.HashMap;
import java.util.Iterator;

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
    
    
    public Operations(){
        
    }
    
    public Operations(HashMap pd, HashMap tk){
        this.productions = pd;
        this.tokens = tk;
        
    }
    
    public void First(){
        
    }
         
    public boolean isValid(){
        
        HashMap<String, Productions> tempproductionsM = new HashMap();
        
        Iterator it = productions.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            Productions pd = productions.get(key);//obtenemos las producciones
            
            Productions tempproductions = new Productions();
            
            for (int i = 0; i < pd.getSize(); i++){//para cada produccion en productions
                
                Production tempproduction = new Production();
                
                for (int j = 0; j < pd.get(i).getSize(); j++){//para cada token de cada produccion
                    int t = Complete(pd.get(i).get(j));
                    if (t==1){//si no es algo
                        return false;//con el primer token no valido, no es valido
                    }else{
                        
                        Token tk = pd.get(i).get(j);//tomamos el token 
                        
                        tk.setType(t);//actualizamos su tipo
                        
                        tempproduction.addToken(tk);//construimos nuevsas producciones
                    }
                }
                tempproductions.addProduction(tempproduction);
            
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
            return tk.getType();//ya tiene tipo definido
        }

        return 1;//no es nada
    }
    
    
}

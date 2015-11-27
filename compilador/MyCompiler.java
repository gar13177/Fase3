import java.io.IOException;
import java.util.ArrayList;
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
public class MyCompiler {
    
    private HashMap<String,String> tokens = new HashMap();
    private HashMap<String,String> keywords = new HashMap();
    private ArrayList<String> whiteSpace = new ArrayList();
    private HashMap<String,DDFA> tokensDFA = new HashMap();
    
    private ScannerC scanner;
    private String result= "";
    
    private ArrayList<Token> readTk = new ArrayList();//array de tokens leidos
    
    private Table parseTable = new Table();
    
    public MyCompiler(ScannerC scanner){
        
        this.scanner = scanner;
        addInit();
        createDFA();
    }
    
    public MyCompiler(String name){
        
        try{
            this.scanner = new ScannerC(name);
        } catch (IOException e){
            //toDo
        }
        addInit();
        createDFA();
    }
    
    public void addInit(int i){
        
    }
    
public void addInit(){
tokens.put("div","/");
tokens.put("number","˥0ø1ø2ø3ø4ø5ø6ø7ø8ø9ȸ˥˥0ø1ø2ø3ø4ø5ø6ø7ø8ø9ȸȸȹ");
tokens.put("res","-");
tokens.put("op","(");
tokens.put("pc",";");
tokens.put("assignop",":=");
tokens.put("mul","*");
tokens.put("lt","<");
tokens.put("sum","+");
tokens.put("id","˥aøbøcødøeøføgøhøiøjøkølømønøoøpøqørøsøtøuøvøwøxøyøzøAøBøCøDøEøFøGøHøIøJøKøLøMøNøOøPøQøRøSøTøUøVøWøXøYøZȸ˥˥aøbøcødøeøføgøhøiøjøkølømønøoøpøqørøsøtøuøvøwøxøyøzøAøBøCøDøEøFøGøHøIøJøKøLøMøNøOøPøQøRøSøTøUøVøWøXøYøZȸȸȹ");
tokens.put("eq","=");
tokens.put("cp",")");
whiteSpace.add(""+(char)32);
whiteSpace.add(""+(char)9);
whiteSpace.add(""+(char)10);
}
  
    public void createDFA(){
        Iterator it = tokens.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            NFA nfa = new NFA(tokens.get(key));
            DDFA ddfa = new DDFA(nfa.getTransitions(),nfa.getAlphabet());
            tokensDFA.put(key, ddfa);           
        }
        
    }
    
    
    public boolean isSomething(String expr){
        boolean val = false;
        
        Iterator it = tokensDFA.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            val = val || tokensDFA.get(key).recognizes(expr);
        }
        
        it = keywords.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            val = val || keywords.get(key).contains(expr);
        }
        
        val = val || isWhiteSpace(expr);
        
        return val;
    }
    
    public Token getSomething(String expr){
        String type = "";
        Iterator it = keywords.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            if (keywords.get(key).equals(expr)){
                //retorno un token de tipo string con el keyword
                return new Token(expr,2);
            }
        }
        
        it = tokensDFA.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            if (tokensDFA.get(key).recognizes(expr)){
                //retorno el token correspondiente
                return new Token(key,4);
            }
        }
        
        //retorna null por defecto
        return null;
    }
    
    
    public boolean read(){
        char ch = (char)scanner.Peek();
        String st = ""+ch;
        int length = scanner.getLength();//longitud del archivo
        int actpos = 0;//posicion de inicio de lectura
        int newpos = 0;//posicion del string leido
        while (actpos < length){//mientras no se haya leido todo el archivo
            newpos = actpos;//inicializo ambos indicies en el mismo lugar
            for (int i = actpos; i <= length; i++){//recorro todo el posible string
                String eval = scanner.getString(actpos, i);//tomo el string actual
                if (isSomething(eval)){//si es algo
                    newpos = i;//refresco el indice del nuevo string
                }
            }
            
            if (newpos != actpos){
                Token tk = getSomething(scanner.getString(actpos, newpos));//creamos un nuevo token
                if (tk != null){//si es null quiere decir que es whitespace
                    readTk.add(tk);//agregamos el token leido
                }
                actpos = newpos;
            }else{
                System.out.println("No se encontro igual, posicion: "+actpos);
                return false;
            }
            
        }
        readTk.add(new Token("$",0));//agregamos el ultimo token al final
        System.out.println(readTk);
        return true;
    }

    public String getResult(){
        return result;
    }

    public boolean isWhiteSpace(String st){
        return whiteSpace.contains(st);
    }
 
}
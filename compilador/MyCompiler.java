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
    private ArrayList<Character> whiteSpace = new ArrayList();
    private HashMap<String,DDFA> tokensDFA = new HashMap();
    
    private ScannerC scanner;
    private String result= "";
    
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
    
public void addInit(){
tokens.put("number","˥0ø1ø2ø3ø4ø5ø6ø7ø8ø9ȸ˥˥0ø1ø2ø3ø4ø5ø6ø7ø8ø9ȸȸȹ");
tokens.put("id","˥aøbøcødøeøføgøhøiøjøkølømønøoøpøqørøsøtøuøvøwøxøyøzøAøBøCøDøEøFøGøHøIøJøKøLøMøNøOøPøQøRøSøTøUøVøWøXøYøZȸ˥˥aøbøcødøeøføgøhøiøjøkølømønøoøpøqørøsøtøuøvøwøxøyøzøAøBøCøDøEøFøGøHøIøJøKøLøMøNøOøPøQøRøSøTøUøVøWøXøYøZȸȸȹ");
tokens.put("hexnumber","˥AøBøCøDøEøFø0ø1ø2ø3ø4ø5ø6ø7ø8ø9ȸ˥˥AøBøCøDøEøFø0ø1ø2ø3ø4ø5ø6ø7ø8ø9ȸȸȹH");
keywords.put("while","while");
keywords.put("if","if");
whiteSpace.add((char)32);
whiteSpace.add((char)9);
whiteSpace.add((char)10);
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
        
        return val;
    }
    
    public String getSomething(String expr){
        String type = "";
        Iterator it = keywords.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            if (keywords.get(key).equals(expr)){
                return key;
            }
        }
        
        it = tokensDFA.keySet().iterator();
        while (it.hasNext()){
            String key = (String)it.next();
            if (tokensDFA.get(key).recognizes(expr)){
                return key;
            }
        }

        return type;
    }
    
    
    public void read(){
        char ch = (char)scanner.Peek();
        String st = ""+ch;
        
        while (scanner.Peek()!=-1){
            //System.out.println(st);
            if (isSomething(st)){
                scanner.NextCh();
                st += ""+(char)scanner.Peek();
            }else{
                if (st.length()>1){
                    st = st.substring(0,st.length()-1);
                    result += "<"+st+","+getSomething(st)+"> ";
                    st = ""+(char)scanner.Peek();
                }else{
                    //if (!isWhiteSpace(st)) result += "<"+st+", ERROR>";
                    scanner.NextCh();
                    st = ""+(char)scanner.Peek();
                }
            }
        }
    }

    public String getResult(){
        return result;
    }

    public boolean isWhiteSpace(String st){
        return whiteSpace.contains(st);
    }
 
}
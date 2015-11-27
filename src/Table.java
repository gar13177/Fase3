
import java.util.ArrayList;
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
public class Table {
    private int states;//cantidad de estados
    private ArrayList<Token> columns;//columas <tokens o producciones y terminales
    private ArrayList<Transition> transitions;//transiciones para shift y goto
    private ArrayList<Relation> reduce;//conjunto reduce
    private Relation accept;
    
    private Action table[][];//table que guarda las acciones
    
    public Table(){
        //-->ADDINIT
    }
    
    public Table(int states, ArrayList<Token> columns, ArrayList<Transition> transitions, ArrayList<Relation> reduce){
        this.states = states;
        this.columns = columns;
        this.transitions = transitions;
        this.reduce = reduce;        
    }
    
    public Table(AutoLR0 automata,ArrayList<Relation> reduce){
        LinkedHashSet<Token> columnstemp = new LinkedHashSet();//arreglo temporal de tokens unicos
        
        this.states = automata.getSizeStates();
        this.transitions = automata.getTransitions();
        this.reduce = reduce;
        
        for (int i = 0; i < states; i++){//recorremos todo el automata
            Items state = automata.getState(i);//tomamos el estado actual
            for (int j = 0; j < state.getSize(); j++){//para cada item del estado
                Item it = state.get(j);//tomamos el item
                columnstemp.addAll(it.getProduction().getProduction());//agregamos todos los tokens de la produccion
            }
        }
        
        columns = new ArrayList();
        columns.addAll(columnstemp);//agrego el linkedhashset
        
        for (int i = 0; i<automata.getSizeStates(); i++){//para cada estado
            Items state = automata.getState(i);//tomamos el estado actual
            for (int j = 0; j<state.getSize(); j++){//para cada item en el estado
                Item it = state.get(j);//tomamos el item actual
                int index = it.getIndex();
                if (index!=it.getProduction().getSize()){
                    Token tk = it.getProduction().get(index);//tomamos el token al que apunta
                    Token acc = new Token("$",0);
                    if (tk.equals(acc)){//si estamos en el estado de aceptacion
                        accept = new Relation(i,acc);

                    }
                }   
            }
        }
        
    }
    
    public boolean buildTable(){
        table = new Action[states][columns.size()];//creamos la tabla
        for (int i = 0; i<transitions.size(); i++){//recorremos cada transicion
            Transition tr = transitions.get(i);//transicion actual
            Token tk = tr.getTransition();//token con el cual se realiza la transicion
            int ini = tr.getState0();//estado de donde sale
            if (table[ini][columns.indexOf(tk)]==null){//si aun no hay nada en esa casilla
                int action = 0;
                if (tk.getType() == 5) action = 1;//si es produccion es goto
                Action ac = new Action(action,tr.getState1());//creamos nueva accion
                table[ini][columns.indexOf(tk)]=ac;
            }else{
                return false;
            }
            
        }
        
        for (int i = 0; i<reduce.size(); i++){//recorremos cada reduce
            Relation rl = reduce.get(i);//reduce actual
            Token tk = rl.getToken();//token con el cual se realiza la transicion
            int ini = rl.getState();//estado de donde sale
            if (table[ini][columns.indexOf(tk)]==null){//si aun no hay nada en esa casilla
                int action = 2;
                
                Action ac = new Action(action,rl.getProduction().getSize(),rl.getHead());//creamos nueva accion
                table[ini][columns.indexOf(tk)]=ac;
            }else{
                return false;
            }
            
        }
        
        //accept
        int ini = accept.getState();//estado accept
        Token tk = accept.getToken();//token de transicion
        if (table[ini][columns.indexOf(tk)]==null){//si aun no hay nada en esa casilla
            
            Action ac = new Action();
            ac.setAction(3);
            table[ini][columns.indexOf(tk)] = ac;            
        }
        
        return true;   
    }
    
    public String toString(){
        String st= "";
        for (int i = 0; i< states; i++){//para cada columna
            for (int j = 0; j < columns.size(); j++){//para cada renglon
                st += ", "+table[i][j];
            }
            st += "\n";
        }
        return st;
    }
    
    public String toStringDraw(){
        String txt = "digraph parse_table {\n";
        
        txt += "node [shape = record];\na [label =\"";
        txt+="{|";
        for (int i = 0; i<states; i++){
            txt+=i;
            if (i != states-1)
                txt +="|";
        }
        txt+="}|";
        for (int i = 0; i< columns.size(); i++){//para cada renglon
            txt += "{"+columns.get(i).getString()+"|";
            for (int j = 0; j < states; j++){//para cada columna
                if (table[j][i]!=null)
                    txt += table[j][i];
                if (j!=states-1)
                    txt += "|";
            }
            txt += "}";
            if (i != columns.size()-1)
                txt+="|";
        }
        txt+="\"];\n";
        
        txt += "}";
        
        
        return txt;
    }
    
    public String autoString(){
        String val = "";
        val += "this.states = "+this.states+";\n";
        val += "this.columns = new ArrayList();\n";
        for (Token tk: columns){//para cada token
            val += "this.columns.add(new Token(\""+tk.getValue()+"\","+tk.getType()+"));\n";                    
        }
        val += "table = new Action[this.states][this.columns.size()];\n";
        
        for (int i = 0; i < this.columns.size(); i++){//para cada columna
            for (int j = 0; j < this.states; j++){//para cada renglon
                Action ac = table[j][i];//tomamos cada accion
                if (ac != null)
                    if (ac.getAction() ==3){
                        val += "Action ac = new Action();\n";
                        val += "ac.setAction(3);\n";
                        val += "table["+j+"]["+i+"] = ac;\n";
                        
                    }else{
                        val += "table["+j+"]["+i+"] = new Action("+ac.getAction()+","+ac.getNumber();
                        if (ac.getHead() != null)
                            val += ",\""+ac.getHead()+"\"";
                        val += ");\n";  
                    }
                      
            }
            
        }
        //System.out.println(val);
        return val;
    }
    
    
    //tomamos una accion por medio de i,j
    public Action getAction(int i, int j){
        //i renglon
        //j columna
        if (i > states) return null;
        if (j > columns.size()) return null;
        
        return table[i][j];
    }
    
    public Action getAction(int i, Token tk){
        //i renglon
        //j columna
        if (i > states) return null;
        if (!columns.contains(tk)) return null;
        
        int j = columns.indexOf(tk);
        
        return table[i][j];
    }
    
    
    
}


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
        this.states = 30;
this.columns = new ArrayList();
this.columns.add(new Token("program",5));
this.columns.add(new Token("$",0));
this.columns.add(new Token("stmtSeq",5));
this.columns.add(new Token("pc",4));
this.columns.add(new Token("stmt",5));
this.columns.add(new Token("assignstmt",5));
this.columns.add(new Token("id",4));
this.columns.add(new Token("assignop",4));
this.columns.add(new Token("exp",5));
this.columns.add(new Token("simpleExp",5));
this.columns.add(new Token("lt",4));
this.columns.add(new Token("eq",4));
this.columns.add(new Token("sum",4));
this.columns.add(new Token("term",5));
this.columns.add(new Token("res",4));
this.columns.add(new Token("mul",4));
this.columns.add(new Token("factor",5));
this.columns.add(new Token("div",4));
this.columns.add(new Token("op",4));
this.columns.add(new Token("cp",4));
this.columns.add(new Token("number",4));
table = new Action[this.states][this.columns.size()];
table[0][0] = new Action(1,1);
Action ac = new Action();
ac.setAction(3);
table[1][1] = ac;
table[2][1] = new Action(2,1,"program");
table[3][1] = new Action(2,1,"stmtSeq");
table[4][1] = new Action(2,1,"stmt");
table[8][1] = new Action(2,3,"stmtSeq");
table[9][1] = new Action(2,3,"assignstmt");
table[10][1] = new Action(2,1,"exp");
table[11][1] = new Action(2,1,"simpleExp");
table[12][1] = new Action(2,1,"term");
table[14][1] = new Action(2,1,"factor");
table[15][1] = new Action(2,1,"factor");
table[23][1] = new Action(2,3,"exp");
table[24][1] = new Action(2,3,"exp");
table[25][1] = new Action(2,3,"simpleExp");
table[26][1] = new Action(2,3,"simpleExp");
table[27][1] = new Action(2,3,"term");
table[28][1] = new Action(2,3,"term");
table[29][1] = new Action(2,3,"factor");
table[0][2] = new Action(1,2);
table[2][3] = new Action(0,6);
table[3][3] = new Action(2,1,"stmtSeq");
table[4][3] = new Action(2,1,"stmt");
table[8][3] = new Action(2,3,"stmtSeq");
table[9][3] = new Action(2,3,"assignstmt");
table[10][3] = new Action(2,1,"exp");
table[11][3] = new Action(2,1,"simpleExp");
table[12][3] = new Action(2,1,"term");
table[14][3] = new Action(2,1,"factor");
table[15][3] = new Action(2,1,"factor");
table[23][3] = new Action(2,3,"exp");
table[24][3] = new Action(2,3,"exp");
table[25][3] = new Action(2,3,"simpleExp");
table[26][3] = new Action(2,3,"simpleExp");
table[27][3] = new Action(2,3,"term");
table[28][3] = new Action(2,3,"term");
table[29][3] = new Action(2,3,"factor");
table[0][4] = new Action(1,3);
table[6][4] = new Action(1,8);
table[0][5] = new Action(1,4);
table[6][5] = new Action(1,4);
table[0][6] = new Action(0,5);
table[6][6] = new Action(0,5);
table[7][6] = new Action(0,15);
table[13][6] = new Action(0,15);
table[16][6] = new Action(0,15);
table[17][6] = new Action(0,15);
table[18][6] = new Action(0,15);
table[19][6] = new Action(0,15);
table[20][6] = new Action(0,15);
table[21][6] = new Action(0,15);
table[5][7] = new Action(0,7);
table[7][8] = new Action(1,9);
table[13][8] = new Action(1,22);
table[7][9] = new Action(1,10);
table[13][9] = new Action(1,10);
table[16][9] = new Action(1,23);
table[17][9] = new Action(1,24);
table[10][10] = new Action(0,16);
table[11][10] = new Action(2,1,"simpleExp");
table[12][10] = new Action(2,1,"term");
table[14][10] = new Action(2,1,"factor");
table[15][10] = new Action(2,1,"factor");
table[25][10] = new Action(2,3,"simpleExp");
table[26][10] = new Action(2,3,"simpleExp");
table[27][10] = new Action(2,3,"term");
table[28][10] = new Action(2,3,"term");
table[29][10] = new Action(2,3,"factor");
table[10][11] = new Action(0,17);
table[11][11] = new Action(2,1,"simpleExp");
table[12][11] = new Action(2,1,"term");
table[14][11] = new Action(2,1,"factor");
table[15][11] = new Action(2,1,"factor");
table[25][11] = new Action(2,3,"simpleExp");
table[26][11] = new Action(2,3,"simpleExp");
table[27][11] = new Action(2,3,"term");
table[28][11] = new Action(2,3,"term");
table[29][11] = new Action(2,3,"factor");
table[10][12] = new Action(0,18);
table[11][12] = new Action(2,1,"simpleExp");
table[12][12] = new Action(2,1,"term");
table[14][12] = new Action(2,1,"factor");
table[15][12] = new Action(2,1,"factor");
table[23][12] = new Action(0,18);
table[24][12] = new Action(0,18);
table[25][12] = new Action(2,3,"simpleExp");
table[26][12] = new Action(2,3,"simpleExp");
table[27][12] = new Action(2,3,"term");
table[28][12] = new Action(2,3,"term");
table[29][12] = new Action(2,3,"factor");
table[7][13] = new Action(1,11);
table[13][13] = new Action(1,11);
table[16][13] = new Action(1,11);
table[17][13] = new Action(1,11);
table[18][13] = new Action(1,25);
table[19][13] = new Action(1,26);
table[10][14] = new Action(0,19);
table[11][14] = new Action(2,1,"simpleExp");
table[12][14] = new Action(2,1,"term");
table[14][14] = new Action(2,1,"factor");
table[15][14] = new Action(2,1,"factor");
table[23][14] = new Action(0,19);
table[24][14] = new Action(0,19);
table[25][14] = new Action(2,3,"simpleExp");
table[26][14] = new Action(2,3,"simpleExp");
table[27][14] = new Action(2,3,"term");
table[28][14] = new Action(2,3,"term");
table[29][14] = new Action(2,3,"factor");
table[11][15] = new Action(0,20);
table[12][15] = new Action(2,1,"term");
table[14][15] = new Action(2,1,"factor");
table[15][15] = new Action(2,1,"factor");
table[25][15] = new Action(0,20);
table[26][15] = new Action(0,20);
table[27][15] = new Action(2,3,"term");
table[28][15] = new Action(2,3,"term");
table[29][15] = new Action(2,3,"factor");
table[7][16] = new Action(1,12);
table[13][16] = new Action(1,12);
table[16][16] = new Action(1,12);
table[17][16] = new Action(1,12);
table[18][16] = new Action(1,12);
table[19][16] = new Action(1,12);
table[20][16] = new Action(1,27);
table[21][16] = new Action(1,28);
table[11][17] = new Action(0,21);
table[12][17] = new Action(2,1,"term");
table[14][17] = new Action(2,1,"factor");
table[15][17] = new Action(2,1,"factor");
table[25][17] = new Action(0,21);
table[26][17] = new Action(0,21);
table[27][17] = new Action(2,3,"term");
table[28][17] = new Action(2,3,"term");
table[29][17] = new Action(2,3,"factor");
table[7][18] = new Action(0,13);
table[13][18] = new Action(0,13);
table[16][18] = new Action(0,13);
table[17][18] = new Action(0,13);
table[18][18] = new Action(0,13);
table[19][18] = new Action(0,13);
table[20][18] = new Action(0,13);
table[21][18] = new Action(0,13);
table[10][19] = new Action(2,1,"exp");
table[11][19] = new Action(2,1,"simpleExp");
table[12][19] = new Action(2,1,"term");
table[14][19] = new Action(2,1,"factor");
table[15][19] = new Action(2,1,"factor");
table[22][19] = new Action(0,29);
table[23][19] = new Action(2,3,"exp");
table[24][19] = new Action(2,3,"exp");
table[25][19] = new Action(2,3,"simpleExp");
table[26][19] = new Action(2,3,"simpleExp");
table[27][19] = new Action(2,3,"term");
table[28][19] = new Action(2,3,"term");
table[29][19] = new Action(2,3,"factor");
table[7][20] = new Action(0,14);
table[13][20] = new Action(0,14);
table[16][20] = new Action(0,14);
table[17][20] = new Action(0,14);
table[18][20] = new Action(0,14);
table[19][20] = new Action(0,14);
table[20][20] = new Action(0,14);
table[21][20] = new Action(0,14);

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
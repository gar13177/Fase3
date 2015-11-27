
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kevin
 * Kevin Estuardo Garcia Guerra
 * Carne 13177
 */
public class DFA {
    
    private boolean[] accept;
    //private Set<Set> states = new LinkedHashSet();
    private List<Digraph> nstates;
    private String alphabet;
    private int init;
    private long time;
    
    public DFA(String alp, char[] re, Digraph G, int M){
        time = System.currentTimeMillis();
        alphabet = alp;
        List<Set> states;
        Set<Integer> arraytemp1 = new LinkedHashSet();
        
        arraytemp1.add(0);//se empieza en el estado 0
        
        Set<Set> arraystate = new LinkedHashSet();//states
        //arraystate.add(eClosure(arraytemp1,G));//primer e-closure
        
        Set<Set> nextarray = new LinkedHashSet();
        nextarray.add(eClosure(arraytemp1,G));//next con e-closure
        
        
        Set<Set> nextarraytemp;
        
        do{
            nextarraytemp = new LinkedHashSet();
            for(Set<Integer> st:nextarray){
                
                if (!arraystate.contains(st) && !st.contains(-1)){

                    for (int index =0; index<alp.length(); index++){
                        arraytemp1 = new LinkedHashSet();
                        
                        for (int i:st){
                            //System.out.println(alp.charAt(i));
                            if (i != re.length){
                                if(re[i]==alp.charAt(index)){
                                    arraytemp1.add(i+1);   
                                }//agrega los que tienen move
                            }
                        }//arraytemp1 tiene los de move con 1 char
                       
                        if (arraytemp1.isEmpty()){//si esta vacio se agrega null
                            arraytemp1.add(-1);
                            arraystate.add(arraytemp1);
                        }else{
                            //aplico e-closure al move
                            arraytemp1 = new LinkedHashSet(eClosure(arraytemp1,G));   
                        }
                        
                        nextarraytemp.add(arraytemp1);//guardo los siguientes
                    }
                    arraystate.add(st);
                }
            }
            nextarray = new LinkedHashSet(nextarraytemp);
            //System.out.println(arraystate);
        }while (!nextarraytemp.isEmpty());
        //arraystate completo
        
        states = new ArrayList(arraystate);
        arraytemp1 = new LinkedHashSet();
        arraytemp1.add(0);
        arraytemp1 = eClosure(arraytemp1,G);
        
        init = states.indexOf(arraytemp1);
        
        //estados de aceptacion
        accept = new boolean[states.size()];
        for (int index = 0;index <states.size();index++){
            accept[index] = false;
            if (states.get(index).contains(M)){
                accept[index]=true;
            }
        }
        
        nstates = new ArrayList();
        for(int i = 0; i<alp.length();i++){
            nstates.add(new Digraph(states.size()));
        }
        
        
        for (int i = 0; i < alp.length(); i++){
            for (Set<Integer> st: states){
                arraytemp1 = new LinkedHashSet();
                for (int index: st){
                    if(index != -1 && index!=re.length){
                        if(re[index] == alp.charAt(i)){
                            arraytemp1.add(index+1);
                        }
                    }
                }
                if (arraytemp1.isEmpty()){
                    arraytemp1.add(-1);
                }else{
                    //System.out.println(G);
                    arraytemp1 = eClosure(arraytemp1,G);
                }
                
                nstates.get(i).addEdge(states.indexOf(st), states.indexOf(arraytemp1));
            }
        }
                
        time = System.currentTimeMillis()-time;
           
    }
    
    public boolean recognizes(String txt){
        for (int i = 0; i< txt.length(); i++){
            
            if (!alphabet.contains(""+txt.charAt(i)))
                return false;
        }
        int state = init;
        
        for (int i = 0; i<txt.length(); i++){
            int j = alphabet.indexOf(txt.charAt(i));
            for (int w: nstates.get(j).adj(state))
                state = w;
        }

        return accept[state];
    }

    @Override
    public String toString(){
        String txt = "ESTADOS = {";
        for (int i = 0; i < accept.length; i++){
            txt += " "+i;
            if (i != accept.length-1) txt +=",";
        }
        txt += "}\n";
        
        txt += "SIMBOLOS ={";
        for (int i = 0; i< alphabet.length(); i++){
            txt += " "+alphabet.charAt(i);
            if (i != alphabet.length()-1) txt += ",";
        }
        txt += "}\nINICIO = {"+init+"}\n";
        
        txt += "ACEPTACION = {";
        for (int i = 0; i < accept.length; i++){
            if (accept[i]){
                txt += " "+i;
                if (i != accept.length-1) txt += ",";
            }
            
        }
        txt += "}\n";
        
        txt += "TRANSICION = ";
        
        for (int i = 0; i < alphabet.length(); i++){
            txt += nstates.get(i).rawString();
            txt = txt.replace('~',alphabet.charAt(i)); 
        }
        txt += "\nTIEMPO = "+time+" milisegundos";
        return txt;
    }
    
    public Set eClosure(Set<Integer> arraytemp, Digraph G){
              
        boolean ctemp;
        Set<Integer> array;
        do{
            array = new LinkedHashSet(arraytemp);
            for (int x : arraytemp){
                for (int w: G.adj(x)){
                    //System.out.println(w);
                    array.add(w); 
                }
            }
            ctemp = array.equals(arraytemp);
            arraytemp = array;
        }while (!ctemp);
        return arraytemp;
    }
    
    public void minimization(){
        Set<Set> partitions = new LinkedHashSet();
        for (int i = 0; i<accept.length; i++){
            Set<Integer> temp = new LinkedHashSet();
            temp.add(i);
            for (int j = 0; j<accept.length; j++){
                if (accept[i] == accept[j]){
                    boolean equals = true;
                    
                    for(int index = 0; index < alphabet.length(); index++){
                        for (int k1: nstates.get(index).adj(j)){
                            for (int k2: nstates.get(index).adj(i))
                                if (k1!=k2) equals = false;
                        }
                        
                    }
                    if (equals){
                        temp.add(j);
                    }
                }
            }
            partitions.add(temp);
        }        
        List<Set> states = new ArrayList(partitions);
        
        //nuevo inicio;
        int ninit = 0;
        
        for (int i = 0; i<states.size(); i++){
            if (states.get(i).contains(init))
                ninit = i;
        }
        
        boolean[] naccept = new boolean[states.size()];
        for (int index = 0;index <states.size();index++){
            naccept[index] = false;
            for (int i = 0; i < accept.length; i++){
                if (accept[i] && states.get(index).contains(i))
                    naccept[index] = true;
            }   
        }
        
        List<Digraph> nnstates = new ArrayList();
        for(int i = 0; i<alphabet.length();i++){
            nnstates.add(new Digraph(states.size()));
        }
        
        Set<Integer> arraytemp1;
        for (int i = 0; i < alphabet.length(); i++){
            for (Set<Integer> st: states){
                arraytemp1 = new LinkedHashSet();
                List<Integer> temp = new ArrayList(st);
                int tempn = temp.get(0);//tengo el primero de cada set
                
                for (int j: nstates.get(i).adj(tempn)){
                    //j es la posicion a donde apunta originalmente
                    for(Set<Integer> stn: states){
                        if (stn.contains(j))
                            nnstates.get(i).addEdge(states.indexOf(st),states.indexOf(stn));
                    }
                }
            }
        } 
        if (accept.length != naccept.length){
            nstates = new ArrayList(nnstates);
            init = ninit;
            accept = naccept;
            minimization();    
        }
        //minimizationH();
   
    }
    
    public void minimizationH(){
        String[][] table = new String[accept.length][accept.length];
        
        for (int i = 0; i<accept.length; i++){
            for (int j = 0; j<accept.length; j++){
                if (accept[i]!=accept[j]) table[i][j]="x";
            }
        }
        
        String[][] temptable = new String[accept.length][accept.length];
        List<Tuple> acumulados = new ArrayList();
        
        do{
            for (int i = 0; i<accept.length; i++){
                for (int j = 0; j<accept.length; j++){
                    temptable[i][j] = table[i][j];
                }
            }
            //System.out.print("\n");
            for (int i = 0; i<accept.length; i++){
                for (int j=0; j<accept.length; j++){
                    //si no estan marcados
                    
                    if (i == j) continue;
                    if (temptable[i][j]==null){
                        
                        //se revisan las transiciones
                        for (int alp = 0; alp<alphabet.length(); alp++){
                            Digraph dg = nstates.get(alp);
                            for (int k:dg.adj(i)){
                                for (int l: dg.adj(j)){
                                    if (temptable[k][l]!=null){
                                        
                                        temptable[i][j] = "x";
                                        temptable[j][i] = "x";
                                    }
                                }
                            }
                        }
                        //si logro ser marcada
                        if (temptable[i][j]!= null){
                            Set<Integer> tempn = new LinkedHashSet();
                            tempn.add(i);
                            tempn.add(j);
                            for (Tuple t: acumulados){
                                if (t.getD().contains(tempn)){
                                    for (Set<Integer> st1: t.getD()){
                                        //System.out.println(st1);
                                        List<Integer> templist = new ArrayList(st1);
                                        int i1 = Collections.min(templist);
                                        int i2 = Collections.max(templist);
                                        temptable[i1][i2] = "x";
                                        temptable[i2][i1] = "x";
                                    }
                                    List<Integer> templist = new ArrayList(t.gets());
                                    int i1 = Collections.min(templist);
                                    int i2 = Collections.max(templist);
                                    temptable[i1][i2] = "x";
                                    temptable[i2][i1] = "x";
                                    
                                    acumulados.remove(t);
                                }
                            }
                        }else{//de lo contrario
                            for (int alp = 0; alp<alphabet.length(); alp++){
                                Digraph dg = nstates.get(alp);
                                for (int k: dg.adj(i)){
                                    for (int l: dg.adj(j)){
                                        if (k!=l){
                                            Set<Integer> st = new LinkedHashSet();
                                            st.add(k);
                                            st.add(l);
                                            Set<Integer> st2 = new LinkedHashSet();
                                            st2.add(i);
                                            st2.add(j);
                                            if (acumulados.contains(st)){
                                                for (Tuple t:acumulados){
                                                    if (t.equals(st)){
                                                        t.add(st2);
                                                    }
                                                }
                                            }else{
                                                Tuple t = new Tuple(new LinkedHashSet(st),st2);
                                            }}
                                    }
                                }
                            }
                        }
                            
                        
                    }
                }
            }
            
            for (int i = 0; i<accept.length; i++){
            for (int j = 0; j<accept.length; j++){
                System.out.print(table[i][j]+"\t");
            }
            System.out.print("\n");
        }
            for (int i = 0; i<accept.length; i++){
            for (int j = 0; j<accept.length; j++){
                System.out.print(temptable[i][j]+"\t");
            }
            System.out.print("\n");
        }
            StdIn.readLine();
            //quitar bloque de impresion arriba y llamada al metodo en reduccion
            
        }while(!temptable.equals(table));
        
        for (int i = 0; i<accept.length; i++){
            for (int j = 0; j<accept.length; j++){
                System.out.print(table[i][j]+"\t");
            }
            System.out.print("\n");
        }
        
        
        
        
        
        
    }
    
    
    
    
    public String toStringDraw(){
        
        String txt = "digraph finite_state_machine {\n";
        txt += "rankdir=LR;\n";
        //txt += "size=\"8.5\";\n";
        
        txt += "node [shape = doublecircle style = filled];\n";
        for (int i = 0; i < accept.length; i++){
            if (accept[i])
                txt += i+" ";
        }
        txt += ";\n";
        
        txt += "node [shape = circle];\n";
        
        for (int index = 0; index < alphabet.length(); index++){
            //tengo el digrafo de cada letra
            Digraph dg = nstates.get(index);
            //para cada vertice
            for (int i = 0; i<dg.V(); i++){
                //para cada conexion
                for (int j: dg.adj(i)){
                    txt += i+" -> "+j+" [ label = \""+alphabet.charAt(index)+"\" ];\n";
                }
            }
        }
        
        txt += init+" [fillcolor = \"red\" ";
        if (accept[init])
            txt+= "shape = doublecircle ];\n";
        else
           txt+= "shape = circle ];\n"; 
        
        
        
        txt += "}";
        //System.out.println(txt);
        
        return txt;
    }
} 

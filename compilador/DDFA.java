
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kevin
 */
public class DDFA {
    private boolean[] accept;
    //private Set<Set> states = new LinkedHashSet();
    private List<Digraph> nstates;
    private String alphabet;
    private int init;
    private long time;
    
    public DDFA(String regex, String alp){
        time = System.currentTimeMillis();
        alphabet = alp;
        regex += "!";
        InfixToPostfix rg = new InfixToPostfix();
        
        //texto en postfix
        regex = rg.convert(regex);
        
        //conexiones del texto
        Digraph dg = new Digraph(regex.length());
        dg = rg.toTreeR(regex,dg,regex.length()-1);
        //System.out.println(dg);
        
        //nullable 
        boolean[] nullable = new boolean[regex.length()];
        for (int i = 0; i< regex.length(); i++){
            nullable[i] = false;
            if (regex.charAt(i)=='~' || regex.charAt(i)=='*')
                nullable[i] = true;
            else if(regex.charAt(i)=='|'){
                boolean temp = false;
                for (int j: dg.adj(i)){
                    if (nullable[j]) temp = true;
                }
                nullable[i] = temp;
                
            }else if(regex.charAt(i)=='.'){
                boolean temp = true;
                for (int j: dg.adj(i)){
                    if (!nullable[j]) temp = false;
                }
                nullable[i] = temp;
            }
        }
        
        
        Set<Integer>[] firstpos = new LinkedHashSet[regex.length()];
        for (int i = 0; i<regex.length(); i++){
            if (alp.contains(""+regex.charAt(i))|| regex.charAt(i)=='!'){
                Set<Integer> temp = new LinkedHashSet();
                temp.add(i);
                firstpos[i] = temp;
            }
            else if(regex.charAt(i)=='|'){
                Set<Integer> temp = new LinkedHashSet();
                for (int j: dg.adj(i)){
                    if (firstpos[j] != null)
                        temp.addAll(firstpos[j]);
                }
                firstpos[i] = temp;
            }
            else if(regex.charAt(i)=='.'){
                List<Integer> tempst = new ArrayList();
                for (int j: dg.adj(i)){
                    tempst.add(j);
                }
                //hoja izquierda
                int lf = Collections.min(tempst);
                
                Set<Integer> temp = new LinkedHashSet();
                if (nullable[lf]){
                    for (int j: dg.adj(i)){
                        if (firstpos[j] != null)
                            temp.addAll(firstpos[j]);
                    }
                    firstpos[i] = temp;
                }else{
                    if (firstpos[lf] != null)
                        temp.addAll(firstpos[lf]);
                }
                firstpos[i] = temp;
            }else if(regex.charAt(i)=='*'){
                Set<Integer> temp = new LinkedHashSet();
                for (int j: dg.adj(i)){
                    if (firstpos[j] !=null)
                        temp.addAll(firstpos[j]);
                }
                firstpos[i] = temp;
            }
        }
        
        Set<Integer>[] lastpos = new LinkedHashSet[regex.length()];
        for (int i = 0; i<regex.length(); i++){
            if (alp.contains(""+regex.charAt(i))|| regex.charAt(i)=='!'){
                Set<Integer> temp = new LinkedHashSet();
                temp.add(i);
                lastpos[i] = temp;
            }
            else if(regex.charAt(i)=='|'){
                Set<Integer> temp = new LinkedHashSet();
                for (int j: dg.adj(i)){
                    if (lastpos[j] != null)
                        temp.addAll(lastpos[j]);
                }
                lastpos[i] = temp;
            }
            else if(regex.charAt(i)=='.'){
                List<Integer> tempst = new ArrayList();
                for (int j: dg.adj(i)){
                    tempst.add(j);
                }
                //hoja izquierda
                int lf = Collections.max(tempst);
                
                Set<Integer> temp = new LinkedHashSet();
                if (nullable[lf]){
                    for (int j: dg.adj(i)){
                        if (lastpos[j] != null)
                            temp.addAll(lastpos[j]);
                    }
                    lastpos[i] = temp;
                }else{
                    if (lastpos[lf] != null)
                        temp.addAll(lastpos[lf]);
                }
                lastpos[i] = temp;
            }else if(regex.charAt(i)=='*'){
                Set<Integer> temp = new LinkedHashSet();
                for (int j: dg.adj(i)){
                    if (lastpos[j] !=null)
                        temp.addAll(lastpos[j]);
                }
                lastpos[i] = temp;
            }
        }
        
        Set<Integer>[] followpos = new LinkedHashSet[regex.length()];
        for (int i = 0;  i<regex.length(); i++)
            followpos[i] = new LinkedHashSet();
        for (int i = 0; i<regex.length(); i++){
            
            if(regex.charAt(i)=='.'){
                List<Integer> tempst = new ArrayList();
                for (int j: dg.adj(i)){
                    tempst.add(j);
                }
                //hoja izquierda
                int lf = Collections.min(tempst);
                int rgh = Collections.max(tempst);
                Set<Integer> temp = new LinkedHashSet();
                for(int j: lastpos[lf]){
                    if (firstpos[rgh]!= null)
                        temp.addAll(firstpos[rgh]);
                    followpos[j].addAll(temp);
                }
                
            }else if(regex.charAt(i)=='*'){
                Set<Integer> temp = new LinkedHashSet();
                for (int j: lastpos[i]){
                    if (firstpos[i] !=null)
                        temp.addAll(firstpos[i]);
                    followpos[j].addAll(temp);
                }
                
            }
        }
        
        Set<Set> arraystate = new LinkedHashSet();
        Set<Set> nextarray = new LinkedHashSet();
        nextarray.add(firstpos[regex.length()-1]);
        Set<Integer> arraytemp1;
        Set<Set> nextarraytemp;
        do{
            nextarraytemp = new LinkedHashSet();
            for (Set<Integer> st: nextarray){
                if (!arraystate.contains(st)){
                    Set<Integer> temp = new LinkedHashSet();
                    for (int index = 0; index < alp.length(); index ++){
                        arraytemp1 = new LinkedHashSet();
                        for (int i: st){
                            if (regex.charAt(i)==alp.charAt(index)){
                                arraytemp1.addAll(followpos[i]);
                            }
                        } 
                       nextarraytemp.add(arraytemp1);
                    }
                    arraystate.add(st);
                }  
            }
            
           nextarray = new LinkedHashSet(nextarraytemp); 
        }while (!nextarraytemp.isEmpty());
        //todos los posibles estados
        List<Set> states = new ArrayList(arraystate);
        init = states.indexOf(firstpos[regex.length()-1]);
        
        accept = new boolean[states.size()];
        for (int index = 0;index <states.size();index++){
            accept[index] = false;
            if (states.get(index).contains(regex.length()-2)){
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
                    if(regex.charAt(index) == alp.charAt(i)){
                        arraytemp1.addAll(followpos[index]);
                    }
                }           
                //System.out.println(arraytemp1);
                nstates.get(i).addEdge(states.indexOf(st), states.indexOf(arraytemp1));
            }
        } 
        
        time = System.currentTimeMillis()-time;
        minimization();//reduccion automatica
        
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
    
    public String valTxt(String txt){
        
        int i = txt.length();
        if (i != 0){
            String txttemp = "";
            for (int j = 0; j < i-1; j++){
                txttemp += txt.charAt(j);
                
                if (txt.charAt(j+1)=='|' || txt.charAt(j+1) == ')' || txt.charAt(j+1) == '*' || txt.charAt(j)=='(' || txt.charAt(j) == '|'){
                    
                }else{
                    txttemp += '.';
                }
                
            }
            txttemp += txt.charAt(i-1);
            txt = txttemp;
        }
        
        return txt;
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
        
        
        return txt;
    }
}

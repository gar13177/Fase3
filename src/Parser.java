
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
public class Parser {
    
    private ScannerC scanner;
    private ArrayList<String> resWords = new ArrayList();
    private ExprBuild builder = new ExprBuild();
    private int type = 0;//separar tipo de symbol. 1 = ident, 2 = string, 3 = char
    private String log = "";
    private int newProduction = 0;//producciones nuevas para quitar kleene
    private int firstProduction = 0;//identificar primera produccion
    
    private String letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private String digit = "123456890";
    private ArrayList<String> whiteSpace;
    private char[] any = new char[93];//todos los caracteres posibles
    
    private HashMap<String, String> characters = new HashMap();
    private HashMap<String, String> keywords = new HashMap();
    private HashMap<String, String> tokens = new HashMap();

    private ArrayList<String> whiteSpaces = new ArrayList();
    
    private HashMap<String, Productions> productions = new HashMap();//contiene todos los tokens
    private HashMap<String, Productions> newProductions = new HashMap();
    private Productions tempProductions = new Productions();
    private Production tempProduction = new Production();
    private Token tempToken;
    
   
    
    
    public Parser(ScannerC scanner){
        
        this.scanner = scanner;
        initArray();
    }
    
    public Parser(String name){
        
        try{
            this.scanner = new ScannerC(name);
        } catch (IOException e){
            //toDo
        }
        initArray();
    }
    
    //palabras reservadas
    private void initArray(){
        resWords.add("COMPILER");//0
        resWords.add("END");//1
        resWords.add("CHARACTERS");//2
        resWords.add("CHR");//3
        resWords.add("KEYWORDS");//4
        resWords.add("TOKENS");//5
        resWords.add("EXCEPT");//6
        resWords.add("KEYWORDS");//7
        resWords.add("IGNORE");//8
        resWords.add("PRODUCTIONS");//9
        
        
        //conjunto any
        for (int i = 33; i < any.length+33; i++){
            any[i-33] = (char)i;
        }
        
        //conjunto whitespace
        whiteSpace = new ArrayList();
        whiteSpace.add(""+(char)32);
        whiteSpace.add(""+(char)9);
        whiteSpace.add(""+(char)10);
        
        characters.put("letter", letter);
        characters.put("digit", digit);
        characters.put("ANY", String.valueOf(any));
    }
    //----------------------------------------
    //---------------COCOR--------------------
    
    public boolean Cocol(){
        jumpWhite();//saltamos en blanco
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura COMPILER\n";
        if (!readSpecWord(0)){
            new Printer(log,"log.txt"); 
            return false;
        }
        
        jumpWhite();//saltamos en blanco
        scanner.setPointer();//fijamos punto de inicio
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura ident\n";
        if (!isIdent()){
            new Printer(log,"log.txt"); 
            return false;
        }
        String ident = scanner.getString(scanner.getPointer(), scanner.getCharPos());//se obtiene el ident
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "ident: "+ident+"\n";
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura ScannerSpecification\n";
        ScannerSpecification();//ejecutamos scannerspecification
        
        checkWhite();//convertimos los / en caracteres de escape
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura ParserSpecification\n";
        if (!ParserSpecification()){//ejecutamos parserSpecification
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "error no ParserSpecification\n";
            new Printer(log,"log.txt"); 
            return false;
        }
        
        jumpWhite();//saltamos en blanco
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura END\n";
        if (!readSpecWord(1)){
            new Printer(log,"log.txt"); 
            return false;
        }//si no hay END, fallo
        
        
        
        jumpWhite();//saltamos en blanco
        scanner.setPointer();//fijamos punto de inicio
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "revision ident\n";
        if (!isIdent()){
            new Printer(log,"log.txt"); 
            return false;
        }//si no es ident fallo
        
        if (!ident.equals(scanner.getString(scanner.getPointer(), scanner.getCharPos()))){
            new Printer(log,"log.txt"); 
            return false;
        }//comparamos ident
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "ident concuerda\n";
        
        jumpWhite();//saltamos en blanco
        new Printer(log,"log.txt");        
        
        
        return scanner.NextCh() == '.';//si no es igual fallo
    }
    
    public void ScannerSpecification(){
        int tempindex;
        
        jumpWhite();//saltamos en blanco
        tempindex = scanner.getCharPos();//guardamos posicion
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura CHARACTERS\n";
        if (readSpecWord(2)){//si dice CHARACTERS
            tempindex = scanner.getCharPos();
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura SetDecl\n";
            while (SetDecl()){
                tempindex = scanner.getCharPos();
            }
        }
        
        
        scanner.setCharPos(tempindex);
        jumpWhite();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura KEYWORDS\n";
        if (readSpecWord(4)){//si dice KEYWORDS
            tempindex = scanner.getCharPos();
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura KeywordDecl\n";
            while (KeywordDecl()){
                tempindex = scanner.getCharPos();
            }
        }
        
        
        scanner.setCharPos(tempindex);
        jumpWhite();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura TOKENS\n";
        if (readSpecWord(5)){//si dice TOKENS
            //System.out.println("si hay tokens");
            tempindex = scanner.getCharPos();
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura TokenDecl\n";
            while (TokenDecl()){
                //System.out.println("nuevo token");
                tempindex = scanner.getCharPos();
                //System.out.println(tempindex);
            }
        }
        
        scanner.setCharPos(tempindex);
        jumpWhite();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura WhiteSpaceDecl\n";
        while (WhiteSpaceDecl()){
            tempindex = scanner.getCharPos();
        }
        if (whiteSpaces.size() ==0){
            whiteSpaces = whiteSpace;
        }
        
        scanner.setCharPos(tempindex);
        
        
        //System.out.println(characters);
    }
    
    public boolean TokenDecl(){
        jumpWhite();
        
        scanner.setPointer();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenDecl lectura ident\n";
        if (!isIdent()) return false;
        String ident = scanner.getString(scanner.getPointer(), scanner.getCharPos());
        //System.out.println(ident);
        if (ident.equals("PRODUCTIONS")){
            int tempindex = scanner.getCharPos();//guardar posicion
            scanner.setCharPos(scanner.getPointer());//retornamos hasta previo de Productions
            if (ParserSpecification()){// si se logra leer parserspecification, no es token
                //System.out.println("esto ocurrio");
                return false;
            }
            scanner.setCharPos(tempindex);
        }else if (ident.equals("IGNORE")){
            int tempindex = scanner.getCharPos();
            scanner.setCharPos(scanner.getPointer());
            if (WhiteSpaceDecl()){//si es whitespace
                //System.out.println("white");
                whiteSpaces = new ArrayList();
                return false;
            }
            scanner.setCharPos(tempindex);            
        }
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenDecl iden encontrado\n";
        
        jumpWhite();
        int tempindex = scanner.getCharPos();
        boolean equal = false;
        if (scanner.NextCh() == '='){
            jumpWhite();
            builder.reset();
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenDecl lectura TokenExpr\n";
            if (TokenExpr()){
                tempindex = scanner.getCharPos();
                //suponer que TokenExpr ya tiene la expresion
                equal = true;
            }
        }
        
        scanner.setCharPos(tempindex);
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "KeywordDecl lectura EXCEPT KEYWORDS\n";
        if (readSpecWord(6)){
            jumpWhite();
            if (readSpecWord(7)){
                tempindex = scanner.getCharPos();
                
            }
        }
        
        scanner.setCharPos(tempindex);
        jumpWhite();
        if (scanner.NextCh() != '.') return false;
        
        if (equal){
            tokens.put(ident, builder.getString());
        }else{
            tokens.put(ident, characters.get(ident));
        }
        
        return true;
    }
    
    public boolean TokenExpr(){
        //System.out.println("nueva llamada tokenExpr");
        jumpWhite();
        
        //suponer que tokenterm retorna valor
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenExpr lectura TokenTerm\n";
        if (!TokenTerm()) return false;
        String temp = builder.getString();
        
        jumpWhite();
        int tempindex = scanner.getCharPos();
        
        boolean cond = true;
        while (cond){
            jumpWhite();
            if (scanner.Peek() == '|'){
                scanner.NextCh();
                jumpWhite();
                //suponer que tokenterm retorna valor
                log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenExpr lectura | Token Term\n";
                if (TokenTerm()){
                    temp += ""+""+(char)248+builder.getString();
                    
                    tempindex = scanner.getCharPos();
                }else{
                    cond = false;
                }
            }else{
                cond = false;
            }
        }
        builder.setString(temp);
        scanner.setCharPos(tempindex);
        //System.out.println("salida de token Expr");
        return true;
    }
    
    public boolean TokenTerm(){
        jumpWhite();
        
        //suponer que token factor retorna valor
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenTerm lectura TokenFactor\n";
        if (!TokenFactor()) return false;
        String temp = builder.getString();
        
        int tempindex = scanner.getCharPos();
        boolean cond = true;
        while (cond){
            jumpWhite();
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenTerm lectura {TokenFactor}\n";
            if (TokenFactor()){
                temp += builder.getString();
                tempindex = scanner.getCharPos();
            }else{
                cond = false;
            }
        }
        builder.setString(temp);
        scanner.setCharPos(tempindex);
        
        return true;
    }
    
    public boolean TokenFactor(){
        jumpWhite();
        int tempindex = scanner.getCharPos();
        //System.out.println("posicion guardada: "+tempindex);
        //System.out.println("el simbolo que sigue en token factor:"+(char)scanner.Peek());
        //System.out.println("es simbolo"); 
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenFactor lectura symbolo\n";
        if (Symbol()) return true;//symbol ya retorna el valor de tokenfactor
        //System.out.println("no es simbolo");
        scanner.setCharPos(tempindex);
        if (scanner.Peek() == '('){
            scanner.NextCh();
            jumpWhite();
            //token expr ya regresa su valor
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenFactor lectura (TokenExpr)\n";
            if (TokenExpr()){
                jumpWhite();
                if (scanner.Peek() == ')'){
                    scanner.NextCh();
                    builder.setString(""+(char)741+builder.getString()+""+(char)568);
                    return true;
                }
            }
        }
        
        scanner.setCharPos(tempindex);
        if (scanner.Peek() == '['){
            scanner.NextCh();
            jumpWhite();
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenFactor lectura [TokenExpr]\n";
            if (TokenExpr()){
                jumpWhite();
                if (scanner.Peek() == ']'){
                    scanner.NextCh();
                    builder.setString(""+(char)741+builder.getString()+""+(char)568+""+(char)182);
                    return true;
                }
            }
        }
        
        scanner.setCharPos(tempindex);
        if (scanner.Peek() == '{'){
            scanner.NextCh();
            jumpWhite();
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "TokenFactor lectura {TokenExpr}\n";
            if (TokenExpr()){
                jumpWhite();
                if (scanner.Peek() == '}'){
                    scanner.NextCh();
                    builder.setString(""+(char)741+builder.getString()+""+(char)568+""+(char)569);
                    return true;
                }   
            }
        }
        
        scanner.setCharPos(tempindex);
        //System.out.println("posicion:"+tempindex);
        
        return false;
    }
    
    public boolean Symbol(){
        jumpWhite();
        int tempindex = scanner.getCharPos();
        if (isIdent()){//corro peligro de identificar EXCEPT como ident
            int tempindex2 = scanner.getCharPos();
            jumpWhite();
            if (!readSpecWord(7)){//si no dice keywords
                builder.setString(""+(char)741+characters.get(scanner.getString(tempindex,tempindex2))+""+(char)568);
                scanner.setCharPos(tempindex2);
                type = 1;// es ident
                return true;
            }  
        }
        
        scanner.setCharPos(tempindex);
        scanner.setPointer();
        if (isString()){
            builder.setString(scanner.getString(scanner.getPointer()+1,scanner.getCharPos()-1));
            type = 2; // es string
            return true;
        }
        
        scanner.setCharPos(tempindex);
        if (isChar()){
            builder.setString(scanner.getString(scanner.getPointer()+1,scanner.getCharPos()-1));
            type = 3; // es char
            return true;
        }
        
        scanner.setCharPos(tempindex);        
        
        return false;
    }
    
    public boolean KeywordDecl(){
        jumpWhite();
        scanner.setPointer();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "KeywordDecl lectura ident\n";
        if (!isIdent()) return false;
        String keyword = scanner.getString(scanner.getPointer(), scanner.getCharPos());
        //System.out.println(keyword);
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "KeywordDecl ident localizado\n";
        jumpWhite();
        if (scanner.NextCh() != '=') return false;
        
        jumpWhite();
        scanner.setPointer();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "KeywordDecl buscado conjunto de ident\n";
        if (!isString()) return false;
        
        String val = scanner.getString(scanner.getPointer()+1, scanner.getCharPos()-1);
        
        if (scanner.NextCh() != '.') return false;
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "KeywordDecl keyword encontrado\n";
        keywords.put(keyword, val);
        
        return true;
    }
    
    public boolean SetDecl(){
        jumpWhite();
        scanner.setPointer();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "SetDecl lectura ident\n";
        if (!isIdent()) return false;//no hay ident
        String ident = scanner.getString(scanner.getPointer(), scanner.getCharPos());
        
        jumpWhite();
        if (scanner.NextCh() != '=') return false;//no hay equals
        
        //no hay jumpWhite porque hay en set
        //--System.out.println("Llega a set");
        builder.reset();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "SetDecl lectura Set\n";
        if (!Set()) return false;//no hay set;
        //--System.out.println("si hay set");
        
        jumpWhite();
        if (scanner.NextCh() != '.') return false;//no hay punto       
        //--System.out.println("hay punto");
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "SetDecl characters match\n";
        characters.put(ident, builder.getString());
        return true;
    }
    
    public boolean Set(){
        jumpWhite();//saltamos en blanco
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "Set lectura BasicSet\n";
        if (!BasicSet()) return false;//no hay primer basic set
        String temp = builder.getString();
        
        int tempindex = scanner.getCharPos();
        boolean cond = true;
        while (cond){
            jumpWhite();
            if (scanner.Peek() == '+' || scanner.Peek() == '-'){
                char symbol = (char)scanner.NextCh();
                //no hay jump white porque basic set lo tiene
                builder.reset();
                log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "Set lectura +- BasicSet\n";
                if (!BasicSet()){//si no hay basic Set
                    cond = false;
                }else{//si hay basicSet
                    tempindex = scanner.getCharPos();
                    if (symbol == '+'){
                        
                        builder.add(""+""+(char)248+temp);
                    }else{
                        builder.remove(temp);
                    }
                    temp =builder.getString();
                }
            }else{
                cond = false;
            }
        }
        
        scanner.setCharPos(tempindex);
        
        return true;
    }
    
    public boolean BasicSet(){
        jumpWhite();//saltamos en blanco
        int tempindex = scanner.getCharPos();
        scanner.setPointer();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "BasicSet lectura string\n";
        if (isString()){
            String stt = scanner.getString(scanner.getPointer()+1, scanner.getCharPos()-1);
            
            builder.add(builder.buildRegExp(stt));
            return true;
        }
        
        scanner.setCharPos(tempindex);
        //no hay jumpwhite porque char ya tiene
        
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "BasicSet lectura char\n";
        if (Char()){//si es char
            jumpWhite();
            tempindex = scanner.getCharPos();
            int ini = (int)builder.getTemp().charAt(0);
            
            if (scanner.Peek() == '.'){
                scanner.NextCh();
                if (scanner.Peek() == '.'){
                    scanner.NextCh();
                    //no hay jumpwhite
                    if (Char()){
                       builder.setTemp(builder.buildChar(ini, (int)builder.getTemp().charAt(0)));
                       //System.out.println(builder.getTemp());
                       //System.out.println("varios chars");
                       tempindex = scanner.getCharPos();
                    }
                }
            }
            scanner.setCharPos(tempindex);
            
            builder.add(builder.buildRegExp(builder.getTemp()));
            //System.out.println(builder.getString());
            return true;
        }
        
        scanner.setCharPos(tempindex);
        jumpWhite();
        scanner.setPointer();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "BasicSet lectura ident\n";
        if (isIdent()){
            String returnval = characters.get(scanner.getString(scanner.getPointer(), scanner.getCharPos()));
            if ( returnval != null){
                builder.add(returnval);
                
            }else{
                //not defined
            }
            return true;
        }
        //--System.out.println("Llegue: C:"+scanner.getColumn()+" L:"+scanner.getLine());
        return false;
    }
    
    public boolean Char(){
        jumpWhite();
        
        int tempindex = scanner.getCharPos();        
        scanner.setPointer();
        if (isChar()){
            builder.setTemp(scanner.getString(scanner.getPointer()+1, scanner.getCharPos()-1));
            return true;
        }
        
        scanner.setCharPos(tempindex);
        if (readSpecWord(3)){
            jumpWhite();
            if (scanner.Peek() == '('){
                scanner.NextCh();
                jumpWhite();
                scanner.setPointer();
                if (isNumber()){
                    String num = scanner.getString(scanner.getPointer(),scanner.getCharPos());
                    jumpWhite();
                    if (scanner.Peek() == ')'){
                        //System.out.println("parentesis: "+(char)scanner.NextCh());
                        builder.setTemp(""+(char) Integer.parseInt(num));
                        scanner.NextCh();
                        return true;
                    }
                }
            }
        }
        
        
        return false;
    }
    
    
    public boolean WhiteSpaceDecl(){
        jumpWhite();
        if (!readSpecWord(8)) return false;
        
        jumpWhite();
        builder.reset();
        if (!Set()) return false;
        
        jumpWhite();
        if (scanner.NextCh() == '.'){
            String[] array = builder.getString().split(""+(char)248);
            //System.out.println("Lectura de  whitespace");
            String lectura = "";
            for (String st: array){
                lectura += st;
                
            }
            whiteSpaces.add(lectura);
            //System.out.println(whiteSpaces);
            return true;
        }
        
        return false;      
        
    }
    
    public boolean ParserSpecification(){
        int tempindex;
        //System.out.println("ingreso a parser");
        jumpWhite();//saltamos en blanco
        tempindex = scanner.getCharPos();//guardamos posicion
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura PRODUCTIONS\n";
        if (readSpecWord(9)){//si dice PRODUCTIONS
            tempindex = scanner.getCharPos();
            log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "lectura Production\n";
            
            //reinicializo los conjuntos
            productions = new HashMap();
            newProduction = 0;
            firstProduction = 0;
            
            while (Production()){
                tempindex = scanner.getCharPos();
            }
        }else{
            return false;
        }
        scanner.setCharPos(tempindex);
     
        
        return true;
    }
    
    public boolean Production(){
        jumpWhite();
        
        scanner.setPointer();
        log += "L: "+scanner.getLine()+" C: "+scanner.getColumn() +" "+ "Production lectura ident\n";
        if (!isIdent()) return false;//no hay ident
        String ident = scanner.getString(scanner.getPointer(), scanner.getCharPos());
        
        jumpWhite();
        int tempindex = scanner.getCharPos();
        if (!Attributes()){
            scanner.setCharPos(tempindex);
        }
        
        jumpWhite();
        tempindex = scanner.getCharPos();
        if (!SemAction()){
            scanner.setCharPos(tempindex);
        }
        
        jumpWhite();
        if (scanner.Peek()!='=') return false;//no hay 
        //System.out.println("Production, si hay =");
        scanner.NextCh();
        
        jumpWhite();
        
        tempProductions = new Productions();
        
        //Expression ya debe regresar las producciones
        if (!Expression()) return false; //no hay expression
        
        jumpWhite();
        if (scanner.Peek() != '.') return false;
        scanner.NextCh();
        
        
        tempProductions.setHead(firstProduction == 0);//establezco la cabeza
        firstProduction++;//establezco la cabeza
        
        if (!productions.containsKey(ident))
            productions.put(ident, tempProductions);//se agrega a producciones la cabeza y las producciones
        else
            productions.get(ident).addAll(tempProductions);//se agregan las nuevas producciones
        
        
            
        return true;
    }
    
    public boolean Attributes(){
        jumpWhite();
        if (scanner.Peek() != '<') return false;
        scanner.NextCh();
        if (scanner.Peek() != '.') return false;
        scanner.NextCh();
        
        while (scanner.Peek() != '.'){
            if (scanner.Peek() == -1) return false;
            
            scanner.NextCh();
        }
        if (scanner.Peek() != '.') return false;
        scanner.NextCh(); 
        if (scanner.Peek() != '>') return false;
        scanner.NextCh();
        
        
        return true;
    }
    
    public boolean SemAction(){
        jumpWhite();
        if (scanner.Peek() != '(') return false;
        scanner.NextCh();
        if (scanner.Peek() != '.') return false;
        scanner.NextCh();
        
        while (scanner.Peek() != '.'){
            if (scanner.Peek() == -1) return false;
            scanner.NextCh();
        }
        if (scanner.Peek() != '.') return false;
        scanner.NextCh(); 
        if (scanner.Peek() != ')') return false;
        scanner.NextCh();
        
        
        return true;
    }
    
    public boolean Expression(){
        jumpWhite();
        
        tempProduction = new Production();
        //term debe regresar una produccion
        if (!Term()) return false;
        
        tempProductions.addProduction(tempProduction);//agrego la produccion de Term
        
        
        jumpWhite();
        int tempindex = scanner.getCharPos();
        boolean cond = true;
        
        while (cond){
            jumpWhite();
            if (scanner.Peek() == '|'){
                scanner.NextCh();
                jumpWhite();
                tempProduction = new Production();
                if (Term()){
                    tempProductions.addProduction(tempProduction);//agrego la produccion de Term
                    tempindex = scanner.getCharPos();
                }else{
                    cond = false;
                }
            }else{
                cond = false;
            }
        }
        
        scanner.setCharPos(tempindex);
        
        
        return true;
    }
    
    public boolean Term(){
        jumpWhite();
        
        //factor retorna en temptoken el token de lectura
        if (!Factor()) return false;
        
        tempProduction.addToken(tempToken);//agrego el token de factor
        
        jumpWhite();
        int tempindex = scanner.getCharPos();
        
        while (Factor()){
            tempProduction.addToken(tempToken);//agrego los nuevos tokens
            jumpWhite();
            tempindex = scanner.getCharPos();
        }
        
        scanner.setCharPos(tempindex);
        return true;
    }
    
    public boolean Factor(){
        jumpWhite();
        int tempindex = scanner.getCharPos();
        if (Symbol()){
            
            //construccion de token de tipo symbol
            if (type == 1){
                tempToken = new Token(scanner.getString(tempindex, scanner.getCharPos()),type);
            }else{
                tempToken = new Token(scanner.getString(tempindex+1, scanner.getCharPos()-1),type);
            }
            
            jumpWhite();
            tempindex = scanner.getCharPos();
            if (!Attributes()){
                scanner.setCharPos(tempindex);
            }
            
            return true;
        }
        
        scanner.setCharPos(tempindex);
        if (scanner.Peek() == '('){
            scanner.NextCh();
            jumpWhite();
            
            /**
             * cada vez que ingresa a Expression debe utilizar tempProductions
             */
            Productions tempPs = new Productions(tempProductions);
            Production tempP = new Production(tempProduction);
            
            tempProductions = new Productions();//se reinicializa
            tempProduction = new Production();//se reinicializa
            
            if (Expression()){
                jumpWhite();
                if (scanner.Peek() == ')'){
                    scanner.NextCh();
                    
                    tempToken = new Token("_init"+newProduction,5);//se agrega _initN como produccion
                    
                    productions.put("_init"+(newProduction++), tempProductions);
                    
                    tempProductions = tempPs;
                    tempProduction = tempP;
                    
                    
                    return true;
                }
            }
            
            tempProductions = tempPs;
            tempProduction = tempP;  
        }
        
        scanner.setCharPos(tempindex);
        if (scanner.Peek() == '['){
            scanner.NextCh();
            jumpWhite();
            
            /**
             * cada vez que ingresa a Expression debe utilizar tempProductions
             */
            Productions tempPs = new Productions (tempProductions);
            Production tempP = new Production (tempProduction);
            
            tempProductions = new Productions();//se reinicializa
            tempProduction = new Production();//se reinicializa
            
            if (Expression()){
                jumpWhite();
                if (scanner.Peek() == ']'){
                    scanner.NextCh();
                    
                    tempToken = new Token("_init"+newProduction,5);//se agrega _initN como produccion
                    
                    Token tk = new Token("~",6);//se agrega token epsilon
                    Production pd = new Production(tk);
                    tempProductions.addProduction(pd);//se agrega una produccion de epsilon
                    
                    productions.put("_init"+(newProduction++), tempProductions);
                    
                    tempProductions = tempPs;
                    tempProduction = tempP;
                    
                    return true;
                }
            }
            
            tempProductions = tempPs;
            tempProduction = tempP;
        }
        
        scanner.setCharPos(tempindex);
        if (scanner.Peek() == '{'){
            scanner.NextCh();
            jumpWhite();
            
            /**
             * cada vez que ingresa a Expression debe utilizar tempProductions
             */
            Productions tempPs = tempProductions;
            Production tempP = tempProduction;
            
            tempProductions = new Productions();//se reinicializa
            tempProduction = new Production();//se reinicializa            
            
            if (Expression()){
                jumpWhite();
                if (scanner.Peek() == '}'){
                    scanner.NextCh();
                    
                    tempToken = new Token("_init"+newProduction,5);//se agrega _initN como produccion
                    
                    tempProductions.addAllEnd(tempToken);//se agrega a todas las producciones kleeneadas El mismo al final
                    
                    Token tk = new Token("~",6);//se agrega token epsilon
                    Production pd = new Production(tk);
                    tempProductions.addProduction(pd);//se agrega una produccion de epsilon
                    
                    productions.put("_init"+(newProduction++), tempProductions);
                    
                    tempProductions = tempPs;
                    tempProduction = tempP;
                    
                    
                    
                    return true;
                }
            }
            tempProductions = tempPs;
            tempProduction = tempP;
        }
        
        scanner.setCharPos(tempindex);
        if (SemAction()) return true;
        
        return false;
    }
    
    
    //----------------------------------------
    //----------------------------------------
    
    //lectura de palabras reservadas
    public boolean readSpecWord(int i){
        String read = "";
        String compare = resWords.get(i);
        if (scanner.Peek() == -1) return false;// no se puede leer mas
            
        char letter = (char)scanner.Peek();
        while (compare.contains(read+letter)){
            
            //quiere decir que hace Peek y verifica si esta adentro
            //si esta adentro continua con el ciclo
            read += (char)scanner.NextCh();//agrego el siguiente caracter
            if (scanner.Peek() == -1) break;//no hay mas que leer
            letter = (char) scanner.Peek();
        }
        
        return compare.equals(read);
    }
    
    public boolean isIdent(){
        String read = "";
        if (scanner.Peek() == -1) return false;// no se puede leer mas
            
        char thisLetter = (char)scanner.Peek();
        if (!letter.contains(""+thisLetter)) return false;//no se cumple la primera condicion
        read += (char)scanner.NextCh();//se agrega la primera letra
        thisLetter = (char)scanner.Peek();
        while (letter.contains(""+thisLetter)||digit.contains(""+thisLetter)){
            //quiere decir que hace Peek y verifica si esta adentro
            //si esta adentro continua con el ciclo

            read += (char)scanner.NextCh();//agrego el siguiente caracter
            if (scanner.Peek() == -1) break;//no hay mas que leer
            thisLetter = (char) scanner.Peek();
        }
        //--System.out.println(read);
        return true;//si llega aca, es ident
    }
    
    public boolean isNumber(){
        String read = "";
        if (scanner.Peek() == -1) return false;// no se puede leer mas
            
        char thisLetter = (char)scanner.Peek();
        if (!digit.contains(""+thisLetter)) return false;//no se cumple la primera condicion
        read += (char)scanner.NextCh();//se agrega el primer numero
        thisLetter = (char)scanner.Peek();
        while (digit.contains(""+thisLetter)){
            //quiere decir que hace Peek y verifica si esta adentro
            //si esta adentro continua con el ciclo
            read += (char)scanner.NextCh();//agrego el siguiente caracter
            if (scanner.Peek() == -1) break;//no hay mas que leer
            thisLetter = (char) scanner.Peek();
        }
        //--System.out.println(read);
        return true;//si llega aca, es numero
    }
    
    //metodo para saltarse todo lo blanco
    public void jumpWhite(){
        while (whiteSpace.contains(""+(char)scanner.Peek())){
            //quiere decir que hace Peek y verifica si esta adentro
            //si esta adentro continua con el ciclo
            scanner.NextCh();
            
        }
    }
    
    public boolean isString(){
        String read = "";
        if (scanner.Peek() == -1) return false;// no se puede leer mas
            
        char thisLetter = (char)scanner.Peek();
        if (thisLetter != '"') return false;//no se cumple la primera condicion
        read += (char)scanner.NextCh();//se agrega el primer "
        thisLetter = (char)scanner.Peek();
        
        while (thisLetter != '"'){
            //quiere decir que hace Peek y verifica si esta adentro
            //si esta adentro continua con el ciclo
            char tmp = (char)scanner.NextCh();//agrego el siguiente caracter
            
            
            if (scanner.Peek() == -1) return false;//no hay mas que leer y no se ha terminado el string
            thisLetter = (char) scanner.Peek();
        }
        read += (char)scanner.NextCh();//dado que termina cuando encuentra "
        //--System.out.println(read);
        return true;//si llega aca, es string
    }
    
    public boolean isChar(){
        String read = "";
        if (scanner.Peek() == -1) return false;// no se puede leer mas
            
        char thisLetter = (char)scanner.Peek();
        if (thisLetter != '\'') return false;//no se cumple la primera condicion
        //read += (char)scanner.NextCh();
        scanner.NextCh();//obviamos el primer '
        thisLetter = (char)scanner.Peek();
        while (thisLetter != '\''){
            //quiere decir que hace Peek y verifica si esta adentro
            //si esta adentro continua con el ciclo
            read += (char)scanner.NextCh();//agrego el siguiente caracter
            if (scanner.Peek() == -1) return false;//no hay mas que leer y no se ha terminado el char
            thisLetter = (char) scanner.Peek();
            //System.out.println(read);
        }
        scanner.NextCh();//terminamos de cerrar '
        //if (read.toCharArray().length != 1) return false; //hay mas de un char
        read = '\''+read+'\'';//agrego apostrofes
        
        return true;//si llega aca, es char
    }
    
    
    public HashMap getTokens(){
        return  tokens;
    }
    
    public HashMap getKeywords(){
        return keywords;
    }
    
    public ArrayList getWhite(){
        return whiteSpaces;
    }
    
    public HashMap getProductions(){
        return productions;
    }
    
    private void checkWhite(){
        for (int i = 0; i < whiteSpaces.size(); i++){//para cada whiteSpace
            String prev = whiteSpaces.get(i);//tomamos el string actual
            String news = "";
            for (int j = 0; j < prev.length(); j++){//para cada uno de sus caracteres
                char ch = prev.charAt(j);
                if (ch == '\\' && j < prev.length()-1){//si es un caracter de escape y no esta al final
                    char nch = prev.charAt(j+1);//tomamos el siguiente char
                    char add;
                    switch (nch){
                        case 'n': 
                            add = '\n';
                            break;
                        case 't':
                            add = '\t';
                            break;
                        case 'b':
                            add = '\b';
                            break;
                        case 'r':
                            add = '\r';
                            break;
                        case 'f':
                            add = '\f';
                            break;
                        case '\'':
                            add = '\'';
                            break;
                        case '\"':
                            add = '\"';
                            break;
                        case '\\':
                            add = '\\';
                            break;
                        default:
                            add = nch;
                            break;  
                    }
                    news += add;
                    j++;
                    
                }else{
                    news += ch;
                }
                whiteSpaces.set(i, news);
                
            }
            
        }
    }
    
}

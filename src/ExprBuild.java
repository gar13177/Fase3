/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kevin
 */
public class ExprBuild {
    
    String val = "";
    String tempval = "";
    
    public ExprBuild(){}
    
    public String buildChar(int ini, int end){
        String temp = "";
        for (int i = ini; i<=end; i++){
            temp += (char)i;
        }
        
        return temp;
    }
    
    public void reset(){
        val = "";
    }
    
    public void add(String val){
        this.val += val;
    }
    
    public void add(char val){
        this.val += (char) val;
    }
    
    public void remove(String val){
        char[] valA = val.toCharArray();
        for (char ch: valA){
            this.val = this.val.replace(""+(char)ch,"");
        }
    }
    
    public void remove (char val){
        this.val = this.val.replace(""+val,"");
    }
    
    public void setString(String val){
        this.val = val;
    }
    
    public String getString(){
        return val;
    }
    
    public void resetTemp(){
        tempval = "";
    }
    
    public void setTemp(String val){
        tempval = val;
    }
    
    public String getTemp(){
        return tempval;
    }
    
    public String buildRegExp(String val){
        String temp = "";
        for (int i = 0; i< val.length()-1; i++){
            temp += ""+(char) val.charAt(i)+(char)248;
        }
        temp += ""+(char)val.charAt(val.length()-1);
        return temp;
    }
    
    
    
}

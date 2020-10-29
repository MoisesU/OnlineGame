/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MoisésUlises
 */
public class JsonAux {
    private final List json;

    public JsonAux() {
        this.json = new ArrayList<String>();
    }
    
    public void addStr(String name, String data){
        this.json.add("\""+name+"\":\""+data+"\"");
    }
    
    public void addInt(String name, int data){
        this.json.add("\""+name+"\":"+data+"");
    }
    
    public void addDouble(String name, double data){
        this.json.add("\""+name+"\":"+data+"");
    }
    
    public String getJSON(){
        return "{"+String.join(",", this.json)+"}";
    }
    
    
}

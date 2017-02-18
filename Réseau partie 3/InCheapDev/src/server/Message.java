/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author bastin
 */
public class Message implements Serializable {
    
    private int type;
    private HashMap<String, Object> params;
    
    public Message() {
        params = new HashMap();
    }
    
    public Message(int t, HashMap p) {
        type = t;
        params = p;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
   
    public HashMap getParams() {
        return params;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }
    
    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }
    
    public boolean hasParam(String key) {
        return this.params.containsKey(key);
    }
    
    public Object getParam(String key) {
        return this.params.get(key);
    }
    
    public void removeParam(String key) {
        this.params.remove(key);
    }
}

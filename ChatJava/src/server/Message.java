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
    
    public static final String SEP = "#";
    
    private int type;
    private HashMap<String, Object> params;
    private ArrayList<String> data;
    
    public Message() {
        params = new HashMap();
        data = new ArrayList();
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
    
    public Object getParam(String name) {
        return this.params.get(name);
    }
    
    public boolean hasParam(String name) {
        return this.params.containsKey(name);
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }
    
    
}

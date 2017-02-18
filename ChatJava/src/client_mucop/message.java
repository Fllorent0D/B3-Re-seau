/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_mucop;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Jerome
 */
class message {
    private String message;
    private String tag;

    public message(String m, String t, String u) {
        
        tag = t;
        
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Calendar c = Calendar.getInstance();
        message = "[" + df.format(c.getTime()) + "] ("+t+") "+u+" : "+m;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public String getTag()
    {
        return tag;
    }
    public boolean isQuestion(){
        return tag.matches("^(.*)");
    }
    public void setTag(String s)
    {
        tag = s;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redmmp;

import com.floca.BeanDBAccess.BeanDBAccessOracle;
import dismap.RunnableDISMAP;
import dismap.ServerDISMAP;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.util.StringUtils;
import server.ServerProperties;
import static sun.security.jgss.GSSUtil.login;

/**
 *
 * @author bastin
 */
public class RunnableREDMMP implements Runnable, protocoleREDMMP {

    private Socket CSocket;
    private ServerREDMMP serverREDMMP;
    private InetAddress addrServer;
    private String ipServer;
    private String ipOracle;
    private BufferedReader br;
    private OutputStreamWriter osw;
    private BeanDBAccessOracle beanOracle;
    private String portOracle;
    private String loginOracle, passwordOracle;
    
    protected  ArrayList<String> refs = new ArrayList();
   
    
    public RunnableREDMMP(Socket CSocket, ServerREDMMP serverREDMMP) {
        this.CSocket = CSocket;
        this.serverREDMMP = serverREDMMP;
        
        ServerProperties properties = new ServerProperties();
        
        ipServer = properties.getProperty("IP_SERVER");
        ipOracle = properties.getProperty("IP_ORACLE");
        portOracle = properties.getProperty("PORT_ORACLE");
        loginOracle = properties.getProperty("LOGIN_ORACLE");
        passwordOracle = properties.getProperty("PASSWORD_ORACLE");
        
                   
        refs.add("SFYWDLE7C7");
        refs.add("SFYWDLE7C8");
        refs.add("SFYWDLE7C9");

        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            br = new BufferedReader(new InputStreamReader(CSocket.getInputStream()));
            osw = new OutputStreamWriter(CSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        beanOracle = new BeanDBAccessOracle(ipOracle, portOracle, loginOracle, passwordOracle);

        try {
            beanOracle.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    @Override
    public void run() {
        
        boolean logged = false;
        
        do
        {  
            String request = receiveRequest();
 
            System.out.println(request);
            
            int protocol = getProtocol(request);
            
            if(protocol != CONNECT)
            {
                return;
            }
            
            logged = executeAuthentification(request, true);
            
        }while(!logged);
        
        boolean terminated = false;  
        
        System.out.println("Client authentifié");
        
        do
        {   
            String request = receiveRequest();
            
            System.out.println("Requete: " + request);
            
            int protocol = getProtocol(request);
            
            switch(protocol)
            {
                case INPUT_DEVICES1:
                    executeInputDevices1(request);
                    break;
                case INPUT_DEVICES2:
                    executeInputDevices2(request);
                    break;
                case GET_DELIVERY1:
                    executeGetDelivery1(request);
                    break;
                case GET_DELIVERY2:
                    executeGetDelivery2(request);
                    break;
                default:
                    terminated = true;
                    break;
            }

        } while(!terminated);
        
        System.out.println("Départ du client");
        
        try {
            CSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean executeAuthentification(String request, boolean isLogin) {
        String response = "0";
        boolean valid = false;
        String content = getContent(request);
        
        String login = getValue(content, 1);
        String password = getValue(content, 2);
        
        try {
            ResultSet rs = beanOracle.executeQuery("SELECT * FROM personnel WHERE login = '" + login + "' AND password = '" + password + "'");
            if (rs.next()) 
            {
                response = "1";
                valid = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RunnableREDMMP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sendResponse(getProtocol(request), response);
        
        return valid;
    }
    
    public void executeInputDevices1(String request)
    {
        String response = "1";
        String content = getContent(request);
 
        sendResponse(getProtocol(request), response);
    }
    
    public void executeInputDevices2(String request)
    {
        String response = "2#4#";
        String content = getContent(request);
        String reference = getValue(content, 1);
        String quantity = getValue(content, 2);

        sendResponse(getProtocol(request), response);
    }
    
    public void executeGetDelivery1(String request)
    {
        String response = "1";
        String content = getContent(request);
        
        String society = getValue(content, 1);
        String zone = getValue(content, 2);
        
        sendResponse(getProtocol(request), response);
    }    
    
 
    public void executeGetDelivery2(String request)
    {
        String response = "";
        String content = getContent(request);
        
        if(refs.size() > 0)
        {
            String ref = refs.get(refs.size() - 1);
            refs.remove(ref);
            response = ref + "#";
        }
        else
        {
             response = "#Il n'y a plus de produit à livrer pour cette zone";
        }
           
        sendResponse(getProtocol(request), response);
    }
    
    public void sendResponse(int protocole, String msg) {
        String message = String.valueOf(protocole) + "#" + msg;
        int length = message.length();
        
        String response = String.valueOf(length) + "#" + message;

        PrintWriter pw = new PrintWriter(osw, true);
        
        System.out.println("Response: " + response);
        
        pw.print(response);
        pw.flush();
    }
    
    public String receiveRequest() {
        String response = null;
        int count;
        char[] buffer = new char[500];
        
           try {
            count = br.read(buffer, 0, 500);
            new String(buffer, 0, count);
            
            response = String.valueOf(buffer);
            
            int totalLength = getLength(response);
            int length = count;
 
            while(totalLength > length)
            {
                count = br.read(buffer, 0, 500);
                length += count;
                new String(buffer, 0, count);
                response += String.valueOf(buffer);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(RunnableREDMMP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);     
        }
        
        return response;
    }
    
    public String getContent(String msg) {
        int length, pos;
        String response, temp;

        pos = msg.indexOf("#");
        
        length = Integer.valueOf(msg.substring(0, pos));
         
        temp = msg.substring(pos+1, msg.length());
        
        pos = temp.indexOf("#");

        response = temp.substring(pos+1, length);
        
        System.out.println("   Content: " + response);
        
        return response;
    }
    
    public int getLength(String msg) {
        int pos = msg.indexOf("#");
        
        return Integer.valueOf(msg.substring(0, pos));
    }
    
    public int getProtocol(String request) { 
        
        int pos = request.indexOf("#");
        
        request = request.substring(pos+1, request.length());
        
        pos = request.indexOf("#");
        
       request = request.substring(0, pos);
        
        return Integer.parseInt(request);
    }
    
    public String getValue(String buff, int elem) 
    {
        String value = null;
        int pos;
        
        for(int i = 0 ; i < elem ; i++)
        {
            pos = buff.indexOf("#");
            
            if(pos > -1)
            {
                value = buff.substring(0, pos);
                buff = buff.substring(pos+1);
            }
            else
                value = buff;
        }
        
        return value;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_dsa;

import client.Client;
import dismap.RunnableDISMAP;
import dismap.protocoleDISMAP;
import dsa.protocoleDSA;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ClientDSA extends Client implements protocoleDSA {
    
    protected String ipServer;
    protected int portDSA;
    protected InetAddress addrServer;
    protected String login, password;
    
    public ClientDSA() throws UnknownHostException {
        ServerProperties properties = new ServerProperties();
        
        ipServer = properties.getProperty("IP_SERVER");
        portDSA = Integer.valueOf(properties.getProperty("PORT_DSA"));
        
        login = properties.getProperty("LOGIN_ADMIN");
        password = properties.getProperty("PASSWORD_ADMIN");
        
        InetAddress addrDSA = InetAddress.getByName(ipServer);
        setIp(addrDSA);
            
        setPort(portDSA);
        
        System.out.println("Configuration client DSA");
        System.out.println("------------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portDSA);
        System.out.println("");       
    }
    
    public Message receiveMessage()
    {
        Message msg = null;
        
        try {
            msg = (Message) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public void sendMessage(Message msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void interrupt()
    {
        Message request = new Message();
        
        request.setType(REQUEST_INTERRUPT);
        
        sendMessage(request);
    }
    
    public Message authentification(String login, String passwotf, boolean isLogin) {
        Message request = new Message();
        request.setType(REQUEST_LOGIN_A);
        
        request.addParam("login", login);
        request.addParam("password", password);
        
        sendMessage(request); 
        
        return receiveMessage();
    }
    
    public ArrayList<String> listClients() {
        Message request = new Message();
        request.setType(REQUEST_LCLIENTS);
        
        sendMessage(request);
        
        Message response = receiveMessage();
        
        return (ArrayList) response.getParam("data");
    }
    
    public Message stop() {
        Message request = new Message();
        request.setType(REQUEST_STOP);
        
        sendMessage(request);
        
        return null;
    }
}

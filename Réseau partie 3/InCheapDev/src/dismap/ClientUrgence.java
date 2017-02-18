/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dismap;

import dsa.protocoleDSA;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ClientUrgence extends Thread implements protocoleDSA {
    
    private Socket CSocket;
    private ServerDISMAP serverDISMAP;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private int portUrgence;
    private InetAddress addrServer;
    private Socket UrgenceSocket;
    boolean urgenceConnected = false;
    
    public ClientUrgence(InetAddress addrServer, Socket CSocket, ServerDISMAP server) {
        ServerProperties properties = new ServerProperties();
        
        serverDISMAP = server;
        portUrgence = Integer.valueOf(properties.getProperty("PORT_URGENCE"));
        
        this.addrServer = addrServer;
        this.CSocket = CSocket;
        
        serverDISMAP.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                executeStop();
            }
        });
    }
    
    @Override
    public void run() {
        
        
        do
        {
            try {     
                InetAddress clientAddr = CSocket.getInetAddress();

                UrgenceSocket = new Socket(clientAddr, portUrgence);
                
                oos = new ObjectOutputStream(new BufferedOutputStream(UrgenceSocket.getOutputStream()));
                oos.flush();
                ois = new ObjectInputStream(new BufferedInputStream(UrgenceSocket.getInputStream()));
                
                urgenceConnected = true;
                
                System.out.println("Connexion urgence établie");

            } catch (IOException ex) {}     
        }while(!urgenceConnected);
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
    
    public void executeStop() {
        Message request = new Message();
        request.setType(REQUEST_STOP);
        
        sendMessage(request);
    }
}

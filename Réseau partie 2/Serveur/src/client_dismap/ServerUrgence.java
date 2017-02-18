/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_dismap;

import dismap.ClientUrgence;
import dismap.RunnableDISMAP;
import dsa.protocoleDSA;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ServerUrgence extends Thread implements protocoleDSA {

    protected ServerSocket SSocket;
    protected Socket CSocket;
    protected InetAddress addrServer;
    protected String ipServer;
    protected int portUrgence;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private JFrame parent;
    
    public ServerUrgence(JFrame parent)
    {
        this.parent = parent;
        
        ServerProperties properties = new ServerProperties();
        
        ipServer = properties.getProperty("IP_SERVER");
        
        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerUrgence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        portUrgence = Integer.valueOf(properties.getProperty("PORT_URGENCE"));
       
    }
    
    @Override
    public void run() {
        
        do
        {
            try {
               SSocket = new ServerSocket(portUrgence, 10, addrServer);

               System.out.println(SSocket.getInetAddress().toString());
               System.out.println(portUrgence);

               CSocket = SSocket.accept();

               oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
               oos.flush(); // Nécessaire pour que le programme ne se bloque pas sur "new ObjectInputStream
               ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));

               boolean terminated = false;
               
               do
               {
                   Message request = receiveMessage();
                   
                   switch(request.getType()) 
                   {
                        case REQUEST_STOP:
                           executeStop(request);
                           break;
                        default:
                            terminated = true;
                   }
                   
               } while(!terminated);          

               CSocket.close();

            } catch (IOException ex) {
               Logger.getLogger(ServerUrgence.class.getName()).log(Level.SEVERE, null, ex);
            }             
        } while(true);
    }
    
    public Message receiveMessage()
    {
        Message msg = null;
        
        try {
            msg = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            return null;
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
    
    public void executeStop(Message request) {
        
        new Stop().start();
        
        JOptionPane.showMessageDialog(parent, "Arrêt du serveur dans 5 secondes !", "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public class Stop extends Thread {
        public Stop() {
            
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ClientUrgence.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.exit(0);
        }
    }
}

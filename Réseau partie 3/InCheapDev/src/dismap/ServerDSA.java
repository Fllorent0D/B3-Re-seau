

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dismap;

import client_dismap.ServerUrgence;
import static dismap.protocoleDISMAP.REQUEST_LOGIN;
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
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ServerDSA extends Thread implements protocoleDSA {
    
    protected ServerSocket SSocket;
    protected Socket CSocket;
    protected InetAddress addrServer;
    protected String ipServer;
    protected int portDSA;
    private ObjectInputStream ois ;
    private ObjectOutputStream oos;
    private ServerDISMAP serverDISMAP;
    
    public ServerDSA(ServerDISMAP serverDISMAP) {
        ServerProperties properties = new ServerProperties();
        
        this.serverDISMAP = serverDISMAP;
        
        ipServer = properties.getProperty("IP_SERVER");
        
        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerUrgence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        portDSA = Integer.valueOf(properties.getProperty("PORT_DSA"));  
        
        System.out.println("Configuration de DSA");
        System.out.println("--------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portDSA);
        System.out.println("");
    }
    
    @Override
    public void run() {
        
        try {
            SSocket = new ServerSocket(portDSA, 10, addrServer);
        } catch (IOException ex) {
            Logger.getLogger(ServerDSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        do
        {
            try {
               System.out.println(SSocket.getInetAddress().toString());

               CSocket = SSocket.accept();

               oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
               oos.flush();
               ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));

               boolean terminated = false;
               
                System.out.println("Client DSA pris en charge");
                
                Message request;
                boolean logged = false;
                
                do
                {
                    request = receiveMessage();
                    System.out.println("Reception d'un message");

                    System.out.println(request.getType());

                    if(request.getType() != REQUEST_LOGIN_A)
                    {
                        if(request.getType() == REQUEST_INTERRUPT)
                            return;
                        else
                            continue;
                    }

                    logged = executeAuthentification(request);

                }while(!logged);
               
                do
                {
                    request = receiveMessage();

                    switch(request.getType()) 
                    {
                        case REQUEST_LCLIENTS:
                            executeLClients(request);
                            break;
                        case REQUEST_STOP:
                            executeStop(request);
                            break;
                        case REQUEST_INTERRUPT:
                         default:
                             terminated = true;
                    }

                } while(!terminated);          

                CSocket.close();
               
                System.out.println("Fin du client DSA");

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
    
    public boolean executeAuthentification(Message request) {
        Message response = new Message();
        response.setType(REQUEST_LOGIN_A);
        boolean logged = false;
        
        String login = (String) request.getParam("login");
        String password = (String) request.getParam("password");
        
        ServerProperties properties = new ServerProperties();
        
        String loginAdmin = properties.getProperty("LOGIN_ADMIN");
        String passwordAdmin = properties.getProperty("PASSWORD_ADMIN");
        
        if(!login.equals(loginAdmin) || !password.equals(passwordAdmin))
        {
            response.addParam("error", "Login ou password invalide");
        }
        else
        {
            logged = true;
        }
        
        sendMessage(response);
        
        return logged;
    }
    
    public void executeLClients(Message request) 
    {
        Message response = new Message();
        
        response.addParam("data", serverDISMAP.getClients());
        
        for(int i = 0 ; i < serverDISMAP.getClients().size() ; i++)
        {
            System.out.println(serverDISMAP.getClients().get(i));
        }
        
        sendMessage(response);
    }
    
    public void executeStop(Message request)
    {
        serverDISMAP.setStop(true);
    }
}

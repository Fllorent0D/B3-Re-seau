/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payp2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ListTasks;
import server.ServerProperties;
import server.SourceTasks;
import server.ThreadClient;


/**
 *
 * @author bastin
 */
public class ServerPAYP2 extends Thread {
    
    private int portPAYP2;
    private int nbrThreadsPAYP2;
    private String ipServer;
    private InetAddress addrServer;
    private ServerProperties properties;
    private SourceTasks tasksToExecute;
    private ServerSocket SSocket;

    public ServerPAYP2(SourceTasks t)
    {
        properties = new ServerProperties();
        
        portPAYP2 = Integer.parseInt(properties.getProperty("PORT_PAYP2"));
        ipServer = properties.getProperty("IP_SERVER");

        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerPAYP2.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        nbrThreadsPAYP2 = Integer.parseInt(properties.getProperty("NBR_THREADS_PAYP2"));
        
        if(t == null)
            tasksToExecute = new ListTasks();
        else
            tasksToExecute = t;
        
        System.out.println("Configuration de PAYP2");
        System.out.println("---------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portPAYP2);
        System.out.println("NBR THREADS: " + nbrThreadsPAYP2);
        System.out.println("");
    }
    
    @Override
    public void run()
    {
        try {
            SSocket = new ServerSocket(portPAYP2, 10, addrServer);
        } catch (IOException ex) {
            Logger.getLogger(ServerPAYP2.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        for(int i = 0 ; i < nbrThreadsPAYP2 ; i++)
        {
            ThreadClient thr = new ThreadClient(tasksToExecute);
            thr.start();
        }
        
        while(!isInterrupted())
        { 
            try {
                Socket CSocket = SSocket.accept();

                System.out.println("PAYP2: Client pris en charge");

                tasksToExecute.recordTask(new RunnablePAYP2(CSocket, this));

            } catch (IOException ex) {
                Logger.getLogger(ServerPAYP2.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }      
        }
    }
    
    public static void main(String[] args) {
        new ServerPAYP2(null).start();
    }     
}
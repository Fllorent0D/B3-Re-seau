/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redmmp;

import dismap.RunnableDISMAP;
import dismap.ServerDISMAP;
import dismap.ServerDSA;
import java.beans.PropertyChangeSupport;
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
public class ServerREDMMP extends Thread {
    
    private int portREDMMP;
    private final int nbrThreadsREDMMP;
    private String ipServer;
    private InetAddress addrServer;
    
    private final SourceTasks tasksToExecute;
    private ServerSocket SSocket;
    
    private boolean stop = false;

    public ServerREDMMP(SourceTasks t) {
        
        ServerProperties properties = new ServerProperties();
        
        portREDMMP = Integer.parseInt(properties.getProperty("PORT_REDMMP"));
        ipServer = properties.getProperty("IP_SERVER");

        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        nbrThreadsREDMMP = Integer.parseInt(properties.getProperty("NBR_THREADS_REDMMP"));
        
        if(t == null)
            tasksToExecute = new ListTasks();
        else
            tasksToExecute = t;
        
        System.out.println("Configuration de REDMMP");
        System.out.println("-----------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portREDMMP);
        System.out.println("NBR THREADS: " + nbrThreadsREDMMP);
        System.out.println("");
    }
    
    @Override
    public void run() {
        try {
            SSocket = new ServerSocket(portREDMMP, 10, addrServer);
        } catch (IOException ex) {
            Logger.getLogger(ServerDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        for(int i = 0 ; i < nbrThreadsREDMMP ; i++)
        {
            ThreadClient thr = new ThreadClient(tasksToExecute);
            thr.start();
        }
        
        while(!isInterrupted())
        { 
            try {
                Socket CSocket = SSocket.accept();
                
                System.out.println("REDMMP: Client pris en charge");
                
                tasksToExecute.recordTask(new RunnableREDMMP(CSocket, this));
                
            } catch (IOException ex) {
                Logger.getLogger(ServerDISMAP.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }
        }        
    }
}

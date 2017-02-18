package server_indep;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import dismap.ServerDISMAP;
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
public class ServerINDEP extends Thread {
    
    private int portINDEP;
    private final int nbrThreadsINDEP;
    private String ipINDEP;
    private InetAddress addrINDEP;
    
    private ServerProperties properties;
    private final SourceTasks tasksToExecute;
    private ServerSocket SSocket;
    
    public ServerINDEP(SourceTasks t)
    {
        properties = new ServerProperties();
        
        portINDEP = Integer.parseInt(properties.getProperty("PORT_INDEP"));

        ipINDEP = properties.getProperty("IP_SERVER");

        try {
            addrINDEP = InetAddress.getByName(ipINDEP);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        nbrThreadsINDEP= Integer.parseInt(properties.getProperty("NBR_THREADS_INDEP"));
        
        if(t == null)
            tasksToExecute = new ListTasks();
        else
            tasksToExecute = t;
        
        System.out.println("Configuration de INDEP");
        System.out.println("----------------------");
        System.out.println("IP: " + ipINDEP);
        System.out.println("PORT: " + portINDEP);
        System.out.println("NBR THREADS: " + nbrThreadsINDEP);
        System.out.println("");
    }
    
    @Override
    public void run()
    {
        try {
            SSocket = new ServerSocket(portINDEP, 10, addrINDEP);
        } catch (IOException ex) {
            Logger.getLogger(ServerINDEP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        for(int i = 0 ; i < nbrThreadsINDEP ; i++)
        {
            ThreadClient thr = new ThreadClient(tasksToExecute);
            thr.start();
        }
        
        while(!isInterrupted())
        { 
            try {
                Socket CSocket = SSocket.accept();
                System.out.println("INDEP: Client pris en charge");
                tasksToExecute.recordTask(new RunnableINDEP(CSocket));
            } catch (IOException ex) {
                Logger.getLogger(ServerINDEP.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            } 
        }
    }
    
    public static void main(String[] args) {
        new ServerINDEP(null).start();
    }
}

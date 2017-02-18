/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta.payp;

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
public class ServerPAYP extends Thread {
    
    private int portPAYP;
    private int nbrThreadsPAYP;
    private String ipServer;
    private InetAddress addrServer;
    private ServerProperties properties;
    private SourceTasks tasksToExecute;
    private ServerSocket SSocket;

    public ServerPAYP(SourceTasks t)
    {
        properties = new ServerProperties();
        
        portPAYP = Integer.parseInt(properties.getProperty("PORT_PAYP"));
        ipServer = properties.getProperty("IP_SERVER");

        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerPAYP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        nbrThreadsPAYP = Integer.parseInt(properties.getProperty("NBR_THREADS_PAYP"));
        
        if(t == null)
            tasksToExecute = new ListTasks();
        else
            tasksToExecute = t;
        
        System.out.println("Configuration de PAYP");
        System.out.println("---------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portPAYP);
        System.out.println("NBR THREADS: " + nbrThreadsPAYP);
        System.out.println("");
    }
    
    @Override
    public void run()
    {
        try {
            SSocket = new ServerSocket(portPAYP, 10, addrServer);
        } catch (IOException ex) {
            Logger.getLogger(ServerPAYP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        for(int i = 0 ; i < nbrThreadsPAYP ; i++)
        {
            ThreadClient thr = new ThreadClient(tasksToExecute);
            thr.start();
        }
        
        while(!isInterrupted())
        { 
            try {
                Socket CSocket = SSocket.accept();

                System.out.println("PAYP: Client pris en charge");

                tasksToExecute.recordTask(new RunnablePAYP(CSocket, this));

            } catch (IOException ex) {
                Logger.getLogger(ServerPAYP.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }      
        }
    }
    
    public static void main(String[] args) {
        new ServerPAYP(null).start();
    }     
}

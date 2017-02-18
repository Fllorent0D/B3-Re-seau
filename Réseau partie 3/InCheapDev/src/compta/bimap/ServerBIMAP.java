/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta.bimap;

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
public class ServerBIMAP extends Thread {
    private int portBIMAP;
    private int nbrThreadsBIMAP;
    private String ipServer;
    private InetAddress addrServer;
    private ServerProperties properties;
    private SourceTasks tasksToExecute;
    private ServerSocket SSocket;

    public ServerBIMAP(SourceTasks t)
    {
        properties = new ServerProperties();
        
        portBIMAP = Integer.parseInt(properties.getProperty("PORT_BIMAP"));
        ipServer = properties.getProperty("IP_SERVER");

        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(compta.bimap.ServerBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        nbrThreadsBIMAP = Integer.parseInt(properties.getProperty("NBR_THREADS_BIMAP"));
        
        if(t == null)
            tasksToExecute = new ListTasks();
        else
            tasksToExecute = t;
        
        System.out.println("Configuration de BIMAP");
        System.out.println("-------------------- -");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portBIMAP);
        System.out.println("NBR THREADS: " + nbrThreadsBIMAP);
        System.out.println("");
    }
    
    @Override
    public void run()
    {
        try {
            SSocket = new ServerSocket(portBIMAP, 10, addrServer);
        } catch (IOException ex) {
            Logger.getLogger(compta.bimap.ServerBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        for(int i = 0 ; i < nbrThreadsBIMAP ; i++)
        {
            ThreadClient thr = new ThreadClient(tasksToExecute);
            thr.start();
        }
        
        while(!isInterrupted())
        { 
            try {
                Socket CSocket = SSocket.accept();

                System.out.println("BIMAP: Client pris en charge");

                tasksToExecute.recordTask(new RunnableBIMAP(CSocket, this));

            } catch (IOException ex) {
                Logger.getLogger(compta.bimap.ServerBIMAP.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }      
        }
    }
    
    public static void main(String[] args) {
        new ServerBIMAP(null).start();
    }      
}

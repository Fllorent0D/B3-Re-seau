package dismap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ListTasks;
import server.ServerProperties;
import server.SourceTasks;
import server.ThreadClient;
import server_indep.RunnableINDEP;

/**
 *
 * @author bastin
 */
public class ServerDISMAP extends Thread {
    private int portDISMAP;
    private final int nbrThreadsDISMAP;
    private String ipServer;
    private InetAddress addrServer;
    
    private ServerProperties properties;
    private final SourceTasks tasksToExecute;
    private ServerSocket SSocket;
    
    private PropertyChangeSupport changeSupport;
    
    private boolean stop = false;
    
    private ArrayList<String> ipClients = new ArrayList();

    public ServerDISMAP(SourceTasks t)
    {
        changeSupport = new PropertyChangeSupport(this);
        properties = new ServerProperties();
        
        portDISMAP = Integer.parseInt(properties.getProperty("PORT_DISMAP"));
        ipServer = properties.getProperty("IP_SERVER");

        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        nbrThreadsDISMAP = Integer.parseInt(properties.getProperty("NBR_THREADS_DISMAP"));
        
        if(t == null)
            tasksToExecute = new ListTasks();
        else
            tasksToExecute = t;
        
        System.out.println("Configuration de DISMAP");
        System.out.println("-----------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portDISMAP);
        System.out.println("NBR THREADS: " + nbrThreadsDISMAP);
        System.out.println("");
    }
    
    @Override
    public void run()
    {
        try {
            SSocket = new ServerSocket(portDISMAP, 10, addrServer);
        } catch (IOException ex) {
            Logger.getLogger(ServerDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        for(int i = 0 ; i < nbrThreadsDISMAP ; i++)
        {
            ThreadClient thr = new ThreadClient(tasksToExecute);
            thr.start();
        }
        
        new ServerDSA(this).start();

        while(!isInterrupted())
        { 
            try {
                Socket CSocket = SSocket.accept();

                System.out.println("DISMAP: Client pris en charge");

                tasksToExecute.recordTask(new RunnableDISMAP(CSocket, this));

            } catch (IOException ex) {
                Logger.getLogger(ServerDISMAP.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }      
        }
    }
    
    public void addClient(String ipClient) {
        ipClients.add(ipClient);
    }
    
    public void removeClient(String ipClient) {  
        ipClients.remove(ipClient);
    }
    
    public ArrayList<String> getClients() {
        return ipClients;
    }
    
    public boolean getStop() {
        return stop;
    }
    
    public synchronized void setStop(boolean stop) {
        this.stop = stop;
        
        changeSupport.firePropertyChange("stop", null, stop);
    }
    
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }
    
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
    public static void main(String[] args) {
        new ServerDISMAP(null).start();
    }
}

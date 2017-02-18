/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import server_indep.protocoleINDEP;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bastin
 */
public class Client implements protocoleINDEP {
    
    protected int port;
    protected InetAddress ip;
    protected Socket CSocket = null;
    protected ObjectOutputStream oos = null;
    protected ObjectInputStream ois = null;
    
    public void connect() throws IOException
    {
        System.out.println("En attente d'une connexion...");
        CSocket = new Socket(ip, port);
        System.out.println("Connexion établie");
        System.out.println("Création des flux");
        oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
        oos.flush();
        ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
        System.out.println("Client opérationnel");
    }
    
    public void close()
    {
        try {
            CSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }


}

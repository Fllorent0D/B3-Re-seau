/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;

/**
 *
 * @author bastin
 */
public class Client {
    
    protected int port_server;
    protected String ip_server;
    protected Socket CSocket = null;
    protected DataOutputStream oos = null;
    protected DataInputStream ois = null;
    
    public Client(String ip, int p) throws IOException {
        port_server = p;
        ip_server = ip;
        
        System.out.println("En attente d'une connexion...");
        CSocket = new Socket(ip_server, port_server);
        System.out.println("Connexion établie");
        System.out.println("Création des flux");
        oos = new DataOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
        oos.flush();
        ois = new DataInputStream(new BufferedInputStream(CSocket.getInputStream()));
        System.out.println("Client opérationnel");
    }
    
    public void connect() throws IOException
    {
        System.out.println("En attente d'une connexion...");
        CSocket = new Socket(ip_server, port_server);
        System.out.println("Connexion établie");
        System.out.println("Création des flux");
        oos = new DataOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
        oos.flush();
        ois = new DataInputStream(new BufferedInputStream(CSocket.getInputStream()));
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

    public int getPort_server() {
        return port_server;
    }

    public void setPort_server(int port_server) {
        this.port_server = port_server;
    }

    public String getIp_server() {
        return ip_server;
    }

    public void setIp_server(String ip_server) {
        this.ip_server = ip_server;
    }

    public Socket getCSocket() {
        return CSocket;
    }

    public void setCSocket(Socket CSocket) {
        this.CSocket = CSocket;
    }

    public DataOutputStream getOos() {
        return oos;
    }

    public void setOos(DataOutputStream oos) {
        this.oos = oos;
    }

    public DataInputStream getOis() {
        return ois;
    }

    public void setOis(DataInputStream ois) {
        this.ois = ois;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import payp2.RunnablePAYP2;
import payp2.ServerPAYP2;

/**
 *
 * @author bastin
 */
public class RunnableClient {
    
    protected Socket CSocket;
    protected String ipServer;
    protected ObjectInputStream ois;
    protected ObjectOutputStream oos;
    
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
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void sendBytes(byte[] msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
        
    public byte[] receiveBytes()
    {
        byte[] msg = null;
        
        try {
            msg = (byte[]) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
        
        return msg;
    }
}

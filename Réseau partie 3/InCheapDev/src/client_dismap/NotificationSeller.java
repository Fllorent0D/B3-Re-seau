/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_dismap;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class NotificationSeller extends Thread {
    ServerSocket SSocket;
    Socket CSocket;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private String ipDISMAP2;
    private InetAddress addrDISMAP2;
    private int portDISMAP2;

    public NotificationSeller() {
        ServerProperties properties = new ServerProperties();

        ipDISMAP2 = properties.getProperty("IP_DISMAP2");

        try {
            addrDISMAP2 = InetAddress.getByName(ipDISMAP2);
        } catch (UnknownHostException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        portDISMAP2 = Integer.valueOf(properties.getProperty("PORT_DISMAP2"));
    }

    public void run() {

        do
        {
            try {   
                SSocket = new ServerSocket(portDISMAP2, 10, addrDISMAP2);

                do
                {
                    CSocket = SSocket.accept();

                    ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));

                    Message request = (Message) ois.readObject();

                    String notification = (String) request.getParam("notification");

                    JOptionPane.showMessageDialog(null, notification);

                    CSocket.close();

                } while(true);
            } catch (ClassNotFoundException | IOException ex) {} 

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while(true);
    }
}

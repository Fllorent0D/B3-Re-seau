/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_payp;

import client.SecurityClient;
import compta.payp.ProtocolePAYP;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ClientPAYP extends SecurityClient implements ProtocolePAYP {

    public static void main(String[] args) {
        try {
            ClientPAYP c = new ClientPAYP();
            c.connect();
            sleep(3000);
            c.close();
        } catch (ClassNotFoundException | NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | KeyStoreException | CertificateException | UnrecoverableKeyException | InterruptedException ex) {
            Logger.getLogger(ClientPAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ClientPAYP() throws UnknownHostException, ClassNotFoundException, NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, KeyStoreException, CertificateException, UnrecoverableKeyException
    {
        super();
        
        ServerProperties properties = new ServerProperties();

        String ipServer = properties.getProperty("IP_SERVER");
        int portPAYP = Integer.valueOf(properties.getProperty("PORT_PAYP"));
        
        InetAddress addrPAYP;
        
        addrPAYP = InetAddress.getByName(ipServer);
        setIp(addrPAYP);
        setPort(portPAYP);
        
        System.out.println("Configuration client PAYP");
        System.out.println("-------------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portPAYP);
        System.out.println("");
    }
    
    public Message payByCard(int cardNumber, int ownerName, Double amount)
    {
        Message request = new Message();
        request.setType(ProtocolePAYP.PAY_BY_CARD);
        request.addParam("cardNumber", cardNumber);
        request.addParam("ownerName", ownerName);
        request.addParam("amount", amount);
        
        System.out.println("Card: " + cardNumber);
        System.out.println("Owner: " + ownerName);
        System.out.println("Amount: " + amount);
        
        sendMessage(request);
        
        return receiveMessage();
    }
}

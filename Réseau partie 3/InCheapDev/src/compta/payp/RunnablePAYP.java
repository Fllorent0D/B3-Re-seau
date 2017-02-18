/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta.payp;

import com.floca.BeanDBAccess.BeanDBAccessOracle;
import compta.client_payp2.ClientPAYP2;
import static dismap.protocoleDISMAP.REQUEST_INTERRUPT;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class RunnablePAYP implements Runnable, ProtocolePAYP {
 
    private Socket CSocket;
    private ClientPAYP2 clientPAYP2;
    private String ipServer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos ;
    private BeanDBAccessOracle beanOracle;

    public RunnablePAYP(Socket socket, ServerPAYP server) {
        
        CSocket = socket;
        
        ServerProperties properties = new ServerProperties();
        
        ipServer = properties.getProperty("IP_SERVER");
        
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
            oos.flush(); 
            ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(RunnablePAYP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        String ip = properties.getProperty("IP_SHOP");
        String port = properties.getProperty("PORT_SHOP");
        String login = properties.getProperty("LOGIN_SHOP");
        String password = properties.getProperty("PASSWORD_SHOP");
        
        beanOracle = new BeanDBAccessOracle(ip, port, login, password);

        try {
            beanOracle.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RunnablePAYP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    boolean terminated = false;

    @Override
    public void run() {
        Message request = null;
        
        do
        {      
            request = receiveMessage();

            if(request == null)
                continue; 
          
            switch(request.getType())
            {
                case PAY_BY_CARD:
                    executePayByCard(request);
                    break;
                case REQUEST_INTERRUPT:
                default:
                    terminated = true;
                    break;
            }

        } while(!terminated);
          
        try {
            CSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RunnablePAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void executePayByCard(Message request)
    {
        Message response = new Message();
        response.setType(request.getType());
        
        try {            
            int cardNumber = (int) request.getParam("cardNumber");
            int ownerName = (int) request.getParam("ownerName");
            int amount = (int) request.getParam("amount");
            
            ClientPAYP2 clientPAYP2 = new ClientPAYP2();
            clientPAYP2.connect();
            
            response = clientPAYP2.paymentTransaction(cardNumber, ownerName, amount);
           
        } catch (IOException | ClassNotFoundException | NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | KeyStoreException | CertificateException | UnrecoverableKeyException ex) {
            Logger.getLogger(RunnablePAYP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        } finally {
            if(clientPAYP2 != null)
                clientPAYP2.close();
        }
           
        sendMessage(response);
    }
    
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
            Logger.getLogger(RunnablePAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendBytes(byte[] msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnablePAYP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

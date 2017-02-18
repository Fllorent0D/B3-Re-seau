/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package payp2;

import crypto.CryptoUtils;
import static dismap.protocoleDISMAP.REQUEST_INTERRUPT;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class RunnablePAYP2 implements Runnable, ProtocolePAYP2 {
 
    private Socket CSocket;
    private ServerPAYP2 serverPAYP2;
    private InetAddress addrServer;
    private String ipServer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    
    private CryptoUtils crypto;
    private PrivateKey privateKeyServerTransaction;
    private PublicKey publicKeyServerCompta;
    private String passwordSafe = "InCheapDev";
    private String passwordKeys = "InCheapDev";

    public RunnablePAYP2(Socket socket, ServerPAYP2 server) {
        
        CSocket = socket;
        serverPAYP2 = server;
        
        ServerProperties properties = new ServerProperties();

        String pathProject = properties.getProperty("PATH_PROJECT");
        String sep = properties.getProperty("SEPARATOR");
        
        try {
            crypto = new CryptoUtils();
            privateKeyServerTransaction = crypto.getPrivateKeyFromCertificate(pathProject + sep + "server" + sep + "serverSafe.jks", "server-transaction", passwordSafe, passwordKeys);
            publicKeyServerCompta = crypto.getPublicKeyFromCertificate(pathProject + sep + "server" + sep + "serverSafe.jks", "server-compta", passwordSafe);
            
        } catch (NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | ClassNotFoundException | KeyStoreException | CertificateException | UnrecoverableKeyException ex) {
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ipServer = properties.getProperty("IP_SERVER");
        
        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
            oos.flush(); 
            ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    boolean terminated = false;
    boolean logged = false;
    
    @Override
    public void run() {

        Message request = null;
        
        do
        {   
            request = (Message) receiveMessage();
            
            if(request == null)
                continue; 
            
            switch(request.getType())
            {
                case PAYMENT_TRANSACTION:
                    executePaymentTransaction(request);
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
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void executePaymentTransaction(Message request)
    {
        Message response = new Message();
        response.setType(request.getType());
        
        byte[] signature = receiveBytes();
        
        try {
            boolean check = crypto.verifySignature(crypto.getByteFromObject(request), signature, publicKeyServerCompta);
            
            if(check)
            {
                response.addParam("refusal", "Votre carte de crédit est périmée");
            }
            else
            {
                int x =(Math.random()<0.5) ? 0 : 1;
                
                if(x == 0)
                    response.addParam("error", "Un problème de sécurité est survenu");
            }
            
            sendMessage(response);
            
        } catch (IOException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        try {
            sendBytes(crypto.getByteFromObject(response));
        } catch (IOException ex) {
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
        }
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

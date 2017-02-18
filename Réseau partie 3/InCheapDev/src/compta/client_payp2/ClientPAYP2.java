/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta.client_payp2;

import client.SecurityClient;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import payp2.ProtocolePAYP2;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ClientPAYP2 extends SecurityClient implements ProtocolePAYP2 {
    
    protected PublicKey publicKeyServerTransaction;
    protected PrivateKey privateKeyServerCompta;
    
    public static void main(String[] args) {
        try {
            new ClientPAYP2().connect();
        } catch (ClassNotFoundException | NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | KeyStoreException | CertificateException | UnrecoverableKeyException ex) {
            Logger.getLogger(ClientPAYP2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ClientPAYP2() throws UnknownHostException, ClassNotFoundException, NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, KeyStoreException, CertificateException, UnrecoverableKeyException
    {
        super();
        
        ServerProperties properties = new ServerProperties();

        String pathProject = properties.getProperty("PATH_PROJECT");
        String sep = properties.getProperty("SEPARATOR");
        
        publicKeyServerTransaction = crypto.getPublicKeyFromCertificate(pathProject + sep + "server" + sep + "server_transaction.cer");
        privateKeyServerCompta = crypto.getPrivateKeyFromCertificate(pathProject + sep + "server" + sep + "serverSafe.jks", "server-compta", "InCheapDev", "InCheapDev"); 
       

        String ipServer = properties.getProperty("IP_SERVER");
        int portPAYP2 = Integer.valueOf(properties.getProperty("PORT_PAYP2"));
        
        InetAddress addrPAYP2;

        addrPAYP2 = InetAddress.getByName(ipServer);
        setIp(addrPAYP2);
        setPort(portPAYP2);
         
        System.out.println("Configuration client PAYP2");
        System.out.println("--------------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portPAYP2);
        System.out.println("");
    }
    
    public Message paymentTransaction(int cardNumber, int ownerName, int amount)
    {
        Message request = new Message();
        Message response = new Message();
        request.setType(ProtocolePAYP2.PAYMENT_TRANSACTION);

        try {
            request.addParam("cardNumber", crypto.encryptASM(crypto.getByteFromObject(cardNumber), publicKeyServerTransaction));
            request.addParam("ownerName", ownerName);
            request.addParam("amount", amount);
            
            sendMessage(request);
            
            byte[] signature = crypto.getSignature(crypto.getByteFromObject(request), privateKeyServerCompta);
            
            send(signature);
            
            response = receiveMessage();
              
        } catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | SignatureException ex) {   
            Logger.getLogger(ClientPAYP2.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        return response;
    }
}

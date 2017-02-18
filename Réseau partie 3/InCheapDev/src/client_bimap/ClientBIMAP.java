/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_bimap;

import client.SecurityClient;
import static compta.bimap.ProtocoleBIMAP.REQUEST_MAKE_BILL;
import compta.bimap.ProtocoleBIMAP;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLSocket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import models.FactureFournisseur;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ClientBIMAP extends SecurityClient implements ProtocoleBIMAP {

    private SecretKey secretKeyComptable;
    private SecretKey secretKeyComptableHMAC;
    private SecretKey secretKeyServerCompta;
    private PublicKey publicKeyServerCompta;
    private X509Certificate certificateKeyComptable;
    private PrivateKey privateKeyComptable;
    private String passwordSafe = "InCheapDev";
    private String passwordComptable = "InCheapDev";
    
    private JFrame parent;
    
    public ClientBIMAP(JFrame parent) throws ClassNotFoundException, NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, KeyStoreException, CertificateException, UnrecoverableKeyException
    {
        super();
        
        this.parent = parent;
        
        ServerProperties properties = new ServerProperties();
        
        String pathProject = properties.getProperty("PATH_PROJECT");
        String sep = properties.getProperty("SEPARATOR");
        
        secretKeyComptable = crypto.getSecretKey(pathProject + sep + "client" + sep + "secretKeyComptable.ser");
        secretKeyComptableHMAC = crypto.getSecretKey(pathProject + sep + "client" + sep + "secretKeyComptableHMAC.ser");
        privateKeyComptable = crypto.getPrivateKeyFromCertificate(pathProject + sep + "client" + sep + "clientSafe.jks", "comptable", passwordSafe, passwordComptable);
        certificateKeyComptable = crypto.getCertificate(pathProject + sep + "client" + sep + "comptable.cer");

        String ipServer = properties.getProperty("IP_SERVER");
        int portBIMAP = Integer.valueOf(properties.getProperty("PORT_BIMAP"));
        setPort(portBIMAP);
        
        InetAddress addrDIMAP = InetAddress.getByName(ipServer);
        setIp(addrDIMAP);
        
        connect();
            
        handshake();
        
        System.out.println("Configuration client BIMAP");
        System.out.println("--------------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portBIMAP);
        System.out.println("");
    }
    
    public void interrupt()
    {
        Message message = new Message();
        message.setType(REQUEST_INTERRUPT);
        sendCypherMessage(getByteFromObject(message), secretKeyComptable);
    }
        
    public void handshake()
    {
        Message message = new Message();
        
        message.addParam("message", "hello_server");
        message.addParam("secretKeyComptableHMAC", secretKeyComptableHMAC);
  
        try {
            X509Certificate certificateServerCompta = (X509Certificate) getObjectFromByte(receive());
            
            certificateServerCompta.checkValidity();
            
            publicKeyServerCompta = certificateServerCompta.getPublicKey();
            
            send(getByteFromObject(certificateKeyComptable));
            
            byte[] request = crypto.encryptSYM(getByteFromObject(message), secretKeyComptable);
            send(request);
            
            byte[] request2 = crypto.encryptASM(getByteFromObject(secretKeyComptable), publicKeyServerCompta);
            send(request2);
            
            Message response = (Message) getObjectFromByte(receiveCypherMessage(secretKeyComptable));
            
            String hello = (String) response.getParam("message");
            
            secretKeyServerCompta = (SecretKey) response.getParam("secretKeyServerCompta");
            
            System.out.println("========= Handshake : " + hello + " ==========");
            
        } catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | CertificateExpiredException | CertificateNotYetValidException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }  
    }
    
    public Message authentification(String login, String password)
    {
        Message request = new Message();
        Message response = null;
        
        request.setType(REQUEST_LOGIN);
        
        long temps = (new Date()).getTime();
        double alea = Math.random();
        
        try {
            byte[] cipherPassword = crypto.makeDigest(password, temps, alea);
        
            request.addParam("temps", temps);
            request.addParam("alea", alea);
        
            request.addParam("login", login);
            request.addParam("cipherPassword", cipherPassword);
            
            sendCypherMessage(getByteFromObject(request), secretKeyComptable);
            
            response = (Message) getObjectFromByte(receiveCypherMessage(secretKeyComptable));
            
        } catch (IOException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
       
        return response;
    }
    
    public FactureFournisseur nextBill()
    {
        FactureFournisseur ff = null;
        Message request = new Message();
        request.setType(REQUEST_GET_NEXT_BILL);

        sendCypherMessage(getByteFromObject(request), secretKeyComptable);
        
        Message response = (Message) getObjectFromByte(receiveCypherMessage(secretKeyComptable));
        
        if(response.hasParam("error"))
            JOptionPane.showMessageDialog(parent, response.getParam("error"));
        else
        {
            byte[] data = (byte[]) response.getParam("factureFournisseur");
            
            try {
                ff = (FactureFournisseur) getObjectFromByte(crypto.decryptSYM(data, secretKeyServerCompta));
            } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
                Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return ff;
    }
    
    public Message listBills(Date dateBegin, Date dateEnd, String idFournisseur)
    {
        Message request = new Message();
        Message inside = new Message();
        Message response = null;
        
        request.setType(compta.bimap.ProtocoleBIMAP.REQUEST_LIST_BILLS);
       
        if(dateBegin != null && dateEnd != null)
        {
            request.addParam("dateBegin", dateBegin);
            request.addParam("dateEnd", dateEnd);
        }
        
        if(idFournisseur != null)
            request.addParam("idFournisseur", idFournisseur);

       try {
            byte[] signature = crypto.getSignature(getByteFromObject(request), privateKeyComptable);
 
            sendCypherMessage(getByteFromObject(request), secretKeyComptable);
            sendCypherMessage(signature, secretKeyComptable);
            
            response = (Message) getObjectFromByte(receiveCypherMessage(secretKeyComptable));
            
        } catch (SignatureException | InvalidKeyException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return response; 
    }
    
    public Message validateBill(int idFacture)
    {
        Message request = new Message();
        Message response = null;
        request.setType(ProtocoleBIMAP.REQUEST_VALIDATE_BILL);
        
        request.addParam("idFacture", idFacture);
        
        try {
            byte[] signature = crypto.getSignature(getByteFromObject(request), privateKeyComptable);
            
            sendCypherMessage(getByteFromObject(request), secretKeyComptable);
            sendCypherMessage(signature, secretKeyComptable);
            
            response = (Message) getObjectFromByte(receiveCypherMessage(secretKeyComptable));
                        
        } catch (SignatureException | InvalidKeyException ex) {
            Logger.getLogger(client_bimap.ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(parent, ex.getMessage());
        }
        
        return response;
    }
    
    public Message sendBills(String billsToAvoid)
    {
        Message response = null;
        Message request = new Message();
        request.setType(REQUEST_SEND_BILLS);
        
        try {            
            request.addParam("billsToAvoid", billsToAvoid);
            
            byte[] signature = crypto.getSignature(getByteFromObject(request), privateKeyComptable);
            
            sendCypherMessage(getByteFromObject(request), secretKeyComptable);
            
            sendCypherMessage(signature, secretKeyComptable);
            
            response = (Message) getObjectFromByte(receiveCypherMessage(secretKeyComptable));
            
        } catch (SignatureException | InvalidKeyException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return response;
    }
    
    public Message makeBill(float montant, int idFournisseur)
    {
        Message response = new Message();
        Message request = new Message();
        request.setType(REQUEST_MAKE_BILL);
        
        request.addParam("montant", montant);
        request.addParam("idFournisseur", idFournisseur);
        
        try {
            byte[] hmac = crypto.hashHMAC(getByteFromObject(request), secretKeyComptableHMAC);
            sendCypherMessage(getByteFromObject(request), secretKeyComptable);
            sendCypherMessage(hmac, secretKeyComptable);
            response = (Message) getObjectFromByte(receiveCypherMessage(secretKeyComptable));
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        return response;        
    }
}

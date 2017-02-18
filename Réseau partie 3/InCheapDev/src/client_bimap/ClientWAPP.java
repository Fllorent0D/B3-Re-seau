/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_bimap;

import client.Client;
import java.io.IOException;
import java.net.InetAddress;
import server.Message;
import server.ServerProperties;
import compta.wapp.ProtocoleWAPP;
import compta.wapp.RunnableWAPP;
import crypto.CryptoUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author bastin
 */
public class ClientWAPP implements ProtocoleWAPP {
    
    protected int port;
    protected InetAddress ip;
    protected SSLSocket CSocket = null;
    protected ObjectOutputStream oos = null;
    protected ObjectInputStream ois = null;
    
    protected SSLContext sslContext;
    protected CryptoUtils crypto;
    protected String passwordSafe = "InCheapDev";
    protected String passwordComptable = "InCheapDev";
    protected PublicKey publicKeySSL;
    protected ServerProperties properties = new ServerProperties();
    protected String pathProject = properties.getProperty("PATH_PROJECT");
    protected String sep = properties.getProperty("SEPARATOR");
    
    private int portWAPP;
    
    public ClientWAPP() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, FileNotFoundException, ClassNotFoundException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException
    {      
        String ipServer = properties.getProperty("IP_SERVER");
        portWAPP = Integer.valueOf(properties.getProperty("PORT_WAPP"));
        setPort(portWAPP);
        
        InetAddress addrWAPP = InetAddress.getByName(ipServer);
        setIp(addrWAPP);
        
        this.sslContext = SSLContext.getInstance("SSLv3");
        this.crypto = new CryptoUtils();

        publicKeySSL = crypto.getPublicKeyFromCertificate(pathProject + sep + "server" + sep + "ssl.cer");
        
        connect();
             
        System.out.println("Configuration client WAPP");
        System.out.println("-------------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portWAPP);
        System.out.println("");

    }
    
    public void connect() throws IOException, KeyStoreException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException
    {
        KeyStore keyStore = crypto.getKeyStore(pathProject + sep + "server" + sep + "serverSafe.jks", passwordSafe);
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, passwordSafe.toCharArray());
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);
        
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        
        SSLSocketFactory sslFac = sslContext.getSocketFactory();
        
        CSocket = (SSLSocket) sslFac.createSocket(ip, port);
        oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
        oos.flush();
        ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
        System.out.println("Client connecté");
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
    
    public Message receiveMessage()
    {
        Message msg = null;
        
        try {
            msg = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RunnableWAPP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
            
    public void sendMessage(Message msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnableWAPP.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void interrupt()
    {
        Message request = new Message();
        request.setType(REQUEST_INTERRUPT);
        sendMessage(request);
    }
    
    public Message payWage(String name)
    {
        Message request = new Message();
        request.setType(REQUEST_PAY_WAGE);
        request.addParam("name", name);
        
        sendMessage(request);
        return receiveMessage();
    }
    
     public Message payWages()
    {
        Message request = new Message();
        request.setType(REQUEST_PAY_WAGES);
        
        sendMessage(request);
        return receiveMessage();
    }
    
    public Message listPayedWages(int month)
    {
        Message request = new Message();
        request.setType(REQUEST_LIST_PAYED_WAGES);
        request.addParam("month", month);
        
        sendMessage(request);
        return receiveMessage();
    }
    
    public static void main(String[] args) throws NoSuchProviderException {
        try {
            new ClientWAPP();
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | ClassNotFoundException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(ClientWAPP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

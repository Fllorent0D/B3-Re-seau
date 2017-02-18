/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta.wapp;

import crypto.CryptoUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import server.ListTasks;
import server.ServerProperties;
import server.SourceTasks;
import server.ThreadClient;

/**
 *
 * @author bastin
 */
public class ServerWAPP extends Thread {
    private int portWAPP;
    private int nbrThreadsWAPP;
    private String ipServer;
    private InetAddress addrServer;
    private ServerProperties properties;
    private SourceTasks tasksToExecute;
    private SSLServerSocket SSocket;
    
    protected SSLContext sslContext;
    protected CryptoUtils crypto;
    protected String passwordSafe = "InCheapDev";
    protected String passwordComptable = "InCheapDev";
    private PrivateKey privateKeySSL;
    private SSLServerSocketFactory sslFac;

    public ServerWAPP(SourceTasks t) throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, ClassNotFoundException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException
    {
        properties = new ServerProperties();

        String pathProject = properties.getProperty("PATH_PROJECT");
        String sep = properties.getProperty("SEPARATOR");
        
        portWAPP = Integer.parseInt(properties.getProperty("PORT_WAPP"));
        ipServer = properties.getProperty("IP_SERVER");

        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(compta.bimap.ServerBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        nbrThreadsWAPP = Integer.parseInt(properties.getProperty("NBR_THREADS_WAPP"));
        
        if(t == null)
            tasksToExecute = new ListTasks();
        else
            tasksToExecute = t;
        
        this.sslContext = SSLContext.getInstance("SSLv3");
        this.crypto = new CryptoUtils();

        privateKeySSL = crypto.getPrivateKeyFromCertificate(pathProject + sep + "server" + sep + "serverSafe.jks", "ssl", passwordSafe, passwordSafe);
        
        KeyStore keyStore = crypto.getKeyStore(pathProject + sep + "server" + sep + "serverSafe.jks", passwordSafe);
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, passwordSafe.toCharArray());
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keyStore);
        
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        
        sslFac = sslContext.getServerSocketFactory();
        
        System.out.println("Configuration de WAPP");
        System.out.println("---------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portWAPP);
        System.out.println("NBR THREADS: " + nbrThreadsWAPP);
        System.out.println("");
    }
    
    @Override
    public void run()
    {
        try {
            SSocket = (SSLServerSocket) sslFac.createServerSocket(portWAPP);
        } catch (IOException ex) {
            Logger.getLogger(ServerWAPP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i = 0 ; i < nbrThreadsWAPP ; i++)
        {
            ThreadClient thr = new ThreadClient(tasksToExecute);
            thr.start();
        }
        
        while(!isInterrupted())
        { 
            try {
                SSLSocket CSocket = (SSLSocket) SSocket.accept();

                System.out.println("WAPP: Client pris en charge");

                tasksToExecute.recordTask(new RunnableWAPP(CSocket, this));

            } catch (IOException ex) {
                Logger.getLogger(compta.bimap.ServerBIMAP.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(1);
            }      
        }
    }
    
    public static void main(String[] args) {
        try {
            new ServerWAPP(null).start();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | ClassNotFoundException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(ServerWAPP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     
}

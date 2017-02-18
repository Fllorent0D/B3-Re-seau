/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta;

import compta.bimap.ServerBIMAP;
import compta.payp.ServerPAYP;
import compta.wapp.ServerWAPP;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import payp2.ServerPAYP2;

/**
 *
 * @author bastin
 */
public class ServerCompta extends Thread {
    
    public void run()
    {
        new ServerBIMAP(null).start();
        new ServerPAYP(null).start();
        try {
            new ServerWAPP(null).start();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | IOException | ClassNotFoundException | KeyStoreException | CertificateException | UnrecoverableKeyException | KeyManagementException ex) {
            Logger.getLogger(ServerCompta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        new ServerCompta().start();
    }
}

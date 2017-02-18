/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import client_bimap.ClientBIMAP;
import compta.bimap.RunnableBIMAP;
import crypto.CryptoUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 *
 * @author bastin
 */
public class SecurityClient extends Client {
    
    protected CryptoUtils crypto;
    
    public SecurityClient() throws ClassNotFoundException, NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, KeyStoreException, CertificateException, UnrecoverableKeyException
    {
        crypto = new CryptoUtils();
    }
    
    public Object getObjectFromByte(byte[] msg)
    {
        Object obj = null;
        
        try {
            obj = crypto.getObjectFromByte(msg);
        } catch (IOException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return obj;
    }
    
    public byte[] getByteFromObject(Object obj)
    {
        byte[] msg = null;
        
        try {
            msg = crypto.getByteFromObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public byte[] receiveCypherMessage(SecretKey secretKey)
    {
        byte[] msg = null;
        
        try {
            msg = crypto.decryptSYM(receive(), secretKey);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public void sendCypherMessage(byte[] msg, SecretKey secretKey)
    {
        try {
            send(crypto.encryptSYM(msg, secretKey));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
            Logger.getLogger(ClientBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

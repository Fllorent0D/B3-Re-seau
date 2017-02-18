/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author bastin
 */
public class CryptoUtils {
    
    private static final String algoASM = "RSA/ECB/PKCS1Padding";
    private static final String algoSYM = "DES/ECB/PKCS5Padding";
    private static final String algoSIGN = "SHA1withRSA";
    private static final String algoHMAC = "HMAC-MD5";
    private static final String algoDigest = "SHA-1";
    private static final String codeProvider = "BC";
    
    private Signature sign;
    private Cipher cphASM;
    private Cipher cphSYM;
    private Mac hmac;
    private MessageDigest md;
        
    public CryptoUtils() throws NoSuchProviderException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, ClassNotFoundException, KeyStoreException, CertificateException, UnrecoverableKeyException {
        Security.addProvider(new BouncyCastleProvider());
        sign = Signature.getInstance(algoSIGN, codeProvider);
        cphASM = Cipher.getInstance(algoASM, codeProvider);
        cphSYM = Cipher.getInstance(algoSYM, codeProvider);
        hmac = Mac.getInstance(algoHMAC, codeProvider);
        md = MessageDigest.getInstance(algoDigest, codeProvider);
    }
    
    public KeyStore getKeyStore(String location, String password) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException
    {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(location), password.toCharArray());
        return ks;
    }
    
    public void generateSeKey(String fileLocation) throws IOException, NoSuchAlgorithmException, NoSuchProviderException
    {
        /* Génération de la clé */
        KeyGenerator keyGen = KeyGenerator.getInstance("DES", codeProvider);
        keyGen.init(new SecureRandom());
        SecretKey key = keyGen.generateKey();
        
        /* Ecriture de la clé */
        ObjectOutputStream keyFile = new ObjectOutputStream(new FileOutputStream(fileLocation));
        keyFile.writeObject(key);
        keyFile.close();
    }
    
    public SecretKey getSecretKey(String fileLocation) throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream keyFile = new ObjectInputStream(new FileInputStream(fileLocation));
        SecretKey secretKey = (SecretKey) keyFile.readObject();
        keyFile.close();
        
        return secretKey;        
    }
    
    public PublicKey getPublicKeyFromCertificate(String certificateLocation) throws FileNotFoundException, CertificateException
    {
        FileInputStream fin = new FileInputStream(certificateLocation);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
        return certificate.getPublicKey();
    }
        
    public PublicKey getPublicKeyFromCertificate(String JKSLocation, String certificatName, String password) throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException 
    {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(JKSLocation), password.toCharArray());
        
        X509Certificate certificat = (X509Certificate) ks.getCertificate(certificatName);
        
        return (PublicKey) certificat.getPublicKey();
    }
    
    public X509Certificate getCertificate(String certificateLocation) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException
    {
        FileInputStream fin = new FileInputStream(certificateLocation);
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
        return certificate;
    }
    
    public X509Certificate getCertificate(String JKSLocation, String certificateName, String password) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException
    {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(JKSLocation), password.toCharArray());
        
        return (X509Certificate) ks.getCertificate(certificateName);
    }
    
    public PrivateKey getPrivateKeyFromCertificate(String JKSLocation, String certificatName, String password, String passwordKey) throws FileNotFoundException, IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException  
    {
        
        
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(JKSLocation), password.toCharArray());
   
        return (PrivateKey) ks.getKey(certificatName, passwordKey.toCharArray());
    }
    
    public byte[] encryptASM(byte[] message, PublicKey publicKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        cphASM.init(Cipher.ENCRYPT_MODE, publicKey);
        return cphASM.doFinal(message);
    }
    
    public byte[] decryptASM(byte[] message, PrivateKey privateKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        cphASM.init(Cipher.DECRYPT_MODE, privateKey);
        return cphASM.doFinal(message);
    }
    
    public byte[] encryptSYM(byte[] message, SecretKey secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException
    {
        cphSYM.init(Cipher.ENCRYPT_MODE, secretKey);
        return cphSYM.doFinal(message);
    }
    
    public byte[] decryptSYM(byte[] cypherText, SecretKey secretKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        cphSYM.init(Cipher.DECRYPT_MODE, secretKey);
        return cphSYM.doFinal(cypherText);
        
    }
    
    public byte[] hashHMAC(byte[] message, SecretKey secretKey) throws InvalidKeyException
    {
        hmac.init(secretKey);
        hmac.update(message);
        return hmac.doFinal();
    }
    
    public boolean verifyHMAC(byte[] receivedHashed, byte[] message, SecretKey secretKey) throws InvalidKeyException
    {
        hmac.init(secretKey);
        hmac.update(message);
        byte[] generatedHashed = hmac.doFinal();
        
        if(MessageDigest.isEqual(generatedHashed, receivedHashed))
            return true;
        else
            return false;
    }
    
    public byte[] makeDigest(String message, long temps, double alea) throws IOException
    {
        md.update(message.getBytes());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        bdos.writeLong(temps);
        bdos.writeDouble(alea);
        
        md.update(baos.toByteArray());
        
        return md.digest();
    }
    
    public byte[] getByteFromObject(Object o) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        return baos.toByteArray();
    }
    
    public Object getObjectFromByte(byte[] b) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }
    
    public byte[] getSignature(byte[] message, PrivateKey privateKey) throws SignatureException, InvalidKeyException
    {
        sign.initSign(privateKey);
        sign.update(message);
        return sign.sign();
    }
    
    public boolean verifySignature(byte[] message, byte[] signature, PublicKey publicKey) throws InvalidKeyException, SignatureException
    {
        sign.initVerify(publicKey);
        sign.update(message);
        return sign.verify(signature);
    }
    
    public static void main(String[] args) {
      /*  FileInputStream fin;
        try {
            fin = new FileInputStream("C:\\Users\\bastin\\Documents\\NetBeansProjects\\InCheapDev\\crypto\\comptable.cer");
            CertificateFactory f = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
            PublicKey pk = certificate.getPublicKey();
            
            System.out.println("Public key: " + pk);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(CryptoUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
}

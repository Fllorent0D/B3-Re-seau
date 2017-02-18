/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta.bimap;

import com.floca.BeanDBAccess.BeanDBAccessOracle;
import crypto.CryptoUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.cert.X509Certificate;
import models.FactureFournisseur;
import models.Fournisseur;
import server.Message;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class RunnableBIMAP implements Runnable, ProtocoleBIMAP {
    private Socket CSocket;
    private ServerBIMAP serverBIMAP;
    private InetAddress addrServer;
    private String ipServer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos ;
    private BeanDBAccessOracle beanOracle;
    
    private CryptoUtils crypto;
    private PrivateKey privateKeyServerCompta;
    private SecretKey secretKeyComptable;
    private SecretKey secretKeyComptableHMAC;
    private SecretKey secretKeyServerCompta;
    private PublicKey publicKeyComptable;
    private X509Certificate certificateServerCompta;
    
    private String passwordSafe = "InCheapDev";
    private String passwordKeys = "InCheapDev";
    
    String pathProject;
    String sep;

    public RunnableBIMAP(Socket socket, ServerBIMAP server) {
        
        CSocket = socket;
        serverBIMAP = server;
               
        ServerProperties properties = new ServerProperties();

        pathProject = properties.getProperty("PATH_PROJECT");
        sep = properties.getProperty("SEPARATOR");
        
        ipServer = properties.getProperty("IP_SERVER");
        
        try {
            crypto = new CryptoUtils();  
            privateKeyServerCompta = crypto.getPrivateKeyFromCertificate(pathProject + sep + "server" + sep + "serverSafe.jks", "server-compta", passwordSafe, passwordKeys);
            secretKeyServerCompta = crypto.getSecretKey(pathProject + sep + "server" + sep + "secretKeyServerCompta.ser");
            certificateServerCompta = crypto.getCertificate(pathProject + sep + "server" + sep + "server_compta.cer");
            
            addrServer = InetAddress.getByName(ipServer);
            
            oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
            oos.flush(); 
            ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
        } catch (ClassNotFoundException | IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException | NoSuchProviderException | NoSuchPaddingException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } 
        
        String ip = properties.getProperty("IP_SHOP");
        String port = properties.getProperty("PORT_SHOP");
        String login = properties.getProperty("LOGIN_SHOP");
        String password = properties.getProperty("PASSWORD_SHOP");
        
        beanOracle = new BeanDBAccessOracle(ip, port, login, password);

        try {
            beanOracle.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
    
    @Override
    public void run() {
        boolean terminated = false;

        executeHandshake();

        executeAuthentification();
        
        try {
            Message request;
            
            do
            {
                request = (Message) getObjectFromByte(receiveCypherMessage());
                
                switch(request.getType())
                {
                    case REQUEST_MAKE_BILL:
                        executeMakeBill(request);
                    break;
                    case REQUEST_GET_NEXT_BILL:
                        executeGetNextBill(request);
                    break;
                    case REQUEST_VALIDATE_BILL:
                        executeValidateBill(request);
                    break;
                    case REQUEST_LIST_BILLS:
                        executeListBills(request);
                    break;
                    case REQUEST_SEND_BILLS:
                        executeSendBills(request);
                    break;
                    case REQUEST_INTERRUPT:
                    default:
                        terminated = true;
                    break;
                }
            
            }while(!terminated);
            
            CSocket.close();
            
            System.out.println("BIMAP: Client déconnecté");
        } catch (IOException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
    
    public void executeHandshake()
    {
        /* Envoie de la clé publique */
        sendMessage(getByteFromObject(certificateServerCompta));
        
        X509Certificate certificateComptable = (X509Certificate) getObjectFromByte(receiveMessage());
        
        try {
            certificateComptable.checkValidity();
        } catch (CertificateExpiredException | CertificateNotYetValidException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        
        publicKeyComptable = (PublicKey) certificateComptable.getPublicKey();
        
        /* Message */
        byte[] request = receiveMessage();
        
        /* Clé Secrète */
        byte[] request2 = receiveMessage();
        
        try {
            secretKeyComptable = (SecretKey) getObjectFromByte(crypto.decryptASM(request2, privateKeyServerCompta));
            
            Message message = (Message) getObjectFromByte(crypto.decryptSYM(request, secretKeyComptable));
            
            String hello = (String) message.getParam("message");
            
            System.out.println(hello);
            
            secretKeyComptableHMAC = (SecretKey) message.getParam("secretKeyComptableHMAC");
            
            System.out.println("Handshake: " + hello);
            
            Message response = new Message();
            response.addParam("message", "hello_client");
            response.addParam("secretKeyServerCompta", secretKeyServerCompta);
            
            sendCypherMessage(getByteFromObject(response));
            
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void executeAuthentification()
    {
        Message request;
        Message response = new Message();
        boolean logged = false;
        
        do
        {
            try {
                request = (Message) getObjectFromByte(receiveCypherMessage());

                double alea = (double) request.getParam("alea");
                long temps = (long) request.getParam("temps");

                String login = (String) request.getParam("login");
                byte[] cipherPassword = (byte[]) request.getParam("cipherPassword");

                ResultSet rs = beanOracle.executeQuery("SELECT * FROM personnel WHERE login = '" + login + "'");

                if(rs.next())
                {
                    String password = (String) rs.getString("password");

                    byte[] cipherPassword2 = crypto.makeDigest(password, temps, alea);

                    if(!MessageDigest.isEqual(cipherPassword, cipherPassword2))
                        response.addParam("error", "Identifiants erronés");
                    else
                        logged = true;
                }
                else
                    response.addParam("error", "Identifiants inconnus");

            } catch (IOException | SQLException ex) {
                response.addParam("error", ex.getMessage());
            }
            
            sendCypherMessage(getByteFromObject(response));

        } while(!logged); 
    }
    
    public byte[] receiveMessage()
    {
        byte[] msg = null;
        
        try {
            msg = (byte[]) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public void sendMessage(byte[] msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public byte[] receiveCypherMessage()
    {
        byte[] msg = null;
        
        try {
            msg = crypto.decryptSYM(receiveMessage(), secretKeyComptable);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public void sendCypherMessage(byte[] msg)
    {
        try {
            sendMessage(crypto.encryptSYM(msg, secretKeyComptable));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Object getObjectFromByte(byte[] msg)
    {
        Object obj = null;
        
        try {
            obj = crypto.getObjectFromByte(msg);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return obj;
    }
    
    public byte[] getByteFromObject(Object obj)
    {
        byte[] msg = null;
        
        try {
            msg = crypto.getByteFromObject(obj);
        } catch (IOException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }

    private void executeMakeBill(Message request) 
    {
        Message response = new Message();
        byte[] hmac;
        response.setType(request.getType());
              
        try 
        {
            hmac = receiveCypherMessage();
            
            if(!crypto.verifyHMAC(hmac, getByteFromObject(request), secretKeyComptableHMAC))
            {
                response.addParam("error", "Un problème de sécurité est survenu");
            }
            else
            {
                float montant = (float) request.getParam("montant");
                int idFournisseur = (int) request.getParam("idFournisseur");
                
                beanOracle.execute("INSERT INTO factures_fournisseurs (date_facturation, montant, envoyee, payee, id_fournisseur) VALUES(current_date, " + montant + ", 0, 0, " + idFournisseur + ")");
            }
            
        } catch (SQLException | InvalidKeyException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        sendCypherMessage(getByteFromObject(response));
    }

    private void executeGetNextBill(Message request) 
    {
        Message response = new Message();
        response.setType(request.getType());
        
        try {
    
            ResultSet rs = beanOracle.executeQuery("SELECT * FROM FACTURES_FOURNISSEURS FF\n" +
                "INNER JOIN FOURNISSEURS F\n" +
                "ON FF.ID_FOURNISSEUR = F.ID_FOURNISSEUR\n" +
                "WHERE ENVOYEE = 0\n" +
                "AND DATE_FACTURATION = (\n" +
                "  SELECT MIN(DATE_FACTURATION) FROM FACTURES_FOURNISSEURS\n" +
                "  WHERE ENVOYEE = 0\n" +
                ")");
            
            rs.next();
            
            FactureFournisseur ff = new FactureFournisseur();
            
            Fournisseur f = new Fournisseur();
            
            ff.setIdFacture(rs.getInt("ID_FACTURE"));
            ff.setDateFacturation(rs.getDate("DATE_FACTURATION"));
            ff.setMontant(rs.getFloat("MONTANT"));
            ff.setEnvoyee(rs.getInt("ENVOYEE"));
            ff.setPayee(rs.getInt("PAYEE"));
            
            f.setIdFournisseur(rs.getInt("ID_FOURNISSEUR"));
            f.setNom(rs.getString("NOM"));
            
            ff.setFournisseur(f);
             
            response.addParam("factureFournisseur", crypto.encryptSYM(getByteFromObject(ff), secretKeyServerCompta));
              
        } catch (SQLException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
            response.addParam("error", ex.getMessage());
        }
   
        sendCypherMessage(getByteFromObject(response));
    }

    private void executeValidateBill(Message request) 
    {
        Message response = new Message();
        response.setType(request.getType());
        
        byte[] signature = receiveCypherMessage();
        
        try {
            boolean valid = crypto.verifySignature(getByteFromObject(request), signature, publicKeyComptable);
            
            if(valid == false)
            {
                System.out.println("VALIDATE_BILL: Erreur de vérification del asignature");
            }
            else 
            {
                System.out.println("VALIDATE_BILL: Signature valide");
                
                int idFacture = (int) request.getParam("idFacture"); 
                
                int ret = beanOracle.executeUpdate("UPDATE FACTURES_FOURNISSEURS SET PAYEE = 1 WHERE PAYEE = 0 AND ID_FACTURE = " + idFacture);       
                
                if(ret == 0)
                    response.addParam("ret", "La facture est déjà noté comme payée");
            }
        } catch (InvalidKeyException | SignatureException | SQLException ex) {
            Logger.getLogger(compta.bimap.RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        sendCypherMessage(getByteFromObject(response));
    }

    private void executeListBills(Message request) 
    {
        ArrayList<FactureFournisseur> data = new ArrayList();
        Message response = new Message();
        response.setType(request.getType());
        
        byte[] signature = receiveCypherMessage();
         
        try {;
            boolean valid = crypto.verifySignature(getByteFromObject(request), signature, publicKeyComptable);
            //boolean valid = crypto.verifySignature(getByteFromObject(request), signature, crypto.getPublicKeyFromCertificate(pathProject + sep + "client" + sep + "clientSafe.jks", "comptable", passwordSafe));

            if(valid == false)
            {
                System.out.println("LIST_BILLS: Erreur de vérification de la signature");
                response.addParam("error", "Erreur lors de la vérificaton de la signature");
            }
            else
            {
                System.out.println("LIST_BILLS: Signature valide");
                
                Date dateBegin = (Date) request.getParam("dateBegin");
                Date dateEnd = (Date) request.getParam("dateEnd");
                String idFournisseur = (String) request.getParam("idFournisseur");
                
                String whereClause = "";
                
                String sqlQuery = "SELECT * FROM FACTURES_FOURNISSEURS FF\n" +
                                        "INNER JOIN FOURNISSEURS F\n" +
                                        "ON FF.ID_FOURNISSEUR = F.ID_FOURNISSEUR";
                
                ResultSet rs = null;
                
                if(dateBegin != null && dateEnd != null)
                {
                    DateFormat df = new SimpleDateFormat("dd/MM/yy");
                    whereClause = "DATE_FACTURATION >= '" + df.format(dateBegin) + "' AND DATE_FACTURATION <= '" + df.format(dateEnd) + "'";
                }
                
                if(idFournisseur != null)
                {
                    if(!whereClause.equals(""))
                        whereClause = whereClause + " AND";
                        
                    whereClause = whereClause + " FF.ID_FOURNISSEUR = " + idFournisseur;
                }
                   
                if(!whereClause.equals(""))
                    sqlQuery = sqlQuery + " WHERE " + whereClause;
                
                rs = beanOracle.executeQuery(sqlQuery);
                
                while(rs.next())
                {
                    FactureFournisseur ff = new FactureFournisseur();

                    Fournisseur f = new Fournisseur();

                    ff.setIdFacture(rs.getInt("ID_FACTURE"));
                    ff.setDateFacturation(rs.getDate("DATE_FACTURATION"));
                    ff.setMontant(rs.getFloat("MONTANT"));
                    ff.setEnvoyee(rs.getInt("ENVOYEE"));
                    ff.setPayee(rs.getInt("PAYEE"));

                    f.setIdFournisseur(rs.getInt("ID_FOURNISSEUR"));
                    f.setNom(rs.getString("NOM"));

                    ff.setFournisseur(f);

                    data.add(ff);
                }
                
                response.addParam("data", data);
            }
        } catch (SQLException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        sendCypherMessage(getByteFromObject(response));

    }

    private void executeSendBills(Message request) 
    {
        Message response = new Message();
        response.setType(request.getType());
        
        byte[] signature = receiveCypherMessage();
        
        try {
            boolean valid = crypto.verifySignature(getByteFromObject(request), signature, publicKeyComptable);
            
            if(valid == false)
            {
                System.out.println("SEND_BILLS: Erreur de vérification de la signature");
                response.addParam("error", "Erreur lors de la vérificaton de la signature");
            }
            else
            {
                System.out.println("LIST_BILLS: signature valide");
                String billsToAvoid = (String) request.getParam("billsToAvoid");
                
                int ret;
                
                if(billsToAvoid.equals(""))
                    ret = beanOracle.executeUpdate("UPDATE FACTURES_FOURNISSEURS SET ENVOYEE = 1"); 
                else
                    ret = beanOracle.executeUpdate("UPDATE FACTURES_FOURNISSEURS SET ENVOYEE = 1 WHERE ID_FACTURE NOT IN(" + billsToAvoid + ")");       
                
                response.addParam("ret", ret);
            }
        } catch (InvalidKeyException | SignatureException | SQLException ex) {
            Logger.getLogger(RunnableBIMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
           
        sendCypherMessage(getByteFromObject(response));
    }
}

package server_mucop;


import com.floca.BeanDBAccess.BeanDBAccessOracle;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bastin
 */
public class RunnableMUCOP implements Runnable, protocoleMUCOP {
    
    private Socket CSocket = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private BeanDBAccessOracle beanOracle = null;
    private String portUDP = "6003";
    private String adreseUDP = "224.0.0.1";
    boolean terminated = false;
    boolean logged = false;

    public RunnableMUCOP(Socket s) {
        CSocket = s;
        
        try {
            System.out.println("Création du flux sortant du client...");
            dos = new DataOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
            dos.flush(); 
            System.out.println("Création du flux rentrant du client...");
            dis = new DataInputStream(new BufferedInputStream(CSocket.getInputStream()));
            System.out.println("Les flux du client établis");
        } catch (IOException ex) {
            Logger.getLogger(RunnableMUCOP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
      
        System.out.println("ok");

        System.out.print("Connexion du client à la BD... ");
        
       
        beanOracle = new BeanDBAccessOracle("127.0.0.1", "22223", "SHOP", "oracle");

        try {
            beanOracle.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RunnableMUCOP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        
        
        System.out.println("ok");

        System.out.println("Client opérationnel");
    }
    

    @Override
    public void run() {

        Message request = null;
        
        System.out.println("Lancement d'un thread client");

        System.out.println("Client connecté");
        
        String reponse = ReceiveMsg();  
        String[] parts = reponse.split("#");
        
        if(parts[0].equals("LOGIN_GROUP"))
        {
            executeLogin(parts);
        }
        else
        {
            SendMsg("ERR#Internal server error");
        }   
        
        System.out.println("Fermeture du thread client...");
        
        try {
            beanOracle.disconnect();
            CSocket.close();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(RunnableMUCOP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Thread client fermé");
    }
    private void executeLogin(String[] request)
    {    
        String query, message, password;
        Integer digest;
        query = "SELECT password FROM personnel WHERE login = '" + request[1] + "'";

        ResultSet rs = null;
        try {
            rs = beanOracle.executeQuery(query);
            if(!rs.next())
            {
                SendMsg("ERR#Nom d'utilisateur incorrecte ");
                return;
            }
            else
                password = rs.getString("password");
            
            digest = hashFunction(request[4] + password + request[3]);  
            if(request[2].equals(Integer.toString(digest)))
                SendMsg("ACK#"+adreseUDP+"#"+portUDP);
            else
                SendMsg("ERR#Mot de passe incorrecte");
            
            
            
        } catch (SQLException ex) {
            message = "Quelque chose est cassé en interne. Veuillez contacter l'administrateur";
            SendMsg("ERR#"+message);
            return;
        }
        
        
        
    
    }
    private int hashFunction(String message)
    {
        int hashValue = 0;
        
        for(int i = 0; i < message.length(); i++)
            hashValue += (int)message.charAt(i);
        
        return hashValue%67;
    }
    public String ReceiveMsg()
    {
        byte b;
        StringBuffer taille = new StringBuffer();
        StringBuffer message = new StringBuffer();
        
        try
        {
            while ((b = dis.readByte()) != (byte)'#')
            {                   
                if (b != (byte)'#')
                    taille.append((char)b);
            }
                
            for (int i = 0; i < Integer.parseInt(taille.toString()); i++)
            {
                b = dis.readByte();
                message.append((char)b);
            }  
        }
        catch(IOException e)
        {
            System.err.println("RunnableTraitement : Erreur de reception de msg (IO) : " + e);
        }
            
        return message.toString();
    }
    
    
   public void SendMsg(String msg)
    {
        String chargeUtile = msg;
        int taille = chargeUtile.length();
        StringBuffer message = new StringBuffer(String.valueOf(taille) + "#" + chargeUtile);
            
        try
        {               
            dos.write(message.toString().getBytes());
            dos.flush();
        }
        catch(IOException e)
        {
            System.err.println("RunnableTraitement : Erreur d'envoi de msg (IO) : " + e);
        }
    }
    

   
}

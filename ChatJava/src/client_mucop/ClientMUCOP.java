/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_mucop;
import client.Client;
import static java.awt.SystemColor.text;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;
import server_mucop.protocoleMUCOP;


/**
 *
 * @author bastin
 */
public class ClientMUCOP extends Client implements protocoleMUCOP {
    
    public ClientMUCOP(String ip, int p) throws IOException {
        super(ip, p);

    }
   
    
    public String login(String login, String password)
    {
        String message;
        String reponse;
        

        String sel1 = UUID.randomUUID().toString();
        String sel2 = Calendar.getInstance().getTime().toString();
        String pwd =  sel2+password+sel1;

        int saltedDigest = hashFunction(pwd);
        
        message = "LOGIN_GROUP#"+login+"#"+saltedDigest+"#"+sel1+"#"+sel2;
        sendMessage(message);

        reponse = receiveMessage();
        System.out.println(reponse);
        return reponse;
      
    }
    private int hashFunction(String message)
    {
        int hashValue = 0;
        
        for(int i = 0; i < message.length(); i++)
            hashValue += (int)message.charAt(i);
        
        return hashValue%67;
    }
    public String receiveMessage()
    {
        byte b;
        StringBuffer taille = new StringBuffer();
        StringBuffer message = new StringBuffer();
        
        try
        {
            while ((b = ois.readByte()) != (byte)'#')
            {                   
                if (b != (byte)'#')
                    taille.append((char)b);
            }
                
            for (int i = 0; i < Integer.parseInt(taille.toString()); i++)
            {
                b = ois.readByte();
                message.append((char)b);
            }  
        }
        catch(IOException e)
        {
            System.err.println("RunnableTraitement : Erreur de reception de msg (IO) : " + e);
        }
            
        return message.toString();
    }
    
    
   public void sendMessage(String msg)
    {
        String chargeUtile = msg;
        int taille = chargeUtile.length();
        StringBuffer message = new StringBuffer(String.valueOf(taille) + "#" + chargeUtile);
            
        try
        {               
            oos.write(message.toString().getBytes());
            oos.flush();
        }
        catch(IOException e)
        {
            System.err.println("RunnableTraitement : Erreur d'envoi de msg (IO) : " + e);
        }
    }
    
    public static void main(String[] args) {
        try {
            new ClientMUCOP("127.0.0.1", 6002);
        } catch (IOException ex) {
            Logger.getLogger(ClientMUCOP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
}

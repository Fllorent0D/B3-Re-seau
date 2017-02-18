/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_indep;

import server_indep.protocoleINDEP;
import client.Client;
import static java.awt.SystemColor.text;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;
import dismap.protocoleDISMAP;
import java.net.InetAddress;

/**
 *
 * @author bastin
 */
public class ClientINDEP extends Client implements protocoleINDEP {
    
    public ClientINDEP(InetAddress addr, int p) throws IOException {
        super(addr, p);
        
        //Demo(); 
    }
    public Message Descr_cont(Integer NbrElem){
        Message request = new Message();
        Message response = new Message();
        
        request.setType(GET_STAT_DESCR_CONT);
        request.addParam("nbrelem", NbrElem);
        
        
        sendMessage(request);
        response = receiveMessage();

        return response;
        
    }
    public Message Infer_test_conf(Integer nbrElem, Integer montant)
    {
        Message request = new Message();
        Message response = new Message();
        
        request.setType(GET_STAT_INFER_TEST_CONF);
        request.addParam("nbrElem", nbrElem);  
        request.addParam("montant", montant);

     
        sendMessage(request);
        response = receiveMessage();

        return response; 
    }
    public Message Infer_test_anova(Integer nbrElem)
    {
        Message request = new Message();
        Message response = new Message();
        
        request.setType(GET_STAT_INFER_TEST_ANOVA);
        request.addParam("nbrElem", nbrElem);  
     
        sendMessage(request);
        response = receiveMessage();

        return response; 
    }
    public Message Ventes_rep(Integer annee, Integer mois)
    {
        Message request = new Message();
        Message response = new Message();
        
        request.setType(GET_GR_VENTES_REP);
        request.addParam("annee", annee);  
        request.addParam("mois", mois);

        
        sendMessage(request);
        response = receiveMessage();

        return response;
    }
    public Message Ventes_comp(Integer annee, Integer nbr)
    {
        Message request = new Message();
        Message response = new Message();
        
        request.setType(GET_GR_VENTES_COMP);
        request.addParam("annee", annee);  
        request.addParam("nbr", nbr);  

        
        sendMessage(request);
        response = receiveMessage();

        return response;
    }
    
    public Message login(String login, String password)
    {
       
        Message request = new Message();
        Message response = new Message();

        request.setType(REQUEST_LOGIN);
        request.addParam("login", login);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(login.getBytes());
            md.update(password.getBytes());
            long temps = (new Date()).getTime();

            double alea = Math.random();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            DataOutputStream bdos = new DataOutputStream(baos); 

            bdos.writeLong(temps); bdos.writeDouble(alea);
            md.update(baos.toByteArray()); 
            byte[] msgD = md.digest();
            request.addParam("password", msgD);
            request.addParam("time", temps);
            request.addParam("random", alea);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClientINDEP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ClientINDEP.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        request.addParam("login", login);


        sendMessage(request);
        response = receiveMessage();

        return response;
      
    }
    
    public Message receiveMessage()
    {
        Message msg = null;
        
        try {
            msg = (Message) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(ClientINDEP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientINDEP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public void sendMessage(Message msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientINDEP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            new ClientINDEP(addr, 6001);
        } catch (IOException ex) {
            Logger.getLogger(ClientINDEP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_dismap;

import client.Client;
import client_payp.ClientPAYP;
import compta.payp.ProtocolePAYP;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;
import dismap.RunnableDISMAP;
import models.Reservation;
import models.TypePrecis;
import models.Ville;
import dismap.protocoleDISMAP;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import javax.crypto.NoSuchPaddingException;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ClientDISMAP extends Client implements protocoleDISMAP, ProtocolePAYP {
    
    protected models.Client client = null;
    
    public ClientDISMAP() throws UnknownHostException {
        super();
        
        ServerProperties properties = new ServerProperties();
        
        String ipServer = properties.getProperty("IP_SERVER");
        int portDISMAP = Integer.valueOf(properties.getProperty("PORT_DISMAP"));
        
        InetAddress addrDISMAP = InetAddress.getByName(ipServer);
        
        setIp(addrDISMAP);
        setPort(portDISMAP);
        
        System.out.println("Configuration client DISMAP");
        System.out.println("---------------------------");
        System.out.println("IP: " + ipServer);
        System.out.println("PORT: " + portDISMAP);
        System.out.println("");
    }

    public void interrupt()
    {
        Message request = new Message();
        
        request.setType(protocoleDISMAP.REQUEST_INTERRUPT);
        
        sendMessage(request);
    }
    
    public Message authentification(String login, String password, boolean isLogin)
    {
        Message request = new Message();
        Message response = new Message();
        
        if(isLogin)
            request.setType(protocoleDISMAP.REQUEST_LOGIN);
        else
            request.setType(protocoleDISMAP.REQUEST_LOGOUT);
          
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(login.getBytes());
            md.update(password.getBytes());
            long temps = (new Date()).getTime();

            double alea = Math.random();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            DataOutputStream bdos = new DataOutputStream(baos); 

            bdos.writeLong(temps); 
            bdos.writeDouble(alea);
            md.update(baos.toByteArray()); 
            byte[] msgD = md.digest();

            request.addParam("time", temps);
            request.addParam("random", alea);
            request.addParam("password", msgD);

        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(ClientDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }

        request.addParam("login", login);
              
        sendMessage(request);
        response = receiveMessage(); 
        
        return response;
    }
    
    public int sumBasket(int idClient)
    {
        Message request = new Message();
        
        request.setType(REQUEST_SUM_BASKET);
        
        request.addParam("idClient", idClient);
        
        sendMessage(request);
        
        Message response = receiveMessage();
        
        return (int) response.getParam("sum_basket");
    }
    
    public ArrayList<TypePrecis> listTypePrecis()
    {
        Message request = new Message();
        
        request.setType(REQUEST_LIST_TYPES_PRECIS);
        
        sendMessage(request);
        
        Message response = receiveMessage();
        
        return (ArrayList<TypePrecis>) response.getParam("data");
    }
    
    public ArrayList<models.Client> listClients()
    {
        Message request = new Message();
        
        request.setType(protocoleDISMAP.REQUEST_LIST_CLIENTS);
        
        sendMessage(request);
        
        Message response = receiveMessage();
        
        return (ArrayList<models.Client>) response.getParam("data");
    }
    
    public ArrayList<Reservation> listItemsBasket()
    {
        Message response = null;
        
        Message request = new Message();
        request.setType(REQUEST_LIST_ITEMS_BASKET);
        request.addParam("client", this.client);

        sendMessage(request);
        
        response = receiveMessage();
       
        return (ArrayList<Reservation>) response.getParam("data");
    }
    
    public ArrayList<Ville> listVilles()
    {
        Message response = null;
        
        Message request = new Message();
        request.setType(REQUEST_LIST_VILLES);

        sendMessage(request);
        
        response = receiveMessage();
       
        return (ArrayList<Ville>) response.getParam("data");
    }
    
    public Message searchGoods(int idTypePrecis)
    {
        Message request = new Message();
      
        request.setType(REQUEST_SEARCH_GOODS);
 
        if(idTypePrecis > 0)
            request.addParam("idTypePrecis", idTypePrecis);
        
        sendMessage(request);
        
        return receiveMessage();
    }
    
    public Message takeGoods(int idTypeAppareil, int quantity)
    {
        Message request = new Message();
            
        request.setType(REQUEST_TAKE_GOODS);
        request.addParam("idClient", client.getId());
        request.addParam("idTypeAppareil", idTypeAppareil);    
        request.addParam("quantity", quantity);
                
        sendMessage(request);
        
        return receiveMessage();
    }
    
    /* Méthode de merde. Si une erreur survient, on est dans la merde niveau cohérence de la BD */
    public Message executeOrder(String modePayment, String address, int city)
    {
        Message response2 = null;
        Message request = new Message(); 
        request.setType(REQUEST_BUY_GOODS);
        request.addParam("idClient", client.getId());
        request.addParam("modePaiement", modePayment);

        sendMessage(request);
        Message response = receiveMessage();
                
        if(response.hasParam("error"))
        {
            return response;
        }
        else
        {            
            if(modePayment.equals("BANCONTACT"))
            {
                int amount = (int) response.getParam("amount");
            
                Message request2 = new Message();
                request2.setType(PAY_BY_CARD);
                request2.addParam("cardNumber", 45646);
                request2.addParam("ownerName", client.getId());
                request2.addParam("amount", amount);
                  
                try {
                    ClientPAYP clientPAYP = new ClientPAYP();
                    clientPAYP.connect();

                    clientPAYP.sendMessage(request2);
                   
                    response2 = (Message) clientPAYP.receiveMessage();
                   
                } catch (IOException | ClassNotFoundException | NoSuchProviderException | NoSuchAlgorithmException | NoSuchPaddingException | KeyStoreException | CertificateException | UnrecoverableKeyException ex) {
                    Logger.getLogger(ClientDISMAP.class.getName()).log(Level.SEVERE, null, ex);
                    response2.addParam("error", ex.getMessage());
                }
                
                if(response2.hasParam("error") || response2.hasParam("refusal"))
                    return response2;
            }
            
            if(address != null && city != 0)
            {
                Message request3 = new Message();
            
                System.out.println(String.valueOf(response.getParam("idFacture")));

                request3.setType(REQUEST_DELIVERY_GOODS);
                request3.addParam("idFacture", response.getParam("idFacture"));
                request3.addParam("address", address);
                request3.addParam("city", city);

                sendMessage(request3);

                return receiveMessage();
            }
            else
            {
                return response;
            }
        }
    }
    
    public void setClient(models.Client client)
    {
        this.client = client;
    }
    
    public models.Client getClient()
    {
        return client;
    }
}

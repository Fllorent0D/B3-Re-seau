/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_dismap;

import client.Client;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;
import dismap.RunnableDISMAP;
import dismap.models.Reservation;
import dismap.models.TypeAppareil;
import dismap.models.TypePrecis;
import dismap.models.Ville;
import dismap.protocoleDISMAP;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class ClientDISMAP extends Client implements protocoleDISMAP {
    
    protected dismap.models.Client client = null;
    
    public ClientDISMAP() throws IOException {
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
    
    public Message receiveMessage()
    {
        Message msg = null;
        
        try {
            msg = (Message) ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public void sendMessage(Message msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public ArrayList<dismap.models.Client> listClients()
    {
        Message request = new Message();
        
        request.setType(protocoleDISMAP.REQUEST_LIST_CLIENTS);
        
        sendMessage(request);
        
        Message response = receiveMessage();
        
        return (ArrayList<dismap.models.Client>) response.getParam("data");
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
    
    public Message executeOrder(String modePayment, String address, int city)
    {
        Message request = new Message();
        
        request.setType(REQUEST_BUY_GOODS);
        request.addParam("idClient", client.getId());
        request.addParam("modePaiement", modePayment);

        sendMessage(request);
        
        Message response1 = receiveMessage();
        
        if(response1.hasParam("error"))
        {
            return response1;
        }
        else
        {
            if(address != null && city != 0)
            {
                Message request2 = new Message();
            
                System.out.println(String.valueOf(response1.getParam("idFacture")));

                request2.setType(REQUEST_DELIVERY_GOODS);
                request2.addParam("idFacture", response1.getParam("idFacture"));
                request2.addParam("address", address);
                request2.addParam("city", city);

                sendMessage(request2);

                return receiveMessage();
            }
            else
            {
                return response1;
            }
        }
    }
    
    public void setClient(dismap.models.Client client)
    {
        this.client = client;
    }
    
    public dismap.models.Client getClient()
    {
        return client;
    }
}

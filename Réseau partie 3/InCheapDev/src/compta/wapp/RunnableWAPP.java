/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compta.wapp;

import com.floca.BeanDBAccess.BeanDBAccessOracle;
import compta.bimap.RunnableBIMAP;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import models.Personnel;
import models.Salaire;
import payp2.RunnablePAYP2;
import server.Message;
import server.RunnableClient;
import server.ServerProperties;

/**
 *
 * @author bastin
 */
public class RunnableWAPP extends RunnableClient implements Runnable, ProtocoleWAPP {

    private ServerWAPP serverWAPP;
    private InetAddress addrServer;
    private BeanDBAccessOracle beanOracle;
    
    public RunnableWAPP(SSLSocket socket, ServerWAPP server)
    {        
        CSocket = socket;
        serverWAPP = server;
        
        ServerProperties properties = new ServerProperties();
        
        ipServer = properties.getProperty("IP_SERVER");
        
        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(RunnableWAPP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
            oos.flush(); 
            ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(RunnableWAPP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
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
        
        Message request = null;
        
        do
        {   
            request = (Message) receiveMessage();
            
            if(request == null)
                continue; 
            
            switch(request.getType())
            {
                case REQUEST_PAY_WAGE:
                    executePayWage(request);
                    break;
                case REQUEST_PAY_WAGES:
                    executePayWages(request);
                    break;
                case REQUEST_LIST_PAYED_WAGES:
                    executeListPayedWages(request);
                    break;
                case REQUEST_INTERRUPT:
                default:
                    terminated = true;
                    break;
            }

        } while(!terminated);
         
        try {
            CSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RunnablePAYP2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void executePayWage(Message request)
    {
        Message response = new Message();
        
        String name = (String) request.getParam("name");
        
        ArrayList<Salaire> data = new ArrayList();
        
        try {
            ResultSet rs = beanOracle.executeQuery("SELECT COUNT(*) AS COUNT FROM PERSONNEL WHERE NOM = '" + name + "'");
            
            rs.next();
            
            if(rs.getInt("COUNT") == 0)
            {
                response.addParam("error", "Aucun membre du nom '" + name + "' n'existe");
            }
            else
            {
                rs = beanOracle.executeQuery("SELECT * FROM SALAIRES S INNER JOIN PERSONNEL P ON S.ID_PERSONNEL = P.ID_PERSONNEL WHERE PAYED = 0 AND P.NOM = '" + name + "'");
                          
                while(rs.next())
                {
                    beanOracle.executeUpdate("UPDATE SALAIRES SET payed = 1 WHERE ID_PERSONNEL = " + rs.getInt("ID_PERSONNEL"));

                    Salaire s = new Salaire();

                    s.setIdSalaire(rs.getInt("ID_SALAIRE"));
                    s.setMontant(rs.getFloat("MONTANT"));

                    Personnel p = new Personnel();

                    p.setIdPersonnel(rs.getInt("ID_PERSONNEL"));
                    p.setNom(rs.getString("NOM"));
                    p.setPrenom(rs.getString("PRENOM"));
                    s.setPersonnel(p);

                    data.add(s);
                }

                response.addParam("data", data);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RunnableWAPP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
    }
    
    public void executePayWages(Message request)
    {
        Message response = new Message();
        
        ArrayList<Salaire> data = new ArrayList();
        
        try {
            ResultSet rs = beanOracle.executeQuery("SELECT * FROM SALAIRES S INNER JOIN PERSONNEL P ON S.ID_PERSONNEL = P.ID_PERSONNEL WHERE PAYED = 0 ");
            beanOracle.executeUpdate("UPDATE SALAIRES SET payed = 1");
            
            while(rs.next())
            {
                Salaire s = new Salaire();
                
                s.setIdSalaire(rs.getInt("ID_SALAIRE"));
                s.setMontant(rs.getFloat("MONTANT"));
                
                Personnel p = new Personnel();
                
                p.setIdPersonnel(rs.getInt("ID_PERSONNEL"));
                p.setNom(rs.getString("NOM"));
                p.setPrenom(rs.getString("PRENOM"));
                s.setPersonnel(p);
                
                data.add(s);
            }
            
            response.addParam("data", data);
           
        } catch (SQLException ex) {
            Logger.getLogger(RunnableWAPP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
    }
    
    public void executeListPayedWages(Message request)
    {
        Message response = new Message();
        
        int month = (int) request.getParam("month");
        
        String query = "SELECT * FROM SALAIRES\n" +
                "INNER JOIN PERSONNEL\n" +
                "ON SALAIRES.ID_PERSONNEL = PERSONNEL.ID_PERSONNEL\n" +
                "WHERE PAYED = 1\n";
        
        if(month != 0)
            query = query + " AND TO_CHAR(date_salaire, 'mm') = " + month;
        
        try { 
            ResultSet rs = beanOracle.executeQuery(query);
            ArrayList<Salaire> salaries = new ArrayList();
            
            while(rs.next())
            {
                Salaire s = new Salaire();
                s.setIdSalaire(rs.getInt("id_salaire"));
                s.setMontant(rs.getInt("montant"));
                s.setDateSalaire(rs.getDate("date_salaire"));
                s.setPayed(rs.getInt("payed"));
                
                Personnel p = new Personnel();
                p.setIdPersonnel(rs.getInt("id_personnel"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                s.setPersonnel(p);
                
                salaries.add(s);
            }
            
            response.addParam("salaries", salaries);
            
        } catch (SQLException ex) {
            Logger.getLogger(RunnableWAPP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
    }
}

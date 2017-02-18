package dismap;

import models.Client;
import com.floca.BeanDBAccess.BeanDBAccessOracle;
import models.Appareil;
import models.Personnel;
import models.Reservation;
import models.Stat;
import models.TypeAppareil;
import models.TypePrecis;
import models.Ville;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Message;
import server.ServerProperties;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bastin
 */
public class RunnableDISMAP implements Runnable, protocoleDISMAP {
    
    private Socket CSocket;
    private ServerDISMAP serverDISMAP;
    private InetAddress addrServer;
    private String ipServer;
    private ObjectInputStream ois;
    private ObjectOutputStream oos ;
    private BeanDBAccessOracle beanOracle;
    
    public RunnableDISMAP(Socket socket, ServerDISMAP server) {
        
        CSocket = socket;
        serverDISMAP = server;
        
        serverDISMAP.addClient(socket.getInetAddress().toString());
        
        ServerProperties properties = new ServerProperties();
        
        ipServer = properties.getProperty("IP_SERVER");
        
        try {
            addrServer = InetAddress.getByName(ipServer);
        } catch (UnknownHostException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
            oos.flush(); 
            ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    boolean terminated = false;
    boolean logged = false;
    
    @Override
    public void run() {

        Message request = null;
        
        new ClientUrgence(addrServer, CSocket, serverDISMAP).start();
        
        do
        {
            request = receiveMessage();
            System.out.println("Reception d'un message");

            System.out.println(request.getType());
            
            if(request.getType() != REQUEST_LOGIN)
            {
                if(request.getType() == REQUEST_INTERRUPT)
                    return;
                else
                    continue;
            }
            
            logged = executeAuthentification(request, true);
            
        }while(!logged);
        
        
        do
        {   
            request = receiveMessage();
            
            if(request == null)
                break;
             
            switch(request.getType())
            {
                case REQUEST_STATS_SOLD_APPAREILS:
                    executeStatsSoldAppareils(request);
                    break;
                    
                case REQUEST_STATS_TURNOVER:
                    executeStatsTurnover(request);
                    break;
                    
                case REQUEST_SEARCH_GOODS:
                    executeSearchGoods(request);
                    break;
                    
                case REQUEST_TAKE_GOODS:
                    executeTakeGoods(request);
                    break;
                    
                case REQUEST_BUY_GOODS:
                    executeBuyGoods(request);
                    break;
                    
                case REQUEST_DELIVERY_GOODS:
                    executeDeliveryGoods(request);
                    break;
                    
                case REQUEST_LIST_CLIENTS:
                    executeListClients(request);
                    break;
                    
                case REQUEST_LIST_TYPES_PRECIS:
                    executeListTypesPrecis(request);
                    break;
                    
                case REQUEST_LIST_ITEMS_BASKET:
                    executeListItemsBasket(request);
                    break;
                    
                case REQUEST_SUM_BASKET:
                    executeGetSumBasket(request);
                    break;
                    
                case REQUEST_LIST_VILLES:
                    executeListVilles(request);
                    break;
                    
               /* case REQUEST_SALE_INFO:
                    executeSaleInfo(request);
                    break;*/
                    
                case REQUEST_LOGOUT:
                    terminated = executeAuthentification(request, false);
                    break;
                case REQUEST_INTERRUPT:
                default:
                    terminated = true;
                    break;
            }

        } while(!terminated);
        
        serverDISMAP.removeClient(CSocket.getInetAddress().toString());
         
        try {
            CSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Message receiveMessage()
    {
        Message msg = null;
        
        try {
            msg = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            return null;
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
    
    private boolean executeAuthentification(Message request, boolean isLogin)
    {    
        Message response = new Message();
        response.setType(REQUEST_LOGIN);
        String query, login;
        byte[] hashedPass;
        double alea;
        long temps;
        String passDb;
        boolean valid = false;
        
        login = (String) request.getParam("login");
        hashedPass = (byte[])request.getParam("password");
        alea = (double)request.getParam("random");
        temps = (long)request.getParam("time");
 
        
        query = "SELECT * FROM personnel WHERE login = '" + login + "'";
       
        try {
            ResultSet rs = beanOracle.executeQuery(query);
            if (!rs.next() ) {
                response.addParam("error", "Votre login n'est pas connu");
            } 
            else 
            {
                passDb = rs.getString("password");
                MessageDigest md = MessageDigest.getInstance("SHA-256"); 
                md.update(login.getBytes());
                md.update(passDb.getBytes());
                ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
                DataOutputStream bdos = new DataOutputStream(baos); 
                bdos.writeLong(temps); 
                bdos.writeDouble(alea);
                md.update(baos.toByteArray());
                byte[] passToTest = md.digest();

                if(MessageDigest.isEqual(hashedPass, passToTest))
                {
                    if(isLogin)
                    {
                        Personnel p = new Personnel();
                        p.setIdPersonnel(rs.getInt("ID_PERSONNEL"));
                        p.setNom(rs.getString("NOM"));
                        p.setPrenom(rs.getString("PRENOM"));
                        p.setLogin(rs.getString("LOGIN"));

                        response.addParam("seller", p);
                    }
  
                    valid = true;
                }
                else
                {
                    response.addParam("error", "Login ou password invalide");
                }
            }
        } catch (SQLException | IOException | NoSuchAlgorithmException ex) {   
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response); 
        
        return valid;
    }
        
    private void executeSearchGoods(Message request)
    {
        Message response = new Message();
        response.setType(request.getType());
        
        try {
            ResultSet rs;
            
            if(request.hasParam("idTypePrecis"))
            {
                int id = (int) request.getParam("idTypePrecis");
                rs = beanOracle.executeQuery("SELECT * FROM type_appareils WHERE type_precis = " + String.valueOf(id));
            }
            else
                rs = beanOracle.executeQuery("SELECT * FROM type_appareils");

            
            ArrayList<TypeAppareil> data = new ArrayList();
            
            while(rs.next())
            {
                ResultSet rsCount = beanOracle.executeQuery("SELECT count(*) as AVAILABLE \n" +
                    "FROM APPAREILS ap\n" +
                    "INNER JOIN ETATS_SITUATION et\n" +
                    "ON ap.etat_situation = et.id_situation\n" +
                    "where et.description IN ('WA', 'SA')\n" +
                    "AND ap.type_appareil = " + rs.getString("ID_TYPE_APPAREIL"));
                
                rsCount.next(); 
                
                TypeAppareil ta = new TypeAppareil();
                ta.setIdTypeAppareil(rs.getInt("ID_TYPE_APPAREIL"));
                ta.setLibelle(rs.getString("LIBELLE"));
                ta.setPrixVenteBase(rs.getInt("PRIX_VENTE_BASE"));
                ta.setMarque(rs.getString("MARQUE"));
                ta.setAvailable(rsCount.getInt("AVAILABLE"));
                
                data.add(ta);
            }
            
            response.addParam("data", data);
            
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
    }
    
    private void executeTakeGoods(Message request)
    {
        Message response = new Message();
        response.setType(request.getType());
        
        int idClient = (int) request.getParam("idClient");
        int idTypeAppareil = (int) request.getParam("idTypeAppareil");
        int quantity = (int) request.getParam("quantity");
         
        ResultSet rs;
        
        try {
            beanOracle.execute("CALL RESERVATION(" + String.valueOf(idClient) + ", " + String.valueOf(idTypeAppareil) + ", " + String.valueOf(quantity) + ")");
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        } 
        
        sendMessage(response);
        
    }
    
    private void executeGetSumBasket(Message request) {
        
        Message response = new Message();
        response.setType(request.getType());
        
        int idClient = (int) request.getParam("idClient");
        
        try {
            ResultSet rs = beanOracle.executeQuery("SELECT SUM(PRIX_VENTE_EFFECTIF) AS SUM_BASKET\n" +
                    "FROM RESERVATIONS R\n" +
                    "INNER JOIN APPAREILS A\n" +
                    "ON R.NUMERO_SERIE = A.NUMERO_SERIE\n" +
                    "WHERE R.ID_CLIENT = " + idClient);
            
            rs.next();
            
            response.addParam("sum_basket", rs.getInt("SUM_BASKET"));
            
        } catch (SQLException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sendMessage(response);
    }
    
    private void executeBuyGoods(Message request) {
        Message response = new Message();
        response.setType(request.getType());
        
        int idClient = (int) request.getParam("idClient");
        String modePaiement = (String) request.getParam("modePaiement");
        
        System.out.println(idClient);
        System.out.println(modePaiement);
        
        try {
            beanOracle.executeUpdate("INSERT INTO FACTURES(ID_CLIENT, DATE_FACTURATION, MODE_PAIEMENT, TYPE_ACHAT) VALUES(" + String.valueOf(idClient) + ", SYSDATE, '" + modePaiement + "', 'SA')");
                
            ResultSet rs = beanOracle.executeQuery("SELECT ID_FACTURE\n" +
                    "FROM FACTURES\n" +
                    "WHERE ID_CLIENT = " + idClient + "\n" +
                    "AND DATE_FACTURATION = (\n" +
                    "  SELECT MAX(DATE_FACTURATION)\n" +
                    "  FROM FACTURES\n" +
                    "  WHERE ID_CLIENT = " + idClient +
                    ")");
            
            rs.next();
            
            int idFacture = rs.getInt("ID_FACTURE");
            
            System.out.println(idFacture);
            
            response.addParam("idFacture", idFacture);

            beanOracle.executeUpdate("INSERT INTO ITEMS_FACTURE(ID_FACTURE, NUMERO_SERIE, PRIX)\n" +
                "  SELECT F.ID_FACTURE, R.NUMERO_SERIE, A.PRIX_VENTE_EFFECTIF\n" +
                "  FROM RESERVATIONS R\n" +
                "  INNER JOIN FACTURES F\n" +
                "  ON R.ID_CLIENT = F.ID_CLIENT\n" +
                "  INNER JOIN APPAREILS A\n" +
                "  ON R.NUMERO_SERIE = A.NUMERO_SERIE\n" +
                "  WHERE R.ID_CLIENT = " + idClient + "\n" +
                "  AND ID_FACTURE = " + idFacture);
            
            beanOracle.executeUpdate("DELETE\n" +
                "FROM RESERVATIONS\n" +
                "WHERE ID_CLIENT = " + idClient);
            
            rs = beanOracle.executeQuery("select sum(prix) AS amount from factures f\n" +
                "inner join items_facture if\n" +
                "on f.id_facture = if.id_facture\n" +
                "where f.id_facture = " + idFacture);
            
            rs.next();
            
            int amount = rs.getInt("amount");
            
            response.addParam("amount", amount);
            
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        }
           
        sendMessage(response);    
    }
    
    private void executeDeliveryGoods(Message request) {
        Message response = new Message();
        response.setType(request.getType());
        
        String address = (String) request.getParam("address");
        int city = (int) request.getParam("city");
        int idFacture = (int) request.getParam("idFacture");
        
        try {
            beanOracle.executeUpdate("UPDATE FACTURES SET ADRESSE_LIVRAISON = '" + address + "', ID_VILLE = " + city + " WHERE ID_FACTURE = " + idFacture);
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        }
            
        sendMessage(response);
    }    
    
    public void executeListClients(Message request)
    {
        Message response = new Message();
        response.setType(response.getType());
        
        try {  
            ResultSet rs = beanOracle.executeQuery("SELECT * from clients");
            ArrayList<Client> data = new ArrayList();
        
            while(rs.next())
            {
                Client client = new Client();
                client.setId(rs.getInt("ID_CLIENT"));
                client.setNom(rs.getString("NOM"));
                client.setPrenom(rs.getString("PRENOM"));
                client.setLogin(rs.getString("LOGIN"));

                data.add(client);
            }
            
            response.addParam("data", data);
            
        } catch (SQLException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sendMessage(response);
    }
    
    private void executeListTypesPrecis(Message request)
    {
        Message response = new Message();
        response.setType(request.getType());

        try {
            ResultSet rs = beanOracle.executeQuery("SELECT * FROM TYPE_PRECIS");
            ArrayList<TypePrecis> data = new ArrayList();
        
            while(rs.next())
            {
                TypePrecis tp = new TypePrecis();
                tp.setIdTypePrecis(rs.getInt("ID_TYPE_PRECIS"));
                tp.setType(rs.getString("TYPE"));
                
                data.add(tp);
            }
            
            response.addParam("data", data);
            
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
    }
    
   public void executeListVilles(Message request) {
       Message response = new Message();
       response.setType(request.getType());
       
       System.out.println("execute");
       
        try {
            ResultSet rs = beanOracle.executeQuery("SELECT * FROM villes");
            
            ArrayList<Ville> data = new ArrayList();
        
            while(rs.next())
            {
                Ville ville = new Ville();
                ville.setIdVille(rs.getInt("ID_VILLE"));
                ville.setNom(rs.getString("NOM"));
                
                data.add(ville);
            }
            
            System.out.println(data.size());
            
            response.addParam("data", data);
            
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
   }
    
    public void executeListItemsBasket(Message request)
    {
        Message response = new Message();
        response.setType(request.getType());
        
        models.Client client = (models.Client) request.getParam("client");
        
        String idClient = String.valueOf(client.getId());
        
        try {
            ResultSet rs = beanOracle.executeQuery("SELECT LIBELLE, MARQUE, PRIX_VENTE_BASE, COUNT(*) AS QUANTITY\n" +
                                                        "FROM RESERVATIONS R\n" +
                                                        "INNER JOIN APPAREILS A\n" +
                                                        "ON R.NUMERO_SERIE = A.NUMERO_SERIE\n" +
                                                        "INNER JOIN TYPE_APPAREILS TA\n" +
                                                        "ON A.TYPE_APPAREIL = TA.ID_TYPE_APPAREIL\n" +
                                                        "WHERE R.ID_CLIENT = " + idClient + "\n" +
                                                        "GROUP BY LIBELLE, MARQUE, PRIX_VENTE_BASE");
            
            ArrayList<Reservation> data = new ArrayList();
            
            while(rs.next())
            {
                Reservation re = new Reservation();
                Appareil ap = new Appareil();
                TypeAppareil ta = new TypeAppareil();
                
                ta.setLibelle(rs.getString("LIBELLE"));
                ta.setMarque(rs.getString("MARQUE"));
                ta.setPrixVenteBase(Float.valueOf(rs.getInt("PRIX_VENTE_BASE")));
                re.setQuantity(rs.getInt("QUANTITY"));
                
                ap.setTypeAppareil(ta);
                re.setAppareil(ap);
                re.setClient(client);
                re.setDateReservation(new Date());
                
                data.add(re);
            }
            
            response.addParam("data", data);
            
        } catch (SQLException ex) {
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sendMessage(response);
    }
    
    private void executeStatsSoldAppareils(Message request) {
        
        Message response = new Message();
        response.setType(request.getType());
        
        ArrayList<Stat> data = new ArrayList();
        
        try {
            ResultSet rs = beanOracle.executeQuery("SELECT DISTINCT TYPE AS KEY, COUNT(*) AS VALUE\n" +
                    "FROM ITEMS_FACTURE IF\n" +
                    "INNER JOIN APPAREILS AP\n" +
                    "ON IF.NUMERO_SERIE = AP.NUMERO_SERIE\n" +
                    "INNER JOIN TYPE_APPAREILS TA\n" +
                    "ON AP.TYPE_APPAREIL = TA.ID_TYPE_APPAREIL\n" +
                    "INNER JOIN TYPE_PRECIS TP\n" +
                    "ON TA.TYPE_PRECIS = TP.ID_TYPE_PRECIS\n" +
                    "GROUP BY TYPE");
            
            while(rs.next()) {
                Stat stat = new Stat();
                stat.setKey(rs.getString("KEY"));
                stat.setValue(rs.getInt("VALUE"));
                
                data.add(stat);
            }
            
            response.addParam("data", data);
            
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
    }
    
    public void executeStatsTurnover(Message request) {
        Message response = new Message();
        response.setType(request.getType());
        
        ArrayList<Stat> data = new ArrayList();
        
        sendMessage(response);
        
        try {
            ResultSet rs = beanOracle.executeQuery("select to_char(fac.date_facturation  - 7/24,'IW') AS KEY, sum(total) AS VALUE\n" +
                "from factures fac \n" +
                "INNER join (SELECT id_facture, sum(prix) as total\n" +
                "            from items_facture it\n" +
                "            group by it.id_facture) tot\n" +
                "on tot.id_facture = fac.id_facture\n" +
                "where fac.DATE_FACTURATION > sysdate - INTERVAL '2' MONTH\n" +
                "GROUP BY to_char(fac.date_facturation - 7/24,'IW')\n" +
                "order by week;");
            
            while(rs.next()) {
                Stat stat = new Stat();
                stat.setKey(rs.getString("KEY"));
                stat.setValue(rs.getInt("VALUE"));
                
                data.add(stat);
            }
            
            response.addParam("data", data);
            
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
    }
}

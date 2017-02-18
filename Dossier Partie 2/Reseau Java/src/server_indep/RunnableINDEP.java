package server_indep;

import com.floca.BeanDBAccess.BeanDBAccessOracle;
import static dismap.protocoleDISMAP.REQUEST_INTERRUPT;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import server.Message;
import dismap.RunnableDISMAP;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bastin
 */
public class RunnableINDEP implements Runnable, protocoleINDEP {
    
    private Socket CSocket = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private BeanDBAccessOracle beanOracle = null;
    boolean terminated = false;
    boolean logged = false;
    private RConnection connexionR;
    
    public RunnableINDEP(Socket s) {
        CSocket = s;
        
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(CSocket.getOutputStream()));
            oos.flush(); 
            ois = new ObjectInputStream(new BufferedInputStream(CSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(RunnableINDEP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        /*System.out.print("Connexion du client à Rserve... ");
        try {
            connexionR  = new RConnection(); //Mettre des paramètres si le serveur R ne se trouve pas en localhost
        } catch (RserveException ex) {
            Logger.getLogger(RunnableINDEP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }*/
       
        beanOracle = new BeanDBAccessOracle("127.0.0.1", "1521", "SHOP", "oracle");

        try {
            beanOracle.connect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RunnableINDEP.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    

    @Override
    public void run() {

        Message request = null;
        
        do
        {
            if((request = receiveMessage()) == null)
                return;
            
            if(request.getType() != REQUEST_LOGIN)
            {
                if(request.getType() == REQUEST_INTERRUPT)
                    return;
                else
                    continue;
            }
            
            executeLogin(request);
            
        }while(!logged);
        

        do
        {
            if((request = receiveMessage()) == null)
                terminated = true;
            else
            {
                switch(request.getType())
                {
                    case REQUEST_LOGIN:
                        break;
 
                    case GET_GR_VENTES_REP:
                        executeVenteRep(request);
                        break;
                    
                    case GET_STAT_DESCR_CONT:
                        executeDescrCont(request);
                        break;
                    case GET_GR_VENTES_COMP:
                        executeVentesComp(request);
                        break;
                    case GET_STAT_INFER_TEST_CONF:
                        executeTestConf(request);
                        break;
                        
                    case GET_STAT_INFER_TEST_ANOVA:
                        executeTestAnova(request);
                        break;
                        
                    case REQUEST_SALE_INFO:
                        executeSaleInfo(request);
                        break;
                        
                    default:
                        terminated = true;
                        break;
                }
            }
               
        } while(!terminated);
        
        try {
            beanOracle.disconnect();
            connexionR.close();
            CSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(RunnableINDEP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RunnableINDEP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Thread client fermé");
    }
    private void executeLogin(Message request)
    {    
        Message response = new Message();
        response.setType(REQUEST_LOGIN);
        String query, login;
        //Integer logged = 0;
        byte[] hashedPass;
        double alea;
        long temps;
        String passDb;
        
        login = (String) request.getParam("login");
        hashedPass = (byte[])request.getParam("password");
        alea = (double)request.getParam("random");
        temps = (long)request.getParam("time");
        //System.out.println(login + " " + alea + " " + " " + temps + " - " + hashedPass);
        
        
        query = "SELECT password FROM personnel WHERE login = '" + login + "'";
       
        try {
            ResultSet result = beanOracle.executeQuery(query);
            if (!result.next() ) {
                response.addParam("status", 0);
                response.addParam("error", "Votre login n'est pas connus.");
            } 
            else 
            {
                passDb = result.getString("password");
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
                    response.addParam("status", 1);
                    response.addParam("message", "Authentification valide");
                    logged = true;
                }
                else
                {
                    response.addParam("status", 0);
                    response.addParam("error", "Login ou password invalide");
                }
            }
           
            
        } catch (SQLException | IOException | NoSuchAlgorithmException ex) {   
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("status", 0);
            response.addParam("error", "Oups... quelque chose est cassé en interne. Veuillez contacter l'administrateur");
        }
        
        sendMessage(response);   
    }
    
    public void executeSaleInfo(Message request) 
    {
        Message response = new Message();
        
        response.setType(request.getType());
       
        int idSeller = (int) request.getParam("idSeller");
        int time = (int) request.getParam("time");
        String arguments = (String) request.getParam("arguments");
        String remarks = (String) request.getParam("remarks");
        boolean successSelling = (boolean) request.getParam("successSelling");
              
        try {
            if(successSelling)
            {
                // Nécessite un développement de l'appli Android
            }
            else
            {
                int idTypeAppareil = (int) request.getParam("idTypeAppareil");
                beanOracle.executeUpdate("INSERT INTO VENTES_RATEES (NUMERO_SERIE, ID_PERSONNEL, TEMPS, ARGUMENT, REMARQUE) VALUES (" + idTypeAppareil + ", " + String.valueOf(idSeller) + ", " + String.valueOf(time) + ", '" + arguments + "', '" + remarks + "')");
            }
            
        } catch (SQLException ex) {
            response.addParam("error", ex.getMessage());
        }
        
        sendMessage(response);
    }
    
    public Message receiveMessage()
    {
        Message msg = null;
        
        try {
            msg = (Message) ois.readObject();
        } catch (IOException ex) {
            System.out.println("Le client est partit.");
            //Logger.getLogger(RunnableINDEP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RunnableINDEP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }
    
    public void sendMessage(Message msg)
    {
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(RunnableINDEP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executeDescrCont(Message request) {
        String query;
        Integer nbrElem = (Integer) request.getParam("nbrelem");
        Message response = new Message();
        REXP rResponse;
        query =    "select to_char(fa.date_facturation, 'DD/MM/YYYY') as jour, sum(tot.total) as total\n" +
                    "from (select * \n" +
                    "      from factures\n" +
                    "      where date_facturation > sysdate - INTERVAL '1' MONTH\n" +
                    "      ORDER BY dbms_random.value\n" +
                    "      FETCH FIRST "+ nbrElem + " ROWS ONLY) fa\n" +
                    "INNER join (\n" +
                    "  SELECT id_facture, sum(prix) as total \n" +
                    "  from items_facture it\n" +
                    "  group by it.id_facture) tot\n" +
                    "on fa.id_facture = tot.id_facture\n" +
                    "GROUP BY to_char(fa.date_facturation, 'DD/MM/YYYY')\n" +
                    "order by jour";
       
        try {
            //Récupère les ventes du mois
            ResultSet result = beanOracle.executeQuery(query);

            //Crée un fichier avec
            File tempFile  = createTmpCsv("descrcont");
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(tempFile)), "UTF-8"));

            while (result.next()) {
                writer.append(result.getString(1)).append(", ").append(String.valueOf(result.getDouble(2))).println();
            }

            writer.close();
            
            //Demander à R
            connexionR.eval("data <- read.table(\""+ tempFile.getAbsolutePath() +"\")");
            rResponse = connexionR.eval("mean(data$V2)");
            response.addParam("moyenne", rResponse.asDouble());
            
            rResponse = connexionR.eval("sd(data$V2)");
            response.addParam("ecart-type", rResponse.asDouble());
            
            rResponse = connexionR.eval("median(data$V2)");
            response.addParam("mediane", rResponse.asDouble());
            
            /* mode ? */
            response.addParam("mode", "?");
            response.addParam("status", 1);

        } catch (SQLException | IOException | RserveException | REXPMismatchException ex) {   
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("status", 0);
            response.addParam("error", "Oups... quelque chose est cassé en interne. Veuillez contacter l'administrateur");
        } 
        sendMessage(response);
        
    }   
    private File createTmpCsv(String name) throws IOException
    {
        File dir = new File("tmp/");
        dir.mkdir();
        File tmp = new File(dir, name+".csv");
        tmp.createNewFile();
        return tmp;
    }

    private void executeVenteRep(Message request) {
        String query;
        Integer annee = (Integer) request.getParam("annee");
        Integer mois = (Integer) request.getParam("mois");

        Message response = new Message();

        //Genere la requete si il faut un mois ou pas 
        query = "select tp.type as type, ROUND((count(*)/(select count(*) from items_facture it2 \n" +
                                                                    "inner join factures fa2 on it2.id_facture = fa2.id_facture\n" +
                                                                    "where EXTRACT(year FROM fa2.date_facturation) = "+ annee +"\n";
        if(mois != 0) query += "and EXTRACT(month FROM fa2.date_facturation) = "+ mois;
        query += "))*100,2) pct\n" +
                "from items_facture it\n" +
                "inner join factures fa on it.id_facture = fa.id_facture\n" +
                "inner join appareils ap on ap.numero_serie = it.numero_serie\n" +
                "inner join type_appareils ta on ta.id_type_appareil = ap.type_appareil\n" +
                "inner join type_precis tp on ta.type_precis = tp.id_type_precis\n" +
                "where EXTRACT(year FROM fa.date_facturation) = "+ annee +"\n";
        if(mois != 0) query += "and EXTRACT(month FROM fa.date_facturation) = "+ mois +"\n";
        query += "group by tp.type";
       
        try {
            //Récupère les ventes du mois
            ResultSet result = beanOracle.executeQuery(query);

            while (result.next()) {
                response.addParam(result.getString("type"), result.getDouble("pct"));
            }

            response.addParam("status", 1);

        } catch (SQLException ex) {   
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("status", 0);
            response.addParam("error", "Oups... quelque chose est cassé en interne. Veuillez contacter l'administrateur");
        } 
        sendMessage(response);
    }

    private void executeVentesComp(Message request) {
        String query, param;
        Integer annee = (Integer) request.getParam("annee");
        Integer tmp;
        Integer nbr = (Integer) request.getParam("nbr");

        Message response = new Message();

        //Genere la requete si il faut un mois ou pas 
        if(nbr == 10)
        {
            param = "annee";
            query = "WITH MissingMonths\n" +
                    "AS \n" +
                    "(\n" +
                    "    SELECT (LEVEL+"+annee+"-10) AS month, 0 total\n" +
                    "    FROM DUAL \n" +
                    "    CONNECT BY LEVEL <= 10\n" +
                    "),\n" +
                    "GetData\n" +
                    "AS\n" +
                    "  (\n" +
                    "    select cast(to_char(fa.date_facturation, 'YYYY') as integer) as month, sum(tot.total) as total\n" +
                    "    from factures fa\n" +
                    "    INNER join (\n" +
                    "      SELECT id_facture, sum(prix) as total\n" +
                    "      from items_facture it\n" +
                    "      group by it.id_facture) tot\n" +
                    "    on fa.id_facture = tot.id_facture\n" +
                    "    where extract(year from fa.date_facturation) > "+annee+"-10 AND extract(year from fa.date_facturation) <= "+annee+"\n" +
                    "    GROUP BY to_char(fa.date_facturation, 'YYYY')\n" +
                    "  )\n" +
                    "SELECT COALESCE(GD.MONTH, MM.MONTH) as annee, COALESCE(GD.total, MM.total) as total\n" +
                    "FROM   MissingMonths MM \n" +
                    "LEFT JOIN GetData GD  ON MM.month = GD.month\n" +
                    "order by MM.month";
        }
        else 
        {
            param = "mois";
            query ="WITH MissingMonths\n" +
                    "AS \n" +
                    "(\n" +
                    "    SELECT LEVEL AS month, 0 total\n" +
                    "    FROM DUAL \n" +
                    "    CONNECT BY LEVEL <= 12\n" +
                    "),\n" +
                    "GetData\n" +
                    "AS\n" +
                    "  (\n" +
                    "    select cast(to_char(fa.date_facturation, 'MM') as integer) as month, sum(tot.total) as total\n" +
                    "    from factures fa\n" +
                    "    INNER join (\n" +
                    "      SELECT id_facture, sum(prix) as total\n" +
                    "      from items_facture it\n" +
                    "      group by it.id_facture) tot\n" +
                    "    on fa.id_facture = tot.id_facture\n" +
                    "    where extract(year from fa.date_facturation) = " + annee + "\n" +
                    "    GROUP BY to_char(fa.date_facturation, 'MM')\n" +
                    "  )\n" +
                    "SELECT COALESCE(GD.MONTH, MM.MONTH) as mois, COALESCE(GD.total, MM.total) as total\n" +
                    "FROM   MissingMonths MM \n" +
                    "LEFT JOIN GetData GD  ON MM.month = GD.month\n" +
                    "order by MM.month";
        }
        
        
        try {
            //Récupère les ventes du mois
            ResultSet result = beanOracle.executeQuery(query);

            while (result.next()) {
                tmp = result.getInt(param);
                response.addParam(tmp.toString(), result.getDouble("total"));
            }

            response.addParam("status", 1);

        } catch (SQLException ex) {   
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("status", 0);
            response.addParam("error", "Oups... quelque chose est cassé en interne. Veuillez contacter l'administrateur");
        } 
        sendMessage(response);    }

    private void executeTestConf(Message request) {
        String query;
        Integer nbrElem = (Integer) request.getParam("nbrElem");
        Integer montant = (Integer) request.getParam("montant");
        String queryR;
        REXP rResponse;
        RList testList;
        REXPDouble pvalue;
        Double pv;
        Message response = new Message();

        
        query = "select ID_PERSONNEL, REPLACE(CAST(sum(tot.total) AS VARCHAR(10)),',', '.') total\n" +
                "from factures fac\n" +
                "inner join (SELECT id_facture, sum(prix) as total\n" +
                "      from items_facture it\n" +
                "      group by it.id_facture ) tot on tot.id_facture = fac.id_facture\n" +
                "inner join ventes_reussies vr on fac.id_facture = vr.id_facture\n" +
                "group by ID_PERSONNEL, to_char(fac.date_facturation, 'MMYYYY')\n" +
                "order by DBMS_RANDOM.value\n" +
                "FETCH FIRST "+nbrElem+" ROWS ONLY";

        queryR = "t.test(x=c(";
        try {
            //Récupère les ventes du mois
            ResultSet result = beanOracle.executeQuery(query);

            while (result.next()) {
                queryR += result.getString("total") +",";
            }
            queryR = queryR.substring(0, queryR.length()-1) + "), mu="+ montant +")";
                
            rResponse = connexionR.eval(queryR);
            testList = rResponse.asList();
            
            if(testList.containsKey("p.value"))
            {    
                response.addParam("status", 1);
                pvalue = (REXPDouble) testList.get("p.value");
                pv = pvalue.asDouble();
                
                response.addParam("pvalue", pv);
                
                if(pv > 0.10){
                    response.addParam("conclusion", "Il n'y a pas de différence significative :) Les vendeurs ont bien ce chiffre d'affaire.");
                }
                else
                {
                    response.addParam("conclusion", "Il y a une différence significative :/ Les vendeurs n'ont pas ce chiffre d'affaire.");
                }
            
                System.out.println(pvalue.asDouble());
                
            }
            else{
                response.addParam("status", 0);
                response.addParam("error", "R n'a pas renvoyé le résultat attendu.");
            }

        } catch (SQLException | RserveException | REXPMismatchException ex) {   
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("status", 0);
            response.addParam("error", "Oups... quelque chose est cassé en interne. Veuillez contacter l'administrateur");
        } 
        
        sendMessage(response);
    }

    private void executeTestAnova(Message request) {
        String query;
        Integer nbrElem = (Integer) request.getParam("nbrElem");
        String queryR;
        String queryR2;

        REXP rResponse;
        RList testList;
        REXPDouble pvalue;
        Double pv;
        
        Message response = new Message();

        
        query = "SELECT name, total FROM (\n" +
                "    SELECT  vi.nom name,\n" +
                "            tot.total,\n" +
                "            row_number() OVER (PARTITION BY fac.id_ville ORDER BY dbms_random.value) ligne\n" +
                "    FROM factures fac\n" +
                "    inner join (SELECT it.id_facture, sum(prix) as total\n" +
                "                from items_facture it\n" +
                "                group by it.id_facture ) tot\n" +
                "      on tot.id_facture = fac.id_facture\n" +
                "    INNER JOIN villes vi\n" +
                "      ON (fac.id_ville = vi.id_ville)\n" +
                ") WHERE ligne <= "+nbrElem;

        queryR = "ville <- c(";
        queryR2 = "montant <- c(";
        try {
            //Récupère les ventes du mois
            ResultSet result = beanOracle.executeQuery(query);

            while (result.next()) {
                queryR += "\"" + result.getString("name") +"\",";
                queryR2 += result.getInt("total") +",";
            }
            queryR = queryR.substring(0, queryR.length()-1) + ")";
            queryR2 = queryR2.substring(0, queryR2.length()-1) + ")";

            System.out.println(queryR);
                        System.out.println(queryR2);
                
            connexionR.eval(queryR);
            connexionR.eval(queryR2);

            connexionR.eval("data <- data.frame(montant,ville)");
            connexionR.eval("results <- aov (montant ~  ville, data = data)");
            rResponse = connexionR.eval("summary(results)");
            REXP element;
            testList = rResponse.asList();
            REXPGenericVector vec = (REXPGenericVector) testList.elementAt(0);
            testList = vec.asList();
            pvalue = (REXPDouble) testList.get(4);
            pv = pvalue.asDouble();
      
                
            response.addParam("pvalue", pv);

            if(pv > 0.10){
                response.addParam("conclusion", "Il n'y a pas de différence significative entre les villes");
            }
            else
            {
                response.addParam("conclusion", "Il y a une différence significative entre les villes.");
            }

            response.addParam("status", 1);

        } catch (SQLException | RserveException | REXPMismatchException ex) {   
            Logger.getLogger(RunnableDISMAP.class.getName()).log(Level.SEVERE, null, ex);
            response.addParam("status", 0);
            response.addParam("error", "Oups... quelque chose est cassé en interne. Veuillez contacter l'administrateur");
        } 
        
        sendMessage(response);
    }
}

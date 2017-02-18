package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bastin
 */
public class ServerProperties {
    
    protected Properties properties;
    protected String pathProperties = "server.properties";
    
    public ServerProperties() {
        
        properties = new Properties();
        
        try {
            FileInputStream fis = new FileInputStream(pathProperties);
            properties.load(fis);
             
        } catch (FileNotFoundException ex) {
            try {
                FileOutputStream fos = new FileOutputStream(pathProperties);
                
                properties.setProperty("PORT_DISMAP", "6000");
                properties.setProperty("PORT_DISMAP2", "6001");
                properties.setProperty("PORT_INDEP", "6002");
                properties.setProperty("PORT_DSA", "6003");
                properties.setProperty("PORT_URGENCE", "6004");
                properties.setProperty("PORT_REDMMP", "6005");
                properties.setProperty("PORT_ORACLE", "1521");
                
                properties.setProperty("IP_SERVER", "192.168.1.4");
                properties.setProperty("IP_ORACLE", "192.168.1.4");
                
                properties.setProperty("NBR_THREADS_DISMAP", "5");
                properties.setProperty("NBR_THREADS_INDEP", "5");
                properties.setProperty("NBR_THREADS_REDMMP", "5");
                
                properties.setProperty("LOGIN_ADMIN", "bastin");
                properties.setProperty("PASSWORD_ADMIN", "bastin");
                
                properties.setProperty("LOGIN_ORACLE", "SHOP");
                properties.setProperty("PASSWORD_ORACLE", "oracle");
                
                try {
                    properties.store(fos, null);
                } catch (IOException ex1) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
                    System.exit(1);
                }
                
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
                System.exit(1);
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getPathProperties() {
        return pathProperties;
    }

    public void setPathProperties(String pathProperties) {
        this.pathProperties = pathProperties;
    }
    
    
}

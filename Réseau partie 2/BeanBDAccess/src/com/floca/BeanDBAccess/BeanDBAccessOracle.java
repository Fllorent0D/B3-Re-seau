package com.floca.BeanDBAccess;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bastin
 */
public class BeanDBAccessOracle extends BeanDBAccess {

    public BeanDBAccessOracle(String h, String p, String l, String pswd) 
    {
        super(h, p, l, pswd, null);
        setDriver("oracle.jdbc.driver.OracleDriver");
    }

    @Override
    public void connect() throws ClassNotFoundException, SQLException
    {
        String url = "jdbc:oracle:thin:@//localhost:" + port + "/orcl";

        Class.forName(driver);
    
        connection = (Connection) DriverManager.getConnection(url, login, password);
    
        statement = (Statement) connection.createStatement();
    }
      
    public static void main(String[] args) {
        BeanDBAccessOracle beanDBAccessOracle = new BeanDBAccessOracle("127.0.0.1", "1521", "bastin", "bastin");
        System.out.println("Création de l'object beanDBAccessOracle");
        System.out.println("Connexion...");
        try {
            beanDBAccessOracle.connect();
            System.out.println("Connexion établie");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BeanDBAccessOracle.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BeanDBAccessOracle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

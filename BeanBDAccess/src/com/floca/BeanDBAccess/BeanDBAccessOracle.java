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
    public void connect()
    {
        String url = "jdbc:oracle:thin:@//localhost:" + port + "/xe";
        try
        {
            Class.forName(driver);
        
            connection = (Connection) DriverManager.getConnection(url, login, password);
        
            statement = (Statement) connection.createStatement();
    

        }
        catch (SQLException | ClassNotFoundException ex)
        {
            System.out.println("Erreur SQL : " + ex.getMessage());
        }
    }
        
}

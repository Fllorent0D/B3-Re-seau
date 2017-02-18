package com.floca.BeanDBAccess;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bastin
 */
public class BeanDBAccessMysql extends BeanDBAccess {

    public ResultSet rs;
    
    public BeanDBAccessMysql(String h, String p, String l, String pswd, String s)
    {
        super(h, p, l, pswd, s);
        setDriver("com.mysql.jdbc.Driver");
    }

    /**
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Override
    public void connect() throws SQLException, ClassNotFoundException
    {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + schema;
        
        Class.forName(driver);
        
        connection = (Connection) DriverManager.getConnection(url, login, password);
        
        statement = (Statement) connection.createStatement();
    }
}

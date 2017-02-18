package com.floca.BeanDBAccess;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bastin
 */
abstract public class BeanDBAccess implements Serializable {
    protected String driver;
    protected String host;
    protected String port;
    protected String login;
    protected String password;
    protected String schema;
    protected Connection connection;
    protected Statement statement;
    
    public BeanDBAccess(String h, String p, String l, String pswd, String s) {
        setHost(h);
        setPort(p);
        setLogin(l);
        setPassword(pswd);
        setSchema(s); 
    }
    
    abstract public void connect() throws SQLException, ClassNotFoundException;
    
    public void disconnect() throws SQLException
    {
        if(statement != null)
            statement.close();
    }
    
    public ResultSet executeQuery(String query) throws SQLException
    {
        Statement stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(query);
    }
    
    public int executeUpdate(String query) throws SQLException
    {
        return statement.executeUpdate(query);
    }
    
    public int executeUpdateWithKey(String query) throws SQLException
    {
        return statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
    }
    
    public boolean execute(String query) throws SQLException
    {
        return statement.execute(query);
    }

    public String getSchema() {
        return schema;
    }

    public final void setSchema(String schema) {
        this.schema = schema;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getHost() {
        return host;
    }

    public final void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public final void setPort(String port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public final void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public final void setPassword(String password) {
        this.password = password;
    }
    
       public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    } 
    public void setAutoCommit(boolean autocomm){
        try
        {
            this.connection.setAutoCommit(autocomm);
        }
         catch (SQLException ex)
        {
            this.rollback();
            //System.out.println("Erreur SQL : " + ex.getMessage());
        }
        
    }
    public void commit()
    {
        try
        {
            this.connection.commit();
        }
         catch (SQLException ex)
        {
            this.rollback();
            //System.out.println("Erreur SQL : " + ex.getMessage());
        }
    }
    public void rollback()
    {
        try
        {
            this.connection.rollback();
        }
         catch (SQLException ex)
        {
            System.out.println("Erreur SQL : " + ex.getMessage());
        }
        
    }
    public synchronized int ecriture(String from, HashMap donnees) throws SQLException
    {
        String url;
        
        String champs = "(";
            String valeurs = "(";
                
            Set cles = donnees.keySet();
            Iterator it = cles.iterator();
                
            while(it.hasNext())
            {
                Object cle = it.next();
                    
                champs = champs + cle.toString();
                if (donnees.get(cle) instanceof Double ||donnees.get(cle) instanceof Float)
                    valeurs = valeurs + donnees.get(cle).toString();
                else
                    valeurs = valeurs + "'" + donnees.get(cle).toString() + "'";
                    
                if (it.hasNext())
                {
                    champs = champs + ",";
                    valeurs = valeurs + ",";
                }
            }
                
            champs = champs + ")";
            valeurs = valeurs + ")";
                
            url = "insert into " + from + champs + " values " + valeurs;
            
        
        
            PreparedStatement pStmt = getConnection().prepareStatement(url);
            pStmt.executeUpdate();
            ResultSet rs = pStmt.getGeneratedKeys();
            if(rs.next())
            {
                int last_inserted_id = rs.getInt(1);        
                return last_inserted_id; 

            }

        

        return 0;
    }
}

package com.se2.invoice.forms;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dataConnectionObject {
	public Connection getConnection() throws IOException {
        Connection conn = null;
        Object pst = null;
        Object rs = null;
        
        Properties prop = new Properties();
        String propFileName = "resources/config.properties";
        
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        
        if(inputStream!=null){
            prop.load(inputStream);
        } else{
            System.out.println("Cannot load config.properties file.");
        }
        
        String url = prop.getProperty("url");
        String dbName = prop.getProperty("dbName");
        String driver = prop.getProperty("driver");
        String userName = prop.getProperty("userName");
        String password = prop.getProperty("password");
        
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(dataConnectionObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException ex) {
            Logger.getLogger(dataConnectionObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(dataConnectionObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(dataConnectionObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
}

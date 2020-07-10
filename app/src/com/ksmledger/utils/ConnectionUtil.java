package com.ksmledger.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionUtil {
    private static Connection conn = null;
    public static Connection connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3307/ksmledgerdb?useTimezone=true&serverTimezone=UTC","root","");
            return conn;
        }catch (Exception ex){
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null,ex);
            return  null;
        }
    }
}

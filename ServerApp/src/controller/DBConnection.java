/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Abraham Krisnanda
 */
import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;
    
    public static Connection getConnection() 
    {
        if (connection != null) 
            return connection;
        else 
        {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/progin_439_13510007", "progin", "progin");
                System.out.println("Koneksi Berhasil !");
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
    }
}
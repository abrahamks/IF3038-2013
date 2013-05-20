/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import controller.*;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.DummySender;
import sun.security.action.GetBooleanAction;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Abraham Krisnanda
 */
public class ServerApp extends DBRecord{

    /**
     * @param args the command line arguments
     */
    private static int current_id;
    private static ServerSocket connection;
    public static void main(String[] args) throws IOException {
//        DBRecord a = new DBRecord();
//        int i = a.FindUser("admin2", "password");
//        current_id = i;
//        System.out.println(current_id);
//        //System.out.println((a.GetUserTask(i)).toString());
//        String hasil = a.GetUserTask(i).toString();
//        byte[] b  = hasil.getBytes();
//        String o="";
//        try {
//            o = new String(b, "UTF8");
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(o);
//        System.out.println(MD5("admin2"));
//        int i = a.FindUser(MD5("admin2"), MD5("password"));
//        System.out.println(i);
//        System.out.println(a.GetUserTask(i));
 //       DummySender sender = new DummySender("127.0.0.1", 44445);
          connection = new ServerSocket(44444);
          while (true)
          {
            /// Cek user password
            System.out.println("Masukkan pass and User");
            
            try
            {
                System.out.println("Nunggu client");
                Socket clientSocket = connection.accept();
                Thread thread;
                thread = new Thread(new ServerHandler(clientSocket));
                thread.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
    }
}

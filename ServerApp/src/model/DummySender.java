/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import controller.DBRecord;
import java.sql.SQLException;
/**
 *
 * @author Abraham Krisnanda
 */
public class DummySender implements Runnable {
    private int port;
    private int id_user;
    private Socket socket;
    private OutputStream outToServer;
    private DataOutputStream out;
    private InputStream inFromServer;
    private DataInputStream in;
    private Thread thread;
    public DummySender(String ip, int port)
    {
        this.port = port;
        
        try {
            socket = new Socket(ip, port);
            outToServer = socket.getOutputStream();
            out = new DataOutputStream(outToServer);
            inFromServer = socket.getInputStream();
            in = new DataInputStream(inFromServer);
            thread = new Thread(this, "thread");
            thread.start();
        } catch (UnknownHostException ex) {
            Logger.getLogger(DummySender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DummySender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try {
                System.out.println("a");
                out.writeUTF("cobalah mengerti");
            } catch (IOException ex) {
                Logger.getLogger(DummySender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void response_success() throws IOException
    //* mengembalikan message success setelah client berhasil login
    {
        out.writeUTF("success " + id_user);
    } 
    
    public void response_fail() throws IOException
    //* mengembalikan message fail apabila client gagal terkoneksi
    {
        out.writeUTF("fail");
    }
    
    public void response_listtask() throws IOException, SQLException
    // mengembalikan list of task yang dimiliki oleh seorang user
    {
        DBRecord a = new DBRecord();
        out.writeUTF(a.GetUserTask(id_user).toString());
    }
    public void response_exit() throws IOException
    //* mengembalikan message exit sebagai response dari logout        
    {
        out.writeUTF("exit");
    }
    
    public boolean is_success_received(String msg)
    {
        return msg.startsWith("success");
    }
    
    public boolean is_listtask_received(String msg){
         return (msg.startsWith("listtask"));
    }
    
    public boolean is_exit_received(String msg){
        return (msg.startsWith("exit"));
    }
}

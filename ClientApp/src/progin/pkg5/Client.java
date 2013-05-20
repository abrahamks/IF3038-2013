/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progin.pkg5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LCF
 */
public class Client implements Runnable{
    private int port;
    private int user_id;
    private Socket socket;
    private OutputStream outToServer;
    private DataOutputStream out;
    private InputStream inFromServer;
    private DataInputStream in;
    private Thread thread;
    public Client(String ip, int port)
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
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                System.out.println("a");
                out.writeUTF("coba");
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void send_login(String username, String pass){
        try {
            out.writeUTF("login "+username+" "+pass);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send_request_list_task(){
        try {
            out.writeUTF("request "+user_id);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send_update(){
        
    }
    
    public void send_logout(){
        try {
            out.writeUTF("logout "+ user_id);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
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

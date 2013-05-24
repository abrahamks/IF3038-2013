/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abraham Krisnanda
 */
public class DummyReceiver implements Runnable{
    private static ServerSocket serverSocket;
    protected final static int port = 44445;
    private static Socket connection;
  
    public DummyReceiver() throws IOException
    {
        try {
            serverSocket = new ServerSocket(44445);
            serverSocket.setSoTimeout(240000);
        } catch (SocketException ex) {
            Logger.getLogger(DummyReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("waiting");
            Socket s = serverSocket.accept();
            System.out.println("Somebody's connected "+s.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(s.getInputStream());
            while(true)
            {
                System.out.println(in.readUTF());
            }
            
        } catch (IOException ex) {
            Logger.getLogger(DummyReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

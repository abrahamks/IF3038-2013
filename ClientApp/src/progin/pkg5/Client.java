/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progin.pkg5;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LCF
 */
public class Client implements Runnable {

    public int port;
    public String ip;
    public int user_id;
    private Socket socket;
    private OutputStream outToServer;
    private DataOutputStream out;
    private InputStream inFromServer;
    private DataInputStream in;
    private Thread thread;
    private Map<Integer, Tugas> tugastugas;
    private boolean isConnected = false;

    public Client(String ip, int port) {
        this.port = port;
        this.ip = ip;
        connect();
        thread = new Thread(this, "thread");
        tugastugas = new TreeMap<>();
        thread.start();
    }

    public void connect() {
        try {
            socket = new Socket(ip, port);
            outToServer = socket.getOutputStream();
            out = new DataOutputStream(outToServer);
            inFromServer = socket.getInputStream();
            in = new DataInputStream(inFromServer);
            isConnected = true;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String msg = in.readUTF();
                
                if(is_success_received(msg)){
                    String[] msgs = msg.split(" ");
                    user_id = Integer.parseInt(msgs[1]);
                    send_request_list_task();
                }
                else if(is_listtask_received(msg)){
                    System.out.println("LIST TASK");
                    System.out.println(msg);
                    parse_list_task(msg);
                }
            } catch (IOException ex) {
                isConnected = false;
    //              reconnecting.....
                connect();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }

    public Map<Integer, Tugas> getTugasTugas() {
        return tugastugas;
    }

    public static String MD5(String input)
    {
    	String result = "";
    	try {
    		MessageDigest MD5 = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(new ByteArrayInputStream(input.getBytes("UTF-8")), MD5);
			
			while (dis.read()!=-1);
			byte[] hash = MD5.digest();
			
			Formatter formatter = new Formatter();
			for (byte b : hash)
			{
				formatter.format("%02x", b);
			}
			result = formatter.toString();
			formatter.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    	return result;
    }
    
    public void send_login(String username, String pass) {
        try {
            out.writeUTF("login " + MD5(username) + " " + MD5(pass));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send_request_list_task() {
        System.out.println("kirim request");
        try {
            out.writeUTF("request " + user_id);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send_update() {
    }

    public void send_logout() {
        try {
            out.writeUTF("logout " + user_id);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean is_success_received(String msg) {
        return msg.startsWith("success");
    }

    public boolean is_listtask_received(String msg) {
        return (msg.startsWith("listtask"));
    }

    public boolean is_exit_received(String msg) {
        return (msg.startsWith("exit"));
    }

    public void parse_list_task(String msg) {
        String str = msg.substring(8);
        String[] array_str = str.split("\n");
        for (int i = 0; i < array_str.length / 7; i++) {
            int idtugas = Integer.parseInt(array_str[i * 7]);
            boolean status = array_str[i * 7 + 5].equals("1");
            int idKategori = Integer.parseInt(array_str[i * 7 + 6]);
            Tugas tugas = new Tugas(idtugas, array_str[i * 7 + 1], array_str[i * 7 + 2], array_str[i * 7 + 3], array_str[i * 7 + 4], status, idKategori);
            tugastugas.put(idtugas, tugas);
        }
    }
}

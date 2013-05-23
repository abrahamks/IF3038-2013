package controller;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author asus
 */
public class ServerHandler extends Thread {

    private Socket socket;
    private boolean succes;
    private int id_user;
    
    public ServerHandler(Socket sock) {
        socket = sock;
    }

    // message
    public void response_success(DataOutputStream out) throws IOException
    //* mengembalikan message success setelah client berhasil login
    {
        out.writeUTF("success " + id_user);
    } 
    
    public void response_fail(DataOutputStream out) throws IOException
    //* mengembalikan message fail apabila client gagal terkoneksi
    {
        out.writeUTF("fail");
    }
    
    public void response_listtask(DataOutputStream out) throws IOException, SQLException
    // mengembalikan list of task yang dimiliki oleh seorang user
    {
        DBRecord a = new DBRecord();
        out.writeUTF("listtask\n" + a.GetUserTask(id_user).toString());
    }
    
    public void response_exit(DataOutputStream out) throws IOException
    //* mengembalikan message exit sebagai response dari logout        
    {
        out.writeUTF("exit");
    }
    
    public boolean is_login_received(String msg)
    {
        return msg.startsWith("login");
    }
    
    public boolean is_requesttask_received(String msg){
        System.out.println("ini ketterima aldasflasldflkasdjhfkldasjf");
         return (msg.startsWith("request"));
    }
    
    public boolean is_exit_received(String msg){
        return (msg.startsWith("exit"));
    }
    
    public boolean is_update_received (String msg){
        return (msg.startsWith("update"));
    }
    @Override
    public void run() {
        while (true)
        {
            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                DataInputStream in = new DataInputStream(socket.getInputStream());

                String temp = "";
                temp = in.readUTF();

                // menangani login
                if (is_login_received(temp))
                {
                    String tempString[];
                    String u;
                    String p;
                    tempString = temp.split(" ");
                    u = tempString[1];
                    p = tempString[2];
                    DBRecord rec = new DBRecord();
                    if(rec.FindUser(u, p) != -1)
                    {
                        id_user = rec.FindUser(u, p);
                        response_success(out);
                    }
                }
                // menangani request list task
                else if (is_requesttask_received(temp))
                {
                    response_listtask(out);
                }
                // meangani request update
                else if (is_update_received(temp))
                {
                    System.out.println("*****************");
                    System.out.println(temp);
                    System.out.println("*****************");
                    String tempString[];
                    String token[];
                    String token2[];
                    tempString = temp.split("\n");
                    
                    // tempString[0] --> 'update'
                    // tempString[1] --> 'task_id, status, tanggal'
                    // 2012 10 9 hh:mm:ss
                    token = tempString[1].split(" ");
                    // token[0] --> id_task
                    // token[1] --> status
                    // token[2] --> timestamp
                    // convert token[2] to Date Class
                    //6 1 113 4 4 15:51:52
                    Calendar dateinput = new GregorianCalendar();
                    String ymd[] = token[5].split(" ");
                    String hms[] = token[5].split(":");
                    int year = Integer.parseInt(token[2]);
                    int month = Integer.parseInt(token[3]);
                    int date = Integer.parseInt(token[4]);
                    int hour = Integer.parseInt(hms[0]);
                    int minute = Integer.parseInt(hms[1]);
                    int second = Integer.parseInt(hms[2]);
                    dateinput.set(year, month, date, hour, minute, second);

                     // convert last_edit from mysql to Date Class
                    Calendar datesql = new GregorianCalendar();
                    DBRecord rec = new DBRecord();
                    String temps = rec.GetTaskDetail(Integer.parseInt(token[0]));
                    token2 = temps.split("\n");
                    // token2[token2.length-1] --> last_edit
                    String last_edit = token2[token2.length-1];
                    String ymd2[] = (last_edit.split(" ")[0].split("-")); //yyyy-mm-dd
                    String hms2[] = (last_edit.split(" ")[1].split(":")); //hh:mm:ss
                    int y = Integer.parseInt(ymd2[0]);
                    int m = Integer.parseInt(ymd2[1]);
                    int d = Integer.parseInt(ymd2[2]);
                    int h = Integer.parseInt(hms2[0]);
                    int mon = Integer.parseInt(hms2[1]);
                    System.out.println(hms2[2].substring(0,1));
//                    String tempssss[] = hms2[2].split(".");
                    int sec = Integer.parseInt(hms2[2].substring(0,1));
                    datesql.set(y,m,d,h,mon,sec);

                    int stat_sql = Integer.parseInt(token2[5]);
                    int stat_input = Integer.parseInt(token[1]);
                    if (stat_sql != stat_input)
                    {
                        if (dateinput.after(datesql))
                            // merubah status sesuai dengan msg input
                        {
                            DBRecord r = new DBRecord();
                            r.UpdateStatus(Integer.parseInt(token[0]), stat_input);
                            //r.UpdateLastEdit(Integer.parseInt(token[0]), dateinput.getTime());
                        }
                    }
                    response_listtask(out);
                }
                else if (is_exit_received(temp))
                {
                    response_exit(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException ex) {
                Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}

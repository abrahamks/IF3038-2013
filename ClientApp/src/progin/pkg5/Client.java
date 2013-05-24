/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progin.pkg5;

import com.sun.java_cup.internal.runtime.Symbol;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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

    private String username;
    private String password;
    public int port;
    public String ip;
    public int user_id;
    private Socket socket;
    private OutputStream outToServer;
    private DataOutputStream out;
    private InputStream inFromServer;
    private DataInputStream in;
    private Thread thread;
    public Map<Integer, Tugas> tugastugas;
    public Map<Integer, Log> logs;
    public boolean isConnected = false;
    public boolean dataSiap = false;

    public Client(String ip, int port) {
        this.port = port;
        this.ip = ip;
//        connect();
        thread = new Thread(this, "thread");
        tugastugas = new TreeMap<>();
        logs = new TreeMap<>();
//        thread.start();
    }

    public void start() {
        connect();
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
        while (true) {
            try {
                String msg = in.readUTF();

                if (is_success_received(msg)) {
                    String[] msgs = msg.split(" ");
                    user_id = Integer.parseInt(msgs[1]);
                    System.out.println(msg);
                    send_request_list_task();
                    System.out.println("ini sekarang kirim ");
                } else if (is_listtask_received(msg)) {
                    System.out.println("LIST TASK");
                    System.out.println(msg);
                    tugastugas.clear();
                    parse_list_task(msg);
                }
            } catch (IOException ex) {
                isConnected = false;
                //              reconnecting.....
                connect();
                send_login(username, password);
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

    public static String MD5(String input) {
        String result = "";
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(new ByteArrayInputStream(input.getBytes("UTF-8")), MD5);

            while (dis.read() != -1);
            byte[] hash = MD5.digest();

            Formatter formatter = new Formatter();
            for (byte b : hash) {
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
        this.username = username;
        this.password = pass;
        try {
            out.writeUTF("login " + MD5(username) + " " + MD5(pass));
            System.out.println("login " + MD5(username) + " " + MD5(pass));
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send_request_list_task() {
        try {
            out.writeUTF("request " + user_id);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send_update() {
        String asd = "update";
        for (Map.Entry<Integer, Log> entry : logs.entrySet()) {
            String baris = "\n" + entry.getValue().id_tugas;
            if (entry.getValue().status) {
                baris += " 1 ";
            } else {
                baris += " 0 ";
            }
            int tahun = entry.getValue().last_edit.getYear() + 1900;
            int bulan = entry.getValue().last_edit.getMonth() + 1;
            int tanggal = entry.getValue().last_edit.getDate();
            int jam = entry.getValue().last_edit.getHours();
            int menit = entry.getValue().last_edit.getMinutes();
            int detik = entry.getValue().last_edit.getSeconds();

            baris += tahun + " " + bulan + " " + tanggal + " " + jam + ":" + menit + ":" + detik;
            asd += baris;
        }
        try {
            System.out.println("paket update =" + asd);
            out.writeUTF(asd);
            File file = new File(user_id + ".log");
            file.delete();
            logs.clear();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

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
        System.out.println("-------------------");
//                listtask
//        [6
//        Tugas Paling Baru Loh
//        2013-05-31
//        [admin2]
//        []
//        0
//        Tugas_Terbaru
//        2013-05-18 18:12:38.0,
//          7
//        Tugas Paling Baru v2
//        2013-05-31
//        [abrahamks, admin2]
//        [intelegensia buatan]
//        0
//        Tugas_Terbaru
//        2013-05-18 18:12:38.0]
        String str = msg.substring(9);
        String[] array_str = str.split("\n");
        int templ = 0;
        int tempi = 0;
        templ = array_str.length;
        if (array_str.length > 1) {
            int first = 0;
            while (tempi < templ) {
                if (tempi % 8 == 0 && first != 0 && tempi != templ - 1) {
                    array_str[tempi] = array_str[tempi].substring(1);
                }
                System.out.println(array_str[tempi]);
                first = 1;
                tempi++;
            }
        }
        System.out.println("---");

        for (int i = 0; i < array_str.length / 8; i++) {
            int idtugas = Integer.parseInt(array_str[i * 8].substring(1));
            boolean status = array_str[i * 8 + 5].equals("1");
            int idKategori = 999;
            Tugas tugas = new Tugas(idtugas, array_str[i * 8 + 1], array_str[i * 8 + 2], array_str[i * 8 + 3], array_str[i * 8 + 4], status, array_str[i * 8 + 7], true);
            tugastugas.put(idtugas, tugas);
        }
        System.out.println("lewat---");
        dataSiap = true;
    }

    public Object[][] getTugas() {
        Object[][] hasil = new Object[tugastugas.size()][8];

        int i = 0;
        for (Map.Entry<Integer, Tugas> entry : tugastugas.entrySet()) {
            hasil[i][0] = entry.getValue().idtugas;
            hasil[i][1] = entry.getValue().namaTugas;
            hasil[i][2] = entry.getValue().deadline;
            hasil[i][3] = entry.getValue().assignee;
            hasil[i][4] = entry.getValue().tags;
            hasil[i][5] = entry.getValue().status;
            hasil[i][6] = entry.getValue().last_edit;
            if(entry.getValue().isSynced){
                hasil[i][7] = "Up to date";
            }
            else{
                hasil[i][7] = "unsynced";
            }
            i++;
        }

        return hasil;
    }

    public void addLog(int id_tugas, boolean new_status) {
        FileWriter fw = null;
        try {
            Log log;
            if (logs.containsKey(id_tugas)) {
                if (tugastugas.get(id_tugas).status == new_status) {
                    logs.remove(id_tugas);
                    System.out.println("a");
//                    log = new Log(id_tugas, new_status);
//                    logs.put(id_tugas, log);
                } else {
                    System.out.println("b");
                    log = new Log(id_tugas, new_status);
                    logs.put(id_tugas, log);
                }
            } else {
                System.out.println("c");
                log = new Log(id_tugas, new_status);
                logs.put(id_tugas, log);
            }

            File file = new File(user_id + ".log");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            fw = new FileWriter(file.getName());
            BufferedWriter bw = new BufferedWriter(fw);
            String msg = "";
            for (Map.Entry<Integer, Log> entry : logs.entrySet()) {
                String baris = "" + entry.getValue().id_tugas;
                if (entry.getValue().status) {
                    baris += " 1 ";
                } else {
                    baris += " 0 ";
                }
                int tahun = entry.getValue().last_edit.getYear() + 1900;
                int bulan = entry.getValue().last_edit.getMonth() + 1;
                int tanggal = entry.getValue().last_edit.getDate();
                int jam = entry.getValue().last_edit.getHours();
                int menit = entry.getValue().last_edit.getMinutes();
                int detik = entry.getValue().last_edit.getSeconds();

                baris += tahun + " " + bulan + " " + tanggal + " " + jam + ":" + menit + ":" + detik;
                bw.write(baris);
                bw.newLine();
                msg += baris + "\n";
            }
            System.out.println(msg);

            bw.close();
            //        System.out.println("LOG : " + id_tugas + " " + new_status);
            //
            //        String content = "" + id_tugas + " " + new_status + " " + log.last_edit.toLocaleString();
            //
            //        File file = new File("/Log/" + user_id + ".log");
            //        if (!file.exists()) {
            //            FileWriter fw = null;
            //            try {
            //                try {
            //                    file.createNewFile();
            //                } catch (IOException ex) {
            //                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            //                }
            //                fw = new FileWriter(file.getAbsoluteFile());
            //                BufferedWriter bw = new BufferedWriter(fw);
            //                bw.write(content);
            //                bw.close();
            //            } catch (IOException ex) {
            //                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            //            } finally {
            //                try {
            //                    fw.close();
            //                } catch (IOException ex) {
            //                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            //                }
            //            }
            //        } else {
            //            try {
            //                PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter("/Log/" + user_id)));
            //                fout.println(content);
            //                fout.close();
            //            } catch (IOException ex) {
            //                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            //            } finally {
            //            }
            //        }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public void parseFromLogFile(){
        try {
            BufferedReader br = null;
            String line;
            br = new BufferedReader(new FileReader(user_id+".log"));
            while((line = br.readLine()) != null){
                line = line.replace(":", " ");
                String[] arr = line.split(" ");
                int id = Integer.parseInt(arr[0]);
                boolean stat;
                if(Integer.parseInt(arr[1]) == 1){
                    stat = true;
                }
                else{
                    stat = false;
                }
                
                int y = Integer.parseInt(arr[2]) + 1900;
                int m = Integer.parseInt(arr[3]) - 1;
                int d = Integer.parseInt(arr[4]);
                int h = Integer.parseInt(arr[5]);
                int min = Integer.parseInt(arr[6]);
                int s = Integer.parseInt(arr[7]);
                Date tanggal = new Date(y, m, d, h, min, s);
                if(tugastugas.get(id).date_last_edit.before(tanggal)){
                    logs.put(id, new Log(id, stat));
                    tugastugas.get(id).isSynced = false;
                    tugastugas.get(id).status = logs.get(id).status;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}

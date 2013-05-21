/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progin.pkg5;

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
    private Map<Integer, Log> logs;
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
                    send_request_list_task();
                } else if (is_listtask_received(msg)) {
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
        try {
            out.writeUTF("login " + MD5(username) + " " + MD5(pass));
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
        String msg = "update";
        for (Map.Entry<Integer, Log> entry : logs.entrySet()) {
            String baris = "\n" + entry.getValue().id_tugas;
            if (entry.getValue().status) {
                baris += " 1 ";
            } else {
                baris += " 0 ";
            }
            int tahun = entry.getValue().last_edit.getYear();
            int bulan = entry.getValue().last_edit.getMonth();
            int tanggal = entry.getValue().last_edit.getDay();
            int jam = entry.getValue().last_edit.getHours();
            int menit = entry.getValue().last_edit.getMinutes();
            int detik = entry.getValue().last_edit.getSeconds();

            baris += tahun + " " + bulan + " " + tanggal + " " + jam + ":" + menit + ":" + detik + "\n";
            msg += baris;
        }
        try {
            out.writeUTF(msg);
            File file = new File("/Log/" + user_id + ".log");
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
        String str = msg.substring(8);
        String[] array_str = str.split("\n");

        for (int i = 0; i < array_str.length / 8; i++) {
            int idtugas = Integer.parseInt(array_str[i * 8]);
            boolean status = array_str[i * 8 + 5].equals("1");
            int idKategori = Integer.parseInt(array_str[i * 8 + 6]);
            Tugas tugas = new Tugas(idtugas, array_str[i * 8 + 1], array_str[i * 8 + 2], array_str[i * 8 + 3], array_str[i * 8 + 4], status, idKategori, array_str[i * 8 + 7]);
            tugastugas.put(idtugas, tugas);
        }

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
            hasil[i][6] = entry.getValue().idKategori;
            hasil[i][7] = entry.getValue().last_edit;
            i++;
        }

        return hasil;
    }

    public void addLog(int id_tugas, boolean new_status) {
        Log log;
        if (logs.containsKey(id_tugas)) {
            if (logs.get(id_tugas).status == new_status) {
//                logs.remove(id_tugas);
                log = new Log(id_tugas, new_status);
                logs.put(id_tugas, log);
            } else {
                log = new Log(id_tugas, new_status);
                logs.put(id_tugas, log);
            }
        } else {
            log = new Log(id_tugas, new_status);
            logs.put(id_tugas, log);
        }

        System.out.println("LOG : " + id_tugas + " " + new_status);

        String content = "" + id_tugas + " " + new_status + " " + log.last_edit.toLocaleString();

        File file = new File("/Log/" + user_id + ".log");
        if (!file.exists()) {
            FileWriter fw = null;
            try {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                PrintWriter fout = new PrintWriter(new BufferedWriter(new FileWriter("/Log/" + user_id)));
                fout.println(content);
                fout.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            }
        }

    }
}

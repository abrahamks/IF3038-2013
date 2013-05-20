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
import sun.security.action.GetBooleanAction;
/**
 *
 * @author Abraham Krisnanda
 */
public class ServerApp extends DBRecord{

    /**
     * @param args the command line arguments
     */
    static int current_id;
    public static void ProcessCommand() {
        String temp_cmd="";
        InputStreamReader key = new InputStreamReader (System.in);
        BufferedReader br = new BufferedReader (key);
        try {
            temp_cmd = br.readLine();
        }
        catch (Exception e) {
            System.out.println("Error reading line");
        }
        String[] cmd = temp_cmd.split(" ");
        switch(cmd[0]) {
            case "login":
                System.out.println("login");
                break;
            case "logout":
                System.out.println("logout");
        }
    }
    
    public static void main(String[] args) throws SQLException {
        DBRecord a = new DBRecord();
        int i = a.FindUser("admin2", "password");
        current_id = i;
        System.out.println(current_id);
        System.out.println((a.GetUserTask(i)).toString());
        
    }
}

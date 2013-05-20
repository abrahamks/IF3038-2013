/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;
import java.io.*;
import java.security.*;
import java.sql.*;
import java.util.*;
/**
 *
 * @author Abraham Krisnanda
 */
public class DBRecord {
    protected static Connection connection;
    
    public DBRecord()
    {
        connection = DBConnection.getConnection();
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
    
    public int FindUser(String username, String password) throws SQLException
    // input username dan password dalam MD5
    {
       String MD5temp="";
       String temp_username="";
       PreparedStatement ps0 = connection.prepareCall("SELECT username FROM user;");
       ResultSet rs0 = ps0.executeQuery();
       while (rs0.next())
       // matching username yang terenkripsi dengan MD5
       {
           MD5temp = MD5(rs0.getString(1));
           if (username.equals(MD5temp))
           {
               temp_username = rs0.getString(1);
           }
       }
       PreparedStatement ps = connection.prepareStatement("SELECT id_user FROM user WHERE username ='"+temp_username+"' AND password ='" +(password).toLowerCase()+ "';");
       ResultSet rs = ps.executeQuery();
       if (rs.next())
       {
            return rs.getInt("id_user");
       }
       else return -1;
    }
    
    public String GetUsername (int id_user) throws SQLException
    {
        PreparedStatement ps = connection.prepareStatement("SELECT username FROM user WHERE id_user = '" + id_user +"';");
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString("username");
        }
        else return null;
    }
    
    public ArrayList<String> GetAssignee (int id_task) throws SQLException
    // mengembalikan list of username dari assignee dari sebuah id_task
    {
        PreparedStatement ps = connection.prepareStatement("SELECT id_user FROM assign WHERE id_task = '" + id_task +"';");
        ResultSet rs = ps.executeQuery();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> out = new ArrayList<>();
        while (rs.next())
        {
            temp.add(GetUsername(rs.getInt("id_user")));
            //out.add(temp.toString().substring(0, (temp.toString().length())));
            //temp.clear();
        }
        return temp;
    }
    
    public ArrayList<Integer> GetTaskId (int id_user) throws SQLException
    // mengembalikan list of id_task yang dimiliki oleh seorang id_user
    {
        // Nama tugas, deadline, daftar assignee, tag, status, dan nama kategori dari tugas tersebut
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM assign WHERE id_user = '" + id_user +"';");
        ResultSet rs = ps.executeQuery();
        ArrayList<Integer> out = new ArrayList<>();
        while (rs.next())
        {
           out.add(rs.getInt("id_task"));
        }   
        return out;
    }
    
    public String GetTagName (int id_tag) throws SQLException
    {
        PreparedStatement ps = connection.prepareStatement("SELECT tag_name FROM tag WHERE id_tag ='"+ id_tag +"';");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) 
        {
            return rs.getString(1);
        }
        else return null;
    }
    
    public ArrayList<String> GetTags (int id_task) throws SQLException
    {
        PreparedStatement ps = connection.prepareStatement("SELECT id_tag FROM have_tags WHERE id_task ='" + id_task +"';");
        ResultSet rs = ps.executeQuery();
        ArrayList<String> out = new ArrayList<>();
        while (rs.next())
        {
            out.add(GetTagName(rs.getInt(1)));
        }
        return out;
    }
    
    public String GetNamaKategori (int id_kategori) throws SQLException
    {
        PreparedStatement ps = connection.prepareStatement("SELECT nama_kategori FROM kategori WHERE id_kategori ='" + id_kategori +"';");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString(1);
        }
        else return null;
    }
    
    public String GetTaskDetail (int id_task) throws SQLException
    // Nama tugas, deadline, daftar assignee, tag, status, dan nama kategori 
    {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM task WHERE id_task ='" + id_task +"';");
        ResultSet rs = ps.executeQuery();
        if (rs.next())
        {
            return 
            (
                // Full texts 	id_task 	nama_task 	status 	deadline 	id_kategori 	id_user 	last_edit 
                rs.getString("id_task") + "\n" +
                rs.getString("nama_task") + "\n" +
                rs.getString("deadline") + "\n" +
                GetAssignee(id_task).toString() + "\n" +
                GetTags(id_task).toString() + "\n" +
                rs.getString("status") + "\n" +
                GetNamaKategori(rs.getInt("id_kategori"))
            );
        }
        else return null;
    }
    
    public ArrayList<String> GetUserTask (int id_user) throws SQLException
    {
        PreparedStatement ps = connection.prepareStatement("SELECT id_task FROM assign WHERE id_user ='"+id_user +"';");
        ResultSet rs = ps.executeQuery();
        ArrayList<String> out = new ArrayList<>();
        while (rs.next())
        {
            out.add(GetTaskDetail(rs.getInt(1)));
        }
        return out;
    }
}

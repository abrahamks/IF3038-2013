/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progin.pkg5;

import java.util.Date;

/**
 *
 * @author LCF
 */
public class Tugas {
    public String namaTugas;
    public int idtugas;
    public String deadline;
    public String assignee;
    public String tags;
    public String last_edit;
    public boolean  status;
    public int idKategori;
    public boolean isSynced;
    public Date date_last_edit;
    
    public Tugas(int idtugas, String namaTugas, String deadline, String assignee, String tags, boolean status, String last_edit, boolean isSynced)
    {
        this.idtugas = idtugas;
        this.namaTugas = namaTugas;
        this.deadline = deadline;
        this.assignee = assignee;
        this.tags = tags;
        this.status = status;
        this.last_edit = last_edit;
        this.isSynced = isSynced;
        String temp = last_edit.replace("-", " ");
        temp = temp.replace(":", " ");
        temp = temp.replace(".0", "");
        String[] arr = temp.split(" ");
        int year = Integer.parseInt(arr[0])+1900;
        int month = Integer.parseInt(arr[1]) -1;
        int date = Integer.parseInt(arr[2]);
        int hour = Integer.parseInt(arr[3]);
        int minute = Integer.parseInt(arr[4]);
        int second = Integer.parseInt(arr[5]);
        date_last_edit = new Date(year, month, date, hour, minute, second);
    }
}

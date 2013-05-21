/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progin.pkg5;

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
    
    public Tugas(int idtugas, String namaTugas, String deadline, String assignee, String tags, boolean status, int idKategori, String last_edit)
    {
        this.idtugas = idtugas;
        this.namaTugas = namaTugas;
        this.deadline = deadline;
        this.assignee = assignee;
        this.tags = tags;
        this.status = status;
        this.idKategori = idKategori;
        this.last_edit = last_edit;
    }
}

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
    private String namaTugas;
    private int idtugas;
    private String assignee;
    private String tags;
    private boolean  status;
    
    public Tugas(int idtugas, String namaTugas, String deadline, String assignee, String tags, boolean status, int idKategori)
    {
        this.idtugas = idtugas;
        this.namaTugas = namaTugas;
        this.assignee = assignee;
        this.tags = tags;
        this.status = status;
    }
}

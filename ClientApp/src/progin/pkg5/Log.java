/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progin.pkg5;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author LCF
 */
public class Log {
    public int id_tugas;
    public boolean  status;
    public Date last_edit;
    
    public Log(int id_tugas, boolean  status) {
        this.id_tugas = id_tugas;
        this.status = status;
        this.last_edit = new Date();
    }
    
//
//    public void addLog(int id_tugas, boolean status) {
//        if (logs.containsKey(id_tugas)) {
//            if (logs.get(id_tugas) == status) {
//                logs.remove(id_tugas);
//            } else {
//                logs.put(id_tugas, status);
//            }
//        } else {
//            logs.put(id_tugas, status);
//        }
//    }
}

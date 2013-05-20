/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progin.pkg5;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author LCF
 */
public class Log {

    public Map<Integer, Boolean> logs;

    public Log() {
        logs = new TreeMap<>();
    }

    public void addLog(int id_tugas, boolean status) {
        if (logs.containsKey(id_tugas)) {
            if (logs.get(id_tugas) == status) {
                logs.remove(id_tugas);
            } else {
                logs.put(id_tugas, status);
            }
        } else {
            logs.put(id_tugas, status);
        }
    }
}

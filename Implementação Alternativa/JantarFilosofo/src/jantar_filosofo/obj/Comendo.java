/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar_filosofo.obj;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jantar_filosofo.obj.thread.Filosofo;

/**
 *
 * @author Mauricio R. Vidal
 */
public class Comendo {

    private ConcurrentHashMap<Integer, Filosofo> map = new ConcurrentHashMap<Integer, Filosofo>(2);

    public int quantidade() {
        return map.size();
    }

    public Filosofo getFilosofo() {
        for (Filosofo f : map.values()) {
            return f;
        }
        return null;
    }

    public synchronized void addFilosofo(Filosofo f, int numFilosofo) {
        while (quantidade() == 2 ||!f.quemCome()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Comendo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        map.put(f.getNumFilosofo(), f);
    }

    public synchronized void retirarFilosofo(Filosofo f) {
        map.remove(f.getNumFilosofo());
        notify();
    }
}
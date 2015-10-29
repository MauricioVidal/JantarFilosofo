/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar.atomico;

/**
 *
 * @author a14014
 */
public class Semaforo {

    public int estado;

    public Semaforo(int estado) {
        this.estado = estado;
    }

    public synchronized void down() throws InterruptedException {
        synchronized (this) {
            while (this.estado == 0) {
                wait();
            }
            this.estado--;
        }
    }

    public synchronized void up() {
        synchronized (this) {
            this.estado++;
            this.notifyAll();
        }
    }
}

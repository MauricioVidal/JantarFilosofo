/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar_filosofo.obj.thread;

import jantar_filosofo.Main;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import jantar_filosofo.obj.Comendo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mauricio R. Vidal
 */
public class Filosofo implements Runnable {

    private static final Random rand = new Random();
    private int numFilosofo;
    private List<Float> tempoSemComer = new ArrayList<Float>();
    private int estado = 1; // 0-Comendo/ 1-Pensando/ 2-Com Fome
    private Comendo comendo;
    private Long tempoMaxSemComer = 0L;

    public Filosofo(int numFilosofo, Comendo comendo) {
        this.numFilosofo = numFilosofo;
        this.comendo = comendo;
        new Thread(new TempoSemComer()).start();
    }

    public void pensar() {
        try {
            estado = 1;
            int t = rand.nextInt(401) + 100;
            Thread.sleep(t);
            estado = 2;
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void comer() {
        try {
            comendo.addFilosofo(this, numFilosofo);
            estado = 0;
            int t = rand.nextInt(401) + 100;
            Thread.sleep(t);
            comendo.retirarFilosofo(this);
            estado = 1;
        } catch (InterruptedException ex) {
            Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized boolean quemCome() {
        Filosofo f = comendo.getFilosofo();
        if (f == null) {
            return true;
        }
        return (f.numFilosofo + 2) % 5 == numFilosofo || (f.numFilosofo + 3) % 5 == numFilosofo;
    }
    
    @Override
    public void run() {
        Long tempoInicioSemComer=0L, tempoSemComerAtual;
        while (!Main.stop) {
            if (estado != 2) {
                pensar();
                tempoInicioSemComer=System.currentTimeMillis();
            }
            if (comendo.quantidade() == 0 || (comendo.quantidade() == 1 && quemCome())) {
                tempoSemComerAtual= System.currentTimeMillis() - tempoInicioSemComer;
                System.out.println("O filosofo " + numFilosofo + " ficou sem comer por " + tempoSemComerAtual + " milissegundos");
                comer();
                if(tempoSemComerAtual > tempoMaxSemComer){
                    tempoMaxSemComer = tempoSemComerAtual;
                }
            }
                
        }
    }

    public int getNumFilosofo() {
        return numFilosofo;
    }

    private float getMediaDeEspera() {
        int cont = 0;
        float soma = 0;
        for (Float s : tempoSemComer) {
            soma += s;
            cont++;
        }
        return soma / cont;
    }

    public void mostrarTempo(){
        System.out.println("\nFilosofo "+ numFilosofo + ":");
        int cont=1;
        for(Float t : tempoSemComer) cont++;
        System.out.println("Comeu "+ cont +" vezes");
        System.out.printf("Media de espera: %.3f segundos\n", getMediaDeEspera());
        System.out.printf("Tempo Máximo de Espera: %.3f segundos\n", ((double) tempoMaxSemComer / 1000));
    }
    
    private class TempoSemComer implements Runnable {

        private float  t = 0;
        
        @Override
        public void run() {
            while (!Main.stop) {
                if (estado != 0) {
                    try {
                        t += 0.001;
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                }else{
                    if(t != 0){
                        tempoSemComer.add(t);
                    }
                    t=0;
                }
            }
        }

    }
    public int getEstado() {
        return estado;
    }
}

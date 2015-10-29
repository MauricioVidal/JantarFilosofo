/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar.atomico;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author a14014
 */
public class Filosofo implements Runnable {

    private final int THINKING = 0;
    private final int HUNGRY = 1;
    private final int EATING = 2;

    private int i; /*id de cada filósofo*/

    private int esquerda, direita;
    public int refeicoes;
    private Random rand = new Random();
    int tempo;
    private double tMaximoEspera;
    private Long tempoMaxSemComer = 0L;

    private List<Double> listaDeEspera = new ArrayList<Double>();

    private class Contagem implements Runnable {

        private double tEspera;

        @Override
        public void run() {

            while (!TesteJantarFilosofico.parada) {
                if (Mesa.arrayState[i] != EATING) {
                    try {
                        tEspera += 0.001;
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    //System.out.println("-");
                    if (tEspera != 0) {
                        listaDeEspera.add(tEspera);

                    }
                    tEspera = 0;

                }
            }
        }

    }

    private double getMedia() {
        double aux = 0;
        int cont = 0;

        for (double s : listaDeEspera) {
            if (tMaximoEspera <= s) {
                tMaximoEspera = s;
            }
            aux += s;
            cont++;
        }
        refeicoes = cont;
        return aux / cont;
    }

    public void relatorio() {
        System.out.println("\nFilósofo " + i + ":");
        System.out.println("Comeu: " + refeicoes + " vezes");
        System.out.printf("Média de Espera: %.3f segundos\n", getMedia());
        System.out.printf("Tempo Máximo de Espera: %.3f segundos\n", ((double) tempoMaxSemComer / 1000));
    }

    public Filosofo(int i, int state) {
        this.i = i;
        esquerda = (i + Mesa.N - 1) % Mesa.N;
        direita = (i + 1) % Mesa.N;
        Mesa.arrayState[i] = state;
        Mesa.arraySemaforo[i] = new Semaforo(1);

        new Thread(new Contagem()).start();
    }

    private synchronized void pensar() throws InterruptedException {
        tempo = rand.nextInt(401) + 100;
        Thread.sleep(tempo);
    }

    private void pegarGarfos(int i) throws InterruptedException {
        Mesa.mutex.down();
        Mesa.arrayState[i] = HUNGRY;
        test(i);
        Mesa.mutex.up();
        Mesa.arraySemaforo[i].down();
    }

    private void largarGarfos(int i) throws InterruptedException {
        Mesa.mutex.down();
        Mesa.arrayState[i] = THINKING;
        test(esquerda);
        test(direita);
        Mesa.mutex.up();
    }

    private void test(int i) {
        if (Mesa.arrayState[i] == HUNGRY && Mesa.arrayState[(i + Mesa.N - 1) % Mesa.N] != EATING && Mesa.arrayState[(i + 1) % Mesa.N] != EATING) {
            Mesa.arrayState[i] = EATING;
            refeicoes++;
            Mesa.arraySemaforo[i].up();
        }
    }

    private synchronized void comer() throws InterruptedException {
        tempo = rand.nextInt(401) + 100;
        Thread.sleep(tempo);
    }

    @Override
    public void run() {
        Long tempoInicioSemComer = 0L, tempoSemComerAtual;
        while (!TesteJantarFilosofico.parada) {
            try {
                pensar();
                tempoInicioSemComer = System.currentTimeMillis();
                pegarGarfos(i);
                tempoSemComerAtual = System.currentTimeMillis() - tempoInicioSemComer;
                System.out.println("O filosofo " + i + " ficou sem comer por " + tempoSemComerAtual + " milissegundos");
                comer();
                largarGarfos(i);
                if (tempoSemComerAtual > tempoMaxSemComer) {
                    tempoMaxSemComer = tempoSemComerAtual;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getEstado() {
        return Mesa.arrayState[i];
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jantar.atomico;

/**
 *
 * @author Hugo
 */
public class Mesa {

    public final static int N = 5;

    public Filosofo[] f = new Filosofo[N];
    public static Semaforo mutex = new Semaforo(1);
    public static int[] arrayState = new int[N];
    public static Semaforo[] arraySemaforo = new Semaforo[N];

    public Mesa() {
        for (int i = 0; i < N; i++) {
            f[i] = new Filosofo(i, 0);
        }
    }
}

package jantar.atomico;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/////////////////////////////////////////////////////
//                                                 //
//  Grupo:                                         //
//  Hugo Luiz Camargo Pinto - 2014.1.08.014        //
//  Marcelo Antonio Mendes Bastos - 2014.1.08.046  //
//  Mauricio Roque Vidal - 2014.1.08.020           //
//  Monica de Cassia P. Roncada - 2014.1.08.021    //
//                                                 //
/////////////////////////////////////////////////////

public class TesteJantarFilosofico {

    public static int[] arrayState = new int[Mesa.N];
    public static boolean parada;
    private static Mesa mesa;

    private static HashMap<Integer, String> status = new HashMap<>();
    static {
        status.put(0, "Pensando");
        status.put(1, "Com Fome");
        status.put(2, "Comendo");
    }

    public static synchronized void criaTabela() {
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.printf("%s %10s %10s %10s %10s\n", "F0", "F1", "F2", "F3", "F4");
        System.out.printf("%s %10s %10s %10s %10s\n", status.get(mesa.f[0].getEstado()), status.get(mesa.f[1].getEstado()), status.get(mesa.f[2].getEstado()), status.get(mesa.f[3].getEstado()), status.get(mesa.f[4].getEstado()));
        System.out.println("-----------------------------------------------------------------------\n");
    }

    public static void main(String[] args) throws InterruptedException /*throws InterruptedException */ {
        int i;
        mesa = new Mesa();

        ExecutorService executor = Executors.newCachedThreadPool();
        for (i = 0; i < Mesa.N; i++) {
            executor.execute(mesa.f[i]);
        }
        
        new Thread() {

            @Override
            public void run() {
                while (!parada) {     
                    try {
                        criaTabela();
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TesteJantarFilosofico.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }

        }.start();

        executor.shutdown();
        Thread.sleep(180 * 1000);
        parada = true;
        Thread.sleep(2 * 1000);

        for (i = 0; i < Mesa.N; i++) {
            mesa.f[i].relatorio();
        }
    }
}

package jantar_filosofo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import jantar_filosofo.obj.Comendo;
import jantar_filosofo.obj.thread.Filosofo;
import java.util.HashMap;

/////////////////////////////////////////////////////
//                                                 //
//  Grupo:                                         //
//  Hugo Luiz Camargo Pinto - 2014.1.08.014        //
//  Marcelo Antonio Mendes Bastos - 2014.1.08.046  //
//  Mauricio Roque Vidal - 2014.1.08.020           //
//  Monica de Cassia P. Roncada - 2014.1.08.021    //
//                                                 //
/////////////////////////////////////////////////////

public class Main {
    
    public static boolean stop = false;
    private static final int SEGUNDOS = 1000;
    private static Filosofo f0;
    private static Filosofo f1;
    private static Filosofo f2;
    private static Filosofo f3;
    private static Filosofo f4 ;
    
    private static HashMap<Integer, String> status= new HashMap<>();
    static{
        status.put(1, "Pensando");
        status.put(2, "Com Fome");
        status.put(0, "Comendo");
    }
    
    
    public static synchronized void criaTabela(){
        System.out.println("\n-----------------------------------------------------------------------");
        System.out.printf("%s %10s %10s %10s %10s\n","F0", "F1", "F2", "F3", "F4");
        System.out.printf("%s %10s %10s %10s %10s\n",status.get(f0.getEstado()), status.get(f1.getEstado()), status.get(f2.getEstado()), status.get(f3.getEstado()), status.get(f4.getEstado()));
        System.out.println("-----------------------------------------------------------------------\n");
    }
    
    public static void main(String[] args) {
        try {
            ExecutorService application = Executors.newCachedThreadPool();
            
            Comendo comendo = new Comendo();
            
            f0 = new Filosofo(0, comendo);
            f1 = new Filosofo(1, comendo);
            f2 = new Filosofo(2, comendo);
            f3 = new Filosofo(3, comendo);
            f4 = new Filosofo(4, comendo);
            
            application.execute(f0);
            application.execute(f1);
            application.execute(f2);
            application.execute(f3);
            application.execute(f4);
            
            new Thread(){

                @Override
                public void run() {
                    while (!stop) {
                        try {
                            criaTabela();
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
            }.start();
            
            application.shutdown();
            
            Thread.sleep(180*SEGUNDOS);
            stop = true;
            Thread.sleep(2*SEGUNDOS);
            
            f0.mostrarTempo();
            f1.mostrarTempo();
            f2.mostrarTempo();
            f3.mostrarTempo();
            f4.mostrarTempo();
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
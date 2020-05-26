

import java.text.SimpleDateFormat;
import java.util.Date;


public class Principal {

    public static void main(String[] args) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");

        System.out.println("Sistemas Distribuidos - APS");
        System.out.println("Aluna: Janiele Nogueira");
        System.out.println("Legenda: (C) - Coordenador");
        System.out.println();
        System.out.println("Algoritmo de Bully(valentão)");
        System.out.println();

        // Criar Cluster
        Cluster cluster = new Cluster();

        // Criar processos iniciais de exemplo
        Processo p1 = new Processo(cluster, true);
        cluster.setCoordenador(p1);
        System.out.println(formato.format(new Date()) + " - Criando processo com PID: " + p1.getId());
        System.out.println(cluster.toStringProcessos());
        System.out.println("------------");
        System.out.println();
        
       //Thread responsável por criar novos processos
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    Util.delay(Config.DELAY_CRIAR_PROCESSO);
                    Processo p = new Processo(cluster, true);
                    System.out.println(formato.format(new Date()) + " - Criando processo com pid " + p.getId());
                    System.out.println(cluster.toStringProcessos());
                    System.out.println("------------");
                    System.out.println();
                }
            }
        }).start();

      //Thread responsável por verificar se o coordenador está vivo
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    Util.delay(Config.DELAY_EXECUCAO);
                    cluster.getCoordenadorVivo();
                }
            }
        }).start();
        
      //Thread responsável por matar o coordenador
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    Util.delay(Config.DELAY_MATAR_COORDENADOR);
                    cluster.matarCoordenador();
                }
            }
        }).start();
        

    }

}

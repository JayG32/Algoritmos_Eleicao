

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Processo {

    //Vari�vel que define se o processo est� em execu��o ou n�o
    private boolean vivo = true;

    //Respons�vel por formatar data e hora
    private SimpleDateFormat formato;
    
   
     //PID - Identificador do processo
    private int id;
    
    //Refer�ncia ao cluster
    private Cluster cluster = null;

    //Construindo processo
    public Processo(Cluster cluster) {
        setCluster(cluster);
        cluster.addProcesso(this);
        gerarId();
        formato = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
    }
    
  //Contruir processo com controle de execu��o no construtor
    public Processo(Cluster cluster, boolean iniciarExecucao){
        this(cluster);
    }
    
   //Elei��o pra decidir o novo coordenador
    public synchronized void eleicao() {
        List<Processo> processosVivos = new ArrayList<>();
        for(Processo p : getCluster().getListaProcessos()) {
            if(p.getId() > getId()) {
                boolean pong = ping(p);
                if(pong) {
                    processosVivos.add(p);
                }
            }
        }
        if(processosVivos.isEmpty()) {
            getCluster().setCoordenador(this);
            atualizarCoordenador(this);
            getCluster().setEleicaoAtiva(false);
            return;
        }
        processosVivos.stream().forEach(Processo::eleicao);
    }
    
   //M�todo respons�vel por atualizar novo coordenador no cluster
    public synchronized void atualizarCoordenador(Processo novoCoordenador) {
        System.out.println(formato.format(new Date()) + " - Novo coordenador com o PID: " + novoCoordenador.getId());
        getCluster().setCoordenador(novoCoordenador);
    }
    
   //Executando ping no processo
    public boolean ping(Processo processo) {
        return processo.pong();
    }
    
    //Resposta do ping
    public boolean pong() {
        return isVivo();
    }
    
    
    //Gerar ID para o processo. O ID n�o � gerado aleatoriamente e n�o ser� igual a um ID existente.
   
    public void gerarId() {
        boolean idValido = false;
        int id = 0;
        do {
            idValido = true;
            Random random = new Random();
            id = random.nextInt(Config.ID_MAX_PROCESSO);
            for(Processo p : getCluster().getListaProcessos()) {
                if(p.getId() == id) {
                    idValido = false;
                }
            }
        } while(!idValido);
        setId(id);
    }
    
    //Retorna lista de processos do tipo string
    public String toStringListaProcessos() {
        String resultado = "[";
        String virgula = "";
        for(Processo p : getCluster().getListaProcessos()) {
            resultado += virgula + p.getId();
            virgula = ", ";
        }
        return resultado + "]";
    }
    
   //Verificando se o coordenador est� vivo
    public boolean verificarCoordenadorVivo() {
        System.out.println(formato.format(new Date()) + " - Processo verificando se o coordenador est� vivo.");
        return getCluster().getCoordenador().isVivo();
    }
    
    @Override
    public String toString() {
        return "Processo " + getId() + (isCoordenador() ? "\t(C)" : "\t( )");
    }

    //Getters e setters *********************************************************************************************
    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public boolean isCoordenador() {
        return getCluster().getCoordenador() == this;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
}



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


//Cluster com lista de processos e controle de eleição.
 
public class Cluster {

   //Lista de processos volátil
    private volatile List<Processo> listaProcessos;
    
   //Variável de controle de eleição
    private volatile boolean eleicaoAtiva = false;

   //Variável que controla o formato da data
    private SimpleDateFormat formato;
    
    //Variável com referência para o coordendor
    private volatile Processo coordenador = null;
    
    //Contruindo o cluster
    public Cluster() {
    	//CopyOnWriteArrayList<> = Faz uma cópia de segurança antes de modificar ou remover algum item da lista. Nativo do JavaUtil 
        listaProcessos = new CopyOnWriteArrayList<>();
        formato = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
    }
    
   
    //Métodod para obter quantidade de registros vivos na lista de processos
     
    public int getQtdProcessosVivos() {
        int contagem = 0;
        for(Processo p : getListaProcessos()) {
            if(p.isVivo()) {
                contagem++;
            }
        }
        return contagem;
    }

    ///Métodod para matar processo coordenador
    public void matarCoordenador() {
        coordenador.setVivo(false);
        System.out.println(formato.format(new Date()) + " - Matando coordenador");
        System.out.println();
        System.out.println("------------");
        System.out.println();
    }
    
    //Verifica se tem mais de um processo rodando  
    public void getCoordenadorVivo() {
        boolean notifyed = false;
        if (getListaProcessos().size() >= 2) {
            do {
                Processo p = getListaProcessos().get(new Random().nextInt(getListaProcessos().size()));
                if (!p.isCoordenador()) {
                    if (!p.verificarCoordenadorVivo()) {
                        if (!isEleicaoAtiva()) {
                            System.out.println(formato.format(new Date()) + " - Iniciando o processo de eleição.");
                            setEleicaoAtiva(true);
                            p.eleicao();
                        }
                    }
                    notifyed = true;
                }
            } while (!notifyed);
        }
    }
    
    
   ///Métodod para adicionar processo
    public void addProcesso(Processo p) {
        getListaProcessos().add(p);
    }
    

   //Imprime status dos processos no formato string
    public String toStringProcessos() {
        String resultado = "";
        for(Processo p : getListaProcessos()) {
            if(p.isVivo()) {
                resultado += p + "\n";
            }
        }
        return resultado;
    }

    //Getters e Setters ******************************************************************************************************
    public List<Processo> getListaProcessos() {
        return listaProcessos;
    }

    public void setListaProcessos(List<Processo> listaProcessos) {
        this.listaProcessos = listaProcessos;
    }

    public Boolean isEleicaoAtiva() {
        return eleicaoAtiva;
    }

    public void setEleicaoAtiva(Boolean eleicaoAtiva) {
        this.eleicaoAtiva = eleicaoAtiva;
    }

    public Processo getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Processo coordenador) {
        this.coordenador = coordenador;
    }
}

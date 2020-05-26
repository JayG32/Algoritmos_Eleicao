

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Anel {
	
	
	private final int ADICIONA = 25000; //A cada 25 segundos um novo processo é criado
	private final int REQUISICAO = 20000;//A cada 20 segundos uma requisição é feita ao coordenador 
	private final int INATIVO_COORDENADOR = 70000;//A cada 100 segundos o coordenador é morto
//	private final int INATIVO_PROCESSO = 80000;

	public static ArrayList<Processo> processosAtivos; //Array estatico de processos ativos que analisa todos os processos em funcioanamento
	private final Object lock = new Object(); //objeto de controle das threads
	
	//Variável que controla o formato da data
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");;

	public Anel() {
		processosAtivos = new ArrayList<Processo>();
	}
	
	
	//Método responsável por criar processos com uma thread rodando em paralelo
	//lock foi utilizado em cada uma das threads pra que elas não acessem a lista de processos ativos ao mesmo tempo
	public void criaProcessos () {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					synchronized (lock) {
						//antes de criar um processo primeiro verifica se a lista de processos está vazia. Caso vazia , cria o processo e ele já se torna coordenador 
						if (processosAtivos.isEmpty()) {
							processosAtivos.add(new Processo(1, true));
						} else {
							processosAtivos.add(new Processo(
									processosAtivos.get(processosAtivos.size() - 1).getPid() + 1, false));
						}
						
						System.out.println(formato.format(new Date()) + " * Criando Processo " + processosAtivos.get(processosAtivos.size() - 1).getPid());
					}
					
					//A thread entra em hibernação pelo tempo especificado e após isso é executada novamente
					try {
						Thread.sleep(ADICIONA);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	//Método responsável por fazer requisições com uma thread rodando em paralelo
	public void fazRequisicoes () {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					//Inicia em hibernação. Verifica se tem algum processo ativo e seleciona um processo aleatório e chama o método enviarRequisição()
					try {
						Thread.sleep(REQUISICAO);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					synchronized (lock) {
						if (processosAtivos.size() > 0) {
							int indexProcessoAleatorio = new Random().nextInt(processosAtivos.size());
														
							Processo processoRequisita = processosAtivos.get(indexProcessoAleatorio);
							System.out.println(formato.format(new Date()) + " * Processo " + processoRequisita.getPid() + " fazendo requisicao.");
							processoRequisita.enviarRequisicao();
						}
					}
				}
			}
		}).start();
	}
	
//	public void inativaProcesso () {
//		new Thread(new Runnable() {
//			public void run() {
//				while (true) {
//					//A thread inicia de forma inativa
//					try {
//						Thread.sleep(INATIVO_PROCESSO);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//					synchronized (lock) {
//						//Checa se a lista não está vazia. Caso não esteja é escolhido um processo aletatório e se não for nulo nem o coordenador é removido da lista de processos s
//						if (!processosAtivos.isEmpty()) {
//							int indexProcessoAleatorio = new Random().nextInt(processosAtivos.size());
//							Processo pRemover = processosAtivos.get(indexProcessoAleatorio);
//							if (pRemover != null && !pRemover.isCoordenador()){
//								processosAtivos.remove(pRemover);
//								System.out.println(formato.format(new Date()) +  " ** Matando Processo "+ pRemover.getPid() + " ** ");
//								System.out.println();
//							}
//						}
//					}
//				}
//			}
//		}).start();
//	}
	
	
	//Método responsável por inativar coordenador com uma thread rodando em paralelo
	public void inativaCoordenador () {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(INATIVO_COORDENADOR);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					synchronized (lock) {

						Processo coordenador = null;
						for (Processo p : processosAtivos) {
							if (p.isCoordenador()) {
								coordenador = p;
							}
						}
						if (coordenador != null){
							processosAtivos.remove(coordenador);
							System.out.println();
							System.out.println(formato.format(new Date()) + " ** Matando Processo  " + coordenador.getPid() + " (C) **");
							System.out.println();
						}
					}
				}
			}
		}).start();
	}
}

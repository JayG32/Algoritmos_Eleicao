

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Processo {
	
	//Atributos
	private int pid;
	private boolean coordenador;
	
	//Variável que controla o formato da data
	 private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");;
	
	
	public Processo (int pid){
		setPid(pid);
	}
	
	public Processo (int id, boolean coordenador){
		setPid(id);
		setCoordenador(coordenador);
	}
	
	public boolean isCoordenador () {
		return coordenador;
	}

	public void setCoordenador (boolean coordenador) {
		this.coordenador = coordenador;
	}

	public int getPid () {
		return pid;
	}
	
	public void setPid (int pid) {
		this.pid = pid;
	}
	
	//Cria uma variável booleana. Percorre a lista de processos ativos a procura do coordenador caso encontre o metodo receber requisição é chamado e o id do processo é passado e armazenado dentro da variavel resultadoRequisicao 
	public boolean enviarRequisicao () {
		boolean resultadoRequisicao = false;
		for (Processo p : Anel.processosAtivos) {
			if (p.isCoordenador())
				resultadoRequisicao = p.receberRequisicao(this.pid);
		}
		
		// Se o coordenador não for encontrado(retorno FALSE ). é realizada a eleição 
		if (!resultadoRequisicao)
			this.realizarEleicao();
		
		System.out.println("------------------------------------------------");
		System.out.println(formato.format(new Date()) + " Fim da requisicao.");
		System.out.println("------------------------------------------------");
		System.out.println();
		return resultadoRequisicao;
	}
	
	private boolean receberRequisicao (int pidOrigemRequisicao) {
		
		/* TRATAMENTO DA REQUISICAO AQUI... */
		
		System.out.println(formato.format(new Date()) + " * Requisicao do processo " + pidOrigemRequisicao + " recebida com sucesso.");
		return true;
	}
	
	private void realizarEleicao () {
		System.out.println();
		System.out.println(formato.format(new Date()) + " * Processo de eleicao iniciado");
		System.out.println();
		
		// Primeiro consulta cada processo, adicionando o id de cada um em uma nova lista.
		
		LinkedList<Integer> idProcessosConsultados = new LinkedList<>();
		for (Processo p : Anel.processosAtivos)
			p.consultarProcesso(idProcessosConsultados);
		
		// Depois percorre a lista de id's procurando pelo maior.
		 
		int idNovoCoordenador = this.getPid();
		for (Integer id : idProcessosConsultados) {
			if (id > idNovoCoordenador)
				idNovoCoordenador = id;
		}
		
		// E entao atualiza o novo coordenador.
		
		boolean resultadoAtualizacao = false;
		resultadoAtualizacao = atualizarCoordenador(idNovoCoordenador);
		
		if (resultadoAtualizacao)
			System.out.println(formato.format(new Date()) + " * Eleicao concluida com sucesso. O novo coordenador é " + idNovoCoordenador + ".");
		else
			System.out.println(formato.format(new Date()) + " * A eleicao falhou. Nao foi encontrado um novo coordenador.");
	}
	
	private void consultarProcesso (LinkedList<Integer> processosConsultados) {
		processosConsultados.add(this.getPid());
	}
	
	private boolean atualizarCoordenador (int idNovoCoordenador) {
		// Garante que nao exista nenhum outro processo cadastrado como coordenador,a nao ser o novo coordenador.
		
		for (Processo p : Anel.processosAtivos) {			
			if (p.getPid() == idNovoCoordenador)
				p.setCoordenador(true);
			else
				p.setCoordenador(false);
		}
		
		return true;
	}
}
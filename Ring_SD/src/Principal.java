

public class Principal {
	
	public static void main(String[] args) {
		System.out.println("Sistemas Distribuidos - APS");
	    System.out.println("Aluna: Janiele Nogueira");
	    System.out.println("Legenda: (C) - Coordenador");
	    System.out.println();
        System.out.println("Algoritmo Logical Ring(Anel lógico)");
        System.out.println();
        
		Anel anelLogico = new Anel();
		
		anelLogico.criaProcessos();
		
		anelLogico.fazRequisicoes();
		
		anelLogico.inativaCoordenador();
		
//		anelLogico.inativaProcesso();
	}

}

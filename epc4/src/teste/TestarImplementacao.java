package teste;

import classes.NeuronioPrimeiraCamada;

/**
 * created by lgcaobianco on 2018-04-29
 */

public class TestarImplementacao {
	public static void main(String[] args) {
		NeuronioPrimeiraCamada neuronio = new NeuronioPrimeiraCamada();
		neuronio.imprimirMatrizPesos();
		neuronio.imprimirMatrizInputs();
		neuronio.imprimirMatrizI();
		neuronio.calcularMatrizI(0);
		neuronio.imprimirMatrizI();
		
	}
}

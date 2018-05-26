package teste;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import classes.RBFDinamica;

/**
 * created by lgcaobianco on 2018-05-26
 */

public class TestarRBFDinamica {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		RBFDinamica rbf = new RBFDinamica();
		double epsilon = Math.pow(10, -7);

		rbf.primeiroEstagioTreinamento();
		
		rbf.imprimirGrupoOmega();
		
		rbf.imprimirQualquer(rbf.getCentroide());
		/*
		 * rbf.primeiroEstagioTreinamento(); int contadorEpocas = 0;
		 * System.out.println("centroide final");
		 * rbf.imprimirQualquer(rbf.getCentroide());
		 * System.out.println("grupo da centroide 1"); rbf.imprimirGrupoCentroide1();
		 * rbf.imprimirGrupoCentroide2();
		 * 
		 * for (int i = 0; i < rbf.getConjuntoEntrada().size(); i++) { rbf.obterG(i); }
		 * 
		 * System.out.println("Vetor G: "); rbf.imprimirQualquer(rbf.getVetorG());
		 * 
		 * double erroAnterior = 1, erroAtual = 0; System.out.println("W2 inicial");
		 * rbf.imprimirQualquer(rbf.getW2());
		 * 
		 * while (Math.abs(erroAtual - erroAnterior) > epsilon) { for (int i = 0; i <
		 * rbf.getConjuntoEntrada().size(); i++) { rbf.propagation(i); } erroAnterior =
		 * erroAtual; erroAtual = rbf.obterEm(); contadorEpocas++; }
		 * System.out.println(contadorEpocas); System.out.println("W2: ");
		 * rbf.imprimirQualquer(rbf.getW2());
		 * 
		 * rbf.faseOperacao();
		 */
	}
}

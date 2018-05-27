package teste;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

		int contadorEpocas = 0;
		System.out.println("centroide final");
		rbf.imprimirQualquer(rbf.getCentroide());

		for (int i = 0; i < rbf.getConjuntoEntrada().size(); i++) {
			rbf.obterG(i);
		}

		double erroAnterior = 1, erroAtual = 0;
		System.out.println("W2 inicial");
		rbf.imprimirQualquer(rbf.getW2());
		PrintWriter writer = new PrintWriter("/home/lgcaobianco/repositorios/epc-rna/epc8/src/base/eqm.csv", "UTF-8");
		while (Math.abs(erroAtual - erroAnterior) > epsilon) {
			for (int i = 0; i < rbf.getConjuntoEntrada().size(); i++) {
				rbf.propagation(i);
			}
			erroAnterior = erroAtual;
			erroAtual = rbf.obterEm();
			writer.println(contadorEpocas + "," + (erroAtual - erroAnterior));
			contadorEpocas++;
		}
		writer.close();
		System.out.println(contadorEpocas);
		System.out.println("W2: ");
		rbf.imprimirQualquer(rbf.getW2());

		rbf.faseOperacao();

	}
}

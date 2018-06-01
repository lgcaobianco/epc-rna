package teste;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import classes.MLP3;

/**
 * created by lgcaobianco on 2018-05-20
 */

public class TreinarMLP {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		MLP3 mlp = new MLP3(4, 10);
		double epsilon = 0.5 * Math.pow(10, -6);
		int contadorEpocas = 0;
		double erroAnterior = 20;
		double erroAtual = 1;
		PrintWriter writer = new PrintWriter(
				"/home/lgcaobianco/repositorios/epc-rna/epc6/src/base/eqm.csv", "UTF-8");
		while (Math.abs(erroAtual - erroAnterior) > epsilon) {
			for (int i = 0; i < mlp.getMatrizInputs().size(); i++) {
				mlp.forwardPropagation(i);
				mlp.backwardPropagation(i);
				mlp.forwardPropagation(i);
				
			}
			erroAnterior = erroAtual;
			erroAtual = mlp.calcularEm();
			writer.println(contadorEpocas+","+Math.abs(erroAtual-erroAnterior));
			contadorEpocas++;
		}
		System.out.println(contadorEpocas);
		writer.close();
		mlp.imprimirQualquer(mlp.getW1());

		mlp.imprimirQualquer(mlp.getW2());
		
		for(int i=99; i<120; i++) {
			mlp.obterNovaAmostra(i);
		}
		
		
	}
}

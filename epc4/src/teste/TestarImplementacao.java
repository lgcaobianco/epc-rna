package teste;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import classes.MultiLayerPerceptron;

/**
 * created by lgcaobianco on 2018-04-29
 */

public class TestarImplementacao {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		MultiLayerPerceptron mlp = new MultiLayerPerceptron();
		double epsilon = Math.pow(10, -6);
		int contadorEpocas = 0;
		double erroAnterior = 10.0;
		double erroAtual = 0;
		PrintWriter writer = new PrintWriter("/home/lgcaobianco/repositorios/epc-rna/epc4/src/base/eqm.csv", "UTF-8");

		// fase de treino
		while (Math.abs((erroAtual - erroAnterior)) > epsilon) {
			for (int i = 0; i < mlp.getMatrizInputs().size(); i++) {
				mlp.forwardPropagation(i);
				mlp.backwardPropagation(i);
				mlp.forwardPropagation(i);
			}
			
			erroAnterior = erroAtual;
			erroAtual = mlp.calcularErroTotal();
			writer.println(contadorEpocas+","+erroAtual);
			contadorEpocas++;
		}
		writer.close();
		System.out.println(contadorEpocas);
		// trocar a matriz para o conjunto de operacao
		mlp.setMatrizInputs(mlp.getMatrizOperacao());

		double erroRelativoMedio = 0.0, variancaErroRelativo = 0.0, erroRelativo;
		for (int i = 0; i < mlp.getMatrizOperacao().size(); i++) {
			mlp.forwardPropagation(i);
			erroRelativoMedio += (mlp.calcularErroRelativo(i) / mlp.getMatrizInputs().size());
			mlp.imprimirY2();
		}
		for (int i = 0; i < mlp.getMatrizOperacao().size(); i++) {
			mlp.forwardPropagation(i);
			erroRelativo = mlp.calcularErroRelativo(i);
			variancaErroRelativo += Math.pow(Math.abs(erroRelativo/100 - erroRelativoMedio/100), 2)/mlp.getMatrizInputs().size();
		}
		System.out.println("Erro relativo medio: " + erroRelativoMedio);
		System.out
				.println("Variancia do erro relativo médio: " + variancaErroRelativo*100);

	}
}

package teste;

import classes.MultiLayerPerceptron;

/**
 * created by lgcaobianco on 2018-04-29
 */

public class TestarImplementacao {
	public static void main(String[] args) {
		MultiLayerPerceptron mlp = new MultiLayerPerceptron();
		double epsilon = Math.pow(10, -6);
		int contadorEpocas = 0;
		double erroAnterior = 10.0;
		double erroTotal = 5.0;

		while (Math.abs(erroTotal - erroAnterior) > epsilon) {
			for (int i = 0; i < mlp.getMatrizInputs().size(); i++) {
				mlp.forwardPropagation(i);
				mlp.backwardPropagation(i);
			}

			erroAnterior = erroTotal;
			erroTotal=0;
			for (int i = 0; i < mlp.getMatrizInputs().size(); i++) {
				mlp.forwardPropagation(i);
				erroTotal += ((mlp.calcularErro(i)) / mlp.getMatrizInputs().size());
			}

			contadorEpocas++;
			if (contadorEpocas % 10000 == 0) {
				System.out.println("Epoca numero: " + contadorEpocas);
				System.out.println("Erro anterior: " + erroAnterior + ", erro atual: " + erroTotal);
				System.out.println("A diferenca entre os erros é: " + (erroTotal-erroAnterior) );
			}
		}
		System.out.println("Quantidade de epocas: " + contadorEpocas);
	}
}

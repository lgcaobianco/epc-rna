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
		
		for(int i=0; i<mlp.getMatrizInputs().size(); i++) {
			mlp.forwardPropagation(i);
			mlp.backwardPropagation(i);
						
		}

		mlp.calcularErroTotal();
	}
}

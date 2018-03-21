package teste;

import classes.Perceptron;

/**
 * * created by lgcaobianco on 21/03/18 **
 */
public class TestarPerceptron {
    public static void main(String[] args){
        Perceptron perceptron = new Perceptron();
        perceptron.construirMatrizPontos();
        perceptron.construirMatrizCoeficientes();

        perceptron.imprimirMatrizCoeficientes(4,4);
        perceptron.imprimirMatrizPontos(4,4);
    }
}

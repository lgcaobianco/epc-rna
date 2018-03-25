package teste;

import classes.Perceptron;

/**
 * * created by lgcaobianco on 21/03/18 **
 */
public class TestarPerceptron {
    public static void main(String[] args) {
        Perceptron perceptron = new Perceptron();

        //Necessário colocar o nome do arquivo que contém as informacoes, bem como o formato
        perceptron.construirMatrizPontos("/home/lgcaobianco/repositorios/epc/epc1/base/valores",
                ".csv");
        perceptron.construirmatrizPesosInicial();

        int epocas = perceptron.treinarPerceptron();
        System.out.println("A quantidade de epocas foi: " + epocas);
        perceptron.imprimirmatrizPesosInicial();

        perceptron.construirConjuntoTeste("/home/lgcaobianco/repositorios/epc/epc1/base/conjuntoTeste",
                ".csv");

        perceptron.classificarVetores();

        perceptron.iniciarTabelaLatex();
        perceptron.preencherTabelaLatex(perceptron);


    }
}

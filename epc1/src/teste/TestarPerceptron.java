package teste;

import classes.Perceptron;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * * created by lgcaobianco on 21/03/18 **
 */
public class TestarPerceptron {
    public static void main(String[] args) {
        Perceptron.iniciarTabelaLatex();
        Scanner input = new Scanner(System.in);
        System.out.println("Insira a quantidade de testes");
        int quantidadeRedes = input.nextInt();

        List<Perceptron> listaPerceptron = new ArrayList<Perceptron>();
        for(int i=0; i < quantidadeRedes; i++){
            listaPerceptron.add(new Perceptron());
            listaPerceptron.get(i)
                    .construirMatrizPontos("/home/lgcaobianco/repositorios/epc/epc1/base/valores",
                    ".csv");
            listaPerceptron.get(i).construirmatrizPesosInicial();
            listaPerceptron.get(i).treinarPerceptron();
            listaPerceptron.get(i).construirConjuntoTeste("/home/lgcaobianco/repositorios/epc/epc1/base/conjuntoTeste",
                    ".csv");
            listaPerceptron.get(i).classificarVetores();
            listaPerceptron.get(i).preencherTabelaLatex(listaPerceptron.get(i), i);
        }
        Perceptron.finalizarTabelaLatex();

    }
}

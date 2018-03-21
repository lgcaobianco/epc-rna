package classes;

import java.util.Random;

/**
 * * created by lgcaobianco on 21/03/18 **
 */
public class Perceptron {
    private double[][] matrizPontos = new double[4][4];
    private double[][] matrizCoeficientes = new double[4][4];

    public void imprimirMatrizCoeficientes(int totalLinhas, int totalColunas) {
        for (int i = 0; i < totalLinhas; i++) {
            for (int j = 0; j < totalColunas; j++) {
                System.out.print(this.matrizCoeficientes[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void imprimirMatrizPontos(int totalLinhas, int totalColunas) {
        for (int i = 0; i < totalLinhas; i++) {
            for (int j = 0; j < totalColunas; j++) {
                System.out.print(this.matrizPontos[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void construirMatrizPontos(){
        LeInformacoes informacoes = new LeInformacoes("/home/lgcaobianco/repositorios/epc/epc1/base/arquivoTeste",
                        ".txt");
        matrizPontos = informacoes.extrairPontos();
    }

    public void construirMatrizCoeficientes() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Random r = new Random();
                matrizCoeficientes[i][j] = r.nextDouble();
            }
        }

    }
}

package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * * created by lgcaobianco on 21/03/18 **
 */
public class Perceptron {
    List<double[]> matrizPontos = new ArrayList<double[]>();
    private double[][] matrizCoeficientes = new double[4][1];


    public void imprimirMatrizCoeficientes() {
        for (int i = 0; i < matrizCoeficientes.length; i++) {
            for (int j = 0; j < matrizCoeficientes[i].length; j++) {
                System.out.print(this.matrizCoeficientes[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void imprimirMatrizPontos() {
        for (int i = 0; i < matrizPontos.size(); i++) {
            for (int j = 0; j < matrizPontos.get(i).length; j++) {
                System.out.print(this.matrizPontos.get(i)[j] + " ");
            }
            System.out.println();
        }
    }

    public void construirMatrizPontos(String nomeArquivo, String extensaoArquivo) {
        LeInformacoes informacoes = new LeInformacoes(nomeArquivo, extensaoArquivo);
        this.matrizPontos = informacoes.extrairPontos();
    }

    public void construirMatrizCoeficientes() {
        matrizCoeficientes[0][0]=-1;
        for (int i = 1; i < matrizCoeficientes.length; i++) {
            for (int j = 0; j < matrizCoeficientes[i].length; j++) {
                Random r = new Random();
                matrizCoeficientes[i][j] = r.nextDouble();
            }
        }

    }


    public void treinarPerceptron() {
        double somatorio = 0, taxaAprendizagem = 0.01, classificacao, contadorEpocas = 0;
        double theta = matrizCoeficientes[0][0];

        percorreLinhasConjuntoTreinamento:
        for (int i = 0; i < matrizPontos.size(); ) {
            somatorio = 0;
            for (int j = 0; j < matrizPontos.get(i).length - 1; j++) {
                somatorio += (matrizPontos.get(i)[j] * matrizCoeficientes[j+1][0]);
            }
            somatorio += theta;

            //passar o somatório pelo g(u)
            if (somatorio >= 0) {
                classificacao = 1;

            } else {
                classificacao = -1;

            }

            if (classificacao == matrizPontos.get(i)[3]) {
                System.out.println("Os valores concidiram, avancar pra prox linha");
                i++;
                continue percorreLinhasConjuntoTreinamento;

            } else { //se classificacao nao coincide com d_i, ajustar coeficientes
                for (int k = 0; k < 4; k++) {
                    double somaMultiplicacaoVetorX=0;
                    for(int l=0; l< matrizPontos.get(i).length - 1; l++) {
                        somaMultiplicacaoVetorX = taxaAprendizagem * (matrizPontos.get(i)[3] - classificacao) *  matrizPontos.get(i)[l];
                    }
                    matrizCoeficientes[k][0] = matrizCoeficientes[k][0] + somaMultiplicacaoVetorX;
                }
                //ajustar o theta

                contadorEpocas++;
                System.out.println("theta: " + theta + ", i: " + i + ", ");
                i = 0;
            }


        }

        imprimirMatrizCoeficientes();

    }
}


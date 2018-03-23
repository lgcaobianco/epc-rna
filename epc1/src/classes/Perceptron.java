package classes;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
        matrizCoeficientes[0][0] = -1;
        for (int i = 1; i < matrizCoeficientes.length; i++) {
            for (int j = 0; j < matrizCoeficientes[i].length; j++) {
                Random r = new Random();
                matrizCoeficientes[i][j] = r.nextDouble();
            }
        }

    }

    public int ativacao(double somatorio) {
        int classificacao;//passar o somatório pelo g(u)
        if (somatorio >= 0) {
            classificacao = 1;
        } else {
            classificacao = -1;
        }
        return classificacao;
    }


    public void treinarPerceptron() {
        double somatorio, taxaAprendizagem = 0.01, classificacao, contadorEpocas = 0;
        int erro, iMaximo=0;

        percorreLinhasConjuntoTreinamento:
        for (int i = 0; i < matrizPontos.size(); ) {
            somatorio = 0;
            for (int j = 0; j < matrizPontos.get(i).length - 1; j++) {
                somatorio += (matrizPontos.get(i)[j] * matrizCoeficientes[j][0]);
            }

            classificacao = ativacao(somatorio);

            if (classificacao == matrizPontos.get(i)[3]) {
                i++;

            } else { //se classificacao nao coincide com d_i, ajustar coeficientes
                erro = (int) (matrizPontos.get(i)[3] - classificacao);
                for (int j = 0; j < matrizCoeficientes.length; j++) {
                    matrizCoeficientes[j][0] += (taxaAprendizagem * erro * matrizPontos.get(i)[j]);
                }
                contadorEpocas++;
                if(contadorEpocas%100000000 == 0){
                    System.out.println("Vetor de pesos: " + matrizCoeficientes[0][0] + matrizCoeficientes[1][0] + matrizCoeficientes[2][0] + matrizCoeficientes[3][0]+", epoca: " + contadorEpocas + ", I maximo: "+ iMaximo);

                }
                if(iMaximo<i){
                    iMaximo = i;
                }

                i=0;
            }


        }

        imprimirMatrizCoeficientes();

    }
}


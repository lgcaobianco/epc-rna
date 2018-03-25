package classes;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * * created by lgcaobianco on 21/03/18 **
 */
public class Perceptron {
    List<double[]> matrizPontos = new ArrayList<double[]>();
    List<double[]> conjuntoTeste = new ArrayList<double[]>();
    private double[][] matrizPesosInicial = new double[4][1];
    private double[][] matrizPesosFinal = new double[4][1];
    private int contadorEpocas;


    public void imprimirmatrizPesosInicial() {
        for (int i = 0; i < matrizPesosInicial.length; i++) {
            for (int j = 0; j < matrizPesosInicial[i].length; j++) {
                System.out.print(this.matrizPesosInicial[i][j] + " ");
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

    public void construirConjuntoTeste(String nomeArquivo, String extensaoArquivo) {
        LeInformacoes informacoes = new LeInformacoes(nomeArquivo, extensaoArquivo);
        this.conjuntoTeste = informacoes.extrairPontos();
    }

    public void construirmatrizPesosInicial() {
        matrizPesosInicial[0][0] = -1;
        for (int i = 1; i < matrizPesosInicial.length; i++) {
            for (int j = 0; j < matrizPesosInicial[i].length; j++) {
                Random r = new Random();
                matrizPesosInicial[i][j] = r.nextDouble();
                matrizPesosFinal[i][j] = matrizPesosInicial[i][j];
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


    public int treinarPerceptron() {
        double somatorio, taxaAprendizagem = 0.01, classificacao;
        int erro, iMaximo = 0;

        percorreLinhasConjuntoTreinamento:
        for (int i = 0; i < matrizPontos.size(); ) {
            somatorio = 0;
            for (int j = 0; j < matrizPontos.get(i).length - 1; j++) {
                somatorio += (matrizPontos.get(i)[j] * matrizPesosFinal[j][0]);
            }

            classificacao = ativacao(somatorio);

            if (classificacao == matrizPontos.get(i)[3]) {
                i++;

            } else { //se classificacao nao coincide com d_i, ajustar coeficientes
                erro = (int) (matrizPontos.get(i)[3] - classificacao);
                for (int j = 0; j < matrizPesosInicial.length; j++) {
                    matrizPesosFinal[j][0] += (taxaAprendizagem * erro * matrizPontos.get(i)[j]);
                }
                this.contadorEpocas++;
                if (contadorEpocas % 100000000 == 0) {
                    System.out.println("Vetor de pesos inicial: " + matrizPesosInicial[0][0] + matrizPesosInicial[1][0] + matrizPesosInicial[2][0] + matrizPesosInicial[3][0] + ", epoca: " + contadorEpocas + ", I maximo: " + iMaximo);
                    System.out.println("Vetor de pesos final: " + matrizPesosFinal[0][0] + matrizPesosFinal[1][0] + matrizPesosFinal[2][0] + matrizPesosFinal[3][0] + ", epoca:");

                }
                if (iMaximo < i) {
                    iMaximo = i;
                }

                i = 0;
            }


        }


        return (int) this.contadorEpocas;
    }


    public void classificarVetores() {
        double somatorio;

        for (int i = 0; i < conjuntoTeste.size(); i++) {
            somatorio = 0;
            for (int j = 0; j < conjuntoTeste.get(i).length; j++) {
                somatorio += conjuntoTeste.get(i)[j] * matrizPesosFinal[j][0];
            }
            int classificacao = ativacao(somatorio);
            System.out.println("A linha: " + (i + 1) + " foi classificada como: " + classificacao);

        }
    }

    public void iniciarTabelaLatex() {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("matrizSaida.tex"), "utf-8"))) {
                writer.write("\\begin{tabular}{llllllllll}" +  System.getProperty( "line.separator" ));
            writer.write("\\hline" + System.getProperty("line.separator"));
            writer.write("\\multicolumn{1}{|c|}{\\multirow{2}{*}{treinamento}} & \\multicolumn{4}{l|}{Vetor de Pesos Inicial} " +
                    "& \\multicolumn{4}{l|}{Vetor de Pesos Final} & \\multicolumn{1}{l|}{\\multirow{2}{*}{Número de Épocas}} \\\\ " +
                    "\\cline{2-9}\n \\multicolumn{1}{|c|}{} & \\multicolumn{1}{l|}{w_0} & \\multicolumn{1}{l|}{w_1} & " +
                    "\\multicolumn{1}{l|}{w_2} & \\multicolumn{1}{l|}{w_3} & \\multicolumn{1}{l|}{w_0} & " +
                    "\\multicolumn{1}{l|}{w_1} & \\multicolumn{1}{l|}{w_2} & \\multicolumn{1}{l|}{w_3} & " +
                    "\\multicolumn{1}{l|}{} \\\\ \\hline");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void preencherTabelaLatex(Perceptron p){
        try (Writer writer = new FileWriter("matrizSaida.tex", true) ){

            for(int i=0; i<4; i++) {
                writer.append("\\multicolumn{1}{|l|}{"+ new DecimalFormat("#.##").format(p.matrizPesosInicial[i][0])+"} & ");
            }

            for(int i=0; i<4; i++){
                writer.append("\\multicolumn{1}{|l|}{"+ new DecimalFormat("#.##").format(p.matrizPesosFinal[i][0])+"} & ");
            }

            writer.append("\\multicolumn{1}{l|} {"+p.contadorEpocas +"}");
            writer.append("\\\\" + "\\hline"+ System.getProperty("line.separator"));
        }   catch(IOException e){
            e.printStackTrace();
        }
    }
}


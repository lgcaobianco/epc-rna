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
        for (int i = 0; i < this.matrizPesosInicial.length; i++) {
            for (int j = 0; j < this.matrizPesosInicial[i].length; j++) {
                System.out.print(this.matrizPesosInicial[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void imprimirMatrizPontos() {
        for (int i = 0; i < this.matrizPontos.size(); i++) {
            for (int j = 0; j < this.matrizPontos.get(i).length; j++) {
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
        for (int i = 1; i < this.matrizPesosInicial.length; i++) {
            for (int j = 0; j < this.matrizPesosInicial[i].length; j++) {
                Random r = new Random();
                this.matrizPesosInicial[i][j] = r.nextDouble();
                this.matrizPesosFinal[i][j] = this.matrizPesosInicial[i][j];
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
        double somatorio, taxaAprendizagem = 0.01, classificacao;
        int erro, iMaximo = 0;

        percorreLinhasConjuntoTreinamento:
        for (int i = 0; i < this.matrizPontos.size(); ) {
            somatorio = 0;
            for (int j = 0; j < this.matrizPontos.get(i).length - 1; j++) {
                somatorio += (this.matrizPontos.get(i)[j] * this.matrizPesosFinal[j][0]);
            }

            classificacao = ativacao(somatorio);

            if (classificacao == this.matrizPontos.get(i)[3]) {
                i++;

            } else { //se classificacao nao coincide com d_i, ajustar coeficientes
                erro = (int) (this.matrizPontos.get(i)[3] - classificacao);
                for (int j = 0; j < this.matrizPesosInicial.length; j++) {
                    this.matrizPesosFinal[j][0] += (taxaAprendizagem * erro * this.matrizPontos.get(i)[j]);
                }
                this.contadorEpocas++;
                i = 0;
            }


        }


    }


    public void classificarVetores() {
        double somatorio;

        for (int i = 0; i < this.conjuntoTeste.size(); i++) {
            somatorio = 0;
            for (int j = 0; j < this.conjuntoTeste.get(i).length; j++) {
                somatorio += this.conjuntoTeste.get(i)[j] * this.matrizPesosFinal[j][0];
            }
            int classificacao = ativacao(somatorio);
            System.out.println((i + 1) + "," + classificacao);

        }
    }

    public static void iniciarTabelaLatex() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("matrizSaida.tex"), "utf-8"))) {
            writer.write("\\documentclass{article}" + System.getProperty("line.separator"));
            writer.write("\\usepackage{multirow}" + System.getProperty("line.separator"));
            writer.write("\\usepackage[utf8]{inputenc}" + System.getProperty("line.separator"));
            writer.write("\\usepackage[bottom]{footmisc}" + System.getProperty("line.separator"));
            writer.write("\\usepackage{lscape}" + System.getProperty("line.separator"));
            writer.write("\\renewcommand\\tablename{Tabela}" + System.getProperty("line.separator"));
            writer.write("\\begin{document}" + System.getProperty("line.separator"));
            writer.write("\\begin{landscape}" + System.getProperty("line.separator"));
            writer.write("\\begin{table}" + System.getProperty("line.separator"));
            writer.write("\\centering" + System.getProperty("line.separator"));
            writer.write("\\begin{tabular}{llllllllll}" + System.getProperty("line.separator"));
            writer.write("\\hline" + System.getProperty("line.separator"));
            writer.write("\\multicolumn{1}{|c|}{\\multirow{2}{*}{Treinamento}} & \\multicolumn{4}{l|}{Vetor de Pesos Inicial} " +
                    "& \\multicolumn{4}{l|}{Vetor de Pesos Final} & \\multicolumn{1}{l|}{\\multirow{2}{*}{Núm. de Épocas}} \\\\ " +
                    "\\cline{2-9}\n \\multicolumn{1}{|c|}{} & \\multicolumn{1}{l|}{w_0} & \\multicolumn{1}{l|}{w_1} & " +
                    "\\multicolumn{1}{l|}{w_2} & \\multicolumn{1}{l|}{w_3} & \\multicolumn{1}{l|}{w_0} & " +
                    "\\multicolumn{1}{l|}{w_1} & \\multicolumn{1}{l|}{w_2} & \\multicolumn{1}{l|}{w_3} & " +
                    "\\multicolumn{1}{l|}{} \\\\ \\hline");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void preencherTabelaLatex(Perceptron p, int contaQuantidadeTreinamentos) {
        try (Writer writer = new FileWriter("matrizSaida.tex", true)) {
            writer.append("\\multicolumn{1}{|l|}{" + (contaQuantidadeTreinamentos+1) + "} &");

            for (int i = 0; i < 4; i++) {
                writer.append("\\multicolumn{1}{|l|}{" + new DecimalFormat("#.####").format(p.matrizPesosInicial[i][0]) + "} & ");
            }

            for (int i = 0; i < 4; i++) {
                writer.append("\\multicolumn{1}{|l|}{" + new DecimalFormat("#.####").format(p.matrizPesosFinal[i][0]) + "} & ");
            }

            writer.append("\\multicolumn{1}{l|} {" + p.contadorEpocas + "}");
            writer.append("\\\\" + "\\hline" + System.getProperty("line.separator"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finalizarTabelaLatex() {
        try (Writer writer = new FileWriter("matrizSaida.tex", true)) {
                writer.append("\\end{tabular}" + System.getProperty("line.separator"));
            writer.append("\\caption{Tabela em \\LaTeX{} gerada automaticamente pelo software em Java}" + System.getProperty("line.separator"));
            writer.append("\\end{table}" + System.getProperty("line.separator"));
            writer.append("\\end{landscape}" + System.getProperty("line.separator"));
            writer.append("\\end{document}" + System.getProperty("line.separator"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


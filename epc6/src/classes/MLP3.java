package classes;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;

/**
 * created by lgcaobianco on 2018-05-21
 */

public class MLP3 {
	private Double[][] W1, W1Anterior, W2, W2Anterior, deltaCamada1;
	private List<Double[]> matrizInputs, matrizOperacao, vetorEntrada;
	private Double[][] I1;
	private Double I2 = 0.0, Y2;
	private Double deltaCamada2 = 0.0;
	private double taxaAprendizagem = 0.1;
	private Double[][] Y1;
	private int qtdEntradas;
	private int qtdNeuronios;
	private Double alfa = 0.8;

	// O construtor irá efetuar as operações essenciais para o funcionamento dos
	// métodos.
	public MLP3(int qtdEntradas, int qtdNeuronios) {
		this.qtdEntradas = qtdEntradas;
		this.qtdNeuronios = qtdNeuronios;

		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc6/src/base/conjunto-treinamento", ".csv");
		this.matrizInputs = leitor.extrairPontos();
		ajustarMatrizTreinamento(qtdEntradas);
		this.vetorEntrada = leitor.extrairPontos();
		imprimirQualquer(matrizInputs);
		leitor = null; // para leitor se tornar candidato ao garbage collector

		Random random = new Random();

		W2Anterior = new Double[1][qtdNeuronios + 1];
		W2 = new Double[1][qtdNeuronios + 1];

		deltaCamada1 = new Double[qtdNeuronios][1];
		I1 = new Double[qtdNeuronios][1];
		Y1 = new Double[qtdNeuronios + 1][1];
		W1Anterior = new Double[qtdNeuronios][qtdEntradas + 2];
		W1 = new Double[qtdNeuronios][qtdEntradas + 2];

		// sorteia W1
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				W1[i][j] = (random.nextDouble()/100);
				W1Anterior[i][j] = 0.0;
			}
		}


		// sorteia W2
		for (int i = 0; i < W2[0].length; i++) {
			W2[0][i] = (random.nextDouble()/100);
			W2Anterior[0][i] = 0.0;
		}

		System.out.println("W1");
		imprimirQualquer(W1);
		System.out.println("W2");
		imprimirQualquer(W2);

	}

	public List<Double[]> getMatrizInputs() {
		return matrizInputs;
	}

	public void setMatrizInputs(List<Double[]> matrizInputs) {
		this.matrizInputs = matrizInputs;
	}

	public List<Double[]> getMatrizOperacao() {
		return matrizOperacao;
	}

	public void setMatrizOperacao(List<Double[]> matrizOperacao) {
		this.matrizOperacao = matrizOperacao;
	}

	public Double[][] getI1() {
		return I1;
	}

	public void setI1(Double[][] i1) {
		I1 = i1;
	}

	public Double[][] getW1() {
		return W1;
	}

	public Double[][] getW2() {
		return W2;
	}

	public Double[][] getY1() {
		return Y1;
	}

	public void obterI1(int linhaMatrizInput) {
		double soma = 0.0;
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				soma += (W1[i][j] * matrizInputs.get(linhaMatrizInput)[j]);
			}
			I1[i][0] = soma;
			soma = 0.0;
		}
	}

	public void obterY1() {
		Y1[0][0] = -1.0;
		for (int i = 0; i < I1.length; i++) {
			Y1[i + 1][0] = 0.5 + 0.5 * Math.tanh((I1[i][0]) / 2);
		}
	}

	public void obterI2() {
		I2 = 0.0;
		for (int i = 0; i < W2[0].length; i++) {
			I2 += Y1[i][0] * W2[0][i];
		}
	}

	public void obterY2() {
		Y2 = 0.0;
		Y2 = 0.5 + 0.5 * Math.tanh(I2 / 2);
	}

	public void obterDeltaCamada2(int linhaMatrizInput) {
		// A derivada de g(Ij) pode ser expressa como f(x) * (1 - f(x))!
		this.deltaCamada2 = (matrizInputs.get(linhaMatrizInput)[matrizInputs.get(0).length - 1] - Y2) * (Y2 * (1 - Y2));
	}

	public void ajustarPesosCamada2() {
		Double[][] aux = new Double[1][W2[0].length];

		for (int i = 0; i < W2[0].length; i++) {
			aux[0][i] = W2[0][i];
			W2[0][i] = W2[0][i] + (taxaAprendizagem * deltaCamada2 * Y1[i][0]) + (alfa * (W2[0][i] - W2Anterior[0][i]));
		}

		for (int i = 0; i < W2[0].length; i++) {
			W2Anterior[0][i] = aux[0][i];
		}
	}

	public void obterDeltaCamada1() {
		for (int i = 0; i < deltaCamada1.length; i++) {
			deltaCamada1[i][0] = deltaCamada2 * W2[0][i] * (Y1[i][0] * (1 - Y1[i][0]));
		}

	}

	public void ajustarPesosCamada1(int linhaInputMatriz) {
		Double[][] aux = new Double[W1.length][W1[0].length];
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				aux[i][j] = W1[i][j];
				W1[i][j] = W1[i][j] + (taxaAprendizagem * deltaCamada1[i][0] * matrizInputs.get(linhaInputMatriz)[j])
						+ (alfa * (W1[i][j] - W1Anterior[i][j]));
			}
		}

		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				W1Anterior[i][j] = aux[i][j];
			}
		}
	}

	public double calcularEk(int linhaMatrizEntrada) {
		double erro = 0.0;
		erro = Math.pow((matrizInputs.get(linhaMatrizEntrada)[qtdEntradas] - Y2), 2);
		return erro / 2;
	}

	public double calcularEm() {
		double erroTotal = 0.0;
		for (int i = 0; i < matrizInputs.size(); i++) {
			erroTotal += calcularEk(i);
		}
		double valorRetorno = erroTotal / matrizInputs.size();
		return valorRetorno;
	}

	public void forwardPropagation(int linhaMatrizInput) {
		this.obterI1(linhaMatrizInput);
		this.obterY1();
		this.obterI2();
		this.obterY2();
	}

	public void backwardPropagation(int linhaMatrizInput) {
		this.obterDeltaCamada2(linhaMatrizInput);
		this.ajustarPesosCamada2();
		this.obterDeltaCamada1();
		this.ajustarPesosCamada1(linhaMatrizInput);

	}

	public void ajustarMatrizTreinamento(int quantidadeEntradas) {
		List<Double[]> matrizAuxiliar = new LinkedList<>();
		int tamanhoConjuntoDados = matrizInputs.size();

		for (int i = 0; i < (tamanhoConjuntoDados - quantidadeEntradas); i++) {
			Double[] teste = inverterPosicoes(i, quantidadeEntradas);
			matrizAuxiliar.add(i, teste);
		}
		this.setMatrizInputs(matrizAuxiliar);
	}

	public Double[] inverterPosicoes(int linhaInicial, int quantidadeEntradas) {
		Double[] vetorAuxiliar = new Double[quantidadeEntradas + 2];
		int auxiliar = quantidadeEntradas;
		vetorAuxiliar[quantidadeEntradas + 1] = this.matrizInputs.get(linhaInicial + quantidadeEntradas)[0];
		vetorAuxiliar[0] = -1.0;
		for (int i = 0; i < quantidadeEntradas; i++) {
			vetorAuxiliar[auxiliar] = this.matrizInputs.get(i + linhaInicial)[0];
			auxiliar--;
		}

		return vetorAuxiliar;
	}

	public void imprimirQualquer(List<Double[]> objeto) {
		System.out
				.println("O objeto tem dimensoes: " + objeto.size() + " linhas e " + objeto.get(0).length + "colunas");
		for (int i = 0; i < objeto.size(); i++) {
			for (int j = 0; j < objeto.get(i).length; j++) {
				System.out.print(objeto.get(i)[j] + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}

	public void imprimirQualquer(Double[][] objeto) {
		System.out.println("O objeto tem dimensoes: " + objeto.length + " linhas e " + objeto[0].length + "colunas");
		for (int i = 0; i < objeto.length; i++) {
			for (int j = 0; j < objeto[i].length; j++) {
				System.out.print(objeto[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}

	public void obterNovaAmostra(int posicaoAmostra) {
		for (int i = 0; i < I1.length; i++) {
			I1[i][0] = 0.0;
		}
		for (int i = 0; i < W1.length; i++) {
			I1[i][0] += -1 * W1[i][0];
			for (int j = 1; j < W1[i].length; j++) {
				I1[i][0] += vetorEntrada.get(posicaoAmostra - j)[0] * W1[i][j];
				// System.out.println("I1[" + i + "] += VetorEntrada(" + (posicaoAmostra - j -
				// 1) + ") * " + W1[i][j]);
			}
		}

		obterY1();
		obterI2();
		obterY2();
		System.out.println(Y2);
		Double[] resultadoRede = new Double[1];
		resultadoRede[0] = Y2;
		vetorEntrada.add(resultadoRede);

	}

}

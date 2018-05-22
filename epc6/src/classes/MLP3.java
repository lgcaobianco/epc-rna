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
		this.matrizOperacao = leitor.extrairPontos();
		this.vetorEntrada = leitor.extrairPontos();
		leitor = null; // para leitor se tornar candidato ao garbage collector

		Random random = new Random();

		W2Anterior = new Double[1][qtdNeuronios+1];
		W2 = new Double[1][qtdNeuronios+1];

		deltaCamada1 = new Double[10][1];
		I1 = new Double[10][1];
		Y1 = new Double[11][1];
		W1Anterior = new Double[qtdNeuronios][qtdEntradas + 1];
		W1 = new Double[qtdNeuronios][qtdEntradas + 1];

		// sorteia W1
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				W1[i][j] = random.nextDouble();
				W1Anterior[i][j] = 0.0;
			}
		}

		// sorteia W2
		for (int i = 0; i < W2[0].length; i++) {
			W2[0][i] = random.nextDouble();
			W2Anterior[0][i] = 0.0;
		}
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

	public void imprimirMatrizI1() {
		for (int i = 0; i < I1.length; i++) {
			for (int j = 0; j < I1[i].length; j++) {
				System.out.print(this.I1[i][j] + ",");
			}
			System.out.println();
		}
	}

	public void imprimirMatrizW2() {
		for (int i = 0; i < W2.length; i++) {
			for (int j = 0; j < W2[i].length; j++) {
				System.out.print(this.W2[i][j] + ",");
			}
			System.out.println();
		}

		System.out.println("\n\n\n");
	}

	public void imprimirMatrizOperacao() {
		for (int i = 0; i < this.matrizOperacao.size(); i++) {
			for (int j = 0; j < this.matrizOperacao.get(i).length; j++) {
				System.out.print(this.matrizOperacao.get(i)[j] + " ,");
			}
			System.out.println();
		}
		System.out.println("\n\n\n");
	}

	public void imprimirMatrizInputs() {
		for (int i = 0; i < this.matrizInputs.size(); i++) {
			for (int j = 0; j < this.matrizInputs.get(i).length; j++) {
				System.out.print(this.matrizInputs.get(i)[j] + " ,");
			}
			System.out.println();
		}
		System.out.println("\n\n\n");
	}

	public void imprimirMatrizInputs(int linhaMatriz) {
		for (int j = 0; j < this.matrizInputs.get(linhaMatriz).length; j++) {
			System.out.print(this.matrizInputs.get(linhaMatriz)[j] + " ,");
		}
		System.out.println();
	}

	public void imprimirMatrizW1() {
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				System.out.print(this.W1[i][j] + ",");
			}
			System.out.println();
		}

		System.out.println("\n\n\n");
	}

	public void imprimirY1() {
		for (int i = 0; i < Y1.length; i++) {
			System.out.println(Y1[i][0]);
		}
		System.out.println("\n\n\n");
	}

	public void imprimirI2() {
		System.out.println(I2);
	}

	public void imprimirY2() {
		System.out.println(Y2);
	}

	public void imprimirDeltaCamada2() {
		System.out.println(deltaCamada2);
	}

	public void imprimirDeltaCamada1() {
		for (int i = 0; i < deltaCamada1.length; i++) {
			System.out.println(deltaCamada1[i][0]);
		}
		System.out.println("\n\n");
	}

	public void obterI1(int linhaMatrizInput) {
		for (int i = 0; i < I1.length; i++) {
			I1[i][0] = 0.0;
		}

		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				I1[i][0] += (matrizInputs.get(linhaMatrizInput)[j] * W1[i][j]);
			}
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
		this.deltaCamada2 = (matrizInputs.get(linhaMatrizInput)[qtdEntradas + 1] - Y2) * (Y2 * (1 - Y2));
	}

	public void ajustarPesosCamada2() {
		Double[][] aux = new Double[W2.length][W2[0].length];
		for (int i = 0; i < W2[0].length; i++) {
			aux[0][i] = W2[0][i];
			this.W2[0][i] = this.W2[0][i] + (this.taxaAprendizagem * this.deltaCamada2 * Y1[i][0])
					+ (alfa * W2[0][i] - W2Anterior[0][i]);
		}

		for (int i = 0; i < W2[0].length; i++) {
			W2Anterior[0][i] = aux[0][i];
		}
	}

	public void obterDeltaCamada1() {
		for (int i = 0; i < deltaCamada1.length; i++) {
			deltaCamada1[i][0] = deltaCamada2 * W2[0][i] * (Y1[1][0] * (1 - Y1[1][0]));
		}

	}

	public void ajustarPesosCamada1(int linhaInputMatriz) {
		Double[][] aux = new Double[W1.length][W1[0].length];
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				aux[i][j] = W1[i][j];
				W1[i][j] = W1[i][j] + taxaAprendizagem * (deltaCamada1[i][0] * matrizInputs.get(linhaInputMatriz)[j])
						* (alfa * W1[i][j] - W1Anterior[i][j]);
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
		erro = Math.pow((matrizInputs.get(linhaMatrizEntrada)[qtdEntradas + 1] - Y2), 2);
		return erro / 2;
	}

	public double calcularEm() {
		double erroTotal = 0.0;
		for (int i = 0; i < matrizInputs.size(); i++) {
			erroTotal += calcularEk(i);
		}
		return (erroTotal / matrizInputs.size());
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

	public void faseOperacao(int amostraProcurada) {
		for (int i = 0; i < I1.length; i++) {
			I1[i][0] = 0.0;
		}
		for (int i = 0; i < W1.length; i++) {
			I1[i][0] += -1 * W1[i][0];
			for (int j = 1; j < W1[i].length; j++) {
				I1[i][0] += vetorEntrada.get(amostraProcurada - j)[0] * W1[i][j];
			}
		}
		setI1(I1);
		obterY1();
		obterI2();
		obterY2();
		Double[] resultado = new Double[1];
		resultado[0] = Y2;
		vetorEntrada.add(amostraProcurada, resultado);
		System.out.println(Y2);
		Y2=0.0;
	}
}

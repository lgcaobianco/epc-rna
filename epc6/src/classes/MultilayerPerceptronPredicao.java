package classes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * created by lgcaobianco on 2018-05-20
 */

public class MultilayerPerceptronPredicao {
	private static final int quantidadeEntradas = 5;
	private static final double alfa = 0.8;
	private Double[][] W1, W1Anterior, W1Inicial, W2, W2Inicial, deltaCamada1, W2Anterior;
	private List<Double[]> matrizInputs;
	private Double[][] I1;
	private Double I2 = 0.0, Y2;
	private Double deltaCamada2 = 0.0;
	private double taxaAprendizagem = 0.1;
	private Double[][] Y1;

	// O construtor irá efetuar as operações essenciais para o funcionamento dos
	// métodos.
	public MultilayerPerceptronPredicao() {
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc6/src/base/conjunto-treinamento", ".csv");
		this.matrizInputs = leitor.extrairPontos();
		prepararMatrizTreinamento(quantidadeEntradas);
		Random random = new Random();

		W2Inicial = W2Anterior = W2 = new Double[10][1];
		deltaCamada1 = I1 = new Double[10][1];
		Y1 = new Double[11][1];
		W1Anterior = W1Inicial = W1 = new Double[10][4];

		// sorteia W1
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				W1Inicial[i][j] = W1[i][j] = random.nextDouble();
				W1Anterior[i][j] = 0.0;
			}
		}

		// inicializar os vetores
		for (int i = 0; i < W2.length; i++) {
			W2[i][0] = W2Inicial[i][0] = random.nextDouble();
			W2Anterior[i][0] = 0.0;
		}
	}

	public List<Double[]> getMatrizInputs() {
		return matrizInputs;
	}

	public void setMatrizInputs(List<Double[]> matrizInputs) {
		this.matrizInputs = matrizInputs;
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

	public void prepararMatrizTreinamento(int quantidadeEntradas) {
		List<Double[]> matrizAuxiliar = new LinkedList<>();
		int tamanhoConjuntoDados = matrizInputs.size();
		
		for (int i = 0; i < (tamanhoConjuntoDados - quantidadeEntradas); i++) {
			Double[] teste = inverterPosicoes(i, quantidadeEntradas);
			matrizAuxiliar.add(i, teste);
		}
		setMatrizInputs(matrizAuxiliar);
	}

	public Double[] inverterPosicoes(int linhaInicial, int quantidadeEntradas) {
		Double[] vetorAuxiliar = new Double[quantidadeEntradas+1];
		int auxiliar = quantidadeEntradas-1;
		vetorAuxiliar[quantidadeEntradas] = this.matrizInputs.get(linhaInicial+quantidadeEntradas)[0];
		for(int i=0; i<quantidadeEntradas; i++) {
			vetorAuxiliar[auxiliar] = this.matrizInputs.get(i+linhaInicial)[0];
			auxiliar--;
		}
				
		return vetorAuxiliar;
	}
	
	public void obterI1(int linhaMatrizInput) {
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
		for (int i = 0; i < W2.length; i++) {
			I2 += Y1[i][0] * W2[i][0];
		}
	}

	public void obterY2() {
		Y2 = 0.0;
		Y2 = 0.5 + 0.5 * Math.tanh(I2 / 2);
	}

	public void obterDeltaCamada2(int linhaMatrizInput) {
		// A derivada de g(Ij) pode ser expressa como f(x) * (1 - f(x))!
		this.deltaCamada2 = (matrizInputs.get(linhaMatrizInput)[4] - Y2) * (Y2 * (1 - Y2));
	}

	public void ajustarPesosCamada2() {
		for (int i = 0; i < W2.length; i++) {
			this.W2[i][0] = this.W2[i][0] + (alfa * (W2[i][0] - W2Anterior[i][0]) ) + (this.taxaAprendizagem * this.deltaCamada2 * Y1[i][0]);
		}
	}

	public void obterDeltaCamada1() {
		for (int i = 0; i < deltaCamada1.length; i++) {
			deltaCamada1[i][0] = deltaCamada2 * W2[i][0] * (Y1[1][0] * (1 - Y1[1][0]));
		}
		
	}

	public void ajustarPesosCamada1(int linhaInputMatriz) {
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				W1[i][j] = W1[i][j] + (alfa * W1[i][j] - W1Anterior[i][j]) + (taxaAprendizagem * deltaCamada1[i][0] * matrizInputs.get(linhaInputMatriz)[j]);
			}
		}
	}

	public double calcularErroRelativo(int linhaInputMatriz) {
		double erro = 0.0;
		erro = (Math.abs((matrizInputs.get(linhaInputMatriz)[4] - Y2)) / ((matrizInputs.get(linhaInputMatriz)[4]))
				* 100);
		return erro;
	}

	public double calcularErroTotal() {
		double erroTotal = 0;
		for (int i = 0; i < matrizInputs.size(); i++) {
			erroTotal += (Math.pow((matrizInputs.get(i)[4] - Y2), 2) / 2);
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
}

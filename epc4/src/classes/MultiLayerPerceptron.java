package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * created by lgcaobianco on 2018-04-29
 */

public class MultiLayerPerceptron {

	private Double[][] W1, W1Inicial, W2, W2Inicial;
	private List<Double[]> matrizInputs;
	private Double[][] I1;
	private Double I2 = 0.0, Y2;
	private Double deltaCamada2 = 0.0, deltaCamada1 = 0.0;
	private int quantidadeCamadas;
	private List<Integer> quantidadeNeuroniosPorCamada = new ArrayList<>();
	private double taxaAprendizagem = 0.1;
	private Double[][] Y1;

	// O construtor irá efetuar as operações essenciais para o funcionamento dos
	// métodos.
	public MultiLayerPerceptron() {
		// inicializar a matriz X
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc4/src/base/conjunto-treinamento", ".csv");
		this.matrizInputs = leitor.extrairPontos();
		Random random = new Random();

		// pegar a quantidade de camadas
		System.out.println("Entre com a quantidade de camadas");
		quantidadeCamadas = leitor.lerInputTerminal();
		for (int i = 0; i < quantidadeCamadas; i++) {
			System.out.println("entre com a quantidade de neuronios da camada" + (i + 1));
			int valorLido = leitor.lerInputTerminal();
			quantidadeNeuroniosPorCamada.add(valorLido);
		}
		// inicializar matriz W2.
		W2Inicial = W2 = new Double[quantidadeNeuroniosPorCamada.get(1)][quantidadeNeuroniosPorCamada.get(0)];
		for (int i = 0; i < W2.length; i++) {
			for (int j = 0; j < W2[i].length; j++) {
				W2Inicial[i][j] = W2[i][j] = random.nextDouble();
			}
		}

		// inicializar a matriz I1 & Y1
		I1 = new Double[quantidadeNeuroniosPorCamada.get(0)][quantidadeNeuroniosPorCamada.get(1)];
		Y1 = new Double[quantidadeNeuroniosPorCamada.get(0)][quantidadeNeuroniosPorCamada.get(1)];
		for (int i = 0; i < I1.length; i++) {
			for (int j = 0; j < I1[i].length; j++) {
				I1[i][j] = 0.0;
				Y1[i][j] = 0.0;
			}
		}

		// inicaliza a matriz de coeficientes
		W1Inicial = W1 = new Double[quantidadeNeuroniosPorCamada.get(0)][matrizInputs.get(0).length - 1];
		// preenche com 0's
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				this.W1Inicial[i][j] = this.W1[i][j] = random.nextDouble();
			}
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
		for (int i = 0; i < 1 /* this.matrizInputs.size() */; i++) {
			for (int j = 0; j < this.matrizInputs.get(i).length; j++) {
				System.out.print(this.matrizInputs.get(i)[j] + ",");
			}
			System.out.println();
		}
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

	public void calcularMatrizI1(int linhaMatrizInput) {
		for (int i = 0; i < I1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				I1[i][0] += (matrizInputs.get(linhaMatrizInput)[j] * W1[i][j]);
			}
		}
	}

	public void obterY1() {
		for (int i = 0; i < I1.length; i++) {
			for (int j = 0; j < I1[i].length; j++) {
				Y1[i][j] = (1 / (1 + Math.exp(-I1[i][j])));
			}
		}
	}

	public void imprimirY1() {
		for (int i = 0; i < Y1.length; i++) {
			for (int j = 0; j < Y1[i].length; j++) {
				System.out.println(Y1[i][j]);
			}
		}
	}

	public void obterI2() {
		for (int i = 0; i < W2.length; i++) {
			for (int j = 0; j < W2[i].length; j++) {
				I2 += Y1[j][i] * W2[i][j];
			}
		}
	}

	public void obterY2() {
		this.Y2 = (1 / (1 + Math.exp(-I2)));
	}

	public void obterDeltaCamada2(int linhaMatrizInput) {
		// A derivada de g(Ij) pode ser expressa como f(x) * (1 - f(x))!
		this.deltaCamada2 = (matrizInputs.get(linhaMatrizInput)[4] - Y2) * (Y2 * (1 - Y2));
	}

	public void ajustarPesosCamada2() {
		for (int i = 0; i < W2.length; i++) {
			for (int j = 0; j < W2[i].length; j++) {
				this.W2[i][j] = this.W2[i][j] + (this.taxaAprendizagem * this.deltaCamada2 * Y1[j][i]);
			}
		}
	}

	public void obterDeltaCamada1() {
		for (int i = 0; i < W2.length; i++) {
			for (int j = 0; j < W2[i].length; j++) {
				deltaCamada1 += (deltaCamada2 * W2[i][j] * (I1[i][0] * (1 - I1[i][0])));
			}
		}
	}

	public void ajustarPesosCamada1() {
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				W1[i][j] = W1[i][j] + taxaAprendizagem * deltaCamada1 * Y1[i][0];
			}
		}
	}

	
	public double calcularErro(int k) {
		return (Math.pow((matrizInputs.get(k)[4] - Y2), 2))/2;
	}
	

	public void forwardPropagation(int linhaMatrizInput) {
		this.calcularMatrizI1(linhaMatrizInput);
		this.obterY1();
		this.obterI2();
		this.obterY2();
	}

	public void backwardPropagation(int linhaMatrizInput) {
		this.obterDeltaCamada2(linhaMatrizInput);
		this.ajustarPesosCamada2();
		this.obterDeltaCamada1();
		this.ajustarPesosCamada1();

	}
}

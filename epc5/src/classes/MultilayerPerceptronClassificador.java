package classes;

import java.util.List;
import java.util.Random;

/**
 * created by lgcaobianco on 2018-05-19
 */

public class MultilayerPerceptronClassificador {
	private LeitorPontosEntrada leitor;
	private List<Double[]> listaPontosEntrada, classificacaoPontosEntrada, matrizOperacao, classificacaoMatrizOperacao;
	private Double[][] W1;
	private Double[][] W2;
	private Double[][] I1;
	private Double[][] I2;
	private Double[][] W1Inicial;
	private Double[][] W2Inicial;
	private Double[][] Y1;
	private Double[][] Y2;
	private Double[][] Y2Ajustado;
	private Double[][] deltaCamadaEscondida1;
	private Double[][] deltaCamadaSaida;
	private Double taxaAprendizagem = 0.1;

	public List<Double[]> getListaPontosEntrada() {
		return listaPontosEntrada;
	}

	public void setListaPontosEntrada(List<Double[]> listaPontosEntrada) {
		this.listaPontosEntrada = listaPontosEntrada;
	}

	public List<Double[]> getClassificacaoPontosEntrada() {
		return classificacaoPontosEntrada;
	}

	public void setClassificacaoPontosEntrada(List<Double[]> classificacaoPontosEntrada) {
		this.classificacaoPontosEntrada = classificacaoPontosEntrada;
	}

	public List<Double[]> getMatrizOperacao() {
		return matrizOperacao;
	}

	public void setMatrizOperacao(List<Double[]> matrizOperacao) {
		this.matrizOperacao = matrizOperacao;
	}

	public List<Double[]> getClassificacaoMatrizOperacao() {
		return classificacaoMatrizOperacao;
	}

	public void setClassificacaoMatrizOperacao(List<Double[]> classificacaoMatrizOperacao) {
		this.classificacaoMatrizOperacao = classificacaoMatrizOperacao;
	}

	public Double[][] getY2() {
		return Y2;
	}

	public void setY2(Double[][] y2) {
		Y2 = y2;
	}

	public MultilayerPerceptronClassificador() {
		this.leitor = new LeitorPontosEntrada("/home/lgcaobianco/repositorios/epc-rna/epc5/src/base/entrada", ".csv");
		this.listaPontosEntrada = leitor.extrairPontos();

		this.leitor = new LeitorPontosEntrada("/home/lgcaobianco/repositorios/epc-rna/epc5/src/base/conjunto-operacao",
				".csv");
		this.matrizOperacao = leitor.extrairPontos();

		this.leitor = new LeitorPontosEntrada("/home/lgcaobianco/repositorios/epc-rna/epc5/src/base/classificacao-conjunto-operacao",
				".csv");
		this.classificacaoMatrizOperacao = leitor.extrairPontos();
		
		this.leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc5/src/base/entrada-classificacao", ".csv");
		this.classificacaoPontosEntrada = leitor.extrairPontos();
		leitor = null;

		this.deltaCamadaEscondida1 = this.I1 = new Double[15][1];

		for (int i = 0; i < 15; i++) {
			I1[i][0] = 0.0;
		}

		this.W1 = this.W1Inicial = new Double[15][5];
		Random random = new Random();

		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				this.W1[i][j] = this.W1Inicial[i][j] = random.nextDouble();
			}
		}

		Y1 = new Double[16][1];
		for (int i = 0; i < Y1.length; i++) {
			Y1[i][0] = 0.0;
		}

		this.W2 = this.W2Inicial = new Double[3][16];

		this.deltaCamadaSaida = this.Y2Ajustado = this.Y2 = this.I2 = new Double[3][1];
		for (int i = 0; i < deltaCamadaSaida.length; i++) {
			deltaCamadaSaida[i][0] = Y2Ajustado[i][0] = Y2[i][0] = I2[i][0] = 0.0;
		}

		for (int i = 0; i < W2.length; i++) {
			for (int j = 0; j < W2[i].length; j++) {
				this.W2[i][j] = this.W2Inicial[i][j] = random.nextDouble();
			}
		}
		System.out.println("Fim da construcao. Tamanho do cjt de entrada: " + this.listaPontosEntrada.size()
				+ " linhas e " + this.listaPontosEntrada.get(0).length + "  colunas");
		System.out.println("I1 tem: " + I1.length + " linhas e : " + I1[0].length + " colunas");
		System.out.println("w1 tem: " + W1.length + " linhas e : " + W1[0].length + " colunas");

	}

	public void imprimirMatrizI1() {
		for (int i = 0; i < I1.length; i++) {
			for (int j = 0; j < I1[i].length; j++) {
				System.out.print(this.I1[i][j] + ",");
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

	public void inicializarI1() {
		for (int i = 0; i < I1.length; i++) {
			I1[i][0] = 0.0;
		}
	}

	public void inicializarI2() {
		for (int i = 0; i < I2.length; i++) {
			I2[i][0] = 0.0;
		}
	}

	public void obterI1(int linhaMatrizEntrada) {
		inicializarI1();
		for (int i = 0; i < I1.length; i++) {
			for (int j = 0; j < W1[i].length; j++) {
				this.I1[i][0] += (W1[i][j] * listaPontosEntrada.get(linhaMatrizEntrada)[j]);

			}
		}
	}

	public void obterY1() {
		Y1[0][0] = -1.0;
		for (int i = 1; i < Y1.length - 1; i++) {
			this.Y1[i][0] = 0.5 + 0.5 * Math.tanh((I1[i - 1][0]) / 2);
		}
	}

	public void obterI2() {
		inicializarI2();
		for (int i = 0; i < W2.length; i++) {
			for (int j = 0; j < W2[i].length; j++) {
				this.I2[i][0] += this.W2[i][j] * this.Y1[j][0];
			}
		}
	}

	public void obterY2() {
		for (int i = 0; i < I2.length; i++) {
			this.Y2[i][0] = 0.5 + 0.5 * Math.tanh((I2[i][0]) / 2);
		}
	}

	public void obterDeltaCamadaSaida(int linhaMatrizEntrada) {
		for (int i = 0; i < deltaCamadaSaida.length; i++) {
			this.deltaCamadaSaida[i][0] = (this.classificacaoPontosEntrada.get(linhaMatrizEntrada)[i] - Y2[i][0])
					* (this.Y2[i][0] * (1 - this.Y2[i][0]));

		}
	}

	public void ajustarPesosCamada2() {
		for (int i = 0; i < W2.length; i++) {
			for (int j = 0; j < W2[i].length; j++) {
				this.W2[i][j] = W2[i][j] + (this.taxaAprendizagem * this.deltaCamadaSaida[i][0] * Y1[j][0]);
			}
		}
	}

	public void obterDeltaCamadaEscondida() {
		Double[] aux = new Double[15];
		for (int i = 0; i < aux.length; i++) {
			aux[i] = 0.0;
		}
		for (int i = 0; i < W2[0].length - 1; i++) {
			for (int j = 0; j < W2.length; j++) {
				aux[i] += deltaCamadaSaida[j][0] * W2[j][i];
			}
		}
		for (int i = 0; i < deltaCamadaEscondida1.length; i++) {
			deltaCamadaEscondida1[i][0] = aux[i] * Y1[i + 1][0];
		}
	}

	public void ajustarPesosCamada1(int linhaMatrizEntrada) {
		for (int i = 0; i < W1.length; i++) {
			for (int j = 0; +j < W1[i].length; j++) {
				W1[i][j] = W1[i][j] + taxaAprendizagem * deltaCamadaEscondida1[i][0]
						* listaPontosEntrada.get(linhaMatrizEntrada)[j];
			}
		}
	}

	public double calcularEk(int linhaMatrizEntrada) {
		double erro = 0.0;

		for (int i = 0; i < classificacaoPontosEntrada.get(0).length; i++) {
			erro += Math.pow((classificacaoPontosEntrada.get(linhaMatrizEntrada)[i] - Y2[i][0]), 2);
		}
		return erro / 2;
	}

	public double calcularEm() {
		double erroTotal = 0.0;
		for (int i = 0; i < classificacaoPontosEntrada.size(); i++) {
			erroTotal += calcularEk(i);
		}
		return (erroTotal / classificacaoPontosEntrada.size());
	}

	public void forwardPropagation(int linhaMatrizEntrada) {
		this.obterI1(linhaMatrizEntrada);
		this.obterY1();
		this.obterI2();
		this.obterY2();
	}

	public void backwardPropagation(int linhaMatrizEntrada) {
		this.obterDeltaCamadaSaida(linhaMatrizEntrada);
		this.ajustarPesosCamada2();
		this.obterDeltaCamadaEscondida();
		this.ajustarPesosCamada1(linhaMatrizEntrada);
	}

}
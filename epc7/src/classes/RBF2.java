package classes;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * created by lgcaobianco on 2018-05-23
 */

public class RBF2 {
	private Double[][] centroide;
	private Double[] distanciasEuclidianasParaNeuronios;
	private static int quantidadeNeuronios = 2;
	private List<Double[]> conjuntoEntrada, conjuntoOperacao;
	private List<Double[]> vetorG;
	private List<Double[]> grupoOmega1;
	private List<Double[]> grupoOmega1Anterior;
	private List<Double[]> grupoOmega2;
	private int qtdEntradas = 2;
	private double varianciaCentroide1;
	private double varianciaCentroide2;
	private Double[] W2;
	public double I2, Y2, Y2Ajustado;
	private double deltaCamadaSaida;
	private double taxaAprendizagem = 0.01;

	public Double[][] getCentroide() {
		return centroide;
	}

	public void setCentroide(Double[][] centroide) {
		this.centroide = centroide;
	}

	public List<Double[]> getConjuntoEntrada() {
		return conjuntoEntrada;
	}

	public void setConjuntoEntrada(List<Double[]> conjuntoEntrada) {
		this.conjuntoEntrada = conjuntoEntrada;
	}

	public List<Double[]> getVetorG() {
		return vetorG;
	}

	public RBF2() {
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc7/src/base/conjunto-treinamento", ".csv");
		conjuntoEntrada = leitor.extrairPontos();

		leitor = new LeitorPontosEntrada("/home/lgcaobianco/repositorios/epc-rna/epc7/src/base/conjunto-operacao",
				".csv");
		conjuntoOperacao = leitor.extrairPontos();

		centroide = new Double[quantidadeNeuronios][2];

		for (int i = 0; i < qtdEntradas; i++) {
			centroide[0][i] = conjuntoEntrada.get(2)[i];
		}

		for (int i = 0; i < qtdEntradas; i++) {
			centroide[1][i] = conjuntoEntrada.get(3)[i];
		}

		System.out.println("Centroide inicial");
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				System.out.print(centroide[i][j] + " ");
			}
			System.out.println();
		}
		Random random = new Random();
		W2 = new Double[3];
		for (int i = 0; i < W2.length; i++) {
			W2[i] = random.nextDouble();
		}

		grupoOmega1 = new ArrayList<Double[]>();
		grupoOmega1Anterior = new ArrayList<Double[]>();
		grupoOmega2 = new ArrayList<Double[]>();
		vetorG = new ArrayList<Double[]>();
		distanciasEuclidianasParaNeuronios = new Double[quantidadeNeuronios];

	}

	public Double[] getW2() {
		return W2;
	}

	public List<Double[]> getGrupoOmega1() {
		return grupoOmega1;
	}

	public void setGrupoOmega1(List<Double[]> grupoOmega1) {
		this.grupoOmega1 = grupoOmega1;
	}

	public List<Double[]> getGrupoOmega2() {
		return grupoOmega2;
	}

	public void setGrupoOmega2(List<Double[]> grupoOmega2) {
		this.grupoOmega2 = grupoOmega2;
	}

	public double calcularDistanciaEuclidianaAAmostra(int linhaAmostra, int centroideReferencia) {
		double catetoX, catetoY, hipotenusa;
		catetoX = Math.pow((conjuntoEntrada.get(linhaAmostra)[0] - centroide[centroideReferencia][0]), 2);
		catetoY = Math.pow((conjuntoEntrada.get(linhaAmostra)[1] - centroide[centroideReferencia][1]), 2);

		hipotenusa = Math.sqrt(catetoY + catetoX);
		return hipotenusa;
	}

	public void atribuirEntradasAGrupoOmega() {
		limparGruposOmegas();
		for (int i = 0; i < conjuntoEntrada.size(); i++) {
			if (conjuntoEntrada.get(i)[2].doubleValue() == -1) {
				System.out.println("Essa amostra nao pode ser usada.");
				continue;
			} else {
				distanciasEuclidianasParaNeuronios[0] = calcularDistanciaEuclidianaAAmostra(i, 0);
				distanciasEuclidianasParaNeuronios[1] = calcularDistanciaEuclidianaAAmostra(i, 1);
				if (distanciasEuclidianasParaNeuronios[0] < distanciasEuclidianasParaNeuronios[1]) {
					// System.out.println("A amostra: " + (i + 1) + " foi colocada no grupo 1");
					grupoOmega1.add(conjuntoEntrada.get(i));
				} else {
					grupoOmega2.add(conjuntoEntrada.get(i));
					// System.out.println("A amostra: " + (i + 1) + " foi colocada no grupo 2");
				}
			}
		}
	}

	public void zerarCentroide() {
		for (int i = 0; i < centroide.length; i++) {
			for (int j = 0; j < centroide[i].length; j++) {
				centroide[i][j] = 0.0;
			}
		}
	}

	public void atualizarCentroide1() {
		double somaCoordenadasX = 0.0;
		double somaCoordenadasY = 0.0;
		for (int i = 0; i < grupoOmega1.size(); i++) {
			somaCoordenadasX += grupoOmega1.get(i)[0].doubleValue();
			somaCoordenadasY += grupoOmega1.get(i)[1].doubleValue();
		}
		centroide[0][0] = somaCoordenadasX / grupoOmega1.size();
		centroide[0][1] = somaCoordenadasY / grupoOmega1.size();

	}

	public void atualizarCentroide2() {
		double somaCoordenadasX = 0.0;
		double somaCoordenadasY = 0.0;
		for (int i = 0; i < grupoOmega2.size(); i++) {
			somaCoordenadasX += grupoOmega2.get(i)[0].doubleValue();
			somaCoordenadasY += grupoOmega2.get(i)[1].doubleValue();
		}
		centroide[1][0] = somaCoordenadasX / grupoOmega2.size();
		centroide[1][1] = somaCoordenadasY / grupoOmega2.size();

	}

	public void limparGruposOmegas() {
		grupoOmega1.removeAll(grupoOmega1);
		grupoOmega2.removeAll(grupoOmega2);
	}

	public void salvarGrupoOmega1Anterior() {
		grupoOmega1Anterior.removeAll(grupoOmega1Anterior);
		for (int i = 0; i < grupoOmega1.size(); i++) {
			grupoOmega1Anterior.add(i, grupoOmega1.get(i));
		}
	}

	public void encontrarCentroides() {
		while (!grupoOmega1.equals(grupoOmega1Anterior)) {
			atualizarCentroide1();
			atualizarCentroide2();
			salvarGrupoOmega1Anterior();
			atribuirEntradasAGrupoOmega();
		}
	}

	public void calcularVarianciaCentroide1() {
		double variancia, somatorio = 0.0, catetoX, catetoY;
		for (int i = 0; i < grupoOmega1.size(); i++) {
			catetoX = Math.pow((centroide[0][0] - grupoOmega1.get(i)[0].doubleValue()), 2);
			catetoY = Math.pow((centroide[0][1] - grupoOmega1.get(i)[1].doubleValue()), 2);
			somatorio += (catetoX + catetoY);
		}

		variancia = somatorio / grupoOmega1.size();
		this.varianciaCentroide1 = variancia;
		System.out.println("Variancia da centroide 1: " + variancia);
	}

	public void calcularVarianciaCentroide2() {
		double variancia, somatorio = 0.0, catetoX, catetoY;
		for (int i = 0; i < grupoOmega2.size(); i++) {
			catetoX = Math.pow((centroide[1][0] - grupoOmega2.get(i)[0].doubleValue()), 2);
			catetoY = Math.pow((centroide[1][1] - grupoOmega2.get(i)[1].doubleValue()), 2);
			somatorio += (catetoX + catetoY);
		}

		variancia = somatorio / grupoOmega2.size();
		this.varianciaCentroide2 = variancia;
		System.out.println("Variancia da centroide 2: " + variancia);
	}

	public void primeiroEstagioTreinamento() {
		atribuirEntradasAGrupoOmega();
		encontrarCentroides();
		calcularVarianciaCentroide1();
		calcularVarianciaCentroide2();
	}

	public void obterG(int linhaConjuntoEntrada) {
		double distanciaCentroide1 = 0.0, distanciaCentroide2 = 0.0, g1, g2;
		for (int i = 0; i < qtdEntradas; i++) {
			distanciaCentroide1 += Math.pow((conjuntoEntrada.get(linhaConjuntoEntrada)[i] - centroide[0][i]), 2);
		}

		double expoente = distanciaCentroide1 / (2 * varianciaCentroide1);
		g1 = Math.exp(-expoente);
		for (int i = 0; i < qtdEntradas; i++) {
			distanciaCentroide2 += Math.pow((conjuntoEntrada.get(linhaConjuntoEntrada)[i] - centroide[1][i]), 2);
		}
		g2 = Math.exp(-(distanciaCentroide1) / (2 * varianciaCentroide2));

		System.out.println("g1: " + g1);
		System.out.println("g2: " + g2);
		Double[] auxiliar = new Double[2];
		auxiliar[0] = g1;
		auxiliar[1] = g2;

		vetorG.add(linhaConjuntoEntrada, auxiliar);
	}

	public void obterI2(int linhaMatriz) {
		I2 = 0.0;
		I2 = W2[0] * -1.0;
		for (int i = 0; i < vetorG.get(linhaMatriz).length; i++) {
			I2 += vetorG.get(linhaMatriz)[i] * W2[i + 1];
		}
	}

	public void obterY2() {
		Y2 = I2;
	}

	public double obterY2Ajustado(double I2) {
		if (Y2 >= 0) {
			Y2Ajustado = 1;
		} else {
			Y2Ajustado = -1;
		}
		return Y2Ajustado;
	}

	public void imprimirQualquer(List<Double[]> objeto) {
		for (int i = 0; i < objeto.size(); i++) {
			for (int j = 0; j < objeto.get(0).length; j++) {
				System.out.print(objeto.get(i)[j] + ", ");
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}

	public void obterDeltaCamadaSaida(int linhaEntradaMatriz) {
		deltaCamadaSaida = conjuntoEntrada.get(linhaEntradaMatriz)[2] - Y2;
	}

	public void ajustarPesosCamadaSaida() {
		for (int i = 0; i < W2.length; i++) {
			W2[i] = W2[i] + taxaAprendizagem * deltaCamadaSaida * Y2;
		}
	}

	public double obterEk(int linhaMatrizEntrada) {
		double ek = 0.0;
		obterI2(linhaMatrizEntrada);
		obterY2();
		ek = Math.pow((conjuntoEntrada.get(linhaMatrizEntrada)[2] - Y2), 2);
		return ek / 2;
	}

	public double obterEm() {
		double somatorio = 0.0;
		for (int i = 0; i < conjuntoEntrada.size(); i++) {
			somatorio += obterEk(i);
		}
		return somatorio / conjuntoEntrada.size();
	}

	public void propagation(int i) {
		obterI2(i);
		obterY2();
		obterDeltaCamadaSaida(i);
		ajustarPesosCamadaSaida();

	}

	public void resetarG() {
		vetorG.clear();
	}

	public void faseOperacao() {
		resetarG();
		System.out.println("Vetor G: ");
		imprimirQualquer(vetorG);
		
		setConjuntoEntrada(conjuntoOperacao);
		for (int i = 0; i < conjuntoEntrada.size(); i++) {
			obterG(i);
			obterI2(i);
			obterY2();
			obterY2Ajustado(I2);
			System.out.println("Y2:  " + Y2 + ", Y2 Ajustado: " + Y2Ajustado);
		}
	}

	public void imprimirQualquer(Double[] objeto) {
		for (int i = 0; i < objeto.length; i++) {
			System.out.print(objeto[i] + ", ");
		}
		System.out.println("\n\n");
	}

	public void imprimirQualquer(Double[][] objeto) {
		for (int i = 0; i < objeto.length; i++) {
			for (int j = 0; j < objeto[i].length; j++) {
				System.out.print(objeto[i][j] + ", ");
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}

	public void imprimirGrupoCentroide1() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("/home/lgcaobianco/repositorios/epc-rna/epc7/src/base/grupoCentroide1.csv",
				"UTF-8");
		for (int i = 0; i < grupoOmega1.size(); i++) {
			writer.println(i + "," + grupoOmega1.get(i)[0] + "," + grupoOmega1.get(i)[1]);
		}
		writer.close();
	}

	public void imprimirGrupoCentroide2() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("/home/lgcaobianco/repositorios/epc-rna/epc7/src/base/grupoCentroide2.csv",
				"UTF-8");
		for (int i = 0; i < grupoOmega2.size(); i++) {
			writer.println(i + "," + grupoOmega2.get(i)[0] + "," + grupoOmega2.get(i)[1]);
		}
		writer.close();
	}
}

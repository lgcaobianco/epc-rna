package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * created by lgcaobianco on 2018-05-26
 */

public class RBFDinamica {
	private Double[][] centroide;
	private Double[] distanciasEuclidianasParaNeuronios;
	private Double[] W2;
	private static final int quantidadeNeuronios = 15;
	private static final int quantidadeEntradas = 3;
	private List<Double[]> conjuntoEntrada, conjuntoOperacao;
	private List<Double[]> vetorG;
	private ArrayList<ArrayList<Double[]>> grupoOmega;
	private ArrayList<ArrayList<Double[]>> grupoOmegaAnterior;
	private List<Double> varianciasCentroides;
	private double I2, Y2, Y2Ajustado;
	private double deltaCamadaSaida;
	private double taxaAprendizagem = 0.01;

	public ArrayList<ArrayList<Double[]>> getGruposOmegas() {
		return grupoOmega;
	}

	public void setGruposOmegas(ArrayList<ArrayList<Double[]>> gruposOmegas) {
		this.grupoOmega = gruposOmegas;
	}

	public ArrayList<ArrayList<Double[]>> getGrupoOmegaAnterior() {
		return grupoOmegaAnterior;
	}

	public void setGrupoOmegaAnterior(ArrayList<ArrayList<Double[]>> grupoOmegaAnterior) {
		this.grupoOmegaAnterior = grupoOmegaAnterior;
	}

	public List<Double> getVarianciasCentroides() {
		return varianciasCentroides;
	}

	public void setVarianciasCentroides(List<Double> varianciasCentroides) {
		this.varianciasCentroides = varianciasCentroides;
	}

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

	public Double[] getW2() {
		return W2;
	}

	public RBFDinamica() {
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc8/src/base/conjunto-treinamento", ".csv");
		conjuntoEntrada = leitor.extrairPontos();

		leitor = new LeitorPontosEntrada("/home/lgcaobianco/repositorios/epc-rna/epc8/src/base/conjunto-operacao",
				".csv");
		conjuntoOperacao = leitor.extrairPontos();

		centroide = new Double[quantidadeNeuronios][quantidadeEntradas];

		for (int i = 0; i < quantidadeNeuronios; i++) {
			for (int j = 0; j < quantidadeEntradas; j++) {
				centroide[i][j] = conjuntoEntrada.get(i)[j];
			}
		}

		System.out.println("Centroide inicial");
		for (int i = 0; i < quantidadeNeuronios; i++) {
			for (int j = 0; j < quantidadeEntradas; j++) {
				System.out.print(centroide[i][j] + " ");
			}
			System.out.println();
		}
		Random random = new Random();
		W2 = new Double[quantidadeNeuronios+1];
		for (int i = 0; i < W2.length; i++) {
			W2[i] = random.nextDouble();
		}

		grupoOmega = new ArrayList<ArrayList<Double[]>>();
		for (int i = 0; i < quantidadeNeuronios; i++) {
			grupoOmega.add(i, new ArrayList<Double[]>());
		}
		grupoOmegaAnterior = new ArrayList<ArrayList<Double[]>>();
		for (int i = 0; i < quantidadeNeuronios; i++) {
			grupoOmegaAnterior.add(i, new ArrayList<Double[]>());
		}
		vetorG = new ArrayList<Double[]>();
		distanciasEuclidianasParaNeuronios = new Double[quantidadeNeuronios];
		varianciasCentroides = new ArrayList<Double>();

	}

	public double calcularDistanciaEuclidianaAAmostra(int linhaAmostra, int centroideReferencia) {
		double catetoX, catetoY, catetoZ, hipotenusa;
		catetoX = Math.pow((conjuntoEntrada.get(linhaAmostra)[0] - centroide[centroideReferencia][0]), 2);
		catetoY = Math.pow((conjuntoEntrada.get(linhaAmostra)[1] - centroide[centroideReferencia][1]), 2);
		catetoZ = Math.pow((conjuntoEntrada.get(linhaAmostra)[2] - centroide[centroideReferencia][2]), 2);

		hipotenusa = Math.sqrt(catetoY + catetoX + catetoZ);
		return hipotenusa;
	}

	public void atribuirEntradasAGrupoOmega() {
		limparGruposOmegas();
		for (int i = 0; i < conjuntoEntrada.size(); i++) {
			for (int j = 0; j < quantidadeNeuronios; j++) {
				distanciasEuclidianasParaNeuronios[j] = calcularDistanciaEuclidianaAAmostra(i, j);
			}
			Double menorValor;
			int posicaoCentroide = 0;
			menorValor = distanciasEuclidianasParaNeuronios[0];
			for (int j = 1; j < distanciasEuclidianasParaNeuronios.length; j++) {
				if (menorValor > distanciasEuclidianasParaNeuronios[j]) {
					menorValor = distanciasEuclidianasParaNeuronios[j];
					posicaoCentroide = j;
				}
			}
			ArrayList<Double[]> aux = new ArrayList<Double[]>();
			aux = grupoOmega.get(posicaoCentroide);
			aux.add(conjuntoEntrada.get(i));
			grupoOmega.set(posicaoCentroide, aux);

		}
	}

	public void zerarCentroide() {
		for (int i = 0; i < centroide.length; i++) {
			for (int j = 0; j < centroide[i].length; j++) {
				centroide[i][j] = 0.0;
			}
		}
	}

	public void atualizarCentroides() {
		double somaCoordenadaX = 0, somaCoordenadaY = 0, somaCoordenadaZ = 0;
		for (int i = 0; i < grupoOmega.size(); i++) {
			for (int j = 0; j < grupoOmega.get(i).size(); j++) {
				somaCoordenadaX += grupoOmega.get(i).get(j)[0];
				somaCoordenadaY += grupoOmega.get(i).get(j)[1];
				somaCoordenadaZ += grupoOmega.get(i).get(j)[2];
			}
			centroide[i][0] = somaCoordenadaX / grupoOmega.get(i).size();
			centroide[i][1] = somaCoordenadaY / grupoOmega.get(i).size();
			centroide[i][2] = somaCoordenadaZ / grupoOmega.get(i).size();

			somaCoordenadaX = 0.0;
			somaCoordenadaY = 0.0;
			somaCoordenadaZ = 0.0;
		}
	}

	public void limparGruposOmegas() {
		for (int i = 0; i < grupoOmega.size(); i++) {
			grupoOmega.get(i).removeAll(grupoOmega.get(i));
		}
	}

	public void salvarGrupoOmegaAnterior() {
		for (int i = 0; i < grupoOmegaAnterior.size(); i++) {
			grupoOmegaAnterior.get(i).removeAll(grupoOmegaAnterior.get(i));
		}
		for (int i = 0; i < grupoOmega.size(); i++) {
			for (int j = 0; j < grupoOmega.get(i).size(); j++) {
				grupoOmegaAnterior.get(i).add(grupoOmega.get(i).get(j));
			}
		}
	}

	public boolean compararGruposOmegas() {
		for (int i = 0; i < grupoOmega.size(); i++) {
			for (int j = 0; j < grupoOmega.get(i).size(); j++) {
				for (int k = 0; k < grupoOmega.get(i).get(j).length; k++) {
					if (grupoOmega.get(i).get(j)[k].doubleValue() != grupoOmegaAnterior.get(i).get(j)[k]
							.doubleValue()) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public void encontrarCentroides() {
		int loops = 0;
		salvarGrupoOmegaAnterior();
		atualizarCentroides();
		limparGruposOmegas();
		atribuirEntradasAGrupoOmega();
		while (!compararGruposOmegas()) {
			salvarGrupoOmegaAnterior();
			atualizarCentroides();
			limparGruposOmegas();
			atribuirEntradasAGrupoOmega();

		}
		System.out.println((loops + 1) + " Loops executados");
	}

	public void primeiroEstagioTreinamento() {
		atribuirEntradasAGrupoOmega();
		encontrarCentroides();
		calcularVarianciaCentroides();
	}

	public void calcularVarianciaCentroides() {
		double variancia, somatorio = 0.0, catetoX, catetoY, catetoZ;
		for (int i = 0; i < grupoOmega.size(); i++) {
			for (int j = 0; j < grupoOmega.get(i).size(); j++) {
				catetoX = Math.pow((centroide[i][0] - grupoOmega.get(i).get(j)[0]), 2);
				catetoY = Math.pow((centroide[i][1] - grupoOmega.get(i).get(j)[1]), 2);
				catetoZ = Math.pow((centroide[i][2] - grupoOmega.get(i).get(j)[2]), 2);

				somatorio += catetoX + catetoY + catetoZ;
			}
			variancia = somatorio / grupoOmega.get(i).size();
			somatorio = 0.0;
			varianciasCentroides.add(variancia);

			System.out.println("A variancia da centroide: " + i + "foi: " + variancia);
		}
	}

	public void obterG(int linhaConjuntoEntrada) {
		double somatorio = 0.0;
		Double[] vetorAuxiliar = new Double[1];
		for (int i = 0; i < centroide.length; i++) {
			for (int j = 0; j < centroide[i].length; j++) {
				somatorio += Math.pow((conjuntoEntrada.get(linhaConjuntoEntrada)[j] - centroide[i][j]), 2);
			}
			double expoente = somatorio / (2 * varianciasCentroides.get(i));
			vetorAuxiliar[0] = Math.exp((-1 * expoente));
			vetorG.add(vetorAuxiliar);
			somatorio = 0.0;
		}
	}

	public void obterI2(int linhaPseudoElementos) {
		I2 = 0.0;
		I2 = W2[0] * -1;
		for (int i = 1; i < W2.length; i++) {
			I2 += vetorG.get(linhaPseudoElementos)[0] * W2[i];
		}
	}

	public void obterY2() {
		Y2 = I2;
	}

	public void obterDeltaCamadaSaida(int linhaMatrizEntrada) {
		deltaCamadaSaida = conjuntoEntrada.get(linhaMatrizEntrada)[quantidadeEntradas] - Y2;
	}

	public void ajustarPesosCamadaSaida() {
		for (int i = 0; i < W2.length; i++) {
			W2[i] = W2[i] + (taxaAprendizagem * deltaCamadaSaida * Y2);
		}
	}

	public double obterEk(int linhaMatrizEntrada) {
		double ek = 0.0;
		obterI2(linhaMatrizEntrada);
		obterY2();
		ek = Math.pow((conjuntoEntrada.get(linhaMatrizEntrada)[quantidadeEntradas] - Y2), 2);
		return ek / 2;
	}

	public double obterEm() {
		double somatorio = 0.0;
		for (int i = 0; i < conjuntoEntrada.size(); i++) {
			somatorio += obterEk(i);
		}
		return (somatorio / conjuntoEntrada.size());
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

		setConjuntoEntrada(conjuntoOperacao);

		for (int i = 0; i < conjuntoEntrada.size(); i++) {
			obterG(i);
			obterI2(i);
			obterY2();
			System.out.println("Y: " + Y2);
		}
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

	public void imprimirGrupoOmega() {
		System.out.println("Impressao de grupos omegas.");

		for (int i = 0; i < grupoOmega.size(); i++) {
			System.out.println("No grupo omega:" + i + " temos: ");
			for (int j = 0; j < grupoOmega.get(i).size(); j++) {
				for (int k = 0; k < grupoOmega.get(i).get(j).length; k++) {
					System.out.print(grupoOmega.get(i).get(j)[k] + " ");
				}
				System.out.println();
			}
			System.out.println("Tamanho do grupo: " + grupoOmega.get(i).size());
			System.out.println("\n");
		}
	}

	public void imprimirGrupoOmegaAnterior() {
		System.out.println("Impressao dos grupos omegas anteriores.");

		for (int i = 0; i < grupoOmegaAnterior.size(); i++) {
			System.out.println("No grupo omega anterior:" + i + " temos: ");
			for (int j = 0; j < grupoOmegaAnterior.get(i).size(); j++) {
				for (int k = 0; k < grupoOmegaAnterior.get(i).get(j).length; k++) {
					System.out.print(grupoOmegaAnterior.get(i).get(j)[k] + " ");
				}
				System.out.println();
			}
			System.out.println("\n");
		}
	}

	public void imprimirQualquer(Double[] object) {
		for (int i = 0; i < object.length; i++) {
			System.out.println(object[i] + " ");
		}
		System.out.println("\n\n");
	}
}

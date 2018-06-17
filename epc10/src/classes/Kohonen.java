package classes;

import java.util.ArrayList;
import java.util.List;

import org.omg.PortableServer.LifespanPolicyOperations;

/**
 * created by lgcaobianco on 2018-06-16
 */

public class Kohonen {
	private double[][] grid;
	private List<Double[]> valoresEntrada;
	private List<Double[]> conjuntoOperacao;
	private double taxaAprendizagem;
	private ArrayList<ArrayList<Integer[]>> listaVizinhos;
	private double raioVizinhanca = 1;
	private int quantidadeNeuronios = 16;
	private double[][] W;

	public void setValoresEntrada(List<Double[]> valoresEntrada) {
		this.valoresEntrada = valoresEntrada;
	}
	
	public List<Double[]> getConjuntoOperacao() {
		return this.conjuntoOperacao;
	}

	public Kohonen() {
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc10/src/base/valoresEntrada", ".csv");
		valoresEntrada = leitor.extrairPontos();
		leitor = new LeitorPontosEntrada("/home/lgcaobianco/repositorios/epc-rna/epc10/src/base/conjuntoOperacao",
				".csv");
		conjuntoOperacao = leitor.extrairPontos();
		Printer.imprimirQualquer(valoresEntrada);
		grid = new double[4][4];
		W = new double[16][3];
		listaVizinhos = new ArrayList<ArrayList<Integer[]>>();
		for (int i = 0; i < quantidadeNeuronios; i++) {
			listaVizinhos.add(i, new ArrayList<Integer[]>());
		}
		int inicioX = 0, inicioY = 0;
		double distanciaX, distanciaY, distancia;
		while (inicioX < 4) {
			while (inicioY < 4) {
				for (int i = 0; i < grid.length; i++) {
					for (int j = 0; j < grid[i].length; j++) {
						distanciaX = Math.pow(inicioX - i, 2);
						distanciaY = Math.pow(inicioY - j, 2);
						distancia = Math.sqrt(distanciaX + distanciaY);
						if (distancia <= 1 && distancia != 0) {
							Integer[] vetorAux = new Integer[2];
							vetorAux[0] = i;
							vetorAux[1] = j;
							listaVizinhos.get((inicioX * 4) + inicioY).add(vetorAux);
						}
					}
				}
				inicioY++;
			}
			inicioY = 0;
			inicioX++;
		}
		Printer.imprimirQualquer(listaVizinhos);
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 3; j++) {
				W[i][j] = valoresEntrada.get(i)[j].doubleValue();
			}
		}
		System.out.println("W INICIAL");
		Printer.imprimirQualquer(W);
	}

	public double calcularDistanciaEuclidianaEntreEntradaENeuronio(int linhaMatrizEntrada, int linhaMatrizNeuronios) {
		double catetoX, catetoY, catetoZ;
		catetoX = Math.pow(W[linhaMatrizNeuronios][0] - valoresEntrada.get(linhaMatrizEntrada)[0], 2);
		catetoY = Math.pow(W[linhaMatrizNeuronios][1] - valoresEntrada.get(linhaMatrizEntrada)[1], 2);
		catetoZ = Math.pow(W[linhaMatrizNeuronios][2] - valoresEntrada.get(linhaMatrizEntrada)[2], 2);
		return Math.sqrt(catetoX + catetoY + catetoZ);

	}

	public void faseTreinamento() {
		double distanciaAmostraParaNeuronio, distanciaAnterior = 1;
		int posicaoMenorDistancia = 0;
		for (int i = 0; i < valoresEntrada.size(); i++) {
			distanciaAnterior = 1;
			for (int j = 0; j < W.length; j++) {
				distanciaAmostraParaNeuronio = calcularDistanciaEuclidianaEntreEntradaENeuronio(i, j);
				if (distanciaAmostraParaNeuronio < distanciaAnterior) {
					distanciaAnterior = distanciaAmostraParaNeuronio;
					posicaoMenorDistancia = j;
				}
			}
			ajustarPesosNeuronio(posicaoMenorDistancia, i);
		}
	}

	private void ajustarPesosNeuronio(int posicaoMenorDistancia, int linhaMatrizEntrada) {
		for (int i = 0; i < W[posicaoMenorDistancia].length; i++) {
			W[posicaoMenorDistancia][i] = W[posicaoMenorDistancia][i]
					+ taxaAprendizagem * (valoresEntrada.get(linhaMatrizEntrada)[i] - W[posicaoMenorDistancia][i]);
		}
		for (int i = 0; i < listaVizinhos.get(posicaoMenorDistancia).size(); i++) {
			int linhaNeuoronioVizinho = listaVizinhos.get(posicaoMenorDistancia).get(i)[0] * 4
					+ listaVizinhos.get(posicaoMenorDistancia).get(i)[1];
			ajustePesoNeuronioVizinho(linhaNeuoronioVizinho, linhaMatrizEntrada);
			normalizarVetorVizinho(linhaNeuoronioVizinho);
		}

	}

	private void ajustePesoNeuronioVizinho(int linhaNeuoronioVizinho, int linhaMatrizEntrada) {
		for (int i = 0; i < W[linhaNeuoronioVizinho].length; i++) {
			W[linhaNeuoronioVizinho][i] = W[linhaNeuoronioVizinho][i] + ((taxaAprendizagem / 2)
					* (valoresEntrada.get(linhaMatrizEntrada)[i] - W[linhaNeuoronioVizinho][i]));
		}
	}

	private void normalizarVetorVizinho(int linhaNeuoronioVizinho) {
		double somaCoordenadas = 0;
		for (int i = 0; i < W[linhaNeuoronioVizinho].length; i++) {
			somaCoordenadas += Math.pow(W[linhaNeuoronioVizinho][i], 2);
		}
		for (int i = 0; i < W[linhaNeuoronioVizinho].length; i++) {
			W[linhaNeuoronioVizinho][i] = W[linhaNeuoronioVizinho][i] / Math.sqrt(somaCoordenadas);
		}

	}

	public void treinarKohonen() {
		int contadorEpocas = 0;
		while (contadorEpocas < 10000) {
			faseTreinamento();
			contadorEpocas++;
		}
		System.out.println("W FINAL");
		Printer.imprimirQualquer(W);
	}

	public void faseOperacao() {
		int posicaoFinal = 0;
		for (int i = 0; i < valoresEntrada.size(); i++) {
			double distanciaAnterior = calcularDistanciaEuclidianaEntreEntradaENeuronio(i, 0);
			for (int j = 1; j < W.length; j++) {
				double distanciaAtual = calcularDistanciaEuclidianaEntreEntradaENeuronio(i, j);
				if (distanciaAtual < distanciaAnterior) {
					distanciaAtual = distanciaAnterior;
					posicaoFinal = j;
				}
			}
			System.out.println("A amostra de entrada: " + i + " foi atribuida ao neuronio: " + posicaoFinal);
		}
	}

}

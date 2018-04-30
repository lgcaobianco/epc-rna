package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * created by lgcaobianco on 2018-04-29
 */

public class NeuronioPrimeiraCamada {

	private Double[][] matrizPesos = new Double[10][4];
	private List<Double[]> matrizInputs;
	private Double[] matrizI = new Double[10];

	public NeuronioPrimeiraCamada() {
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc4/src/base/conjunto-treinamento", ".csv");
		this.matrizInputs = leitor.extrairPontos();
		Random random = new Random();
		inicializarMatrizI();
		for (int i = 0; i < 10; i++) {
			this.matrizPesos[i][0] = (double) -1;
			for (int j = 1; j <= 3; j++) {
				this.matrizPesos[i][j] = random.nextDouble();
			}
		}
	}

	public void inicializarMatrizI() {
		for (int i = 0; i < 10; i++) {
			matrizI[i] = (double) 0;
		}
	}

	public void imprimirMatrizI() {
		for (int i = 0; i < 10; i++) {
			System.out.print(this.matrizI[i] + " ");
		}
		System.out.println();
	}

	public void imprimirMatrizInputs() {
		for (int i = 0; i < matrizInputs.size(); i++) {
			for (int j = 0; j < matrizInputs.get(i).length; j++) {
				System.out.print(matrizInputs.get(i)[j] + " ");
			}
			System.out.println();
		}
	}

	public void imprimirMatrizPesos() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j <= 3; j++) {
				System.out.print(this.matrizPesos[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void calcularMatrizI(int linhaMatrizInput) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 4; j++) {
				matrizI[i] += matrizInputs.get(linhaMatrizInput)[j] * matrizPesos[i][j];
			}
		}
	}

}

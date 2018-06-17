package classes;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * created by lgcaobianco on 2018-06-16
 */

public class Hopfield {
	private double[][] vetoresZ;
	private double[][] w;
	private double[][] sinalRuidoso;
	private double[] vetorSaida;
	private double[] vetorSaidaAnterior;
	private List<Double[]> valoresEntrada;
	private double[][] matrizIdentidade;
	private int p = 4;
	private int n = 45;
	private double eta = 0.01;

	public Hopfield() {
		vetoresZ = new double[p][n];
		matrizIdentidade = new double[n][n];
		w = new double[n][n];
		vetorSaida = new double[n];
		vetorSaidaAnterior = new double[n];

		// obter a matriz de vetores Z
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc9/src/base/padrao1", ".csv");
		valoresEntrada = leitor.extrairPontos();
		Printer.imprimirQualquer(valoresEntrada);
		for (int i = 0; i < p; i++) {
			for (int j = 0; j < n; j++) {
				vetoresZ[i][j] = valoresEntrada.get(i)[j];
				vetorSaidaAnterior[j] = 0;
			}
		}

		// obter o sinal ruidoso
		leitor = new LeitorPontosEntrada("/home/lgcaobianco/repositorios/epc-rna/epc9/src/base/sinalRuidoso", ".csv");
		valoresEntrada = leitor.extrairPontos();
		sinalRuidoso = new double[valoresEntrada.size()][valoresEntrada.get(0).length];
		for (int i = 0; i < valoresEntrada.size(); i++) {
			for (int j = 0; j < valoresEntrada.get(i).length; j++) {
				sinalRuidoso[i][j] = valoresEntrada.get(i)[j];
			}
		}

		Printer.imprimirQualquer(sinalRuidoso);

		// obter a matriz identidade
		for (int i = 0; i < matrizIdentidade.length; i++) {
			for (int j = 0; j < matrizIdentidade[i].length; j++) {
				if (i == j) {
					matrizIdentidade[i][j] = 1;
					continue;
				}
				matrizIdentidade[i][j] = 0;
			}
		}
		// obter a matriz W
		double somatorio = 0;
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				for (int k = 0; k < p; k++) {
					somatorio += (vetoresZ[k][i] * vetoresZ[k][j]);
				}
				w[i][j] = (somatorio / n) - ((p / n) * matrizIdentidade[i][j]);
				somatorio = 0;
			}
		}

		Printer.imprimirQualquer(w);

		Printer.imprimirQualquer(matrizIdentidade);
	}

	public void obterVetorSaida() {
		double somatorio = 0;
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				somatorio += (w[i][j] * sinalRuidoso[0][j]);
			}
			vetorSaida[i] = funcaoG(somatorio);
			somatorio = 0;
		}
		Printer.imprimirQualquer(vetorSaida);
	}

	public double funcaoG(double saidaRede) {
		if (saidaRede > 0) {
			return 1;
		}
		return -1;
	}

	public void obterVetorSaidaAnterior() {
		for (int i = 0; i < sinalRuidoso.length; i++) {
			vetorSaidaAnterior[i] = sinalRuidoso[0][i];
			sinalRuidoso[0][i] = vetorSaida[i];
		}
	}

	public double obterErro() {
		double erro = 0;
		for (int k = 0; k < p; k++) {
			for (int i = 0; i < vetorSaida.length; i++) {
				erro += Math.pow((vetoresZ[k][i] - vetorSaida[i]), 2);
			}
		}

		return erro;
	}

	public void obterRespostaRede() {
		obterVetorSaida();
		double erro = obterErro();
		int contadorRepeticoes = 0;
		while (Math.abs(erro) > 0.1 && contadorRepeticoes < 1000) {
			System.out.println();
			obterVetorSaidaAnterior();
			obterVetorSaida();
			erro = obterErro();
			contadorRepeticoes++;
		}

		try {
			imprimirResultadoEmFormatoTabela();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void imprimirResultadoEmFormatoTabela() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(
				"/home/lgcaobianco/repositorios/epc-rna/epc9/src/base/imagemRecuperada.csv", "UTF-8");
		for (int i = 0; i < n; i++) {
			if(i%5==0) {
				writer.append("\n");
			}
			writer.append(vetorSaida[i] + ",");
			
		}

		writer.close();
	}

}

package teste;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import classes.MultilayerPerceptronClassificador;

/**
 * created by lgcaobianco on 2018-05-19
 */

public class TreinarMLP {
	public static Double[] ajustarValor(Double[][] resultadosObtidos) {
		Double[] resultadoAjustado = new Double[3];
		for (int i = 0; i < resultadosObtidos.length; i++) {
			if (resultadosObtidos[i][0] >= 0.5) {
				resultadoAjustado[i] = 1.0;
			} else {
				resultadoAjustado[i] = 0.0;
			}
		}
		return resultadoAjustado;
	}

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		MultilayerPerceptronClassificador mlp = new MultilayerPerceptronClassificador();
		double epsilon = Math.pow(10, -6);
		int contadorEpocas = 0;
		double erroAnterior = 10.0;
		double erroAtual = 0;
		PrintWriter writer = new PrintWriter("/home/lgcaobianco/repositorios/epc-rna/epc5/src/base/eqm.csv", "UTF-8");
		long inicioTreinamento = System.currentTimeMillis();
		while (Math.abs(erroAtual - erroAnterior) > epsilon) {

			for (int i = 0; i < mlp.getListaPontosEntrada().size(); i++) {
				mlp.forwardPropagation(i);
				mlp.backwardPropagation(i);
				mlp.forwardPropagation(i);
			}

			erroAnterior = erroAtual;
			erroAtual = mlp.calcularEm();
			writer.println(contadorEpocas + "," + Math.abs(erroAtual - erroAnterior));
			contadorEpocas++;
		}
		long fimTreinamento = System.currentTimeMillis();

		System.out.println("Tempo gasto: " + (fimTreinamento - inicioTreinamento));
		mlp.setListaPontosEntrada(mlp.getMatrizOperacao());
		System.out.println("Epocas: " + contadorEpocas);
		System.out.println("tamanho do conjunto de operacao: " + mlp.getListaPontosEntrada().size());
		int acertos = 0;
		List<Double[]> resultadosEsperados = mlp.getClassificacaoMatrizOperacao();
		List<Double[]> resultadosAjustados = new ArrayList<>();
		writer.close();
		writer = new PrintWriter("/home/lgcaobianco/repositorios/epc-rna/epc5/src/base/saida-obtida-fase-operacao.csv",
				"UTF-8");
		for (int i = 0; i < mlp.getMatrizOperacao().size(); i++) {
			mlp.forwardPropagation(i);
			resultadosAjustados.add(ajustarValor(mlp.getY2()));
			writer.print(resultadosAjustados.get(i)[0] + "," + resultadosAjustados.get(i)[1] + ","
					+ resultadosAjustados.get(i)[2] + ",");
			writer.print(mlp.getY2()[0][0] + "," + mlp.getY2()[1][0] + "," + mlp.getY2()[2][0]);
			writer.print("\n");
		}

		for (int i = 0; i < resultadosAjustados.size(); i++) {
			if (resultadosAjustados.get(i)[0].equals(resultadosEsperados.get(i)[0])
					&& resultadosAjustados.get(i)[1].equals(resultadosEsperados.get(i)[1])
					&& resultadosAjustados.get(i)[2].equals(resultadosEsperados.get(i)[2])) {
				acertos++;
			}
		}
		writer.close();
		System.out.println("A rede acertou: " + acertos);

	}

}

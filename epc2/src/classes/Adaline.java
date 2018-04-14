package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * created by lgcaobianco on 2018-04-14
 */

public class Adaline {
	List<Double[]> inputs;
	List<Double> coeficientesIniciais = new ArrayList<Double>();
	List<Double> coeficientesFinais = new ArrayList<Double>();
	List<Double> u = new ArrayList<Double>(35);
	int contadorEpocas = 0;
	double taxaAprendizagem;
	double erroQuadraticoAtual = 0;
	double erroQuadraticoAnterior = 0;

	public Adaline(String nomeArquivo, String tipoArquivo) {
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(nomeArquivo, tipoArquivo);
		this.inputs = leitor.extrairPontos();
		for (int i = 0; i < (inputs.get(0).length - 1); i++) {
			Random random = new Random();
			double randomDouble = random.nextDouble();
			coeficientesIniciais.add(randomDouble);
			coeficientesFinais.add(randomDouble);
			taxaAprendizagem = 0.0025;
		}
	}

	public void imprimirMatrizInputs() {
		for (int i = 0; i < inputs.size(); i++) {
			for (int j = 0; j < inputs.get(i).length; j++) {
				System.out.print(inputs.get(i)[j] + " ");
			}
			System.out.println();
		}
	}

	public void imprimirMatrizCoeficientes(String qualMatriz) {
		if (qualMatriz.equals("inicial")) {
			for (int i = 0; i < coeficientesIniciais.size(); i++) {
				System.out.print(coeficientesIniciais.get(i) + " ");
			}
			System.out.println();
		}
		if (qualMatriz.equals("final")) {
			for (int i = 0; i < coeficientesFinais.size(); i++) {
				System.out.print(coeficientesFinais.get(i) + " ");
			}
			System.out.println();
		}
	}

	public double combinador(int linhaMatriz) {
		double somatorio = 0;
		for (int i = 0; i < coeficientesFinais.size(); i++) {
			somatorio += coeficientesFinais.get(i) * inputs.get(linhaMatriz)[i];
		}
		return somatorio;
	}

	public void treinarAdaline() {
		double novoElemento = 0, u = 0;
		for (int i = 0; i < inputs.size(); i++) {
			u = combinador(i);
			for (int j = 0; j < coeficientesFinais.size(); j++) {
				novoElemento = coeficientesFinais.get(j)
						+ (taxaAprendizagem * (inputs.get(i)[5] - u) * inputs.get(i)[j]);
				coeficientesFinais.set(j, novoElemento);
			}
		}
	}

	public double calcularErroQuadratico() {
		double eqm = 0, u = 0;
		for (int i = 0; i < inputs.size(); i++) {
			u = combinador(i);
			eqm += Math.pow((inputs.get(i)[5] - u), 2);
		}

		return eqm / inputs.size();
	}

	public static void main(String[] args) {
		double epsilon = Math.pow(10, -6);
		List<Adaline> adalines = new ArrayList<Adaline>(5);
		for (int i = 0; i < 5; i++) {
			Adaline aux = new Adaline("/home/lgcaobianco/repositorios/epc-rna/epc2/src/classes/base/inputs", ".csv");
			adalines.add(aux);
		}

		for (Adaline adaline : adalines) {
			adaline.erroQuadraticoAtual = adaline.calcularErroQuadratico();
			while (Math.abs(adaline.erroQuadraticoAtual - adaline.erroQuadraticoAnterior) > epsilon) {
				adaline.treinarAdaline();
				adaline.erroQuadraticoAnterior = adaline.erroQuadraticoAtual;
				adaline.erroQuadraticoAtual = adaline.calcularErroQuadratico();
				adaline.contadorEpocas++;
			}
			System.out.println("Os coeficientes finais são: ");
			adaline.imprimirMatrizCoeficientes("final");
			System.out.println("O erro quadratico terminou em: " + adaline.erroQuadraticoAtual + "com: "
					+ adaline.contadorEpocas + " epocas.");
			System.out.println();
		}
	}

}

package teste;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import classes.MLP3;

/**
 * created by lgcaobianco on 2018-05-20
 */

public class TreinarMLP {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		MLP3 mlp  = new MLP3(5,10);
		double epsilon = 0.5 * Math.pow(10, -6);
		int contadorEpocas = 0;
		double erroAnterior = 10.0;
		double erroAtual = 0.0;
		mlp.imprimirMatrizInputs();
		mlp.imprimirMatrizW1();
		mlp.imprimirMatrizW2();
		PrintWriter writer = new PrintWriter("/home/lgcaobianco/repositorios/epc-rna/epc6/src/base/eqm.csv", "UTF-8");
		long inicioTreinamento = System.currentTimeMillis();
		System.out.println("Erro atual" + erroAtual + ", erro anterior: " + erroAnterior + ", diferenca: " +(erroAtual-erroAnterior));
		while (Math.abs(erroAtual - erroAnterior) > epsilon) {
			for (int i = 0; i < mlp.getMatrizInputs().size(); i++) {
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
		System.out.println("Tempo gasto: " + (fimTreinamento - inicioTreinamento) + ", quantidade de epocas: " + contadorEpocas);
		writer.close();
		for(int i =0; i<20; i++) {
			mlp.faseOperacao(100+i 	);	
		}
		
	}
}

package classes;

import java.util.ArrayList;
import java.util.List;

/**
 * created by lgcaobianco on 2018-06-16
 */

public class Printer {
	public static void imprimirQualquer(double[][] object) {
		for (int i = 0; i < object.length; i++) {
			for (int j = 0; j < object[i].length; j++) {
				System.out.print(object[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}

	public static void imprimirQualquer(double[] object) {
		for (int i = 0; i < object.length; i++) {
			System.out.print(object[i] + " ");
		}
		System.out.println("\n\n");
	}
	
	public static void imprimirQualquer(List<Double[]>  object) {
		for (int i = 0; i < object.size(); i++) {
			for (int j = 0; j < object.get(i).length; j++) {
				System.out.print(object.get(i)[j] + " ");
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}

	public static void imprimirQualquer(ArrayList<ArrayList<Integer[]>> listaVizinhos) {
		System.out.println("Impressao da lista de vizinhos");
		
		
		for(int i=0; i<listaVizinhos.size(); i++) {
		System.out.println("Os pontos vizinhos do neuronio: " + (i+1) + " são: ");	
			for(int j=0; j<listaVizinhos.get(i).size(); j++) {
				System.out.println("x: " + listaVizinhos.get(i).get(j)[0] + ", Y: " + listaVizinhos.get(i).get(j)[1]);
			}
			System.out.println();
		}
	}
	
}

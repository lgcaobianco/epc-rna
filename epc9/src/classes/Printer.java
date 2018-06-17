package classes;

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
	
}

package teste;

import classes.Kohonen;

/**
 * created by lgcaobianco on 2018-06-16
 */

public class TestarKohonen {
	public static void main(String[] args) {
		Kohonen kohonen = new Kohonen();
		kohonen.treinarKohonen();
		kohonen.faseOperacao();
		kohonen.setValoresEntrada(kohonen.getConjuntoOperacao());
		kohonen.faseOperacao();
	}
}

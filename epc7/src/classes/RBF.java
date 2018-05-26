package classes;

import java.util.ArrayList;
import java.util.List;

/**
 * created by lgcaobianco on 2018-05-23
 */

public class RBF {
	private List<Double[]> conjuntoEntrada;
	private List<Double[]> neuroniosPrimeiraCamada;
	private List<Double[]> neuroniosPrimeiraCamadaIteracaoAnterior;
	private List<Double[]> grupoOmega1;
	private List<Double[]> grupoOmega2;
	private Double[] distanciasEuclidianasParaNeuronios;
	private static final int quantidadeNeuroniosPrimeiraCamada = 2;

	public RBF() {
		LeitorPontosEntrada leitor = new LeitorPontosEntrada(
				"/home/lgcaobianco/repositorios/epc-rna/epc7/src/base/conjunto-treinamento", ".csv");
		this.conjuntoEntrada = leitor.extrairPontos();
		neuroniosPrimeiraCamada = new ArrayList<Double[]>();
		grupoOmega1 = new ArrayList<Double[]>();
		neuroniosPrimeiraCamadaIteracaoAnterior = new ArrayList<Double[]>();
		grupoOmega2 = new ArrayList<Double[]>();
		distanciasEuclidianasParaNeuronios = new Double[quantidadeNeuroniosPrimeiraCamada];
		for (int i = 0; i < quantidadeNeuroniosPrimeiraCamada; i++) {
			neuroniosPrimeiraCamada.add(i, conjuntoEntrada.get(i));
			neuroniosPrimeiraCamadaIteracaoAnterior.add(i, conjuntoEntrada.get(i));
		}

	}

	public List<Double[]> getConjuntoEntrada() {
		return conjuntoEntrada;
	}

	public void setConjuntoEntrada(List<Double[]> conjuntoEntrada) {
		this.conjuntoEntrada = conjuntoEntrada;
	}

	public List<Double[]> getNeuroniosPrimeiraCamada() {
		return neuroniosPrimeiraCamada;
	}

	public void setNeuroniosPrimeiraCamada(List<Double[]> neuroniosPrimeiraCamada) {
		this.neuroniosPrimeiraCamada = neuroniosPrimeiraCamada;
	}

	public List<Double[]> getNeuroniosPrimeiraCamadaIteracaoAnterior() {
		return neuroniosPrimeiraCamadaIteracaoAnterior;
	}

	public void setNeuroniosPrimeiraCamadaIteracaoAnterior(List<Double[]> neuroniosPrimeiraCamadaIteracaoAnterior) {
		this.neuroniosPrimeiraCamadaIteracaoAnterior = neuroniosPrimeiraCamadaIteracaoAnterior;
	}

	public List<Double[]> getGrupoOmega1() {
		return grupoOmega1;
	}

	public void setGrupoOmega1(List<Double[]> grupoOmega1) {
		this.grupoOmega1 = grupoOmega1;
	}

	public List<Double[]> getGrupoOmega2() {
		return grupoOmega2;
	}

	public void setGrupoOmega2(List<Double[]> grupoOmega2) {
		this.grupoOmega2 = grupoOmega2;
	}

	public Double[] getDistanciasEuclidianasParaNeuronios() {
		return distanciasEuclidianasParaNeuronios;
	}

	public void setDistanciasEuclidianasParaNeuronios(Double[] distanciasEuclidianasParaNeuronios) {
		this.distanciasEuclidianasParaNeuronios = distanciasEuclidianasParaNeuronios;
	}

	public int getQuantidadeNeuroniosPrimeiraCamada() {
		return quantidadeNeuroniosPrimeiraCamada;
	}

	public double calcularDistanciaEuclidiana(int amostraEntrada, int neuronioReferencia) {
		double distanciaEuclidiana, a, b;
		a = Math.pow((conjuntoEntrada.get(amostraEntrada)[0] - neuroniosPrimeiraCamada.get(neuronioReferencia)[0]), 2);
		b = Math.pow((conjuntoEntrada.get(amostraEntrada)[1] - neuroniosPrimeiraCamada.get(neuronioReferencia)[1]), 2);
		distanciaEuclidiana = Math.sqrt(a + b);
		return distanciaEuclidiana;
	}

	public void atribuirEntradasAGrupoOmega() {
		for (int i = 0; i < conjuntoEntrada.size(); i++) {
			distanciasEuclidianasParaNeuronios[0] = calcularDistanciaEuclidiana(i, 0);
			distanciasEuclidianasParaNeuronios[1] = calcularDistanciaEuclidiana(i, 1);
			if (distanciasEuclidianasParaNeuronios[0] < distanciasEuclidianasParaNeuronios[1]
					&& (!neuroniosPrimeiraCamada.get(0).equals(conjuntoEntrada.get(i))
							|| !neuroniosPrimeiraCamada.get(1).equals(conjuntoEntrada.get(i)))) {
				System.out.println("A amostra" + (i + 1) + " foi colocada no grupo 1");
				grupoOmega1.add(conjuntoEntrada.get(i));
			} else {
				grupoOmega2.add(conjuntoEntrada.get(i));
				System.out.println("A amostra" + (i + 1) + " foi colocada no grupo 2");
			}
		}
	}

	public void imprimirQualquer(List<Double[]> objeto) {
		for (int i = 0; i < objeto.size(); i++) {
			for (int j = 0; j < objeto.get(0).length; j++) {
				System.out.print(objeto.get(i)[j] + ", ");
			}
			System.out.println();
		}
		System.out.println("\n\n");
	}

	public void imprimirQualquer(Double[] objeto) {
		for (int i = 0; i < objeto.length; i++) {
			System.out.print(objeto[i] + ", ");
		}
		System.out.println("\n\n");
	}

	public void atualizarPesosNeuroniosPrimeiraCamada() {
		System.out.println("Atualizando o neuronio da primeira camada...");
		Double[] aux = new Double[3];
		for (int j = 0; j < neuroniosPrimeiraCamada.size(); j++) {
			for (int i = 0; i < aux.length; i++) {
				aux[i] = neuroniosPrimeiraCamada.get(j)[i].doubleValue();
			}
			neuroniosPrimeiraCamadaIteracaoAnterior.set(j, aux);
		}

		for (int i = 0; i < quantidadeNeuroniosPrimeiraCamada; i++) {
			this.neuroniosPrimeiraCamada.get(i)[0] = 0.0;
			this.neuroniosPrimeiraCamada.get(i)[1] = 0.0;
			if (i == 0) {
				for (int j = 0; j < grupoOmega1.size(); j++) {
					this.neuroniosPrimeiraCamada.get(i)[0] += (grupoOmega1.get(j)[0] / grupoOmega1.size());
					this.neuroniosPrimeiraCamada.get(i)[1] += (grupoOmega1.get(j)[1] / grupoOmega1.size());
				}
			}
			if (i == 1) {
				for (int j = 0; j < grupoOmega2.size(); j++) {
					neuroniosPrimeiraCamada.get(i)[0] += (grupoOmega2.get(j)[0] / grupoOmega1.size());
					neuroniosPrimeiraCamada.get(i)[1] += (grupoOmega2.get(j)[1] / grupoOmega1.size());
				}
			}
		}
	}

	public void primeiroEstagioTreinamento() {
		int contador = 0;
		while (!neuroniosPrimeiraCamada.equals(neuroniosPrimeiraCamadaIteracaoAnterior)) {
			System.out.println("Neuronios atuais");
			imprimirQualquer(neuroniosPrimeiraCamada);
			System.out.println("Neuronios anteriores");
			imprimirQualquer(neuroniosPrimeiraCamadaIteracaoAnterior);
			atribuirEntradasAGrupoOmega();
			atualizarPesosNeuroniosPrimeiraCamada();
			contador++;
		}
		System.out.println(contador);
	}

}

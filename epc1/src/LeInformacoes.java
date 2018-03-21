/**
 * * created by lgcaobianco on 21/03/18 **
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeInformacoes {

    private String nomeArquivo;
    private String formato;
    private String separadorValor;
    
    private String getNomeArquivo() {
        return nomeArquivo;
    }

    private String getFormato() {
        return formato;
    }

    private String getSeparadorValor() {
        return separadorValor;
    }

    private LeInformacoes(String nomeArquivo, String formato) {
        this.nomeArquivo = nomeArquivo;
        this.formato = formato;
        switch (formato) {
            case ".csv":
                this.separadorValor = ",";
                break;
            case ".txt":
                this.separadorValor = " ";
                break;
            default:
                System.out.println("Formato ainda não suportado");
                System.exit(1);
                break;

        }
    }

    public static void imprimirMatriz(double[][] matrizCoeficientes, int totalLinhas,
                                      int totalColunas) {
        for (int i = 0; i < totalLinhas; i++) {
            for (int j = 0; j < totalColunas; j++) {
                System.out.print(matrizCoeficientes[i][j] + " ");
            }
            System.out.println();
        }
    }

    private double[][] extrairCoeficientes() {
        int totalRows = 4, totalColumns = 4;
        double[][] matrizCoeficientes = new double[totalRows][totalColumns];
        BufferedReader stream = null;
        try {
            stream = new BufferedReader(new FileReader(getNomeArquivo() + getFormato()));
            for (int currentRow = 0; currentRow < totalRows; currentRow++) {
                String line = stream.readLine();

                String[] splitted = line.split(getSeparadorValor());
                for (int currentColumn = 0; currentColumn < totalColumns; currentColumn++) {
                    matrizCoeficientes[currentRow][currentColumn] = Double.parseDouble(splitted[currentColumn]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        return matrizCoeficientes;
    }

    //main p/ teste
    public static void main(String[] args) {
        //inicializa a classe
        LeInformacoes teste = new LeInformacoes("/home/lgcaobianco/repositorios/epc/epc1/src/arquivoTeste", ".txt");

        double[][] matrizCoeficientes = teste.extrairCoeficientes();

        teste.imprimirMatriz(matrizCoeficientes, 4, 4);

    }

}

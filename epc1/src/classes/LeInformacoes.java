package classes;
/**
 * * created by lgcaobianco on 21/03/18 **
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public  LeInformacoes(String nomeArquivo, String formato) {
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



    public List<double[]> extrairPontos() {
        List<double[]> matrizPontos = new ArrayList<double[]>();
        String linhaLida = "";
        BufferedReader stream = null;
        try {
            stream = new BufferedReader(new FileReader(getNomeArquivo() + getFormato()));
            while ((linhaLida = stream.readLine()) != null) {
                String[] temporario = linhaLida.split(getSeparadorValor());
                double[] numerosSeparados = new double[temporario.length];
                for (int i = 0; i < temporario.length; i++) {
                    numerosSeparados[i] = Double.parseDouble(temporario[i]);
                }
                matrizPontos.add(numerosSeparados);

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
        return matrizPontos;
    }

}

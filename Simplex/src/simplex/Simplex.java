/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplex;

/**
 *
 * @author CITec
 */
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Simplex {

    Scanner input = new Scanner(System.in);
    int linha = 4;
    int coluna = 8;
    double[][] matrizA;
    double[] vetorSolucao;
    double custoTotal;

    //abrir arquivo .txt
    public void AbrirArquivo() {
        try {
            input = new Scanner(new File("simplex.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Simplex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //ler arquivo 
    public void LerArquivo() {

        matrizA = new double[linha][coluna];
        while (input.hasNextInt()) {
            for (int i = 0; i < linha; i++) {
                for (int j = 0; j < coluna; j++) {
                    matrizA[i][j] = input.nextInt();
                }
            }
        }
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                System.out.print(matrizA[i][j] + " ");
                System.out.print('\t');
            }
            System.out.println('\n');
        }
    }

    //encontrar o maior custo = analisa a ultima linha
    public int MaiorCusto() {
        double custoMaior;
        custoMaior = matrizA[matrizA.length - 1][0];
        int colunaMaior = 0;
        for (int j = 0; j < (coluna); j++) {
            if (custoMaior < matrizA[matrizA.length - 1][j]) {
                custoMaior = matrizA[matrizA.length - 1][j];
                colunaMaior = j;
            }
        }
        //System.err.println("" + colunaMaior);
        return colunaMaior;
    }

    //Localizar linha pivo
    public int SelecaoLinhaPivo() {
        int z = MaiorCusto();
        double[] vetor = new double[linha - 1];
        for (int i = 0; i < linha - 1; i++) {
            vetor[i] = (int) matrizA[i][coluna - 1] / matrizA[i][z];//divisao p/encontrar menor num
        }

        double menor = 999999;
        int indice = 0;

        for (int j = 0; j < vetor.length; j++) {
            if ((vetor[j] > 0) && (menor > vetor[j])) { // estou comparando se é maior que zero e menor que menor
                menor = vetor[j];
                indice = j;//vai receber a posiaco no vetor de menor valor
            }

        }
        return indice;
    }

    public void Escalonamento() {
        //coordenadas do pivo
        int pivoColuna = MaiorCusto();
        int pivoLinha = SelecaoLinhaPivo();

        double pivo = matrizA[pivoLinha][pivoColuna];
        double mudarNum;
        //trocando o valor do pivo por 1
        for (int j = 0; j < coluna; j++) {
            if (pivo != 0) {
                matrizA[pivoLinha][j] = matrizA[pivoLinha][j] / pivo;
            }
        }
        pivo = matrizA[pivoLinha][pivoColuna];
        for (int i = 0; i < linha; i++) {
            mudarNum = matrizA[i][pivoColuna];
            if (i != pivoLinha) {
                for (int j = 0; j < coluna; j++) {
                    //continhas de escalonamento = pivo*linhaMudar - numZerar*LinhaPivo
                    matrizA[i][j] = (pivo * matrizA[i][j]) - (mudarNum * matrizA[pivoLinha][j]);
                }
            }
        }
        //imprimir matriz modificada
        for (int i = 0; i < linha; i++) {
            for (int j = 0; j < coluna; j++) {
                System.out.print(matrizA[i][j]);
            }
            System.out.println('\n');
        }
        //return matrizA[i][j];
    }
    //Verificar se inda existe numeros positivos na ultima linha - 

    public boolean CustoReduzido() {
        for (int j = 0; j < coluna; j++) {
            if (matrizA[matrizA.length - 1][j] > 0) {
                return true;
            }
        }
        return false;
    }

    private void Solucao() {
        double soma;
        int index = 0;
        vetorSolucao = new double[linha];
        for (int i = 0; i < linha; i++) {
            vetorSolucao[i] = matrizA[i][coluna - 1];//vai receber os valores da coluna b
        }

        //somando os valores da coluna
        for (int j = 0; j < coluna - 1; j++) {
            soma = 0;
            for (int i = 0; i < linha; i++) {
                soma = soma + matrizA[i][j];
            }

            if (soma == 1) {
                for (int i = 0; i < linha; i++) {
                    if (matrizA[i][j] == 1) {
                        index = i;
                    }
                }
                System.out.println("Nao basica x" + (j + 1) + " = " + matrizA[index][coluna - 1]);
            } else {
                System.out.println("Basica x" + (j + 1) + " = 0");
            }
        }
        System.out.println("Z = " + -1 * matrizA[linha - 1][coluna - 1]);

    }

    public void Solucionar() {
        while (CustoReduzido()) {
            System.out.println("****OPERACOES DE ESCALONAMENTO****");
            Escalonamento();

            // printMat();
        }
        Solucao();
    }

    public static void main(String[] args) {

        Simplex simplex = new Simplex();
        simplex.AbrirArquivo();
        simplex.LerArquivo();
        simplex.MaiorCusto();
        simplex.Escalonamento();
        simplex.Solucionar();
    }
}

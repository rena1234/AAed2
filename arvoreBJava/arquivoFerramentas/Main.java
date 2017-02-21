package arquivoFerramentas;

import java.io.*;
import java.util.Scanner;

import arvoreB.ArvoreBMais;
import hash.HashTable;

import static java.util.Arrays.fill;

public class Main{


    public static void main(String [] args){
        RandomAccessFile arqBinario = null;
        HashTable hash = null;
        ArvoreBMais arv = null;

        try{
            arqBinario = new RandomAccessFile("arquivos/arqBinario","rw");

            /*
             * 8000 é um número genérico, na versão final será um número primo bem grande
             */
            hash = new HashTable(8000);
            arv = new ArvoreBMais("arquivos/arqArv",121);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        try {
            if(arqBinario.length() == 0){
                Inicializador.transformaBinario(arqBinario);
                Inicializador.geraIndices(arqBinario,arv,hash);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scanner scan = new Scanner(System.in);
        boolean sair = false;
        int opt;
        do {
            System.out.println("1 para inserção");
            System.out.println("2 para busca");
            System.out.println("3 para remoção");
            System.out.println("4 para sair");
            System.out.print("Opção: ");
            opt = scan.nextInt();
            switch(opt){
                case 1: Controle.insere(arv,hash,arqBinario);
                    break;
                case 2: Controle.busca(arv,hash,arqBinario);
                    break;
                case 3: Controle.remove(arv,hash,arqBinario);
                    break;
                case 4: sair = true;
                default: System.out.println("Digite uma opção válida");

            }
        }
        while(!sair);

        arv.encerrar();
        try {
            arqBinario.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
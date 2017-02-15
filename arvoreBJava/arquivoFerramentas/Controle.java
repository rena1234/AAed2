
package arquivoFerramentas;

import java.io.IOException;
import java.util.Scanner;

import arvoreB.ArvoreBMais;
import hash.HashTable;

public class Controle{

    public static void insere(ArvoreBMais arv, HashTable hash){

        /*Em algum lugar tem ou vai ter uma classe/método que
         * escreve o registro em arqBinario( arq do orleans em bin )
         * e retorna o offset aonde o registro está.
         */
        long offsetRegistro;  /* Recebe o offset em que o registro foi
        *colocado ver comentário acima
        */ 

        long chave;
        boolean retardo = false;
        Scanner scan = new Scanner(System.in);
        do{
            if(retardo)
                System.out.println("Digite uma chave válida");
            
            System.out.println("Digite a chave a ser inserida");
            chave = scan.nextLong();
            if (chave < 0) retardo = true;
            else retardo = false;
        }
        while(retardo);
        
        try{
            hash.insere(chave,offsetRegistro);
            arv.inserirArvore(chave,offsetRegistro);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(" ");
    }

    public static void busca(ArvoreBMais arv, HashTable hash){
        long chave;
        boolean retardo = false;
        do{
            if(retardo)
                System.out.println("Digite uma chave válida");
            
            System.out.println("Digite a chave a ser removida");
            chave = scan.nextLong();
            if (chave < 0) retardo = true;
            else retardo = false;
        }
        while(retardo);
        int opt;
        do{
            if(retardo)
                System.out.println("Digite uma opção válida");
            
            System.out.println("1 para busca sequencial");
            System.out.println("2 para busca Hash-Table");
            System.out.println("3 para busca Árvore B+");
            System.out.println(" ");
            opt = scan.nextLong();
            if (opt < 1 || opt > 3 ) retardo = true;
        }
        while(retardo);

        long offsetRegistro;
        
        try{
            switch(opt){
                case 1: offsetRegistro = Sequencial.busca(chave); 
                        break;
                case 2: offsetRegistro = hash.buscaRegistro(chave); 
                        break;
                case 3: offsetRegistro = arv.buscaOffsetChave(chave); 
                        break; 
                default: break;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void remove(ArvoreBMais arv, HashTable hash){
        long chave;
        boolean retardo = false;
        Scanner scan = new Scanner(System.in);
        do{
            if(retardo)
                System.out.println("Digite uma chave válida");
            else
                System.out.println("Digite a chave excluida");
            chave = scan.nextLong();
            if (chave < 0) retardo = true;
        }
        while(retardo);
        
        try{
            hash.delete(chave);
            arv.removerChave(chave);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }

}

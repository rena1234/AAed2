package arquivoFerramentas;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.Scanner;

import arvoreB.ArvoreBMais;
import hash.HashTable;

public class Controle{

    public static void insere(ArvoreBMais arv, HashTable hash, RandomAccessFile file){

        //long offsetRegistro;
        String chaveString;
        long chave = 0;
        boolean retardo = false;
        Scanner scan = new Scanner(System.in);
        do{
            if(retardo)
                System.out.println("Digite uma chave válida");

            System.out.print("Digite o cpf: ");
            chaveString = scan.nextLine();
            if (chaveString.length() <= 0) retardo = true;
            else if(chaveString.length() >14) retardo = true;
            else retardo = false;
        }
        while(retardo);
        String cpf2= chaveString.replaceAll("[\\D]","");
        chave = Long.parseLong(cpf2);
        if(arv.buscaOffsetChave(chave) != -1){
            System.out.println("CPF já existe na base de dados!");
        }else{
            String nome,endereco,email,data_nascimento,telefone;
            int numCompras = -1;
            do{
                if(retardo)
                    System.out.println("Digite um nome válido");

                System.out.print("Digite o nome: ");
                nome = scan.nextLine();
                if (nome.length() <= 0) retardo = true;
                else retardo = false;
            }
            while(retardo);

            do{
                if(retardo)
                    System.out.println("Digite um endereço válido!");

                System.out.print("Digite o endereço: ");
                endereco = scan.nextLine();
                if (endereco.length() <= 0) retardo = true;
                else retardo = false;
            }
            while(retardo);

            do{
                if(retardo)
                    System.out.println("Digite um e-mail válido!");

                System.out.print("Digite o e-mail: ");
                email = scan.nextLine();
                if (email.length() <= 0) retardo = true;
                else retardo = false;
            }
            while(retardo);

            do{
                if(retardo)
                    System.out.println("Digite uma data de nascimento válida");

                System.out.print("Digite a data de nascimento: ");
                data_nascimento = scan.nextLine();
                if (data_nascimento.length() <= 0) retardo = true;
                else retardo = false;
            }
            while(retardo);

            do{
                if(retardo)
                    System.out.println("Digite um telefone válido");

                System.out.print("Digite o telefone: ");
                telefone = scan.nextLine();
                if (telefone.length() <= 0) retardo = true;
                else retardo = false;
            }
            while(retardo);
            do{
                if(retardo)
                    System.out.println("Digite um número de compras válido");

                System.out.print("Digite o número de compras: ");
                numCompras = scan.nextInt();
                if (numCompras == -1) retardo = true;
                else retardo = false;
            }
            while(retardo);
            Cliente c = new Cliente(chaveString,nome,endereco,email,data_nascimento,telefone,numCompras);
            c.escreveRegistro(file,arv,hash);
        }

    }

    public static void busca(ArvoreBMais arv, HashTable hash, RandomAccessFile file){
        String chaveString;
        long chave = 0;
        boolean retardo = false;
        Scanner scan = new Scanner(System.in);
        do{
            if(retardo)
                System.out.println("Digite uma chave válida");

            System.out.print("Digite a chave a ser buscada: ");
            chaveString = scan.nextLine();
            if (chaveString.length() <= 0) retardo = true;
            else if(chaveString.length() >14) retardo = true;
            else retardo = false;
        }
        while(retardo);
        String cpf2= chaveString.replaceAll("[\\D]","");
        chave = Long.parseLong(cpf2);
        int opt;
        do{
            if(retardo)
                System.out.println("Digite uma opção válida");

            System.out.println("1 para busca sequencial");
            System.out.println("2 para busca Hash-Table");
            System.out.println("3 para busca Árvore B+");
            System.out.print("Opção: ");
            opt = scan.nextInt();
            if (opt < 1 || opt > 3 ) retardo = true;
        }
        while(retardo);

        long offsetRegistro = -1;

        try{
            switch(opt){
                case 1: offsetRegistro = Inicializador.buscaSequencial(file,chave);
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

        if(offsetRegistro != -1){

            Cliente c = Cliente.leRegistro(file,offsetRegistro);
            System.out.println("CPF: " + c.getCpf() + "\nNome: " + c.getNome() +
                    " \nEndereco: " + c.getEndereco() + "\n email: " + c.getEmail() + "\nnumero de compras: " + c.getNum_compras());
        }else{
            System.out.println("CPF não foi encontrado!");
        }
    }

    public static void remove(ArvoreBMais arv, HashTable hash,RandomAccessFile file){
        String chaveString;
        long chave = 0;
        boolean retardo = false;
        Scanner scan = new Scanner(System.in);
        do{
            if(retardo)
                System.out.println("Digite uma chave válida");

            System.out.print("Digite a chave a ser removida: ");
            chaveString = scan.nextLine();
            if (chaveString.length() <= 0) retardo = true;
            else if(chaveString.length() >14) retardo = true;
            else retardo = false;
        }
        while(retardo);
        String cpf2= chaveString.replaceAll("[\\D]","");
        chave = Long.parseLong(cpf2);
        Cliente.removerRegistro(file,chave,arv,hash);

    }

}
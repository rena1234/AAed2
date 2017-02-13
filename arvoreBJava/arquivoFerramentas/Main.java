package arquivoFerramentas;

import arvoreB.ArvoreBMais;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by gabriel on 12/02/17.
 */
public class Main {
    public static void main(String[] args) {
        ArvoreBMais arv = new ArvoreBMais("arquivos/arvoreB.bin",121);

        try {
            RandomAccessFile file1 = new RandomAccessFile("arquivos/clientes.bin","rw");
            //Inicializador.inserir(file1,arv);
            //Inicializador.transformaBinario(file1);
            //Inicializador.geraIndices(file1,arv);
            //arv.removeChave(12);
            //arv.printaArvore(arv.getRaiz());
           // arv.buscaOffsetChave(25);
            //Inicializador.removeSequencial(file1,arv);

           //Cliente c = new Cliente("999.999.999-99","Jorgin Me empresta a 12", "Rua sem nome, 123","jorgin@ufrrj.br","04/05/1997","(021) 1234-6135",12);
            //c.escreveRegistro(file1,arv);
            /// / c = Cliente.leRegistro(file,arv.buscaOffsetChave(15307498241l));
          //  System.out.println(c.getNum_compras());
            //arv.removeChave(4361582998l);
            //arv.removeChave(4361529833l);
           // System.out.println(arv.buscaOffsetChave(23805914661l));
            //file1.seek(arv.buscaOffsetChave(83094751288l));
            //file1.seek(arv.buscaOffsetChave(99999999999l));

            /*long startTime = System.currentTimeMillis();
                long offset = arv.buscaOffsetChave(83094751288l);
            long endTime = System.currentTimeMillis();

            float duration = ((endTime - startTime)/1000);
            System.out.println("tempo arvore b+ " + duration);
            startTime = System.currentTimeMillis();
                offset = Inicializador.buscaSequencial(file1,83094751288l);
            endTime = System.currentTimeMillis();
            duration = ((endTime - startTime)/1000);
            System.out.println("tempo sequencial " + duration);*/
            //Inicializador.removeSequencial(file1,arv);

            //Cliente.removerRegistro(file1,1234568942l,arv);
            //arv.bsucaSequencia(12309867411l);
           // Cliente c = Cliente.leRegistro(file1,arv.buscaOffsetChave(12340569761l));
            //System.out.println(c.getNome());
            //System.out.println(c.getEndereco());
            file1.close();
            arv.encerrar();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        arv.encerrar();

    }
}


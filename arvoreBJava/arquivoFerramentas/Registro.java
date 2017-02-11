package arquivoFerramentas;

import arvoreB.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by gabriel on 11/02/17.
 */
public class Registro {
    public static void main(String[] args) {
        ArvoreBMais arv = new ArvoreBMais("arvoreB.bin",121);



        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile("clientes.txt","rw");
            String aux;
            long tamanho = 0;
            arv.removeChave(4361582998l);
            arv.removeChave(4361529833l);
            file.seek(arv.buscaOffsetChave(4361528942l));
            System.out.println(file.readLine());
           /* while(file.getFilePointer() != file.length()){
                long offset = file.getFilePointer();
                 aux = file.readLine();
                String cpf = aux.substring(0,14);
                String cpf1= cpf.replaceAll("[\\D]","");
                long cpfNum = Long.parseLong(cpf1);
                arv.inserirArvore(cpfNum,offset);
                tamanho++;
            }
            System.out.println("Tamanho " + tamanho);*/

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        arv.encerrar();

    }
}

package arquivoFerramentas;

import arvoreB.ArvoreBMais;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by gabriel on 12/02/17.
 */
public class Gerenciador {
    public static void transformaBinario(RandomAccessFile file1){
        try {
            byte[] fileData = Files.readAllBytes(Paths.get("clientes.txt"));
            file1.write(fileData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void geraIndices(RandomAccessFile file1, ArvoreBMais arv){
        try {
            while(file1.getFilePointer() != file1.length()){
                    String aux;
                    long offset = file1.getFilePointer();
                    aux = file1.readLine();
                    String cpf = aux.substring(0,14);
                    String cpf1= cpf.replaceAll("[\\D]","");
                    long cpfNum = Long.parseLong(cpf1);
                    arv.inserirArvore(cpfNum,offset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

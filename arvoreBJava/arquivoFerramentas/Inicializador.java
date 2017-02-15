package arquivoFerramentas;

import arvoreB.ArvoreBMais;
import hash.HashTable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by gabriel on 12/02/17.
 */
public class Inicializador {

    public static void transformaBinario(RandomAccessFile file1){
        try {
            byte[] fileData = Files.readAllBytes(Paths.get("arquivos/clientes.txt"));
            file1.write(fileData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static  long buscaSequencial(RandomAccessFile file, long cpf){
        long offset = -1;
        try {
            while(file.getFilePointer() != file.length()){
                String aux;
                offset = file.getFilePointer();
                aux = file.readLine();
                String cpf1 = aux.substring(0,14);
                String cpf2= cpf1.replaceAll("[\\D]","");
                long cpfNum = Long.parseLong(cpf2);
                if(cpfNum == cpf) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  offset;
    }
    public static void geraIndices(RandomAccessFile file1, ArvoreBMais arv,
            HashTable hash){
        try {
            file1.seek(0);
            while(file1.getFilePointer() != file1.length()){
                    String aux;
                    long offset = file1.getFilePointer();
                    aux = file1.readLine();
                    String cpf = aux.substring(0,14);
                    String cpf1= cpf.replaceAll("[\\D]","");
                    long cpfNum = Long.parseLong(cpf1);
                    arv.inserirArvore(cpfNum,offset);
                    hash.insere(cpfNum,offset);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package hash;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

//import arquivoFerramentas.Cliente;

public class TesteHashTable{
    public static void main(String [] args){
        /* HashTable hash = null;
        try{
            hash = new HashTable(5);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        try{
           hash.insere(1,59);
           hash.insere(11,600);
           System.out.println("Pus o 11");
           hash.insere(6,333);
           System.out.println("Pus o 6");
           hash.insere(2,25);
           hash.insere(7,99);
          
           hash.delete(0);
           

           System.out.println(hash.buscaRegistro(0));
           System.out.println(hash.buscaRegistro(7));
           System.out.println(hash.buscaRegistro(2));
           System.out.println(hash.buscaRegistro(1));
           System.out.println(hash.buscaRegistro(6));
           System.out.println(hash.buscaRegistro(11));

           //hash.delete(1);
        }catch(IOException e){
            e.printStackTrace();
        }
         */
        RandomAccessFile file1 = null;
        try{
            file1 = new RandomAccessFile("arqRegTest","rw");
          //  byte[] fileData = Files.readAllBytes(Paths.get("clientes.txt"));
          //  file1.write(fileData);

        }catch(IOException e){
            e.printStackTrace();
        }
        HashTable hash = null;
        try{
            hash = new HashTable(874037);
            file1.seek(hash.buscaRegistro(98765431057L));
            System.out.println(file1.readLine());
            //System.out.println(hash.buscaRegistro(98765431057L));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        /*
        try {
            file1.seek(0);
            while(file1.getFilePointer() != file1.length()){
                    String aux;
                    long offset = file1.getFilePointer();
                    aux = file1.readLine();
                    String cpf = aux.substring(0,14);
                    String cpf1= cpf.replaceAll("[\\D]","");
                    long cpfNum = Long.parseLong(cpf1);
                    hash.insere(cpfNum,offset);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

            

    }
}

package arvoreB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.RandomAccess;

/**
 * Created by gabriel on 30/01/17.
 */
public class Main {
    private static String filePath = "espacos2.bin";
    public static void main(String[] args) {
            ArvoreBMais arv = new ArvoreBMais("arvoreB.bin",1);

            //arv.inserirArvore(1,1);//1
            //arv.inserirArvore(2,2);//2

            //arv.inserirArvore(3,3);//3
            //arv.inserirArvore(4,4);//4
            arv.inserirArvore(5,5);
            arv.printaArvore(arv.getRaiz());
            arv.encerrar();



    }
}

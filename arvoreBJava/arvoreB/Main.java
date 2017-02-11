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

           /* arv.inserirArvore(60,60);//1
            arv.inserirArvore(2,2);//2
            arv.inserirArvore(40,40);//3
            arv.inserirArvore(4,4);//4
            arv.inserirArvore(5,5);//5
            arv.inserirArvore(12,12);//6
            arv.inserirArvore(10,10);
            arv.inserirArvore(44,44);
            arv.inserirArvore(11,11);
            arv.inserirArvore(3,3);
            arv.inserirArvore(1,1);
            arv.inserirArvore(87,87);
            arv.inserirArvore(6,6);
            arv.inserirArvore(90,90);
            arv.inserirArvore(96,96);
            arv.inserirArvore(7,7);
            arv.inserirArvore(97,97);
            arv.inserirArvore(98,98);
            arv.inserirArvore(8,8);
            arv.inserirArvore(9,9);

           // System.out.println("OLHA MEU OFFSET MANIN " + arv.buscaOffsetChave(44));
        arv.removeChave(1);
        arv.removeChave(2);
            arv.removeChave(3);
        arv.removeChave(60);
           arv.removeChave(90);*/
           arv.removeChave(10);
        //System.out.println("offset " + arv.buscaOffsetChave(40));

            arv.printaArvore(arv.getRaiz());
            arv.encerrar();



    }
}

package arvoreB;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.RandomAccess;

/**
 * Created by gabriel on 30/01/17.
 */
public class Main {
    public static void main(String[] args) {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile("teste.bin","rw");
            for (int i = 0;i<4;i++){
                int aux = file.readInt();
                System.out.println(aux);
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

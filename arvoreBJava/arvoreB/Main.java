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
        RandomAccessFile file = null;
        RandomAccessFile file2 = null;
        try {
            file = new RandomAccessFile(filePath,"rw");
            file2 = new RandomAccessFile("beba.bin","rw");
            file.seek(file2.readLong());
            file.writeInt(12);
            file.seek(file2.readLong());
            file.writeInt(14);
            file.close();
            file = new RandomAccessFile(filePath,"rw");
            file.seek(file.length());
            file2.writeLong(file.getFilePointer());
            file2.writeLong(file.getFilePointer() + 4);
            file.close();
            file2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}

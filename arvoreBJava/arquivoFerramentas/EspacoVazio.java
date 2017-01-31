Espackage arquivoFerramentas;

import java.io.*;
import java.util.RandomAccess;

/**
 * Created by gabriel on 30/01/17.
 */
public class EspacoVazio {
    private static String filePath = "espacos.bin";

    public static long buscaEspacoVazio(){
        RandomAccessFile file;
        RandomAccessFile file2;
        long offSetEspacoVazio = 0;
        int qtdOffsets= -1;
        try {
            file = new RandomAccessFile(filePath,"r");
            file2 = new RandomAccessFile(filePath + ".temp","w");
            qtdOffsets = file.readInt();
            offSetEspacoVazio = file.readLong();
            file.writeInt(qtdOffsets -1);
            for(int i =0;i<qtdOffsets - 1;i++){
                file2.writeLong(file.readLong());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return offSetEspacoVazio;
    }

}

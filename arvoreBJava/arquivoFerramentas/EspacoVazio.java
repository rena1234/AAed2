package arquivoFerramentas;

import java.io.*;
import java.util.RandomAccess;

/**
 * Created by gabriel on 30/01/17.
 */
public class EspacoVazio {
    private String filePath = "arquivos/espacos";
    private  RandomAccessFile file;
    public EspacoVazio(){
        try {
            this.file = new RandomAccessFile(filePath,"rw");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void escreveOffsetEspVazio(long offset){
        try {
            this.file.seek(this.file.length());
            this.file.writeLong(offset);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public long buscaEspacoVazio(RandomAccessFile bTree){
        long offSetEspacoVazio = 0;
        try {

            if(this.file.length() == 0){
                bTree.seek(bTree.length());
                offSetEspacoVazio = bTree.getFilePointer();
                this.file.writeLong(bTree.length() + 4062*2);
                this.file.writeLong(bTree.length() + 4062);
            } else{
                this.file.seek( this.file.length() - Long.SIZE/Byte.SIZE);
                offSetEspacoVazio = this.file.readLong();
                this.file.setLength( this.file.length() - Long.SIZE/Byte.SIZE );
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return offSetEspacoVazio;
    }

}
package arquivoFerramentas;

import java.io.*;
import java.util.RandomAccess;

/**
 * Created by gabriel on 30/01/17.
 */
public class EspacoVazio {
    private String filePath = "espacos.bin";
    private  RandomAccessFile file;
    public EspacoVazio(){
        try {
            this.file = new RandomAccessFile(filePath,"rw");
            if(this.file.length() == 0){
                this.file.writeInt(0);
            }

            this.file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void escreveOffsetEspVazio(long offset){
        try {
            file = new RandomAccessFile(filePath,"rw");
            int qtdOffsets = file.readInt();
            file.seek(file.length());
            file.writeLong(offset);
            file.seek(0);
            file.writeInt(qtdOffsets + 1);
            this.file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public long buscaEspacoVazio(RandomAccessFile bTree){
        RandomAccessFile file2;
        long offSetEspacoVazio = 0;
        int qtdOffsets= -1;
        try {
            this.file = new RandomAccessFile(filePath,"r");
            file2 = new RandomAccessFile(filePath + ".temp","rw");
            qtdOffsets = this.file.readInt();

            if(qtdOffsets == 0){
                bTree.seek(bTree.length());
                offSetEspacoVazio = bTree.getFilePointer();
                file2.writeInt(2);
                bTree.seek(bTree.length() + 4062); // mudar para 4076
                file2.writeLong(bTree.getFilePointer());
                bTree.seek(bTree.length() + 4062*2); // mudar para 4076
                file2.writeLong(bTree.getFilePointer());
            } else{
                offSetEspacoVazio = this.file.readLong();
                file2.writeInt((qtdOffsets -1));
                for(int i =0;i<qtdOffsets - 1;i++){
                    file2.writeLong(file.readLong());
                }
            }

            file.close();
            file2.close();
            File file1 = new File(filePath);
            File fileTemp = new File(filePath+ ".temp");
            boolean successful = fileTemp.renameTo(file1);
            if(successful == false){
                System.out.println("Deu ruim lesk!");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return offSetEspacoVazio;
    }

}

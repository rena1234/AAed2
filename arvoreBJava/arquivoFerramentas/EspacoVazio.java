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
            this.file.writeInt(0);
            this.file.writeLong(0);
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
            file = new RandomAccessFile(filePath,"r");
            file2 = new RandomAccessFile(filePath + ".temp","rw");
            qtdOffsets = file.readInt();
            offSetEspacoVazio = file.readLong();
            if(qtdOffsets -1 == 0){//n tem mais espacos vazios, por algum motivo '-'
                file2.writeInt(qtdOffsets + 1);
                bTree.seek(bTree.length());
                file2.writeLong(bTree.getFilePointer());
                bTree.seek(bTree.length() + 4076);// odeio constantes, mas n tem oq fazer
                file2.writeLong(bTree.getFilePointer());
            }else{
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

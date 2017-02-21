package arquivoFerramentas;

/**
 * Created by gabriel on 11/02/17.
 */
import java.io.IOException;
import java.io.RandomAccessFile;

public class EspVazio{

    private RandomAccessFile file;

    public  void setLength() throws IOException{
        this.file = new RandomAccessFile("arqTeste.bin", "rw");
        int qtdOffsets = file.readInt();
        file.seek(0);
        file.writeInt(qtdOffsets -1);
        file.setLength( file.length() - Long.SIZE/Byte.SIZE);

    }
}
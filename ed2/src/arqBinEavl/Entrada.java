import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public class Entrada {

    private RandomAccessFile file;
    public static long tamanho(String filePath){
        RandomAccessFile file = null;
        long tam = -1;
        try {
            file = new RandomAccessFile(filePath,"rw");
            tam = file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tam;
    }
    public static void escreve(String filePath, Registro registro){
        byte[] entrada = offsetBytes(registro);
        AVLTree tree = AVLTree.openTree();
        RandomAccessFile file = null;
        try{
            file = new RandomAccessFile(filePath,"rw");
            long posicao = file.length() + 1;
            tree.insert(registro.getMatricula(),posicao);
            tree.saveTree();
            file.seek(posicao);
            file.write(entrada);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String leNome(String filePath,long posicao){
        byte [] offset1 = new byte[4];
        byte [] offset2 = new byte[4];
        offset1 = ler(filePath,posicao,4);
        offset2= ler(filePath,posicao+4,4);
        int tamanho = ByteBuffer.wrap(offset2).getInt() - ByteBuffer.wrap(offset1).getInt();

        byte [] nome = new byte[tamanho];
        nome = ler(filePath,posicao+24,tamanho);
        return new String(nome, Charset.forName("UTF-8"));


    }

    public static int leAno(String filePath,long posicao){

        byte [] ano = ler(filePath,posicao +12,4);
        return ByteBuffer.wrap(ano).getInt();
    }

    public static int leMat(String filePath,long posicao){

        byte [] mat = ler(filePath,posicao +8,4);
        return ByteBuffer.wrap(mat).getInt();
    }

    public static int leSem(String filePath,long posicao){

        byte [] sem = ler(filePath,posicao +16,4);
        return ByteBuffer.wrap(sem).getInt();
    }

    public static int leCod(String filePath,long posicao){

        byte [] cod = ler(filePath,posicao +20,4);
        return ByteBuffer.wrap(cod).getInt();
    }

    public static byte[] ler(String filePath,long posicao, int tamanho){
        byte [] ret = new byte[tamanho];
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(filePath,"r");
            file.seek(posicao);
            file.read(ret);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;


    }
    public static byte[] offsetBytes(Registro registro){
        byte [] nome = registro.getNome().getBytes(Charset.forName("UTF-8"));

        byte[] bytes1 = ByteBuffer.allocate(4).putInt(24).array();//offset inicio do Nome
        byte[] bytes2 = ByteBuffer.allocate(4).putInt(24 + nome.length).array();//offset fim do Nome
        byte[] bytes3 = ByteBuffer.allocate(4).putInt(registro.getMatricula()).array();
        byte[] bytes4 = ByteBuffer.allocate(4).putInt(registro.getAno()).array();
        byte[] bytes5 = ByteBuffer.allocate(4).putInt(registro.getSemestre()).array();
        byte[] bytes6 = ByteBuffer.allocate(4).putInt(registro.getCodigo_curso()).array();
        byte[] bytes7 = nome;



        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write( bytes1 );
            outputStream.write( bytes2 );
            outputStream.write( bytes3 );
            outputStream.write( bytes4 );
            outputStream.write( bytes5 );
            outputStream.write( bytes6 );
            outputStream.write( bytes7 );
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = outputStream.toByteArray();


        return bytes;
    }

}

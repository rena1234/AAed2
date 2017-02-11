package arvoreB;

import java.io.*;
import java.nio.ByteBuffer;

import static java.util.Arrays.fill;

/**
 * Created by gabriel on 30/01/17.
 */
public class Pagina  implements  Serializable{
    private long chaves[];
    private long offsets[];
    private long offsetPai;
    private long offsetPag;
    private int  tamanho;
    private int eh_folha;



    public Pagina(int ordem) {
        this.chaves = new long[2*ordem+1];
        this.offsets = new long[2*ordem + 2];
        fill(offsets,-1);// o offset pode acabar sendo 0, por isso o fill com -1
        this.setOffsetPag(-1);
        this.setOffsetPai(-1);
        this.setTamanho(0);
        this.setEh_folha(1);
    }
    /*calculei a ordem da seguinte maneira
        (2m+1) *sizeof(int) + (2*m+2)*sizeof(long) + 44(offset pai,pag, tamanho,eh_folha) <= 4096(tamanho do bloco)*/
    // ordem d =168 sendo minimo = d e maximo 2d
    //tamanho maximo de uma pagina seguindo esses calculos 4076

    public static void escrevePagina(RandomAccessFile file, Pagina p,int ordem){
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(p);
                o.close();
            }
            file.seek(p.getOffsetPag());
            file.write(b.toByteArray());
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            file.seek(p.getOffsetPag());
            file.writeLong(p.getOffsetPag());
            file.writeLong(p.getOffsetPai());
            file.writeInt(p.getTamanho());
            file.writeInt(p.getEh_folha());
            for(int i = 0;i<2*ordem +1;i++){
                file.writeLong(p.getChave(i));
            }
            for(int i = 0;i< 2*ordem + 2;i++){
                file.writeLong(p.getOffset(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    public static Pagina lePagina(long offset, RandomAccessFile file,int ordem){
        Pagina p = null;
        byte [] bytes = new byte[4062];
        try {
            file.seek(offset);
            file.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                b.close();
                 return (Pagina)o.readObject();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
        /*Pagina p = new Pagina(ordem);
        try {
            file.seek(offset);
            p.setOffsetPag(file.readLong());
            p.setOffsetPai(file.readLong());
            p.setTamanho(file.readInt());
            p.setEh_folha(file.readInt());
            for(int i = 0;i<(2*ordem) +1;i++){
                p.setChave(file.readLong(),i);
            }
            for(int i = 0;i<(2*ordem) +2;i++){
                p.setOffset(file.readLong(),i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p;*/
    }


    public int getEh_folha() {
        return eh_folha;
    }

    public void setEh_folha(int eh_folha) {
        this.eh_folha = eh_folha;
    }

    public long getChave(int pos) {
        return chaves[pos];
    }

    public void setChave(long chave,int pos) {
        this.chaves[pos] = chave;
    }
    public void setOffsetPag(long offsetPag){
        this.offsetPag = offsetPag;
    }
    public long getOffsetPag(){
        return  this.offsetPag;
    }

    public long getOffset(int pos) {
        return offsets[pos];
    }

    public void setOffset(long offset,int pos) {
        this.offsets[pos] = offset;
    }

    public long getOffsetPai() {
        return offsetPai;
    }

    public void setOffsetPai(long offsetPai) {
        this.offsetPai = offsetPai;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
}
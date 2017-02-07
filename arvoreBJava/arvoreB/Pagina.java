package arvoreB;

import java.io.IOException;
import java.io.RandomAccessFile;

import static java.util.Arrays.fill;

/**
 * Created by gabriel on 30/01/17.
 */
public class Pagina {
    private int chaves[];
    private long offsets[];
    private long offsetPai;
    private long offsetPag;
    private int  tamanho;
    private int eh_folha;



    public Pagina(int ordem){
        this.chaves = new int[2*ordem+1];
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
        try {
            file.seek(p.getOffsetPag());
            file.writeLong(p.getOffsetPag());
            file.writeLong(p.getOffsetPai());
            file.writeInt(p.getTamanho());
            file.writeInt(p.getEh_folha());
            for(int i = 0;i<2*ordem +1;i++){
                file.writeInt(p.getChave(i));
            }
            for(int i = 0;i< 2*ordem + 2;i++){
                file.writeLong(p.getOffset(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Pagina lePagina(long offset, RandomAccessFile file,int ordem){
        Pagina p = new Pagina(ordem);
        try {
            file.seek(offset);
            p.setOffsetPag(file.readLong());
            p.setOffsetPai(file.readLong());
            p.setTamanho(file.readInt());
            p.setEh_folha(file.readInt());
            for(int i = 0;i<(2*ordem) +1;i++){
                p.setChave(file.readInt(),i);
            }
            for(int i = 0;i<(2*ordem) +2;i++){
                p.setOffset(file.readLong(),i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p;
    }


    public int getEh_folha() {
        return eh_folha;
    }

    public void setEh_folha(int eh_folha) {
        this.eh_folha = eh_folha;
    }

    public int getChave(int pos) {
        return chaves[pos];
    }

    public void setChave(int chave,int pos) {
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

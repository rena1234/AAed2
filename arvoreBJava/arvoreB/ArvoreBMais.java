package arvoreB;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.RandomAccess;

/**
 * Created by gabriel on 30/01/17.
 */
public class ArvoreBMais {
    private RandomAccessFile file;
    private Pagina raiz;
    private int ordem;


    public ArvoreBMais(String filePath){
        try{
            this.file = new RandomAccessFile(filePath,"rw");
        }catch(IOException e ){
            System.err.print("Nao foi possivel abrir o arquivo");
            e.printStackTrace();
        }

    }
    public int inserirArvore(int chave, long offset){
        return 0;

    }

    /*public void split(Pagina p, int ordem, RandomAccessFile file){
        int metade = p.getTamanho()/2;
        int chaveCentral = p.getChave(metade);
        p.setTamanho(metade);
        Pagina irma = new Pagina(ordem);
        Pagina pai = null; // nao eh inicializado ainda, pois pode ser q ja exista um pai
        irma.setEh_folha(p.getEh_folha()); // se a pagina do split eh folha a irma tbm sera
        irma.setTamanho(metade);
        p.setTamanho(metade);
        if(p.getEh_folha() == 1){// a pagina eh folha entao eu preciso manter os registros np split
            int aux = 0;
            //copia metade ate 2*d para a pagina irma
            for(int i = metade;i<p.getTamanho();i++){
                irma.setChave(p.getChave(i),aux);
                p.setChave(-1,i);//zerei as chaves
                irma.setOffset(p.getOffset(i),aux);
                p.setOffset(-1,i);//zerei os offsets
            }
            if(p.getOffsetPai() == -1){// Se for -1 preciso criar a pagina pai
                pai = new Pagina(ordem);
                pai.setEh_folha(0);//o pai nunca sera folha
                pai.setChave(chaveCentral,0);// subi a chave central para o pai
                pai.setTamanho(1);
                pai.setOffset(p.getOffset(),0);
                long offsetPagPai;
                try {
                    file.seek(file.length());
                    offsetPagPai = file.getFilePointer();
                    irma.setOffset(offsetPagIrma);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        }

        if(p.getOffsetPai() == -1){//Nao tem pagina pai preciso criar uma

        }else{

        }
    }*/



    public RandomAccessFile getFile() {
        return file;
    }

    public void setFile(RandomAccessFile file) {
        this.file = file;
    }

    public Pagina getRaiz() {
        return raiz;
    }

    public void setRaiz(Pagina raiz) {
        this.raiz = raiz;
    }
}


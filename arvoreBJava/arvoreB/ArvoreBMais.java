package arvoreB;

import arquivoFerramentas.EspacoVazio;

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
    private  EspacoVazio ferramenta1;


    public ArvoreBMais(String filePath){
        this.ferramenta1 = new EspacoVazio();
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


    public void split(Pagina p, int ordem, RandomAccessFile file){
        //ATENCAO COLOCAR ENCADEAMENTO NOS NO FOLHA, ESQUECI ASS: GABRIEL DO PRESENTE Q AGR É PASSADO
        int metade = p.getTamanho()/2;
        int chaveCentral = p.getChave(metade);
        p.setTamanho(metade);
        Pagina irma = new Pagina(ordem);
        Pagina pai = null;
        irma.setEh_folha(p.getEh_folha()); // se a pagina do split eh folha a irma tbm sera
        if(p.getEh_folha() == 1){// a pagina eh folha entao eu preciso manter os registros no split
            int aux = 0;
            irma.setTamanho(metade);
            p.setTamanho(metade);
            //copia metade ate 2*d para a pagina irma
            for(int i = metade;i<p.getTamanho();i++){
                irma.setChave(p.getChave(i),aux);
                p.setChave(-1,i);//zerei as chaves
                irma.setOffset(p.getOffset(i),aux);
                p.setOffset(-1,i);//zerei os offsets
                aux += 1;
            }


            if(p.getOffsetPai() == -1){// Se for -1 preciso criar a pagina pai, q vai ser a nova raiz :o
                pai = new Pagina(ordem);
                pai.setEh_folha(0);//o pai nunca sera folha
                pai.setChave(chaveCentral,0);// subi a chave central para o pai
                pai.setTamanho(1);
                pai.setOffsetPai(-1);
                pai.setOffset(p.getOffsetPag(),0);
                long offsetPagPai,offsetPagIrma;
                offsetPagPai = ferramenta1.buscaEspacoVazio(file);
                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
                p.setOffsetPai(offsetPagPai);
                pai.setOffset(offsetPagIrma,1);
                irma.setOffsetPag(offsetPagIrma);
                irma.setOffsetPai(offsetPagPai);
                this.setRaiz(pai);
                Pagina.escrevePagina(file,p,this.getOrdem());
                Pagina.escrevePagina(file,pai,this.getOrdem());
                Pagina.escrevePagina(file,irma,this.getOrdem());
            }else{//Pagina ja tem um pai entao vou inserir o no central nele e atualizar os ponteiros
                long offsetPagIrma;
                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
                irma.setOffsetPai(p.getOffsetPai());
                irma.setOffsetPag(offsetPagIrma);
                pai = Pagina.lePagina(p.getOffsetPai(),file,this.getOrdem());// carrego pai na memoria
                this.inserePaiSplit(pai,chaveCentral,irma.getOffsetPag());
                if(pai.getTamanho() - 1 == this.getOrdem()*2){

                }
                Pagina.escrevePagina(file,p,this.getOrdem());
                Pagina.escrevePagina(file,pai,this.getOrdem());
                Pagina.escrevePagina(file,irma,this.getOrdem());

            }



        }else{// split em uma pagina do index set
            irma.setTamanho(metade -1); //como nao eh folha copia da metade + 1 em diante
            p.setTamanho(metade);
            int aux = 0;
            p.setChave(-1,metade);//zerei a chave
            p.setOffset(-1,metade +1);//zerei o offset
            //copia metade +1 ate 2*d para a pagina irma
            for(int i = metade +1 ;i<p.getTamanho();i++){
                irma.setChave(p.getChave(i),aux);
                p.setChave(-1,i);//zerei as chaves
                irma.setOffset(p.getOffset(i),aux);
                p.setOffset(-1,i);//zerei os offsets
                aux += 1;
            }
        }
    }

    private void inserePaiSplit(Pagina pai, int chave, long offset){
        int tempChave;
        long tempOffset;
        pai.setChave(chave,pai.getTamanho());
        pai.setTamanho(pai.getTamanho() + 1);
        pai.setOffset(offset,pai.getTamanho());
        for(int pos = pai.getTamanho()-1;pos>=0;pos--){
            if(pai.getChave(pos)<pai.getChave(pos - 1)){
                tempChave = pai.getChave(pos);
                tempOffset = pai.getOffset(pos+1);

                pai.setChave(pai.getChave(pos-1),pos);
                pai.setOffset(pai.getOffset(pos),pos+1);
                pai.setChave(tempChave,pos-1);
                pai.setOffset(tempOffset,pos);

            }


        }
    }



    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
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


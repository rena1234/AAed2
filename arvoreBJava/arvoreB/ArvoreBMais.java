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
        this.setRaiz(null);
        try{
            this.file = new RandomAccessFile(filePath,"rw");

        }catch(IOException e ){
            System.err.print("Nao foi possivel abrir o arquivo");
            e.printStackTrace();
        }

    }
    public void inserirArvore(int chave, long offset){
        Pagina auxiliar = this.getRaiz();
        if(auxiliar == null){// nao existe raiz, arvore vazia
            auxiliar = new Pagina(this.getOrdem());
            auxiliar.setChave(chave,0);
            auxiliar.setOffset(offset,0);
            auxiliar.setTamanho(1);
            long offsetEcrita = ferramenta1.buscaEspacoVazio(this.getFile());
            auxiliar.setOffsetPag(offsetEcrita);
            this.setRaiz(auxiliar);
            Pagina.escrevePagina(this.getFile(),auxiliar,this.getOrdem());

        }else{//raiz ja existe começo a buscar a pagina folha onde inseir
            Pagina folha = buscaInserir(auxiliar,chave,offset);
            if(folha.getTamanho() -1 == this.getOrdem()*2){// split
                split(folha,this.getOrdem(),this.getFile());
            }else{// n preciso fazer nd acabou
                Pagina.escrevePagina(this.getFile(),folha,this.getOrdem());
            }
        }

    }

    private Pagina buscaInserir(Pagina p, int chave, long offset){
        int esquerda= 0, direita = p.getTamanho() -1;
        if(p.getEh_folha() == 1){// encontrei pagina folha para inseir
            p.setChave(chave,p.getTamanho());
            // preciso copiar o offset q esta encadeando as irmas sendo ele -1 ou n
            long offsetListaEncadeada = p.getOffset(p.getTamanho());
            p.setOffset(offset,p.getTamanho());
            p.setTamanho(p.getTamanho() + 1);
            p.setOffset(offsetListaEncadeada,p.getTamanho());
            // ordeno pagina
            for(int i = p.getTamanho() -1;i>=0;i--){
                int chaveTemp = p.getChave(i);
                long offsetTemp = p.getOffset(i);
                if(chaveTemp < p.getChave(i -1)){
                    p.setChave(p.getChave(i-1),i);
                    p.setOffset(p.getOffset(i-1),i);
                    p.setChave(chaveTemp,i-1);
                    p.setOffset(offsetTemp,i-1);
                }else{// ja ordenei n tem pq continuar, poderia ser um while talvez
                    break;
                }

            }
        }else{
            if(chave < p.getChave(0)){
                Pagina aux = Pagina.lePagina(p.getOffset(0),this.getFile(),this.getOrdem());
                buscaInserir(aux,chave,offset);
            }else{
                int meio;
                while(esquerda <= direita){
                    meio = (esquerda + direita)/2;
                    if(p.getChave(meio) == chave){
                        // achei chave vou para a pagina
                        Pagina aux = Pagina.lePagina(p.getOffset(meio + 1),this.getFile(),this.getOrdem());
                    }
                    if(p.getChave(meio) < chave){
                        esquerda = meio +1;
                    }else{
                        direita = meio -1;
                    }
                    Pagina aux = Pagina.lePagina(p.getOffset(meio + 1),this.getFile(),this.getOrdem());
                    buscaInserir(aux,chave,offset);
            }


            }//nao encontrei chave no index set, vou para a pagina de baixo

        }
        return p;
    }


    private void split(Pagina p, int ordem, RandomAccessFile file){
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
            for(int i = metade;i<irma.getTamanho();i++){
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
                p.setOffset(offsetPagIrma,metade +1);// encadeamento esse offset aponta pra pag irma
                pai.setOffset(offsetPagIrma,1);
                irma.setOffsetPag(offsetPagIrma);
                irma.setOffsetPai(offsetPagPai);
                this.setRaiz(pai);
            }else{//Pagina ja tem um pai entao vou inserir o no central nele e atualizar os ponteiros
                long offsetPagIrma;
                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
                irma.setOffsetPai(p.getOffsetPai());
                irma.setOffsetPag(offsetPagIrma);
                p.setOffset(offsetPagIrma,metade +1);
                pai = Pagina.lePagina(p.getOffsetPai(),file,this.getOrdem());// carrego pai na memoria
                this.inserePaiSplit(pai,chaveCentral,irma.getOffsetPag());
                if(pai.getTamanho() - 1 == this.getOrdem()*2){
                    split(pai,this.getOrdem(),file);//split pode se propagar entao eh recursivo
                }

            }



        }else{// split em uma pagina do index set
            irma.setTamanho(metade -1); //como nao eh folha copia da metade + 1 em diante
            p.setTamanho(metade);
            int i,aux = 0;
            p.setChave(-1,metade);//zerei a chave
            //p.setOffset(-1,metade +1);//zerei o offset
            //copia metade +1 ate 2*d para a pagina irma
            for(i = metade +1 ;i<irma.getTamanho();i++){
                irma.setChave(p.getChave(i),aux);
                p.setChave(-1,i);//zerei as chaves
                irma.setOffset(p.getOffset(i),aux);
                p.setOffset(-1,i);//zerei os offsets
                aux += 1;
            }
            irma.setOffset(p.getOffset(i),aux);//irma agora tem tamanho + 1 offsets
            p.setOffset(-1,i);

            if(p.getOffsetPai() == -1){// nao tem pai preciso criar e logo ele vai ser a nova raiz
                pai = new Pagina(this.getOrdem());
                pai.setEh_folha(0);
                pai.setTamanho(1);
                pai.setChave(chaveCentral,0);
                pai.setOffset(p.getOffsetPag(),0);
                long offsetPagPai,offsetPagIrma;
                offsetPagPai = ferramenta1.buscaEspacoVazio(file);
                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
                p.setOffsetPai(offsetPagPai);
                pai.setOffset(offsetPagIrma,1);
                irma.setOffsetPag(offsetPagIrma);
                irma.setOffsetPai(offsetPagPai);
                this.setRaiz(pai);
            }else{//ja tem pai
                long offsetPagIrma;
                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
                irma.setOffsetPai(p.getOffsetPai());
                irma.setOffsetPag(offsetPagIrma);
                p.setOffset(offsetPagIrma,metade +1);
                pai = Pagina.lePagina(p.getOffsetPai(),file,this.getOrdem());// carrego pai na memoria
                this.inserePaiSplit(pai,chaveCentral,irma.getOffsetPag());
                if(pai.getTamanho() - 1 == this.getOrdem()*2){
                    split(pai,this.getOrdem(),file);//split pode se propagar entao eh recursivo
                }
            }

        }
        Pagina.escrevePagina(file,p,this.getOrdem());
        Pagina.escrevePagina(file,pai,this.getOrdem());
        Pagina.escrevePagina(file,irma,this.getOrdem());
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


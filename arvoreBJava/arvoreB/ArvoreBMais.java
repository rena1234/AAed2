package arvoreB;

import arquivoFerramentas.EspacoVazio;

import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.RandomAccess;

/**
 * Created by gabriel on 30/01/17.
 */
public class ArvoreBMais {
    private RandomAccessFile file;
    private Pagina raiz;
    private  RandomAccessFile raizFile;
    private int ordem;
    private  EspacoVazio ferramenta1;


    public ArvoreBMais(String filePath,int ordem){
        this.ferramenta1 = new EspacoVazio();
        this.setOrdem(ordem);
        try{
            this.file = new RandomAccessFile(filePath,"rw");
            this.raizFile = new RandomAccessFile("arquivos/raiz","rw");

        }catch(IOException e ){
            System.err.print("Nao foi possivel abrir o arquivo");
            e.printStackTrace();
        }
        this.setRaiz(this.leRaiz());
    }

    public RandomAccessFile getRaizFile() {
        return raizFile;
    }

    public void setRaizFile(RandomAccessFile raizFile) {
        this.raizFile = raizFile;
    }

    public Pagina leRaiz(){
        Pagina p = null;
        try {

            if(this.getRaizFile().length() == 0){
                return null;
            }else{
                long offset = this.getRaizFile().readLong();
                p = Pagina.lePagina(offset,this.getFile(),this.getOrdem());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;

    }

    public void inserirArvore(long chave, long offset){
        Pagina auxiliar = this.getRaiz();
        if(auxiliar == null){// nao existe raiz, arvore vazia
            auxiliar = new Pagina(this.getOrdem());
            auxiliar.setChave(chave,0);
            auxiliar.setOffset(offset,0);
            auxiliar.setTamanho(1);
            long offsetEcrita = this.ferramenta1.buscaEspacoVazio(this.getFile());
            auxiliar.setOffsetPag(offsetEcrita);
            this.setRaiz(auxiliar);
            try {
                this.getRaizFile().seek(0);
                this.getRaizFile().writeLong(offsetEcrita);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Pagina.escrevePagina(this.getFile(),auxiliar,this.getOrdem());

        }else{//raiz ja existe começo a buscar a pagina folha onde inseir
            buscaInserir(auxiliar,chave,offset);
        }

    }
    public void encerrar(){
        try {
            this.getRaizFile().close();
            this.getFile().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public long buscaOffsetChave(long chave){
        Pagina auxiliar = this.getRaiz();
        if(auxiliar == null){
            System.out.println("A árvore esta vazia");
            return -1;
        }
        return buscaOffsetChaveaux(auxiliar,chave);

    }
    private long buscaOffsetChaveaux(Pagina p, long chave){
        long offset = -1;
        int esquerda= 0, direita = p.getTamanho() -1;
        int meio = 0;
        if(p.getEh_folha() == 1){

            while(esquerda <= direita){
                meio = (esquerda + direita)/2;
                if(p.getChave(meio) == chave){
                    // achei chave vou para a pagina
                    System.out.println("Chave " + chave +  " encontrada!!!!!");
                    return p.getOffset(meio);
                }
                if(p.getChave(meio) < chave){
                    esquerda = meio +1;
                }else{
                    direita = meio -1;
                }
            }

            System.out.println("Chave " + chave + " não encontrada!!!!");
            return -1;
        }
        Pagina p1 = null;
        while(esquerda <= direita){
            meio = (esquerda + direita)/2;
            if(p.getChave(meio) == chave){
                // achei chave vou para a pagina
                break;
            }else if(p.getChave(meio) < chave){
                esquerda = meio +1;
            }else{
                direita = meio -1;
            }
        }
        if(p.getChave(meio) > chave){//desco pelo offset do meio
            p1 = Pagina.lePagina(p.getOffset(meio),this.getFile(),this.getOrdem());
        }else{
            p1 = Pagina.lePagina(p.getOffset(meio +1),this.getFile(),this.getOrdem());
        }
        return buscaOffsetChaveaux(p1,chave);
    }
    private void buscaInserir(Pagina p, long chave, long offset){
        int esquerda= 0, direita = p.getTamanho() -1;
        if(p.getEh_folha() == 1){// encontrei pagina folha para inseir
            int meio = 0;
            while(esquerda <= direita){
                meio = (esquerda + direita)/2;
                if(p.getChave(meio) == chave){
                    // achei chave vou para a pagina
                    System.out.println("Chave " + chave +  " ja existe!!!!!");
                    return;
                }
                if(p.getChave(meio) < chave){
                    esquerda = meio +1;
                }else{
                    direita = meio -1;
                }
            }
            p.setChave(chave,p.getTamanho());
            // preciso copiar o offset q esta encadeando as irmas sendo ele -1 ou n
            long offsetListaEncadeada = p.getOffset(p.getTamanho());
            p.setOffset(offset,p.getTamanho());
            p.setTamanho(p.getTamanho() + 1);
            p.setOffset(offsetListaEncadeada,p.getTamanho());
            // ordeno pagina
            for(int i = p.getTamanho() -1;i>0;i--){
                long chaveTemp = p.getChave(i);
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
            if(p.getTamanho() - 1 == this.getOrdem()*2){
                split(p,this.getOrdem(),this.getFile());
            }else{// n preciso fazer nd acabou
                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
            }

        }else{
            if(chave < p.getChave(0)){
                Pagina aux = Pagina.lePagina(p.getOffset(0),this.getFile(),this.getOrdem());
                buscaInserir(aux,chave,offset);
            }else{
                int meio = 0;
                esquerda = 0;
                direita = p.getTamanho() -1;
                while(esquerda <= direita){
                    meio = (esquerda + direita)/2;
                    if(p.getChave(meio) == chave){
                        // achei chave vou para a pagina
                        Pagina aux = Pagina.lePagina(p.getOffset(meio +1),this.getFile(),this.getOrdem());
                        buscaInserir(aux,chave,offset);
                        return;
                    }else if(p.getChave(meio) < chave){
                        esquerda = meio +1;
                    }else{
                        direita = meio -1;
                    }
                 }
                if(p.getChave(meio) > chave){//desco pelo offset do meio
                    Pagina aux = Pagina.lePagina(p.getOffset(meio),this.getFile(),this.getOrdem());
                    buscaInserir(aux,chave,offset);
                }else{
                    Pagina aux = Pagina.lePagina(p.getOffset(meio +1),this.getFile(),this.getOrdem());
                    buscaInserir(aux,chave,offset);
                }



            }//nao encontrei chave no index set, vou para a pagina de baixo

        }
    }

    private void split(Pagina p, int ordem, RandomAccessFile file){
        //ATENCAO COLOCAR ENCADEAMENTO NOS NO FOLHA, ESQUECI ASS: GABRIEL DO PRESENTE Q AGR É PASSADO
        int metade = p.getTamanho()/2,metadeIrma;
            metadeIrma = metade +1;

        long offsetListaEncadeada = p.getOffset(p.getTamanho());
        long chaveCentral = p.getChave(metade);
        Pagina irma = new Pagina(ordem);
        Pagina pai = null;
        irma.setEh_folha(p.getEh_folha()); // se a pagina do split eh folha a irma tbm sera

        if(p.getEh_folha() == 1){// a pagina eh folha entao eu preciso manter os registros no split
            int aux = 0;
            irma.setTamanho(metadeIrma);
            //copia metade ate 2*d para a pagina irma
            for(int i = metade;i<p.getTamanho();i++){
                irma.setChave(p.getChave(i),aux);
                p.setChave(0,i);//zerei as chaves
                irma.setOffset(p.getOffset(i),aux);
                p.setOffset(-1,i);//zerei os offsets
                aux += 1;
            }
            p.setTamanho(metade);

            if(p.getOffsetPai() == -1){// Se for -1 preciso criar a pagina pai, q vai ser a nova raiz :o
                pai = new Pagina(ordem);
                pai.setEh_folha(0);//o pai nunca sera folha
                pai.setChave(chaveCentral,0);// subi a chave central para o pai
                pai.setTamanho(1);
                pai.setOffsetPai(-1);
                long offsetPagPai,offsetPagIrma;
                offsetPagPai = ferramenta1.buscaEspacoVazio(file);
                pai.setOffsetPag(offsetPagPai);
                Pagina.escrevePagina(file,pai,this.getOrdem());

                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
                p.setOffsetPai(offsetPagPai);
                p.setOffset(offsetPagIrma,p.getTamanho());// encadeamento esse offset aponta pra pag irma
                pai.setOffset(p.getOffsetPag(),0);
                pai.setOffset(offsetPagIrma,1);

                irma.setOffsetPag(offsetPagIrma);
                irma.setOffsetPai(offsetPagPai);
                this.setRaiz(pai);
                try {
                    this.getRaizFile().seek(0);
                    this.getRaizFile().writeLong(offsetPagPai);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Pagina.escrevePagina(file,pai,this.getOrdem());
                Pagina.escrevePagina(file,p,this.getOrdem());
                Pagina.escrevePagina(file,irma,this.getOrdem());

            }else{//Pagina ja tem um pai entao vou inserir o no central nele e atualizar os ponteiros
                long offsetPagIrma;
                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
                irma.setOffsetPai(p.getOffsetPai());
                irma.setOffsetPag(offsetPagIrma);
                p.setOffset(offsetPagIrma,metade);
                irma.setOffset(offsetListaEncadeada,irma.getTamanho());
                Pagina.escrevePagina(file,p,this.getOrdem());
                Pagina.escrevePagina(file,irma,this.getOrdem());
                pai = Pagina.lePagina(p.getOffsetPai(),file,this.getOrdem());// carrego pai na memoria
                pai = this.inserePaiSplit(pai,chaveCentral,irma.getOffsetPag());
                if(pai.getTamanho() - 1 == this.getOrdem()*2){
                    split(pai,this.getOrdem(),file);//split pode se propagar entao eh recursivo
                }else{
                    Pagina.escrevePagina(file,pai,this.getOrdem());
                    if(this.getRaiz().getOffsetPag() == pai.getOffsetPag()){
                        this.setRaiz(pai);// update na raiz runtime
                        try {
                            this.getRaizFile().seek(0);
                            this.getRaizFile().writeLong(pai.getOffsetPag());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }





            }



        }else{// split em uma pagina do index set
            irma.setTamanho(metadeIrma); //como nao eh folha copia da metade + 1 em diante

            int i,aux = 0;
            p.setChave(-1,metade);//zerei a chave
            //p.setOffset(-1,metade +1);//zerei o offset
            //copia metade +1 ate 2*d para a pagina irma
            long offsetPagPai,offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
            irma.setOffsetPag(offsetPagIrma);
            Pagina.escrevePagina(file,irma,this.getOrdem());
            for(i = metade +1 ;i<p.getTamanho();i++){
                irma.setChave(p.getChave(i),aux);
                p.setChave(-1,i);//zerei as chaves
                irma.setOffset(p.getOffset(i),aux);
                Pagina temp = Pagina.lePagina(p.getOffset(i),this.getFile(),this.getOrdem());
                temp.setOffsetPai(offsetPagIrma);
                Pagina.escrevePagina(this.getFile(),temp,this.getOrdem());
                p.setOffset(-1,i);//zerei os offsets
                aux += 1;
            }
            p.setTamanho(metade);
            irma.setOffset(p.getOffset(i),aux);//irma agora tem tamanho + 1 offsets
            Pagina temp = Pagina.lePagina(p.getOffset(i),this.getFile(),this.getOrdem());
            temp.setOffsetPai(offsetPagIrma);
            Pagina.escrevePagina(this.getFile(),temp,this.getOrdem());
            p.setOffset(-1,i);
            irma.setTamanho(metadeIrma -1);

            if(p.getOffsetPai() == -1){// nao tem pai preciso criar e logo ele vai ser a nova raiz
                pai = new Pagina(this.getOrdem());
                pai.setEh_folha(0);
                pai.setTamanho(1);
                pai.setChave(chaveCentral,0);
                pai.setOffset(p.getOffsetPag(),0);
                offsetPagPai = ferramenta1.buscaEspacoVazio(file);
                pai.setOffsetPag(offsetPagPai);

                try {
                    this.getRaizFile().seek(0);
                    this.getRaizFile().writeLong(offsetPagPai);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                p.setOffsetPai(offsetPagPai);
                pai.setOffset(offsetPagIrma,1);
                pai.setOffsetPag(offsetPagPai);

                irma.setOffsetPai(offsetPagPai);
                this.setRaiz(pai);//atualizo raiz
                Pagina.escrevePagina(file,pai,this.getOrdem());
                Pagina.escrevePagina(file,p,this.getOrdem());
                Pagina.escrevePagina(file,irma,this.getOrdem());
            }else{//ja tem pai
                irma.setOffsetPai(p.getOffsetPai());
                p.setOffset(offsetPagIrma,metade +1);
                pai = Pagina.lePagina(p.getOffsetPai(),file,this.getOrdem());// carrego pai na memoria
                pai = this.inserePaiSplit(pai,chaveCentral,irma.getOffsetPag());
                Pagina.escrevePagina(file,p,this.getOrdem());
                Pagina.escrevePagina(file,irma,this.getOrdem());
                if(pai.getTamanho() - 1 == this.getOrdem()*2){
                    split(pai,this.getOrdem(),file);//split pode se propagar entao eh recursivo
                }else{
                    Pagina.escrevePagina(file,pai,this.getOrdem());
                    if(this.getRaiz().getOffsetPag() == pai.getOffsetPag()){
                        this.setRaiz(pai);// update na raiz
                    }
                }



            }

        }
    }

    private Pagina inserePaiSplit(Pagina pai, long chave, long offset){
        long tempChave;
        long tempOffset;
        pai.setChave(chave,pai.getTamanho());
        pai.setTamanho(pai.getTamanho() + 1);
        pai.setOffset(offset,pai.getTamanho());
        for(int pos = pai.getTamanho()-1;pos>0;pos--){
            if(pai.getChave(pos)<pai.getChave(pos - 1)){
                tempChave = pai.getChave(pos);
                tempOffset = pai.getOffset(pos+1);

                pai.setChave(pai.getChave(pos-1),pos);
                pai.setOffset(pai.getOffset(pos),pos+1);
                pai.setChave(tempChave,pos-1);
                pai.setOffset(tempOffset,pos);

            }else{
                break;
            }

        }
        return pai;
    }


    public void removeChave(long chave){
        if(this.getRaiz() == null){
            System.out.println("Arvore esta vazia!");
        }else{
            this.buscaRemover(this.getRaiz(),chave);
        }
    }

    private void buscaRemover(Pagina p, long chave){
        int esquerda= 0, direita = p.getTamanho() -1;
        if(p.getEh_folha() == 1){// encontrei pagina folha onde a chave pode estar ou nnn
            int meio = 0;
            while(esquerda <= direita){
                meio = (esquerda + direita)/2;
                if(p.getChave(meio) == chave){
                    // achei chave
                    // apaguei a chave
                    p.setChave(0,meio);
                    p.setOffset(-1,meio);
                    int i;
                    p.setTamanho(p.getTamanho() -1);// diminui o tamanho
                    for(i=meio;i<p.getTamanho();i++){
                        // aplico "shift" para deixar a sequencia de chaves
                        p.setChave(p.getChave(i+1),i);
                        p.setOffset(p.getOffset(i+1),i);
                    }
                    p.setOffset(p.getOffset(i+1),i);// offset q esta encadeando as folhas

                    if(p.getTamanho() < this.getOrdem() && p.getOffsetPai() != -1) {
                        // preciso aplicar merge ou redistribuicao, e verificar q a pagina n eh raiz
                        //Carrego pai para ver as imas da pagina
                        Pagina pai = Pagina.lePagina(p.getOffsetPai(),this.getFile(),this.getOrdem());
                        // preciso descobrir quem eh a chave pai da pagina folha e suas irmas
                        meio = 0;
                        esquerda = 0;
                        direita = pai.getTamanho() -1;
                        while(esquerda <= direita){
                            meio = (esquerda + direita)/2;
                            if(pai.getChave(meio) == chave){
                                // achei chave pai
                                break;
                            }else if(pai.getChave(meio) < chave){
                                esquerda = meio +1;
                            }else{
                                direita = meio -1;
                            }
                        }


                        int posOffset = meio;

                        if(pai.getChave(meio) <= chave){// vejo se tenho q descer pelo offset do meio ou direita
                            posOffset = meio + 1;
                        }else{
                            if(meio != 0){
                                meio -= 1;
                            }

                        }
                        if(posOffset == 0){//so tem a irma direita
                            Pagina irmaDireita = Pagina.lePagina(pai.getOffset(1),this.getFile(),this.getOrdem());
                            if(irmaDireita.getTamanho() -1  >= this.getOrdem()){
                                // estou vendo se posso pegar um elemento da irma sem ferir a ordem
                                pai.setChave(irmaDireita.getChave(1),meio);
                                redistribuicao(p,irmaDireita,'d',null,-1);
                                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                    this.setRaiz(pai);//atualizo raiz runtime
                                }
                                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
                                Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                                Pagina.escrevePagina(this.getFile(),irmaDireita,this.getOrdem());
                            }else{// preciso concatenar
                                int j = 0;
                                long chavePai = pai.getChave(meio);
                                for(j = meio; j < pai.getTamanho() -1; j++){
                                    pai.setChave(pai.getChave(j+1),j);
                                    pai.setOffset(pai.getOffset(j+2),j+1);
                                }
                                pai.setTamanho(pai.getTamanho() -1);
                                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                    this.setRaiz(pai); // atualizo runtime
                                }

                                concatencao(pai,p,irmaDireita,'d',chavePai);

                            }


                        }else if( posOffset == pai.getTamanho()){// so tenho irma esquerda
                            Pagina irmaEsquerda = Pagina.lePagina(pai.getOffset(posOffset -1),this.getFile(),this.getOrdem());
                            if(irmaEsquerda.getTamanho() -1 >= this.getOrdem()){//verifico se posso remover
                                pai.setChave(irmaEsquerda.getChave(irmaEsquerda.getTamanho() -1),meio);
                                redistribuicao(p,irmaEsquerda,'e',null,-1);
                                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                    this.setRaiz(pai);//atualizo raiz runtime
                                }
                                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
                                Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                                Pagina.escrevePagina(this.getFile(),irmaEsquerda,this.getOrdem());
                            }else{
                                int j = 0;
                                long chavePai = pai.getChave(meio);
                                for(j = meio; j < pai.getTamanho() -1; j++){
                                    pai.setChave(pai.getChave(j+1),j);
                                    pai.setOffset(pai.getOffset(j+2),j+1);
                                }
                                pai.setTamanho(pai.getTamanho() -1);
                                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                    this.setRaiz(pai); // atualizo runtime
                                }

                                concatencao(pai,p,irmaEsquerda,'e',chavePai);
                            }

                        }else{// tenho as duas irmas
                            Pagina irmaDireita = Pagina.lePagina(pai.getOffset(posOffset + 1),this.getFile(),this.getOrdem());
                            Pagina irmaEsquerda = Pagina.lePagina(pai.getOffset(posOffset -1),this.getFile(),this.getOrdem());

                            if(irmaEsquerda.getTamanho() -1 >= this.getOrdem()){//verifico se posso remover
                                pai.setChave(irmaEsquerda.getChave(irmaEsquerda.getTamanho() -1),meio);
                                redistribuicao(p,irmaEsquerda,'e',null,-1);
                                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                    this.setRaiz(pai);//atualizo raiz runtime
                                }
                                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
                                Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                                Pagina.escrevePagina(this.getFile(),irmaEsquerda,this.getOrdem());
                            } else if(irmaDireita.getTamanho() - 1 >= this.getOrdem()){
                                pai.setChave(irmaDireita.getChave(1),meio + 1);// meio + 1 eh o pai da irma e pagina com ordem zuada
                                redistribuicao(p,irmaDireita,'d',null,-1);

                                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                    this.setRaiz(pai);//atualizo raiz runtime
                                }
                                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
                                Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                                Pagina.escrevePagina(this.getFile(),irmaDireita,this.getOrdem());
                            }else{
                                int j = 0;
                                long chavePai = pai.getChave(meio);
                                for(j = meio; j < pai.getTamanho() -1; j++){
                                    pai.setChave(pai.getChave(j+1),j);
                                    pai.setOffset(pai.getOffset(j+2),j+1);
                                }
                                pai.setTamanho(pai.getTamanho() -1);
                                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                    this.setRaiz(pai); // atualizo runtime
                                }

                                concatencao(pai,p,irmaEsquerda,'e',chavePai);
                            }

                        }


                    }else{
                        Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
                    }
                    System.out.println("Chave " + chave +  " foi removida !!!!!");
                    return;

                }
                if(p.getChave(meio) < chave){
                    esquerda = meio +1;
                }else{
                    direita = meio -1;
                }
            }
            System.out.println("Chave " + chave + " não foi encontrada!");
        }else{
            if(chave < p.getChave(0)){
                Pagina aux = Pagina.lePagina(p.getOffset(0),this.getFile(),this.getOrdem());
                buscaRemover(aux,chave);
            }else{
                int meio = 0;
                esquerda = 0;
                direita = p.getTamanho() -1;
                while(esquerda <= direita){
                    meio = (esquerda + direita)/2;
                    if(p.getChave(meio) == chave){
                        // achei chave vou para a pagina
                        Pagina aux = Pagina.lePagina(p.getOffset(meio +1),this.getFile(),this.getOrdem());
                        buscaRemover(aux,chave);
                        return;
                    }else if(p.getChave(meio) < chave){
                        esquerda = meio +1;
                    }else{
                        direita = meio -1;
                    }
                }
                if(p.getChave(meio) > chave){//desco pelo offset do meio
                    Pagina aux = Pagina.lePagina(p.getOffset(meio),this.getFile(),this.getOrdem());
                    buscaRemover(aux,chave);
                }else{
                    Pagina aux = Pagina.lePagina(p.getOffset(meio +1),this.getFile(),this.getOrdem());
                    buscaRemover(aux,chave);
                }



            }
        }
    }

    private void redistribuicao(Pagina p, Pagina irma, char lado,Pagina pai, int meio){// preciso saber se eh irma esquerda ou direita
        if(p.getEh_folha() == 1){//redistribuicao na folha
            if(lado == 'd'){// irma direita pego o menor elemento da irma
                long chaveIrma = irma.getChave(0);// pego menor chave da irma
                long offsetIrma = irma.getOffset(0); //pego o offset da menor chave
                int i;
                irma.setTamanho(irma.getTamanho() -1);
                for(i = 0; i<irma.getTamanho();i++){
                    irma.setChave(irma.getChave(i+1),i);
                    irma.setOffset(irma.getOffset(i+1),i);
                }
                irma.setOffset(irma.getOffset(i+1),i);// offset do encadeamento
                // vou inserir chave na pagina
                long offsetPaginaIrma = p.getOffset(p.getTamanho());
                p.setChave(chaveIrma,p.getTamanho());
                p.setOffset(offsetIrma,p.getTamanho());
                p.setTamanho(p.getTamanho() + 1);
                p.setOffset(offsetPaginaIrma,p.getTamanho());// mantenho encadeamento
            }else if(lado == 'e'){
                long chaveIrma = irma.getChave(irma.getTamanho() -1);
                long offsetIrma = irma.getOffset(irma.getTamanho() -1);
                long offsetPaginaIrma = irma.getOffset(irma.getTamanho());
                irma.setOffset(-1,irma.getTamanho());
                irma.setTamanho(irma.getTamanho()-1);
                irma.setChave(0,irma.getTamanho());
                irma.setOffset(offsetPaginaIrma,irma.getTamanho());

                p.setOffset(p.getOffset(p.getTamanho()),p.getTamanho() + 1);
                int i;
                for(i = p.getTamanho(); i>0;i--){
                    p.setChave(p.getChave(i-1),i);
                    p.setOffset(p.getOffset(i-1),i);
                }
                p.setChave(chaveIrma,0);
                p.setOffset(offsetIrma,0);
                p.setTamanho(p.getTamanho() + 1);


            }

        }else{//redistribuicao no index set
            if(lado == 'd'){
                p.setChave(pai.getChave(meio+ 1),p.getTamanho());// desco a chave do pai
                p.setTamanho(p.getTamanho() +1);
                pai.setChave(irma.getChave(0),meio +1);
                Pagina temp = Pagina.lePagina(irma.getOffset(0),this.getFile(),this.getOrdem());
                temp.setOffsetPai(p.getOffsetPag());
                Pagina.escrevePagina(this.getFile(),temp,this.getOrdem());
                p.setOffset(irma.getOffset(0),p.getTamanho());
                int i;
                irma.setTamanho(irma.getTamanho() -1);
                for (i = 0;i< irma.getTamanho();i++){
                    irma.setChave(irma.getChave(i+1),i);
                    irma.setOffset(irma.getOffset(i+1),i);
                }

                irma.setOffset(irma.getOffset(i+1),i);
                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                    this.setRaiz(pai);
                }
                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
                Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                Pagina.escrevePagina(this.getFile(),irma,this.getOrdem());
            }else if (lado == 'e'){
                int i;
                p.setOffset(p.getOffset(p.getTamanho()),p.getTamanho()+1);
                for(i = p.getTamanho(); i>0;i--){
                    p.setChave(p.getChave(i-1),i);
                    p.setOffset(p.getOffset(i-1),i);
                }
                p.setTamanho(p.getTamanho() + 1);
                p.setChave(pai.getChave(meio),0);
                pai.setChave(irma.getChave(irma.getTamanho()-1),meio);
                p.setOffset(irma.getOffset(irma.getTamanho()),0);
                Pagina temp = Pagina.lePagina(irma.getOffset(irma.getTamanho()),this.getFile(),this.getOrdem());
                temp.setOffsetPai(p.getOffsetPag());
                irma.setChave(0,irma.getTamanho()-1);
                irma.setOffset(-1,irma.getTamanho());
                Pagina.escrevePagina(this.getFile(),temp,this.getOrdem());
                irma.setTamanho(irma.getTamanho() -1);
                if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                    this.setRaiz(pai);
                }

                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
                Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                Pagina.escrevePagina(this.getFile(),irma,this.getOrdem());
            }



        }
    }

    private void concatencao(Pagina pai, Pagina p, Pagina irma, char lado,long chavePai){
        if(p.getEh_folha() == 1){// concatencao na folha
            if(lado == 'e'){
                int i, aux = 0;

                for(i=irma.getTamanho(); i<irma.getTamanho()+p.getTamanho();i++){
                    irma.setChave(p.getChave(aux),i);
                    irma.setOffset(p.getOffset(aux),i);
                    aux +=1;
                }
                irma.setOffset(p.getOffset(aux),i);//offset da lista encadeada
                irma.setTamanho(irma.getTamanho() + p.getTamanho());
                ferramenta1.escreveOffsetEspVazio(p.getOffsetPag());
                Pagina.escrevePagina(this.getFile(),irma,this.getOrdem());
            }else if(lado == 'd'){
                int i, aux = 0;
                for(i= p.getTamanho();i<p.getTamanho() + irma.getTamanho();i++){
                    p.setChave(irma.getChave(aux),i);
                    p.setOffset(irma.getOffset(aux),i);
                    aux += 1;
                }
                p.setOffset(irma.getOffset(aux),i); // offset da lista encadeada
                p.setTamanho(irma.getTamanho() + p.getTamanho());
                this.ferramenta1.escreveOffsetEspVazio(irma.getOffsetPag());
                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
            }
            if(pai.getTamanho() == 0 && pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                if(lado == 'd'){
                    try {
                        this.getRaizFile().seek(0);
                        this.getRaizFile().writeLong(p.getOffsetPag());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.setRaiz(p);
                }else{
                    try {
                        this.getRaizFile().seek(0);
                        this.getRaizFile().writeLong(irma.getOffsetPag());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.setRaiz(irma);
                }

            }else if(pai.getTamanho() < this.getOrdem() && pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                this.setRaiz(pai);
                Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
            }else if(pai.getTamanho() < this.getOrdem() && pai.getOffsetPag() != this.getRaiz().getOffsetPag()){
                //pagina do index set feriu ordem e n eh raiz
                Pagina paiDoPai = Pagina.lePagina(pai.getOffsetPai(),this.getFile(),this.getOrdem());
                // preciso descobrir quem eh a chave pai da pagina folha e suas irmas
                int meio = 0,
                esquerda = 0, direita = paiDoPai.getTamanho() -1;
                int posOffset;

                while(esquerda <= direita){
                    meio = (esquerda + direita)/2;
                    if(paiDoPai.getChave(meio) == chavePai){
                        // achei chave pai
                        break;
                    }else if(paiDoPai.getChave(meio) <chavePai ){
                        esquerda = meio +1;
                    }else{
                        direita = meio -1;
                    }
                }
                posOffset = meio;
                if(paiDoPai.getChave(meio) <= chavePai){// vejo se tenho q descer pelo offset do meio ou direita
                    posOffset = meio + 1;
                }else{
                    if(meio != 0){
                        meio -= 1;
                    }

                }
                if(posOffset == 0){//so tem a irma direita
                    Pagina irmaDireita = Pagina.lePagina(paiDoPai.getOffset(1),this.getFile(),this.getOrdem());

                    if(irmaDireita.getTamanho() -1  >= this.getOrdem()){
                        // estou vendo se posso pegar um elemento da irma sem ferir a ordem

                        if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                            this.setRaiz(paiDoPai);//atualizo raiz runtime
                        }

                        redistribuicao(pai,irmaDireita,'d',paiDoPai,-1);

                    }else{// preciso concatenar
                        int j = 0;
                        long chavePaidoPai = paiDoPai.getChave(meio);
                        for(j = meio; j < pai.getTamanho() -1; j++){
                            paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                            paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                        }
                        paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                        if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                            this.setRaiz(paiDoPai); // atualizo runtime
                        }
                        concatencao(paiDoPai,pai,irmaDireita,'d',chavePaidoPai);
                    }


                }else if( posOffset == paiDoPai.getTamanho()){// so tenho irma esquerda
                    Pagina irmaEsquerda = Pagina.lePagina(paiDoPai.getOffset(posOffset -1),this.getFile(),this.getOrdem());
                    if(irmaEsquerda.getTamanho() -1 >= this.getOrdem()){//verifico se posso remover
                        if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                            this.setRaiz(paiDoPai);//atualizo raiz runtime
                        }
                        redistribuicao(pai,irmaEsquerda,'e',paiDoPai,meio);

                    }else{
                        int j = 0;
                        long chavePaidoPai = paiDoPai.getChave(meio);
                        for(j = meio; j < paiDoPai.getTamanho() -1; j++){
                            paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                            paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                        }
                        paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                        if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                            this.setRaiz(paiDoPai); // atualizo runtime
                        }

                        concatencao(paiDoPai,pai,irmaEsquerda,'e',chavePaidoPai);
                    }

                }else{// tenho as duas irmas
                    Pagina irmaDireita = Pagina.lePagina(paiDoPai.getOffset(posOffset + 1),this.getFile(),this.getOrdem());
                    Pagina irmaEsquerda = Pagina.lePagina(paiDoPai.getOffset(posOffset -1),this.getFile(),this.getOrdem());
                    if(irmaEsquerda.getTamanho() -1 >= this.getOrdem()){//verifico se posso remover
                        redistribuicao(pai,irmaEsquerda,'e',paiDoPai,meio);
                        if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                            this.setRaiz(paiDoPai);//atualizo raiz runtime
                        }
                        Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                        Pagina.escrevePagina(this.getFile(),paiDoPai,this.getOrdem());
                        Pagina.escrevePagina(this.getFile(),irmaEsquerda,this.getOrdem());
                    } else if(irmaDireita.getTamanho() - 1 >= this.getOrdem()){
                        redistribuicao(pai,irmaDireita,'d',paiDoPai,meio);
                        if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                            this.setRaiz(paiDoPai);//atualizo raiz runtime
                        }
                        Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                        Pagina.escrevePagina(this.getFile(),paiDoPai,this.getOrdem());
                        Pagina.escrevePagina(this.getFile(),irmaDireita,this.getOrdem());
                    }else{
                        int j = 0;
                        long chavePaidoPai = paiDoPai.getChave(meio);
                        for(j = meio; j < paiDoPai.getTamanho() -1; j++){
                            paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                            paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                        }
                        paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                        if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                            this.setRaiz(paiDoPai); // atualizo runtime
                        }

                        concatencao(paiDoPai,pai,irmaEsquerda,'e',chavePaidoPai);
                    }

                }


            }else{
                Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
            }
        }else{// concatencao no index set
            if(lado == 'd'){
                int i, aux = 0;
                p.setChave(chavePai,p.getTamanho());
                p.setTamanho(p.getTamanho() +1);
                for(i= p.getTamanho();i<p.getTamanho() + irma.getTamanho();i++){
                    p.setChave(irma.getChave(aux),i);
                    p.setOffset(irma.getOffset(aux),i);
                    Pagina temp = Pagina.lePagina(irma.getOffset(aux),this.getFile(),this.getOrdem());
                    temp.setOffsetPai(p.getOffsetPag());
                    Pagina.escrevePagina(this.getFile(),temp,this.getOrdem());
                    aux += 1;
                }
                p.setOffset(irma.getOffset(aux),i); // offset arg eh tamanho + 1
                Pagina temp = Pagina.lePagina(irma.getOffset(aux),this.getFile(),this.getOrdem());
                temp.setOffsetPai(p.getOffsetPag());
                Pagina.escrevePagina(this.getFile(),temp,this.getOrdem());
                p.setTamanho(irma.getTamanho() + p.getTamanho());
                this.ferramenta1.escreveOffsetEspVazio(irma.getOffsetPag());
                Pagina.escrevePagina(this.getFile(),p,this.getOrdem());
                if(pai.getTamanho() == 0 && pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                    //raiz ficou vazia agr a pagina p eh a nova raiz, arvore diminuiu altura
                    this.setRaiz(p);
                    try {
                        this.getRaizFile().seek(0);
                        this.getRaizFile().writeLong(p.getOffsetPag());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(pai.getTamanho() <= this.getOrdem() && pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                    this.setRaiz(pai);// aztualizo raiz runtime
                    Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());

                }else if( pai.getTamanho() < this.getOrdem() && pai.getOffsetPag() != this.getRaiz().getOffsetPag()){
                    Pagina paiDoPai = Pagina.lePagina(pai.getOffsetPai(),this.getFile(),this.getOrdem());
                    // preciso descobrir quem eh a chave pai da pagina folha e suas irmas
                    int meio = 0,
                            esquerda = 0, direita = paiDoPai.getTamanho() -1;
                    int posOffset;

                    while(esquerda <= direita){
                        meio = (esquerda + direita)/2;
                        if(paiDoPai.getChave(meio) == chavePai){
                            // achei chave pai
                            break;
                        }else if(paiDoPai.getChave(meio) <chavePai ){
                            esquerda = meio +1;
                        }else{
                            direita = meio -1;
                        }
                    }
                    posOffset = meio;
                    if(paiDoPai.getChave(meio) <= chavePai){// vejo se tenho q descer pelo offset do meio ou direita
                        posOffset = meio + 1;
                    }else{
                        if(meio != 0){
                            meio -= 1;
                        }

                    }
                    if(posOffset == 0){//so tem a irma direita
                        Pagina irmaDireita = Pagina.lePagina(paiDoPai.getOffset(1),this.getFile(),this.getOrdem());

                        if(irmaDireita.getTamanho() -1  >= this.getOrdem()){
                            // estou vendo se posso pegar um elemento da irma sem ferir a ordem

                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai);//atualizo raiz runtime
                            }

                            redistribuicao(pai,irmaDireita,'d',paiDoPai,-1);

                        }else{// preciso concatenar
                            int j = 0;
                            long chavePaidoPai = paiDoPai.getChave(meio);
                            for(j = meio; j < paiDoPai.getTamanho() -1; j++){
                                paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                                paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                            }


                            paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai); // atualizo runtime
                            }
                            concatencao(paiDoPai,pai,irmaDireita,'d',chavePaidoPai);
                        }


                    }else if( posOffset == paiDoPai.getTamanho()){// so tenho irma esquerda
                        Pagina irmaEsquerda = Pagina.lePagina(paiDoPai.getOffset(posOffset -1),this.getFile(),this.getOrdem());
                        if(irmaEsquerda.getTamanho() -1 >= this.getOrdem()){//verifico se posso remover
                            if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai);//atualizo raiz runtime
                            }
                            redistribuicao(pai,irmaEsquerda,'e',paiDoPai,meio);

                        }else{
                            int j = 0;
                            long chavePaidoPai = paiDoPai.getChave(meio);
                            for(j = meio; j < paiDoPai.getTamanho() -1; j++){
                                paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                                paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                            }
                            paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai); // atualizo runtime
                            }

                            concatencao(paiDoPai,pai,irmaEsquerda,'e',chavePaidoPai);
                        }

                    }else{// tenho as duas irmas
                        Pagina irmaDireita = Pagina.lePagina(paiDoPai.getOffset(posOffset + 1),this.getFile(),this.getOrdem());
                        Pagina irmaEsquerda = Pagina.lePagina(paiDoPai.getOffset(posOffset -1),this.getFile(),this.getOrdem());

                        if(irmaEsquerda.getTamanho() -1 >= this.getOrdem()){//verifico se posso remover
                            redistribuicao(pai,irmaEsquerda,'e',paiDoPai,meio);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai);//atualizo raiz runtime
                            }
                            Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                            Pagina.escrevePagina(this.getFile(),paiDoPai,this.getOrdem());
                            Pagina.escrevePagina(this.getFile(),irmaEsquerda,this.getOrdem());
                        } else if(irmaDireita.getTamanho() - 1 >= this.getOrdem()){
                            paiDoPai.setChave(irmaDireita.getChave(1),meio + 1);// meio + 1 eh o pai da irma e pagina com ordem zuada
                            redistribuicao(pai,irmaDireita,'d',paiDoPai,meio);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai);//atualizo raiz runtime
                            }
                            Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                            Pagina.escrevePagina(this.getFile(),paiDoPai,this.getOrdem());
                            Pagina.escrevePagina(this.getFile(),irmaDireita,this.getOrdem());
                        }else{
                            int j = 0;
                            long chavePaidoPai = paiDoPai.getChave(meio);
                            for(j = meio; j < paiDoPai.getTamanho() -1; j++){
                                paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                                paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                            }
                            paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai); // atualizo runtime
                            }

                            concatencao(paiDoPai,pai,irmaEsquerda,'e',chavePaidoPai);
                        }

                    }

                }else{
                    Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                }
            }else if(lado == 'e'){
                irma.setChave(chavePai,irma.getTamanho());
                irma.setTamanho(irma.getTamanho() +1);
                int i = irma.getTamanho(), aux = 0;
                for(i=irma.getTamanho(); i<irma.getTamanho()+p.getTamanho();i++){
                    irma.setChave(p.getChave(aux),i);
                    irma.setOffset(p.getOffset(aux),i);
                    Pagina temp = Pagina.lePagina(p.getOffset(aux),this.getFile(),this.getOrdem());
                    temp.setOffsetPai(irma.getOffsetPag());
                    Pagina.escrevePagina(this.getFile(),temp,this.getOrdem());
                    aux +=1;
                }
                Pagina temp = Pagina.lePagina(p.getOffset(aux),this.getFile(),this.getOrdem());
                temp.setOffsetPai(irma.getOffsetPag());
                Pagina.escrevePagina(this.getFile(),temp,this.getOrdem());
                irma.setOffset(p.getOffset(aux),i);//offset da lista encadeada
                irma.setTamanho(irma.getTamanho() + p.getTamanho());
                ferramenta1.escreveOffsetEspVazio(p.getOffsetPag());
                Pagina.escrevePagina(this.getFile(),irma,this.getOrdem());
                if(pai.getTamanho() == 0 && pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                    //raiz ficou vazia agr a pagina p eh a nova raiz, arvore diminuiu altura
                    this.setRaiz(irma);
                    try {
                        this.getRaizFile().seek(0);
                        this.getRaizFile().writeLong(irma.getOffsetPag());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(pai.getTamanho() <= this.getOrdem() && pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                    this.setRaiz(pai);// aztualizo raiz runtime
                    Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());

                }else if( pai.getTamanho() < this.getOrdem() && pai.getOffsetPag() != this.getRaiz().getOffsetPag()){
                    Pagina paiDoPai = Pagina.lePagina(pai.getOffsetPai(),this.getFile(),this.getOrdem());
                    // preciso descobrir quem eh a chave pai da pagina folha e suas irmas
                    int meio = 0,
                            esquerda = 0, direita = paiDoPai.getTamanho() -1;
                    int posOffset;

                    while(esquerda <= direita){
                        meio = (esquerda + direita)/2;
                        if(paiDoPai.getChave(meio) == chavePai){
                            // achei chave pai
                            break;
                        }else if(paiDoPai.getChave(meio) <chavePai ){
                            esquerda = meio +1;
                        }else{
                            direita = meio -1;
                        }
                    }
                    posOffset = meio;
                    if(paiDoPai.getChave(meio) <= chavePai){// vejo se tenho q descer pelo offset do meio ou direita
                        posOffset = meio + 1;
                    }else{
                        if(meio != 0){
                            meio -= 1;
                        }

                    }
                    if(posOffset == 0){//so tem a irma direita
                        Pagina irmaDireita = Pagina.lePagina(paiDoPai.getOffset(1),this.getFile(),this.getOrdem());

                        if(irmaDireita.getTamanho() -1  >= this.getOrdem()){
                            // estou vendo se posso pegar um elemento da irma sem ferir a ordem

                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai);//atualizo raiz runtime
                            }
                            redistribuicao(pai,irmaDireita,'d',paiDoPai,-1);

                        }else{// preciso concatenar
                            int j = 0;
                            long chavePaidoPai = paiDoPai.getChave(meio);
                            for(j = meio; j < pai.getTamanho() -1; j++){
                                paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                                paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                            }
                            paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai); // atualizo runtime
                            }
                            concatencao(paiDoPai,pai,irmaDireita,'d',chavePaidoPai);
                        }


                    }else if( posOffset == paiDoPai.getTamanho()){// so tenho irma esquerda
                        Pagina irmaEsquerda = Pagina.lePagina(paiDoPai.getOffset(posOffset -1),this.getFile(),this.getOrdem());
                        if(irmaEsquerda.getTamanho() -1 >= this.getOrdem()){//verifico se posso remover
                            if(pai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai);//atualizo raiz runtime
                            }
                            redistribuicao(pai,irmaEsquerda,'e',paiDoPai,meio);

                        }else{
                            int j = 0;
                            long chavePaidoPai = paiDoPai.getChave(meio);
                            for(j = meio; j < paiDoPai.getTamanho() -1; j++){
                                paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                                paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                            }
                            paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai); // atualizo runtime
                            }

                            concatencao(paiDoPai,pai,irmaEsquerda,'e',chavePaidoPai);
                        }

                    }else{// tenho as duas irmas
                        Pagina irmaDireita = Pagina.lePagina(paiDoPai.getOffset(posOffset + 1),this.getFile(),this.getOrdem());
                        Pagina irmaEsquerda = Pagina.lePagina(paiDoPai.getOffset(posOffset -1),this.getFile(),this.getOrdem());

                        if(irmaEsquerda.getTamanho() -1 >= this.getOrdem()){//verifico se posso remover
                            paiDoPai.setChave(irmaEsquerda.getChave(irmaEsquerda.getTamanho() -1),meio);
                            redistribuicao(pai,irmaEsquerda,'e',paiDoPai,meio);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai);//atualizo raiz runtime
                            }
                            Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                            Pagina.escrevePagina(this.getFile(),paiDoPai,this.getOrdem());
                            Pagina.escrevePagina(this.getFile(),irmaEsquerda,this.getOrdem());
                        } else if(irmaDireita.getTamanho() - 1 >= this.getOrdem()){
                            paiDoPai.setChave(irmaDireita.getChave(1),meio + 1);// meio + 1 eh o pai da irma e pagina com ordem zuada
                            redistribuicao(pai,irmaDireita,'d',paiDoPai,meio);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai);//atualizo raiz runtime
                            }
                            Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                            Pagina.escrevePagina(this.getFile(),paiDoPai,this.getOrdem());
                            Pagina.escrevePagina(this.getFile(),irmaDireita,this.getOrdem());
                        }else{
                            int j = 0;
                            long chavePaidoPai = paiDoPai.getChave(meio);
                            for(j = meio; j < paiDoPai.getTamanho() -1; j++){
                                paiDoPai.setChave(paiDoPai.getChave(j+1),j);
                                paiDoPai.setOffset(paiDoPai.getOffset(j+2),j+1);
                            }
                            paiDoPai.setTamanho(paiDoPai.getTamanho() -1);
                            if(paiDoPai.getOffsetPag() == this.getRaiz().getOffsetPag()){
                                this.setRaiz(paiDoPai); // atualizo runtime
                            }

                            concatencao(paiDoPai,pai,irmaEsquerda,'e',chavePaidoPai);
                        }

                    }

                }else{
                    Pagina.escrevePagina(this.getFile(),pai,this.getOrdem());
                }
            }


        }

    }
    public void printaArvore(Pagina p){
        if(p.getEh_folha() == 1){
            int i;
            for(i = 0; i < p.getTamanho();i++){
                System.out.println("chave " +p.getChave(i));
                System.out.println("offset " + p.getOffset(i));
            }
            System.out.println("Meu tamaho eh: " + p.getTamanho());
            System.out.println("meu offset eh: " + p.getOffsetPag());
            System.out.println("Meu pai eh: " + p.getOffsetPai());
            System.out.println("----------------------");
            if(p.getOffset(i) != -1){
                Pagina p1 = null;
                p1 = Pagina.lePagina(p.getOffset(i),this.getFile(),this.getOrdem());
                printaArvore(p1);
            }
        }else{
            System.out.println("index set: ");
            int i;
            for(i = 0; i < p.getTamanho();i++){
                System.out.println("chaveeee " +p.getChave(i));
                System.out.println("offsetee " + p.getOffset(i));
            }
            System.out.println("offsetee " + p.getOffset(i));
            System.out.println("Meu offset eh: " + p.getOffsetPag());
            System.out.println("Meu tamaho eh: " + p.getTamanho());
            System.out.println("Meu pai eh: " + p.getOffsetPai());
            System.out.println("----------------------");
            Pagina p1 = null;
            p1 = Pagina.lePagina(p.getOffset(0),this.getFile(),this.getOrdem());
            printaArvore(p1);
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


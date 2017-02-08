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
    private  RandomAccessFile raizFile;
    private int ordem;
    private  EspacoVazio ferramenta1;
    private String filePathRaiz = "raiz.bin";


    public ArvoreBMais(String filePath,int ordem){
        this.ferramenta1 = new EspacoVazio();
        this.setOrdem(ordem);
        try{
            this.file = new RandomAccessFile(filePath,"rw");
            this.raizFile = new RandomAccessFile("raiz.bin","rw");

        }catch(IOException e ){
            System.err.print("Nao foi possivel abrir o arquivo");
            e.printStackTrace();
        }
        this.setRaiz(this.leRaiz());
        //System.out.println(this.getRaiz().getChave(0));

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
    public void inserirArvore(int chave, long offset){
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
    private void buscaInserir(Pagina p, int chave, long offset){
        int esquerda= 0, direita = p.getTamanho() -1;
        if(p.getEh_folha() == 1){// encontrei pagina folha para inseir
            int meio = 0;
            while(esquerda < direita){
                meio = (esquerda + direita)/2;
                if(p.getChave(meio) == chave){
                    // achei chave vou para a pagina
                    System.out.println("Chave " + chave +  " ja existe na arvore !!!!!");
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
                while(esquerda < direita){
                    meio = (esquerda + direita)/2;
                    if(p.getChave(meio) == chave){
                        // achei chave vou para a pagina
                        Pagina aux = Pagina.lePagina(p.getOffset(meio +1),this.getFile(),this.getOrdem());
                        buscaInserir(aux,chave,offset);
                    }else if(p.getChave(meio) < chave){
                        esquerda = meio +1;
                    }else{
                        direita = meio -1;
                    }
                 }
                Pagina aux = Pagina.lePagina(p.getOffset(meio +1),this.getFile(),this.getOrdem());
                buscaInserir(aux,chave,offset);


            }//nao encontrei chave no index set, vou para a pagina de baixo

        }
    }

    private void split(Pagina p, int ordem, RandomAccessFile file){
        //ATENCAO COLOCAR ENCADEAMENTO NOS NO FOLHA, ESQUECI ASS: GABRIEL DO PRESENTE Q AGR É PASSADO
        int metade = p.getTamanho()/2,metadeIrma;
            metadeIrma = metade +1;

        long offsetListaEncadeada = p.getOffset(p.getTamanho());
        int chaveCentral = p.getChave(metade);
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
            for(i = metade +1 ;i<p.getTamanho();i++){
                irma.setChave(p.getChave(i),aux);
                p.setChave(-1,i);//zerei as chaves
                irma.setOffset(p.getOffset(i),aux);
                p.setOffset(-1,i);//zerei os offsets
                aux += 1;
            }
            p.setTamanho(metade);
            irma.setOffset(p.getOffset(i),aux);//irma agora tem tamanho + 1 offsets
            p.setOffset(-1,i);
            irma.setTamanho(metadeIrma -1);

            if(p.getOffsetPai() == -1){// nao tem pai preciso criar e logo ele vai ser a nova raiz
                pai = new Pagina(this.getOrdem());
                pai.setEh_folha(0);
                pai.setTamanho(1);
                pai.setChave(chaveCentral,0);
                pai.setOffset(p.getOffsetPag(),0);
                long offsetPagPai,offsetPagIrma;
                offsetPagPai = ferramenta1.buscaEspacoVazio(file);
                pai.setOffsetPag(offsetPagPai);
                Pagina.escrevePagina(file,pai,this.getOrdem());
                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);


                try {
                    this.getRaizFile().seek(0);
                    this.getRaizFile().writeLong(offsetPagPai);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                p.setOffsetPai(offsetPagPai);
                pai.setOffset(offsetPagIrma,1);
                pai.setOffsetPag(offsetPagPai);
                irma.setOffsetPag(offsetPagIrma);
                irma.setOffsetPai(offsetPagPai);
                this.setRaiz(pai);//atualizo raiz
                Pagina.escrevePagina(file,pai,this.getOrdem());
                Pagina.escrevePagina(file,p,this.getOrdem());
                Pagina.escrevePagina(file,irma,this.getOrdem());
            }else{//ja tem pai
                long offsetPagIrma;
                offsetPagIrma = ferramenta1.buscaEspacoVazio(file);
                irma.setOffsetPai(p.getOffsetPai());
                irma.setOffsetPag(offsetPagIrma);
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

    private Pagina inserePaiSplit(Pagina pai, int chave, long offset){
        int tempChave;
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

    public void printaArvore(Pagina p){
        if(p.getEh_folha() == 1){
            int i;
            for(i = 0; i < p.getTamanho();i++){
                System.out.println("chave " +p.getChave(i));
                System.out.println("offset " + p.getOffset(i));
            }
            System.out.println("Meu tamaho eh: " + p.getTamanho());
            System.out.println("meu offset eh: " + p.getOffsetPag());
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


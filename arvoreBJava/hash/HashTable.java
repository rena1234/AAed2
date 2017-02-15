

package hash;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

public class HashTable{

    private RandomAccessFile arqLists;
    private RandomAccessFile arqEspVazio;
    private RandomAccessFile arqHash;
    
    private long tamanho;

    private long offsetAnterior;
    private long offsetProx;
    private long offsetRegistro;
    private long offsetAtual;
    private long chave;

    public HashTable(long tamanhoHash)throws FileNotFoundException{
        this.tamanho = tamanhoHash;
        this.arqLists = new RandomAccessFile("arqLists","rw");
        this.arqEspVazio = new RandomAccessFile("arqEspVazio","rw");
        this.arqHash = new RandomAccessFile("arqHash","rw");
    }    

    private void escreveNoArquivo  (long offsetDestino,long offsetAnterior,
            long chave,long offsetRegistro, long offsetProx)throws IOException{
        /*arqLists.seek(offsetDestino);
        arqLists.writeLong(offsetAnterior);
        arqLists.writeLong(chave);
        arqLists.writeLong(offsetRegistro);
        arqLists.writeLong(offsetProx);*/
        long[] arrayNo = new long[4];
        arrayNo[0] = offsetAnterior; arrayNo[1] = chave; 
        arrayNo[2] = offsetRegistro; arrayNo[3] = offsetProx;
        
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(arrayNo);
            }
            arqLists.seek(offsetDestino);
            arqLists.write(b.toByteArray());
         } catch (IOException e) {
            e.printStackTrace();
          }
    }

    private void moveCabeca(long offset)throws IOException{
        /*arqLists.seek(offset);
        this.offsetAtual = offset;
        this.offsetAnterior = arqLists.readLong();
        this.chave = arqLists.readInt();
        this.offsetRegistro = arqLists.readLong();
        this.offsetProx = arqLists.readLong();*/
        
        /*
         * IMPORTANTE
         * Tem que fzr um teste para saber quantos bytes são aqui 
         */
        byte [] bytes = new byte[4062];
        
        try {
            this.arqLists.seek(offset);
            this.arqLists.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                /*
                 * IMPORTANTE
                 * Testa essa merda também
                 * vvvvvvvvvvvvvvvvvvvvvvv
                 */
                 long[] infoNo = (long[])o.readObject();
                 this.offsetAtual = offset;
                 this.offsetAnterior = infoNo[0];
                 this.chave = infoNo[1];
                 this.offsetRegistro = infoNo[2];
                 this.offsetProx = infoNo[3];

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void avanca()throws IOException{
        if ( !(this.offsetProx == -1) )
            moveCabeca(this.offsetProx);
    }

    private void retrocede()throws IOException{
        if ( !(this.offsetAnterior == -1) )
            moveCabeca(this.offsetAnterior);
    }

    public void iniciaHash()throws IOException{
        if(this.arqHash.length() == 0)
            for(long i = 0; i < this.tamanho ;i++)
                arqHash.writeLong(-1);
    }

    private long achaInicioLista(long chave)throws IOException{
        arqHash.seek((chave % this.tamanho)*(Long.SIZE/Byte.SIZE));
        return arqHash.readLong(); 
    }

    public long buscaRegistro(long chave)throws IOException{
        long inicioLista = this.achaInicioLista(chave);
        if(inicioLista == -1)
            return -1;
        this.moveCabeca(inicioLista);
        while(true){
            if(this.chave == chave)
                return this.offsetRegistro;
            if(this.offsetProx == -1)
                return -1;
        }
    }

    public void insere(long chave,long offsetRegistro)throws IOException{
        
        long offsetNoNovo = -1; /* Esse -1 está aí só pra iniciar a variável */
        long offsetInicioLista = this.achaInicioLista(chave);
        this.arqLists.seek(offsetInicioLista);
        
        if(arqHash.length() == 0){
            this.iniciaHash();
        }

        boolean podeInserir = true;

        if(this.buscaRegistro(chave) != -1){
            System.out.println("Esta chave já foi inserida");
            podeInserir = false;
        }

        if(offsetInicioLista != -1 && podeInserir) moveCabeca(offsetInicioLista);



        if(arqEspVazio.length() > 0 && podeInserir){
            /*
             * Acha um espaço vazio no meio do arquivo e move file pointer
             * para este espaço
             */
            arqEspVazio.seek( arqEspVazio.length() - Long.SIZE/Byte.SIZE);
            offsetNoNovo = arqEspVazio.readLong();
            arqEspVazio.setLength( arqEspVazio.length() - Long.SIZE/Byte.SIZE );
            arqLists.seek(offsetNoNovo);
        }
        
        /*
         * Coloca file pointer no final do arquivo
         */
        else if(podeInserir){
            offsetNoNovo = arqLists.length();
            arqLists.seek(offsetNoNovo);
        }
        
        /*
         * offIniLis == -1 representa lista não iniciada
         */
        if(offsetInicioLista == -1 && podeInserir){
            arqHash.seek(offsetInicioLista);
            arqHash.writeLong(offsetNoNovo);
            this.escreveNoArquivo(offsetNoNovo,-1,chave,offsetRegistro,-1);
        }

        /*
         * this.chave == -1 significa que a cabeça de leitura está em
         * nó vazio e se estou inserindo deve sobrescrever-lo
         */
        else if(this.chave == -1 && podeInserir){
            this.escreveNoArquivo(this.offsetAtual,-1,chave,offsetRegistro,-1);
        }

        /*
         * Nó atual recebe nó novo como próx
         * vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
         */
        else if(podeInserir){
            this.moveCabeca(offsetInicioLista);
            this.escreveNoArquivo(offsetNoNovo,this.offsetAtual,chave,offsetRegistro,
                    this.offsetProx);
            /*
             * Atualiza o nó atual
             */
            this.escreveNoArquivo(this.offsetAtual,this.offsetAnterior,this.chave,
                    this.offsetRegistro,offsetNoNovo);
        }
    }

    /*
     * Caso o nó retirado possua:
     *  
     *  offsetAnterior == -1 && offsetProx == -1
     *
     * então, o nó ao primeiro offset da lista deve receber
     * a chave == -1, indicando uma lista vazia
     *
     * Todo nó excluido deve ser sobrescrito por um "nó nulo"
     * (todos os atributos == -1) 
     */
    public void delete(long chave)throws IOException{
        long offsetInicioLista = achaInicioLista(chave);
        if(offsetInicioLista != -1){
            this.moveCabeca(offsetInicioLista);
            while(true){
                if(this.offsetProx == -1) break;
                if(this.chave == chave){
                    if(this.offsetAnterior == -1 && this.offsetProx == 1){
                        escreveNoArquivo(this.offsetAtual,-1,-1,-1,-1);
                        break;
                    }
                    
                    /*
                     * Passa segundo elemento para primeira posição
                     */
                    else if(this.offsetAnterior == -1){
                        long aux = this.offsetAtual;
                        avanca();
                        escreveNoArquivo(aux,this.offsetAnterior,this.chave,
                                this.offsetRegistro,this.offsetProx);
                        arqEspVazio.seek(arqEspVazio.length());
                        arqEspVazio.writeLong(this.offsetAtual);
                        break;
                    }
                    arqEspVazio.seek(arqEspVazio.length());
                    arqEspVazio.writeLong(this.offsetAtual);
                    
                    long aux = this.offsetProx;
                    this.retrocede();
                    this.escreveNoArquivo(this.offsetAtual,this.offsetAnterior,
                            this.chave,this.offsetRegistro,aux);
                    break;
                }
                avanca();
            }   
        } 
    }

}


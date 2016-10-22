package bMais;

import java.io.RandomAccessFile;

public class Arvore {

	private RandomAccessFile arvore;
	public int tamChave;
	public int tamPonteiro;
	private Pagina raiz;
	
	public Arvore(RandomAccessFile arvore, int tamChave, int tamPonteiro, Pagina raiz) {
		super();
		this.arvore = arvore;
		this.tamChave = tamChave;
		this.tamPonteiro = tamPonteiro;
		this.raiz = raiz;
	}
	private No busca(int chave){
		int proxPagByte;
		int i = 1;
		if(chave < raiz.chaves[0])
			proxPagByte = raiz.ponteiros[0];
		else if(chave >= raiz.chaves[raiz.chaves.length -1])
			proxPagByte = raiz.ponteiros[raiz.chaves.length];
		else{
			while(chave >= raiz.chaves[i]){
				i++;
			}
			proxPagByte = raiz.ponteiros[i];
		}
		return buscaAux(chave,proxPagByte);
	}
	private No buscaAux(int chave, int posicao){
		if(ES.ler(arvore, posicao, 4)==1){
			//Verifica se é um nó
			return new No(arvore,this,posicao);
		}
		Pagina pagina = new Pagina(arvore,this,posicao);
		int proxPagByte;
		int i = 1;
		if(chave < pagina.chaves[0])
			proxPagByte = pagina.ponteiros[0];
		else if(chave >= pagina.chaves[pagina.chaves.length -1])
			proxPagByte = pagina.ponteiros[pagina.chaves.length];
		else{
			while(chave >= pagina.chaves[i]){
				i++;
			}
			proxPagByte = pagina.ponteiros[i];
		}
		return buscaAux(chave,proxPagByte);

	}
}

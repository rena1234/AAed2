package bMais;

import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;

public class Pagina {
	public RandomAccessFile arquivo;
	public int [] chaves = new int[8];
	public int [] ponteiros = new int[9];
	private Arvore arv;
	
	public Pagina(RandomAccessFile arquivo,Arvore arv,int posicao){
		this.arquivo = arquivo;
		this.arv = arv;
		int posCnt = posicao+4;
		for(int i=0;i<ponteiros.length;i++){
			ponteiros[i]=ES.ler(arquivo, posCnt, arv.tamPonteiro);
			if(i<chaves.length){
				posCnt+=arv.tamPonteiro;
				chaves[i]=ES.ler(arquivo, posCnt, arv.tamChave);
				posCnt+=arv.tamChave;
			}
		}
	}
	
	public void escrevePagina(int posicao){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		TransformaBinario.byteStream(0,4,stream);
		for(int i=0;i<ponteiros.length;i++){
			TransformaBinario.byteStream(ponteiros[i],arv.tamPonteiro, stream);
			if(i<chaves.length)
				TransformaBinario.byteStream(chaves[i],arv.tamChave, stream);
			
		}
		byte[] bytes = stream.toByteArray();
		ES.escreve(arquivo, posicao, bytes);
	}
	

}

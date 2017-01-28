package bMais;

import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;

public class No {
	private int anterior;
	private int proximo;
	private int chave;
	private RandomAccessFile arquivo;
	private Arvore arv;
	
	public No(RandomAccessFile arquivo,int anterior,int proximo,int chave,Arvore arv) {
		super();
		this.chave = chave;
		this.anterior = anterior;
		this.proximo = proximo;
		this.arquivo = arquivo;
		this.arv =arv;
	}
	public No(RandomAccessFile arquivo,Arvore arv,int posicao){
		int posCnt = posicao+4;
		this.arv = arv;
		this.arquivo = arquivo;
		this.anterior = ES.ler(arquivo, posCnt, arv.tamPonteiro);
		posCnt+=arv.tamPonteiro;
		this.chave = ES.ler(arquivo, posCnt, arv.tamChave);
		posCnt+=arv.tamChave;
		this.proximo = ES.ler(arquivo, posCnt, arv.tamPonteiro);

	}
	
	public void escreveNo(int posicao){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		TransformaBinario.byteStream(1, 4, stream);
		TransformaBinario.byteStream(anterior,arv.tamPonteiro,stream);
		TransformaBinario.byteStream(chave,arv.tamChave,stream);
		TransformaBinario.byteStream(proximo,arv.tamPonteiro,stream);
		byte[] bytes = stream.toByteArray();
		ES.escreve(arquivo, posicao, bytes);
	}
}

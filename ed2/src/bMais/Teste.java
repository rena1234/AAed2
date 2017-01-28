package bMais;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class Teste {
	public static void main(String[] args) {
		RandomAccessFile arquivo=null;
		try {
			arquivo = new RandomAccessFile("arqTeste","rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Arvore arvore = new Arvore(arquivo,4,4,null);
		No no = new No(arquivo,1,2,3,arvore);
		no.escreveNo(3);
		
		System.out.println(ES.ler(arquivo,3,4));
	}
}

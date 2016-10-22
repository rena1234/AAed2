package bMais;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;


public class ES {
		
	public static void escreve(RandomAccessFile file,int posicao, byte[] entrada){
		try {
			 file.seek(posicao);
			 file.write(entrada);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int ler(RandomAccessFile file,int posicao, int tamanho){
		byte [] ret = new byte[tamanho];
		try {
			 file.seek(posicao);
			 file.read(ret);
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
           
		return ByteBuffer.wrap(ret).getInt();
		
		
	}

}

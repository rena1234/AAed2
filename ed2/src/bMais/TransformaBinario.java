package bMais;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class TransformaBinario {
	
	public static ByteArrayOutputStream byteStream(int num,int tam,ByteArrayOutputStream stream){
		byte [] bNum = ByteBuffer.allocate(tam).putInt(num).array();
		try {
			stream.write(bNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stream;
	}

}

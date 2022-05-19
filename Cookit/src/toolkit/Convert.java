package toolkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Convert {

	public static byte[] toByteArray(InputStream is, int fileLength) {
		
		byte[] buffer = new byte[fileLength];
		int bytesRead;
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    
	    try {
	    	
			while ((bytesRead = is.read(buffer)) != -1)
			{
			    output.write(buffer, 0, bytesRead);
			}
			
		} catch (IOException e) {
			System.err.println("No se ha podido convertir a bytes");
			e.printStackTrace();
		}
		
		return output.toByteArray();
		
	}
	
}

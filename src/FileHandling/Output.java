package FileHandling;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Output {
	
	PrintWriter writer;
	
	public Output(String path){
		try {
			writer = new PrintWriter(path, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String value) {
		writer.write(value);
		writer.flush();
	}
	
	public void close() {
		writer.close();
	}
}

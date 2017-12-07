import java.io.IOException;

public class Main{
	
	public static void main(String [] args) {
		/**
		 * Handle parsing
		 */
		Parser parser = new Parser();
		
		try {
			parser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

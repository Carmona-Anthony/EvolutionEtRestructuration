import java.io.IOException;

import Visitor.ASTVisitor;
import Visitor.FieldAccessVisitor;
import Visitor.MethodDeclarationVisitor;
import Visitor.MethodInvocationVisitor;
import Visitor.TypeDeclarationVisitor;

public class Main{
	
	public void main (String [] args) {
		/**
		 * Handle parsing
		 */
		Parser parser = new Parser();
		parser.addVisitor(new FieldAccessVisitor());
		parser.addVisitor(new TypeDeclarationVisitor());
		parser.addVisitor(new MethodDeclarationVisitor());
		parser.addVisitor(new ASTVisitor());
		parser.addVisitor(new MethodInvocationVisitor());
		
		try {
			parser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

package decorator;

import org.eclipse.jdt.core.dom.ASTNode;

public class Decorator {
	
	public ASTNode node;
	
	Decorator(ASTNode node){
		this.node = node;
	}
	
	public ASTNode get(){
		return node;
	}
}

package decorator;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class SingleVariableDecorator extends Decorator {
	
	String name;

	public SingleVariableDecorator(SingleVariableDeclaration node) {
		super(node);
		// TODO Auto-generated constructor stub
		name = node.getName().toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	

}

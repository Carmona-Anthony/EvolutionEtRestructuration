package Visitor;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationVisitor extends Visitor {
	
	List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	static int nbParameterMax = 0;
	
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		
		if(node.parameters().size() > nbParameterMax) nbParameterMax = node.parameters().size();
		
		return super.visit(node);
	}
	
	public static int getMaxParameter() {
		return nbParameterMax;
	}
	
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	
	public String toString() {
		return "Method count : " + methods.size();
		
	}
}


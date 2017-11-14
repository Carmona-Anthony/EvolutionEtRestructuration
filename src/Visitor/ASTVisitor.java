package Visitor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ASTVisitor extends Visitor {
	
	HashMap<String, LinkedHashMap <String, Integer>> methodsPositionsByType = new HashMap<>();
	
	public boolean visit(TypeDeclaration node) {
		
		MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();
		node.accept(methodVisitor);
		
		List<MethodDeclaration> methods = methodVisitor.getMethods();
		
		LinkedHashMap<String, Integer> positionByMethods = new LinkedHashMap<>();
		for(MethodDeclaration method : methods) {
			int lineNumber = node.getStartPosition() - 1;
			System.out.println("Method : " + method.getName() + " " + lineNumber);
		}
		return true;
	}
}

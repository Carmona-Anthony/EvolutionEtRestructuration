package Visitor;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import decorator.MethodDecorator;

public class MethodDeclarationVisitor extends Visitor {
	
	List<MethodDecorator> methods = new ArrayList<>();
	
	MethodDeclaration currentMethod;
	MethodDecorator currentDecorator;
	
	int nbParameters;
	
	MethodDeclarationVisitor(CompilationUnit cu){
		super(cu);
	}
	
	public boolean visit(MethodDeclaration node) {
		
		MethodDecorator methodDecorator = new MethodDecorator(node);
		methods.add(methodDecorator);
		
		currentMethod = node;
		currentDecorator = methodDecorator;
		
		//Get the list of parameters
		List<SingleVariableDeclaration> list = node.parameters();
		currentDecorator.setParameters(list);
		
		MethodInvocationVisitor methodInvocationVisitor = new MethodInvocationVisitor(root);
		node.accept(methodInvocationVisitor);
		
		currentDecorator.setInvocationMethods(methodInvocationVisitor.get());
		
		return super.visit(node);
	}
	
	public void endVisit(MethodDeclaration node) {
		
		for (MethodDecorator method : methods) {
			int startLine = root.getLineNumber(method.get().getStartPosition());
			int endLine = root.getLineNumber(method.get().getStartPosition() + currentMethod.getLength() - 1);

			currentDecorator.setNbLines(endLine - startLine);
		}
		super.endVisit(node);
	}
	
	public String toString() {
		return "Method count : " + methods.size();	
	}

	@Override
	public List<MethodDecorator> get() {
		// TODO Auto-generated method stub
		return methods;
	}
}


package Visitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import decorator.InvocationMethodDecorator;

public class MethodInvocationVisitor extends Visitor {
	
	List<InvocationMethodDecorator> methods = new ArrayList<>();
	List<SuperMethodInvocation> superMethods = new ArrayList<SuperMethodInvocation>();
	
	MethodInvocationVisitor(CompilationUnit cu) {
		super(cu);
	}
	
	public boolean visit(MethodInvocation node) {
		//if(node.resolveMethodBinding() != null) {
			methods.add(new InvocationMethodDecorator(node));
		//}
		 
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethods.add(node);
		return super.visit(node);
	}
	
	public List<SuperMethodInvocation> getSuperMethod() {
		return superMethods;
	}

	@Override
	public List<InvocationMethodDecorator> get() {
		// TODO Auto-generated method stub
		return methods;
	}
}

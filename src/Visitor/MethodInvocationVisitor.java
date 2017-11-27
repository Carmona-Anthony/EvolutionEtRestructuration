package Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

public class MethodInvocationVisitor extends Visitor {
	
	List<MethodInvocation> methods = new ArrayList<MethodInvocation>();
	List<SuperMethodInvocation> superMethods = new ArrayList<SuperMethodInvocation>();
	
	static LinkedHashMap<MethodInvocation, ArrayList<MethodInvocation>> invokedMethodsByMethod = new LinkedHashMap<>();
	
	
	public boolean visit(MethodInvocation node) {
		methods.add(node);
		
		 node.accept(new ASTVisitor() {
			 public boolean visit(MethodInvocation methodInvocation) {
				 
				 invokedMethodsByMethod.computeIfAbsent(node, k -> new ArrayList<MethodInvocation>()).add(methodInvocation);
				 
				 return true; 
			 }
		 });
		 
		return super.visit(node);
	}
	
	public static LinkedHashMap<MethodInvocation, ArrayList<MethodInvocation>> getInvokedMethodsByMethod(){
		return invokedMethodsByMethod;
	}
	@Override
	public boolean visit(SuperMethodInvocation node) {
		superMethods.add(node);
		return super.visit(node);
	}

	
	public List<MethodInvocation> getMethods() {
		return methods;
	}
	
	public List<SuperMethodInvocation> getSuperMethod() {
		return superMethods;
	}
	
	public void printInvokedMethods() {
		
	}
}
